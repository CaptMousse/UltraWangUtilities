package wang.ultra.my_utilities.stock_exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.cache.stockData.StockMaCacheList;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.SendMailUtils;
import wang.ultra.my_utilities.common.utils.StringUtils;
import wang.ultra.my_utilities.stock_exchange.entity.NotifyEntity;
import wang.ultra.my_utilities.stock_exchange.entity.NotifyMaEntity;
import wang.ultra.my_utilities.stock_exchange.mapper.NotifyLogMapper;

import java.util.*;

@Service("stockMaService")
public class StockMaService {

    private static final Logger LOG = LoggerFactory.getLogger(StockMaService.class);

    @Autowired
    StockPreparingService stockPreparingService;

    @Autowired
    NotifyLogMapper notifyLogMapper;

    /**
     * 计算实时的MA5和MA10
     *
     * @param stockId
     * @param stockNowMap
     */
    public void calMa(String stockId, Map<String, String> stockNowMap) {

        StockMaCacheList stockMaCacheList = new StockMaCacheList();

        Map<String, String> stockMaCache = stockMaCacheList.get(stockId);
        if (stockMaCache == null) {
            LOG.info(stockId + "的均线缓存为空, 重建缓存中... ");
            stockPreparingService.preparingMA(stockId);
        }
        stockMaCache = stockMaCacheList.get(stockId);

        if (stockMaCache == null) {
            return;
        }

        Float aMa5Float = Float.parseFloat(stockMaCache.get("abstractMa5"));    // 抽象的MA5, 是前4天收盘价之和, 需要加上实时的收盘价算出来实时的MA5
        Float aMa10Float = Float.parseFloat(stockMaCache.get("abstractMa10"));  // 抽象的MA10, 同理
        String spj = stockNowMap.get("收盘价");
        Float spjFloat = Float.parseFloat(spj);
        float ma5 = (aMa5Float + spjFloat) / 5;
        float ma10 = (aMa10Float + spjFloat) / 10;
        float ma5Math = (float) (Math.round(ma5 * 100)) / 100;
        float ma10Math = (float) (Math.round(ma10 * 100)) / 100;
        float yesterdayMa5 = Float.parseFloat(stockMaCache.get("yesterdayMa5"));
        float yesterdayMa10 = Float.parseFloat(stockMaCache.get("yesterdayMa10"));

        String stockName = stockNowMap.get("股票名字");
        String indicatorTime = stockNowMap.get("请求时间");
        String indicatorDate = indicatorTime.substring(0, 8);

        String contentStockName = "股票: " + stockName;
        String contentIndicatorTime = "时间: " + DateConverter.getTime(indicatorTime);
        String contentSpj = "最新价: " + spj;
        String contentMa5 = "MA5: " + ma5;
        String contentMa10 = "MA10: " + ma10;
        String content = contentStockName + ", " + contentIndicatorTime + ", " + contentSpj + ", " + contentMa5 + ", " + contentMa10;
        LOG.info(content);

        // 判断是否有均线的金叉死叉发生
        String title = null;
        if (yesterdayMa5 < yesterdayMa10) {
            if (ma5Math >= ma10Math) {
                title = "MA5/MA10均线发生金叉, 注意观察!!! ";
            }
        } else if (yesterdayMa5 > yesterdayMa10) {
            if (ma5Math <= ma10Math) {
                title = "MA5/MA10均线发生死叉, 注意警戒!!! ";
            }
        }
        if (title == null) {
            return;
        }

        if (ifTodayNotifiedMa(stockId)) {
            LOG.info("今日已推送过 " + stockId + " 的均线预警, 不再推送!!! ");
            return;
        }

        LOG.info(title);

        // 邮件提醒
        List<String> contentList = new ArrayList<>();
        contentList.add(contentStockName);
        contentList.add(contentIndicatorTime);
        contentList.add(contentSpj);
        contentList.add(contentMa5);
        contentList.add(contentMa10);
        sendMail(stockNowMap.get("股票名字"), title, contentList);

        // 持久化
        NotifyEntity notifyEntity = new NotifyEntity();
        NotifyMaEntity notifyMaEntity = new NotifyMaEntity();
        String uuid = StringUtils.getMyUUID();
        String price = stockNowMap.get("收盘价");
        notifyEntity.setId(stockId + indicatorDate);
        notifyEntity.setStockId(stockId);
        notifyEntity.setIndicatorDate(indicatorDate);
        notifyEntity.setIndicatorTime(indicatorTime);
        notifyEntity.setNotifyType("MA");
        notifyEntity.setNotifyId(uuid);
        notifyEntity.setRecordTime(DateConverter.getNoSymbolTime());
        notifyEntity.setMsg(title);
        notifyMaEntity.setId(uuid);
        notifyMaEntity.setBrief(content);
        notifyMaEntity.setPrice(price);
        notifyMaEntity.setMa5(String.valueOf(ma5));
        notifyMaEntity.setMa10(String.valueOf(ma10));
        notifyRecord(notifyEntity, notifyMaEntity);
    }

    /**
     * 发送邮件
     * @param stockName
     * @param title
     * @param contentList
     */
    private void sendMail(String stockName, String title, List<String> contentList) {
        String mailTo = ConstantFromFile.getMailTo();
        String mailSubject = "【UW股票均线提醒】" + stockName + "在" + DateConverter.getSimpleTime() + "发出均线提醒";
        StringBuilder mailContent = new StringBuilder("<h1 style=\"text-align: center;\">" + title + "</h1>");
        for (String content : contentList) {
            mailContent.append("<br>").append(content);
        }
        SendMailUtils sendMailUtils = new SendMailUtils();
        sendMailUtils.sendMail(mailTo, mailSubject, mailContent.toString());
    }

    /**
     * 持久化提醒
     * @param notifyEntity
     * @param notifyMaEntity
     */
    private void notifyRecord(NotifyEntity notifyEntity, NotifyMaEntity notifyMaEntity) {
        List<NotifyEntity> notifyList = new ArrayList<>();
        notifyList.add(notifyEntity);
        notifyLogMapper.notifyAdd(notifyList);
        List<NotifyMaEntity> notifyMaList = new ArrayList<>();
        notifyMaList.add(notifyMaEntity);
        notifyLogMapper.notifyMaAdd(notifyMaList);
    }

    /**
     * 检查今天是否推送过均线提醒
     * @param stockId
     * @return
     */
    private boolean ifTodayNotifiedMa(String stockId) {
        String today = DateConverter.getDate();
        List<Map<String, Object>> resultList = notifyLogMapper.getTodayNotifyMaRecordByStockId(stockId, today);
        return resultList != null && !resultList.isEmpty();
    }
}
