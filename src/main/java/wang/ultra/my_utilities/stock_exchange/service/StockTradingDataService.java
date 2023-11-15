package wang.ultra.my_utilities.stock_exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.cache.stockData.StockNowPriceCacheList;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.stock_exchange.apis.MairuiApi;
import wang.ultra.my_utilities.stock_exchange.apis.QtGtimgApi;
import wang.ultra.my_utilities.stock_exchange.apis.SlzBssJyjlApi;
import wang.ultra.my_utilities.stock_exchange.entity.MaEntity;
import wang.ultra.my_utilities.stock_exchange.entity.MacdEntity;
import wang.ultra.my_utilities.stock_exchange.entity.PriceEntity;
import wang.ultra.my_utilities.stock_exchange.entity.StockEntity;
import wang.ultra.my_utilities.stock_exchange.mapper.TradingDataMapper;
import wang.ultra.my_utilities.stock_exchange.tasks.StockNowTask;

import java.util.*;

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
        String maMsg = String.valueOf(dayMAMap.get("msg"));
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
        } else if ("0".equals(maStatus)) {
            LOG.info("获取" + stockId + "的历史均线失败, 原因是: " + maMsg);
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

    /**
     * 把股票代码加上交易所代码再调用腾讯股票API
     *
     * @param stockId
     * @return
     */
    public Map<String, String> getExchangeIdPrefixStockNow(String stockId) {
        Map<String, Object> stockData = tradingDataMapper.getStock(stockId).get(0);
        stockId = stockData.get("exchange_id") + stockId;
        return getStockNow(stockId);
    }

    /**
     * 调用腾讯股票API获取最新的股票价格
     * 需要加上交易所前缀例如sh600104
     *
     * @param stockId
     * @return
     */
    public Map<String, String> getStockNow(String stockId) {
        QtGtimgApi qtApi = new QtGtimgApi(stockId);
        String stockNow = qtApi.getStockNow();

        if (stockNow.contains("none")) {
            return null;
        }

        stockNow = stockNow.substring(0, stockNow.lastIndexOf("\""));
        stockNow = stockNow.substring(stockNow.lastIndexOf("\"") + 1);
        String[] stockNowArr = stockNow.split("~");
        String key = """
                交易所
                股票名字
                股票代码
                收盘价
                昨收
                开盘价
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
                最高价
                最低价
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
                成交额2""";

        String[] keyArr = key.split("\n");

        Map<String, String> stockNowMap = new LinkedHashMap<>();
        for (int i = 0; i < keyArr.length; i++) {
            stockNowMap.put(keyArr[i], stockNowArr[i]);
        }

        return stockNowMap;
    }

    /**
     * 获取需要同步的股票的列表
     * 包含代码, 名称, 交易所等内容
     *
     * @return
     */
    public List<Map<String, String>> getStockSyncList() {
        List<Map<String, Object>> getSyncStockIdList = tradingDataMapper.getStockSyncList();
        return ListConverter.mapValueToString(getSyncStockIdList);
    }

    /**
     * 从缓存中获取股票价格
     *
     * @return
     */
    public List<Map<String, String>> getSyncStockPrice() {
        StockNowPriceCacheList cacheList = new StockNowPriceCacheList();
        List<Map<String, String>> syncNowPriceList = cacheList.getAll();
        if (syncNowPriceList == null || syncNowPriceList.isEmpty()) {
            //需要重建缓存
            StockNowTask stockNowTask = new StockNowTask();
            stockNowTask.runJob();
        }
        List<Map<String, String>> returnList = new ArrayList<>();
        for (Map<String, String> map : syncNowPriceList) {
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("stockId", map.get("股票代码"));
            returnMap.put("stockName", map.get("股票名字"));
//            returnMap.put("zs", map.get("昨收"));
            returnMap.put("zxj", map.get("收盘价"));
            returnMap.put("zf", map.get("涨跌%"));
            returnMap.put("cjl", map.get("成交量"));
            returnMap.put("cje", map.get("成交额"));
            returnMap.put("sj", map.get("请求时间"));
            returnList.add(returnMap);
        }
        return returnList;
    }

    /**
     * 更新监控状态
     *
     * @param stockId
     * @return 1=已关闭 2=已开启
     */
    public int updateSyncStatus(String stockId) {
        StockNowPriceCacheList cacheList = new StockNowPriceCacheList();

        // 先查有没有
        List<Map<String, String>> stockSyncList = getStockSyncList();
        for (Map<String, String> syncStockMap : stockSyncList) {
            String syncStockId = syncStockMap.get("stock_id");
            if (syncStockId.equals(stockId)) {
                // 有的话就关掉, 然后删缓存
                tradingDataMapper.updateSyncOff(stockId);
                cacheList.remove(stockId);
                return 1;
            }
        }

        // 没有的话就更新状态, 再调用接口更新缓存
        tradingDataMapper.updateSyncOn(stockId);
        getStockRecentDayMA(stockId);   // 持久化最新的均线数据
        getStockMacdInTenDays(stockId); // 持久化最近十天的MACD
        getPrice(stockId);              // 持久化最新价格
        Map<String, String> stockNowMap = getExchangeIdPrefixStockNow(stockId);
        cacheList.add(stockNowMap);
        return 2;
    }

    /**
     * 获取需要同步的股票的列表
     * 只包含股票代码
     *
     * @return
     */
    public List<String> getSyncStockIdList() {
        List<Map<String, String>> preStockMapList = getStockSyncList();
        List<String> preStockList = new ArrayList<>();
        for (Map<String, String> map : preStockMapList) {
            preStockList.add(map.get("stock_id"));
        }
        return preStockList;
    }

    /**
     * 持久化股票列表
     * 默认ifSync=0
     */
    public List<String> syncStockList() {

        // 获取已有的列表
        List<String> syncStockIdList = getSyncStockIdList();

        MairuiApi api = new MairuiApi();
        List<Map<String, String>> hsList = api.getHSList();
        List<StockEntity> stockList = new ArrayList<>();
        List<String> syncList = new ArrayList<>();
        for (Map<String, String> stockMap : hsList) {
            String stockId = stockMap.get("dm");
            // 排除掉已有的股票
            if (!syncStockIdList.contains(stockId)) {
                StockEntity entity = new StockEntity();
                entity.setStockId(stockId);
                entity.setStockName(stockMap.get("mc"));
                entity.setExchangeId(stockMap.get("jys"));
                entity.setIfSync("0");
                stockList.add(entity);
                syncList.add(stockId);
            }
        }
        tradingDataMapper.stockAdd(stockList);

        return syncList;
    }

    public int getStockFromCache(String stockId) {
        List<Map<String, Object>> stockList = tradingDataMapper.getStock(stockId);
        if (stockList == null || stockList.isEmpty()) {
            return -1;
        }
        Map<String, String> stockNowMap = getStockNow(stockId);
        StockNowPriceCacheList cacheList = new StockNowPriceCacheList();
        cacheList.add(stockNowMap);

        return 1;
    }

    /**
     * 获取历史每日价格
     *
     * @param stockId
     */
    public int getHistoryPrice(String stockId) {
        LOG.info("获取" + stockId + "的历史价格开始");

        MairuiApi api = new MairuiApi(stockId);
        List<Map<String, String>> historyPriceList = api.getHistoryPrice("dn");

        if (historyPriceList == null) {
            return -1;
        }

        List<PriceEntity> priceList = new ArrayList<>();
        for (Map<String, String> map : historyPriceList) {
            PriceEntity entity = new PriceEntity();
            String indicatorDate = map.get("d").replaceAll("-", "");
            entity.setId(stockId + indicatorDate);
            entity.setStockId(stockId);
            entity.setIndicatorDate(indicatorDate);
            entity.setKpj(map.get("o"));
            entity.setZgj(map.get("h"));
            entity.setZdj(map.get("l"));
            entity.setSpj(map.get("c"));
            entity.setCjl(map.get("v"));
            entity.setCje(map.get("e"));
            entity.setZf(map.get("zf"));
            entity.setHsl(map.get("hs"));
            entity.setZdf(map.get("zd"));
            entity.setZde(map.get("zde"));
            priceList.add(entity);
        }

        tradingDataMapper.priceAdd(priceList);
        LOG.info("获取" + stockId + "的历史价格成功");
        return 1;
    }

    /**
     * 获取历史每日价格
     */
    public void getHistoryPrice() {
        List<String> syncStockIdList = getSyncStockIdList();
        for (String stockId : syncStockIdList) {
            getHistoryPrice(stockId);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取今日价格
     *
     * @param stockId
     */
    public void getPrice(String stockId) {
        MairuiApi api = new MairuiApi(stockId);
        Map<String, String> priceMap = api.getPrice("dn");

        if (priceMap == null || priceMap.isEmpty()) {
            LOG.info("获取价格出错, 可能是licence已经失效");
            return;
        }

        PriceEntity entity = new PriceEntity();
        String indicatorDate = priceMap.get("d").replaceAll("-", "");
        entity.setId(stockId + indicatorDate);
        entity.setStockId(stockId);
        entity.setIndicatorDate(indicatorDate);
        entity.setKpj(priceMap.get("o"));
        entity.setZgj(priceMap.get("h"));
        entity.setZdj(priceMap.get("l"));
        entity.setSpj(priceMap.get("c"));
        entity.setCjl(priceMap.get("v"));
        entity.setCje(priceMap.get("e"));
        entity.setZf(priceMap.get("zf"));
        entity.setHsl(priceMap.get("hs"));
        entity.setZdf(priceMap.get("zd"));
        entity.setZde(priceMap.get("zde"));
        List<PriceEntity> priceList = new ArrayList<>();
        priceList.add(entity);
        tradingDataMapper.priceAdd(priceList);
    }

    public void getPrice() {
        List<String> syncStockIdList = getSyncStockIdList();
        for (String stockId : syncStockIdList) {
            getPrice(stockId);
        }
    }
}
