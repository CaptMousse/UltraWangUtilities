package wang.ultra.my_utilities.stock_exchange.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;
import wang.ultra.my_utilities.stock_exchange.service.StockTradingDataService;

import java.util.List;
import java.util.Map;

public class StockRecentDayMATask implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(StockRecentDayMATask.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        getStockRecentDayMA();
    }

    private static void getStockRecentDayMA() {
        StockTradingDataService service = SpringBeanUtils.getBean(StockTradingDataService.class);
        List<Map<String, String>> stockSyncList = service.getStockSyncList();
        for (Map<String, String> stockMap : stockSyncList) {
            String stockId = stockMap.get("stock_id");
            LOG.info("获取" + stockId + "的均线开始...");
            service.getStockRecentDayMA(stockId);
            LOG.info("获取" + stockId + "的均线结束...");
        }
    }
}
