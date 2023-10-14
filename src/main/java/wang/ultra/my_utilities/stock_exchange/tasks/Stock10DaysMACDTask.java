package wang.ultra.my_utilities.stock_exchange.tasks;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.scheduler.service.BaseJobService;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;
import wang.ultra.my_utilities.stock_exchange.service.StockTradingDataService;
import wang.ultra.my_utilities.stock_exchange.utils.TradingDaysUtils;

import java.util.List;

public class Stock10DaysMACDTask implements BaseJobService {

    private static final Logger LOG = LoggerFactory.getLogger(Stock10DaysMACDTask.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (!TradingDaysUtils.getTradingDay()) {
            LOG.info("今天不是交易日, 不获取近10日的MACD数据... ");
        }

        runJob();
    }

    @Override
    public void runJob() {
        StockTradingDataService service = SpringBeanUtils.getBean(StockTradingDataService.class);
        List<String> syncStockIdList = service.getSyncStockIdList();
        for (String stockId : syncStockIdList) {
            LOG.info("获取" + stockId + "近10日的MACD开始...");
            service.getStockMacdInTenDays(stockId);
            LOG.info("获取" + stockId + "近10日的MACD结束...");
            try {   // 接口每分钟限制10次访问
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
