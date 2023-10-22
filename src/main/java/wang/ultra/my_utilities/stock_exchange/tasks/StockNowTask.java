package wang.ultra.my_utilities.stock_exchange.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wang.ultra.my_utilities.common.scheduler.service.BaseJobService;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;
import wang.ultra.my_utilities.stock_exchange.service.StockMaService;
import wang.ultra.my_utilities.stock_exchange.service.StockTradingDataService;
import wang.ultra.my_utilities.stock_exchange.utils.TradingDaysUtils;

import java.util.List;
import java.util.Map;

public class StockNowTask implements BaseJobService {

    private static final Logger LOG = LoggerFactory.getLogger(StockNowTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (!TradingDaysUtils.getTradingDay()) {
            LOG.info("今天不是交易日, 不获取实时数据... ");
        }

        runJob();
    }

    @Override
    public void runJob() {
        // AutoWired注解引入sercvice不管用, 要用bean来创建才行
        StockTradingDataService tradingDataService = SpringBeanUtils.getBean(StockTradingDataService.class);
        List<Map<String, String>> stockSyncList = tradingDataService.getStockSyncList();
        for (Map<String, String> stockMap : stockSyncList) {
            String stockId = stockMap.get("exchange_id") + stockMap.get("stock_id");

            Thread t = new Thread(() -> {
                LOG.info("获取" + stockId + "的实时数据开始...");
                Map<String, String> stockNowMap = tradingDataService.getStockNow(stockId);
                if (stockNowMap == null) {
                    LOG.info("获取" + stockId + "的实时数据失败...");
                }
                LOG.info("获取" + stockId + "的实时数据结束...");

                StockMaService maService = SpringBeanUtils.getBean(StockMaService.class);
                String stockIdSubstring = stockId.substring(2);
                maService.calMa(stockIdSubstring, stockNowMap);
            });
            t.setName("获取" + stockId + "实时数据的多线程");
            t.start();
        }
    }
}
