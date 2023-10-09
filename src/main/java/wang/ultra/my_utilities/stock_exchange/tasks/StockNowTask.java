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

public class StockNowTask implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(StockNowTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        getStockNow();
    }

    public void getStockNow() {
        StockTradingDataService service = SpringBeanUtils.getBean(StockTradingDataService.class);
        List<Map<String, String>> stockSyncList = service.getStockSyncList();
        for (Map<String, String> stockMap : stockSyncList) {
            String stockId = stockMap.get("exchange_id") + stockMap.get("stock_id");

            Thread t = new Thread(() -> {
                LOG.info("获取" + stockId + "的实时数据开始...");
                service.getStockNow(stockId);
                LOG.info("获取" + stockId + "的实时数据结束...");
            });
            t.setName("获取" + stockId + "实时数据的多线程");
            t.start();
        }
    }
}
