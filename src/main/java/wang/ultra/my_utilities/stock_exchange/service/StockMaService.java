package wang.ultra.my_utilities.stock_exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.cache.stockData.StockMaCacheList;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.SendMailUtils;

import java.util.Map;

@Service("stockMaService")
public class StockMaService {

    private static final Logger LOG = LoggerFactory.getLogger(StockMaService.class);

    /**
     * 计算实时的MA5和MA10
     *
     * @param stockId
     * @param spj
     */
    public void calMa(String stockId, String spj) {

        StockMaCacheList stockMaCacheList = new StockMaCacheList();
        Map<String, String> stockMaCache = stockMaCacheList.get(stockId);

        if (stockMaCache == null) {
            LOG.info(stockId + "的均线缓存为空, 不再计算均线数据!!!");
            return;
        }

        Float aMa5Float = Float.parseFloat(stockMaCache.get("abstractMa5"));    // 抽象的MA5, 是前4天收盘价之和, 需要加上实时的收盘价算出来实时的MA5
        Float aMa10Float = Float.parseFloat(stockMaCache.get("abstractMa10"));  // 抽象的MA10, 同理
        Float spjFloat = Float.parseFloat(spj);
        float ma5 = (aMa5Float + spjFloat) / 5;
        float ma10 = (aMa10Float + spjFloat) / 10;
        float ma5Math = (float) (Math.round(ma5 * 100)) / 100;
        float ma10Math = (float) (Math.round(ma10 * 100)) / 100;
        float yesterdayMa5 = Float.parseFloat(stockMaCache.get("yesterdayMa5"));
        float yesterdayMa10 = Float.parseFloat(stockMaCache.get("yesterdayMa10"));

        LOG.info("股票: " + stockId + ", 最新价: " + spj + ", MA5: " + ma5 + ", MA10: " + ma10);
        // 判断是否有均线的金叉死叉发生
        String content = null;
        if (yesterdayMa5 < yesterdayMa10) {
            if (ma5Math >= ma10Math) {
                content = "MA5/MA10均线金叉发生, 注意观察!!!";
            }
        } else if (yesterdayMa5 > yesterdayMa10) {
            if (ma5Math <= ma10Math) {
                content = "MA5/MA10均线死叉发生, 注意警戒!!!";
            }
        }
        if (content == null) {
            return;
        }

        LOG.info(content);
        sendMail(stockId, spj, String.valueOf(ma5), String.valueOf(ma10), content);
    }

    private void sendMail(String stockId, String spj, String ma5, String ma10, String content) {
        String mailTo = ConstantFromFile.getMailTo();
        String mailSubject = "【UltraWang股票提醒】" + stockId + "在" + DateConverter.getSimpleTime() + "发出提醒";
        String mailContent = "<h1 style=\"text-align: center;\">" + content + "</h1>" +
                "<br>最新价: " + spj +
                "<br>MA5: " + ma5 +
                "<br>MA10: " + ma10;
        SendMailUtils sendMailUtils = new SendMailUtils();
        sendMailUtils.sendMail(mailTo, mailSubject, mailContent);
    }
}
