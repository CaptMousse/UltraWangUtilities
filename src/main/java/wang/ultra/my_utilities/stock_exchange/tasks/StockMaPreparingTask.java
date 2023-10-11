package wang.ultra.my_utilities.stock_exchange.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.cache.stockData.StockMaCacheList;
import wang.ultra.my_utilities.common.scheduler.service.BaseJobService;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;
import wang.ultra.my_utilities.stock_exchange.service.StockPreparingService;
import wang.ultra.my_utilities.stock_exchange.service.StockTradingDataService;
import wang.ultra.my_utilities.stock_exchange.utils.TradingDaysUtils;

import java.util.List;
import java.util.Map;

public class StockMaPreparingTask implements BaseJobService {

    private static final Logger LOG = LoggerFactory.getLogger(StockMaPreparingTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (!TradingDaysUtils.getTradingDay()) {
            LOG.info("今天不是交易日, 不准备均线数据... ");
        }

        runJob();
    }

    @Override
    public void runJob() {
        StockPreparingService preparingService = SpringBeanUtils.getBean(StockPreparingService.class);
        StockTradingDataService tradingService = SpringBeanUtils.getBean(StockTradingDataService.class);

        // 缓存清空
        StockMaCacheList cacheList = new StockMaCacheList();
        cacheList.clean();

        List<String> syncStockIdList = tradingService.getSyncStockIdList();
        for (String stockId : syncStockIdList) {
            LOG.info("准备" + stockId + "的均线数据开始...");
            preparingService.preparingMA(stockId);
            LOG.info("准备" + stockId + "的均线数据结束...");
        }
    }
}
