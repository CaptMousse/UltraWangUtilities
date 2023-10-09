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

public class Stock10DaysMACDTask implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(Stock10DaysMACDTask.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        getStock10DaysMACD();
    }

    private static void getStock10DaysMACD() {
        StockTradingDataService service = SpringBeanUtils.getBean(StockTradingDataService.class);
        List<Map<String, String>> stockSyncList = service.getStockSyncList();
        for (Map<String, String> stockMap : stockSyncList) {
            String stockId = stockMap.get("stock_id");
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
