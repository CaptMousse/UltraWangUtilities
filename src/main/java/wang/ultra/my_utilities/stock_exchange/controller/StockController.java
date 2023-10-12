package wang.ultra.my_utilities.stock_exchange.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.cache.stockData.StockMaCacheList;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.stock_exchange.service.StockMaService;
import wang.ultra.my_utilities.stock_exchange.service.StockPreparingService;
import wang.ultra.my_utilities.stock_exchange.service.StockTradingDataService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/stockExchange")
public class StockController {

    private static final Logger LOG = LoggerFactory.getLogger(StockController.class);

    @Autowired
    StockTradingDataService stockTradingDataService;

    @Autowired
    StockPreparingService stockPreparingService;

    @Autowired
    StockMaService stockMaService;

    @GetMapping("getStockHistoryMA")
    public AjaxUtils getHistoryMA(String stockId) {

        if (stockId == null || stockId.isEmpty()) {
            return AjaxUtils.failed("股票代码有误");
        }
        stockTradingDataService.getStockHistoryDayMA(stockId);
        return AjaxUtils.success();
    }

    /**
     * 同步股票列表
     * 没事不要乱执行
     * @return
     */
    @GetMapping("syncStockList")
    public AjaxUtils syncStockList() {
        List<String> syncList = stockTradingDataService.syncStockList();
        return AjaxUtils.success("没事不要乱执行", syncList);
    }

    /**
     * 同步历史价格
     * @return
     */
    @GetMapping("syncHistoryPrice")
    public AjaxUtils syncHistoryPrice(String stockId) {
        int result = stockTradingDataService.getHistoryPrice(stockId);

        return switch (result) {
            case -1 -> AjaxUtils.failed("获取失败! ");
            case 1 -> AjaxUtils.success("获取成功! ");
            default -> AjaxUtils.success("也不知道成功了没~ ");
        };
    }

    @GetMapping("getTodayPrice")
    public AjaxUtils getTodayPrice() {
        List<String> syncStockIdList = stockTradingDataService.getSyncStockIdList();
        for (String stockId : syncStockIdList) {
            LOG.info("获取" + stockId + "的今日价格开始...");
            stockTradingDataService.getPrice(stockId);
            LOG.info("获取" + stockId + "的今日价格结束...");
        }

        return AjaxUtils.success();
    }

    @GetMapping("getNow")
    public AjaxUtils getNow(String stockId) {

        Map<String, String> stockNowMap = stockTradingDataService.getStockNow(stockId);

        stockId = stockId.substring(2);

        if (stockNowMap == null) {
            return AjaxUtils.failed("数据错误, 请检查! ");
        }
        stockMaService.calMa(stockId, stockNowMap);

        return AjaxUtils.success();
    }

    @GetMapping("testMa")
    public AjaxUtils testMa() {

        stockPreparingService.preparingMA("600919");

        StockMaCacheList stockMaCacheList = new StockMaCacheList();

        Map<String, String> maMap = stockMaCacheList.get("600919");

        return AjaxUtils.success(maMap);
    }

    @GetMapping("getCacheList")
    public AjaxUtils getCacheList() {
        StockMaCacheList stockMaCacheList = new StockMaCacheList();
        List<Map<String, String>> mapList = stockMaCacheList.getAll();
        return AjaxUtils.success(mapList);
    }
}
