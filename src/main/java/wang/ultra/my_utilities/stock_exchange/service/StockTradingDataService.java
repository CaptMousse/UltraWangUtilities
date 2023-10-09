package wang.ultra.my_utilities.stock_exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.stock_exchange.apis.MairuiApi;
import wang.ultra.my_utilities.stock_exchange.apis.QtGtimgApi;
import wang.ultra.my_utilities.stock_exchange.apis.SlzBssJyjlApi;
import wang.ultra.my_utilities.stock_exchange.entity.MaEntity;
import wang.ultra.my_utilities.stock_exchange.entity.MacdEntity;
import wang.ultra.my_utilities.stock_exchange.mapper.TradingDataMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("stockTradingDataService")
public class StockTradingDataService {

    private static final Logger LOG = LoggerFactory.getLogger(StockTradingDataService.class);

    @Autowired
    TradingDataMapper tradingDataMapper;

    /**
     * 获取最近10日的MACD
     *
     * @param stockId
     */
    public void getStockMacdInTenDays(String stockId) {
        SlzBssJyjlApi slz = new SlzBssJyjlApi(stockId);
        Map<String, Object> slzMap = slz.getMacdInTenDays();
        String slzStatus = String.valueOf(slzMap.get("status"));
        if ("1".equals(slzStatus)) {
            List<Map<String, Object>> slzDataList = (List<Map<String, Object>>) slzMap.get("data");
            List<MacdEntity> daoList = new ArrayList<>();
            for (Map<String, Object> slzData : slzDataList) {
                MacdEntity entity = new MacdEntity();
                String indicatorDate = DateConverter.getNoSymbolTime(Long.parseLong(String.valueOf(slzData.get("indicatorDate")))).substring(0, 8);
                entity.setId(stockId + indicatorDate);
                entity.setStockId(stockId);
                entity.setIndicatorDate(indicatorDate);
                entity.setDea(String.valueOf(slzData.get("dea")));
                entity.setDiff(String.valueOf(slzData.get("diff")));
                entity.setMacd(String.valueOf(slzData.get("macd")));

                daoList.add(entity);
            }
            tradingDataMapper.macdAdd(daoList);
            LOG.info("获取" + stockId + "近10日的MACD成功!!!");
        }

    }

    /**
     * 获取历史均线
     *
     * @param stockId
     */
    public void getStockHistoryDayMA(String stockId) {
        MairuiApi mairui = new MairuiApi(stockId);
        // 分时级别
        // 5m, 15m, 30m, 60m,
        // dn (日线未复权) , dq (日线前复权) , dh (日线后复权) ,
        // wn (周线未复权) , wq (周线前复权) ,wh (周线后复权) ,
        // mn (月线未复权) , mq (月线前复权) , mh(月线后复权) ,
        // yn (年线未复权) , yq (年线前复权) , yh (年线后复权)
        Map<String, Object> dayMAMap = mairui.getHistoryMA("dn");
        String maStatus = String.valueOf(dayMAMap.get("status"));
        if ("1".equals(maStatus)) {
            List<Map<String, Object>> dayMADataList = (List<Map<String, Object>>) dayMAMap.get("data");
            List<MaEntity> daoList = new ArrayList<>();
            for (Map<String, Object> map : dayMADataList) {
                String indicatorDate = String.valueOf(map.get("t")).replaceAll("-", "");
                MaEntity entity = new MaEntity();
                entity.setId(stockId + indicatorDate);
                entity.setStockId(stockId);
                entity.setIndicatorDate(indicatorDate);
                entity.setMa3(String.valueOf(map.get("ma3")));
                entity.setMa5(String.valueOf(map.get("ma5")));
                entity.setMa10(String.valueOf(map.get("ma10")));
                entity.setMa15(String.valueOf(map.get("ma15")));
                entity.setMa20(String.valueOf(map.get("ma20")));
                entity.setMa30(String.valueOf(map.get("ma30")));
                entity.setMa60(String.valueOf(map.get("ma60")));
                entity.setMa120(String.valueOf(map.get("ma120")));
                entity.setMa200(String.valueOf(map.get("ma200")));
                entity.setMa250(String.valueOf(map.get("ma250")));
                daoList.add(entity);
            }
            tradingDataMapper.maAdd(daoList);
            LOG.info("获取" + stockId + "的历史均线成功");
        }
    }

    /**
     * 获取最近交易日的均线
     *
     * @param stockId
     */
    public void getStockRecentDayMA(String stockId) {
        MairuiApi mairui = new MairuiApi(stockId);
        Map<String, Object> dayMAMap = mairui.getMA("dn");

        System.out.println("dayMAMap = " + dayMAMap);

        String maStatus = String.valueOf(dayMAMap.get("status"));
        if ("1".equals(maStatus)) {
            Map<String, Object> dayMADataMap = (Map<String, Object>) dayMAMap.get("data");
            String indicatorDate = String.valueOf(dayMADataMap.get("t")).replaceAll("-", "");
            MaEntity entity = new MaEntity();
            entity.setId(stockId + indicatorDate);
            entity.setStockId(stockId);
            entity.setIndicatorDate(indicatorDate);
            entity.setMa3(String.valueOf(dayMADataMap.get("ma3")));
            entity.setMa5(String.valueOf(dayMADataMap.get("ma5")));
            entity.setMa10(String.valueOf(dayMADataMap.get("ma10")));
            entity.setMa15(String.valueOf(dayMADataMap.get("ma15")));
            entity.setMa20(String.valueOf(dayMADataMap.get("ma20")));
            entity.setMa30(String.valueOf(dayMADataMap.get("ma30")));
            entity.setMa60(String.valueOf(dayMADataMap.get("ma60")));
            entity.setMa120(String.valueOf(dayMADataMap.get("ma120")));
            entity.setMa200(String.valueOf(dayMADataMap.get("ma200")));
            entity.setMa250(String.valueOf(dayMADataMap.get("ma250")));

            List<MaEntity> daoList = new ArrayList<>();
            daoList.add(entity);
            tradingDataMapper.maAdd(daoList);
            LOG.info("获取" + stockId + "的均线成功!!!");
        }
    }

    public void getStockNow(String stockId) {
        QtGtimgApi qtApi = new QtGtimgApi(stockId);
        String stockNow = qtApi.getStockNow();
        stockNow = stockNow.substring(0, stockNow.lastIndexOf("\""));
        stockNow = stockNow.substring(stockNow.lastIndexOf("\"") + 1);
        String[] stockNowArr = stockNow.split("~");
        String key = """
            交易所
            股票名字
            股票代码
            当前价格
            昨收
            开盘
            成交量
            外盘
            内盘
            买1
            买1量
            买2
            买2量
            买3
            买3量
            买4
            买4量
            买5
            买5量
            卖1
            卖1量
            卖2
            卖2量
            卖3
            卖3量
            卖4
            卖4量
            卖5
            卖5量
            -
            请求时间
            涨跌
            涨跌%
            最高
            最低
            最新价/成交量(手)/成交额
            成交量
            成交额
            换手率
            TTM市盈率
            -
            均价
            动态市盈率
            静态市盈率
            -
            -
            -
            成交额""";

        String[] keyArr = key.split("\n");

        Map<String, String> stockNowMap = new LinkedHashMap<>();
        for (int i = 0; i < keyArr.length; i++) {
            stockNowMap.put(keyArr[i], stockNowArr[i]);
        }

        System.out.println(stockNowMap);
    }

    /**
     * 获取需要同步的股票的列表
     *
     * @return
     */
    public List<Map<String, String>> getStockSyncList() {
        List<Map<String, Object>> getSyncStockIdList = tradingDataMapper.getStockSyncList();
        return ListConverter.mapValueToString(getSyncStockIdList);
    }
}
