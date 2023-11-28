package wang.ultra.my_utilities.common.scheduler.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.scheduler.service.QuartzService;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;

import java.util.List;
import java.util.Map;

public class ResumeQuartzController {

    private static final Log LOG = LogFactory.getLog(ResumeQuartzController.class);

    QuartzService quartzService = SpringBeanUtils.getBean(QuartzService.class);

    public void resume() {

        String schedulerSwitcher = ConstantFromFile.getSchedulerSwitcher();
        if (!"1".equals(schedulerSwitcher)) {
            LOG.info("定时任务自动启动已关闭");
            return;
        }

        LOG.info("定时任务恢复中... ");

        List<Map<String, String>> isRunningList = quartzService.getRunningJob();

        for (Map<String, String> schedulerMap : isRunningList) {
            LOG.info("定时任务 " + schedulerMap.get("job_name") + " 正在恢复中... ");
            try {
                quartzService.launchTask(schedulerMap);
                LOG.info("定时任务 " + schedulerMap.get("job_name") + " 恢复成功! ");
            } catch (SchedulerException e) {
                LOG.info("定时任务 " + schedulerMap.get("job_name") + " 恢复失败, 原因是: " + e);
            }
        }
    }
}
