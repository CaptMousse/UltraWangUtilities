package wang.ultra.my_utilities.stock_exchange.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import wang.ultra.my_utilities.common.scheduler.service.BaseJobService;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;
import wang.ultra.my_utilities.stock_exchange.service.StockTradingDataService;
import wang.ultra.my_utilities.stock_exchange.utils.TradingDaysUtils;

import java.util.List;

public class StockRecentDayMATask implements BaseJobService {
    private static final Log LOG = LogFactory.getLog(StockRecentDayMATask.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (!TradingDaysUtils.getTradingDay()) {
            LOG.info("今天不是交易日, 不获取均线数据... ");
        }

        runJob();
    }

    @Override
    public void runJob() {
        StockTradingDataService service = SpringBeanUtils.getBean(StockTradingDataService.class);
        List<String> syncStockIdList = service.getSyncStockIdList();
        for (String stockId : syncStockIdList) {
            LOG.info("获取" + stockId + "的均线开始...");
            service.getStockRecentDayMA(stockId);
            LOG.info("获取" + stockId + "的均线结束...");
        }
    }
}
