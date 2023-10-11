package wang.ultra.my_utilities.stock_exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.cache.stockData.StockMaCacheList;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.stock_exchange.mapper.TradingDataMapper;

import java.util.*;

/**
 * 交易前的准备
 */
@Service("stockPreparingService")
public class StockPreparingService {

    @Autowired
    TradingDataMapper tradingDataMapper;

    /**
     * 根据前面交易日的收盘价总结抽象的MA5和MA10, 可以和今日的实时数据组成实时的MA5和MA10
     * @param stockId
     */
    public void preparingMA(String stockId) {

        // 获取根据日期排序后的收盘价List
        String date = DateConverter.getDate();
        List<Map<String, Object>> abstractMaList = tradingDataMapper.getAbstractMa(stockId, date);
        List<Long> dateList = new ArrayList<>();
        for (Map<String, Object> map : abstractMaList) {
            long indicatorDate = Long.parseLong((String) map.get("indicator_date"));
            dateList.add(indicatorDate);    // 先获取日期
        }
        Collections.sort(dateList); // 对日期进行排序
        List<Float> spjList = new ArrayList<>();

        // 不知道为啥Collections.reverse()不管用, 只能倒序排序了
        for (int i = dateList.size() - 1; i >= 0; i--) {
            for (Map<String, Object> map : abstractMaList) {
                // 根据日期查出收盘价再塞进收盘价List里面
                if (map.containsValue(String.valueOf(dateList.get(i)))) {
                    spjList.add(Float.parseFloat((String) map.get("spj")));
                }
            }
        }

        // 计算出MA5和MA10的基数, 等到后面实时数据拿来计算出MA用
        float abstractMa5 = 0;
        float abstractMa10 = 0;
        int i = 1;
        for (float spj : spjList) {
            abstractMa10 += spj;
            if (i <= 4) {
                abstractMa5 += spj;
            }
            i++;
        }

        List<Map<String, Object>> yesterdayMa = tradingDataMapper.getYesterdayMa(stockId, date);
        String yesterdayMa5 = String.valueOf(yesterdayMa.get(0).get("ma5"));
        String yesterdayMa10 = String.valueOf(yesterdayMa.get(0).get("ma10"));

        // 放到缓存里
        Map<String, String> stockMaMap = new HashMap<>();
        stockMaMap.put("stockId", stockId);
        stockMaMap.put("abstractMa5", String.valueOf(abstractMa5));
        stockMaMap.put("abstractMa10", String.valueOf(abstractMa10));
        stockMaMap.put("yesterdayMa5", yesterdayMa5);
        stockMaMap.put("yesterdayMa10", yesterdayMa10);

        StockMaCacheList cacheList = new StockMaCacheList();
        cacheList.add(stockMaMap);
    }
}
