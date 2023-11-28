package wang.ultra.my_utilities.common.scheduler.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.scheduler.service.QuartzService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/common/scheduler/quartz")
public class QuartzController {

    private static final Log LOG = LogFactory.getLog(QuartzController.class);

    @Autowired
    QuartzService quartzService;

    @GetMapping("getRunningList")
    public AjaxUtils getRunningList() {
        List<Map<String, String>> list = quartzService.getRunningList();
        if (list != null) {
            return AjaxUtils.success(list);
        }
        return AjaxUtils.failed();
    }

    @GetMapping("getAllList")
    public AjaxUtils getAllList() {
        List<Map<String, String>> jobList = quartzService.getAllJob();
        List<Map<String, String>> runningList = quartzService.getRunningList();

        List<Map<String, String>> returnList = new ArrayList<>();
        for (Map<String, String> jobMap : jobList) {
            String jobName = jobMap.get("job_name");
            String triggerName = jobMap.get("trigger_name");
            String cronExpression = jobMap.get("cron_expression");
            String jobClass = jobMap.get("job_class");
            String jobNote = jobMap.get("job_note");
            String jobStatus = "0";
            for (Map<String, String> runningMap : runningList) {
                String runningJobName = runningMap.get("JobName");
                if (runningJobName.equals(jobName)) {
                    jobStatus = "1";
                }
            }
            Map<String, String> returnMap = new LinkedHashMap<>();
            returnMap.put("JobName", jobName);
            returnMap.put("TriggerName", triggerName);
            returnMap.put("CronExpression", cronExpression);
            returnMap.put("JobClass", jobClass);
            returnMap.put("JobNote", jobNote);
            returnMap.put("JobStatus", jobStatus);
            returnList.add(returnMap);
        }

        return AjaxUtils.success(returnList);
    }

    @GetMapping("manualStart")
    public AjaxUtils manualStart(String jobName) {
        Map<String, String> jobMap = quartzService.getJob(jobName);
        if (jobName == null || jobName.isEmpty() || jobMap == null || jobMap.isEmpty()) {
            return AjaxUtils.failed("任务名错误! ");
        }

        Thread thread = new Thread(() -> {
            Class<Job> clazz;
            try {
                String jobClass = jobMap.get("job_class");
                clazz = (Class<Job>) Class.forName(jobClass);
                Method method = clazz.getMethod("runJob");
                method.invoke(clazz.newInstance());
            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                     InstantiationException | NoSuchMethodException e) {
            LOG.info("手动执行 " + jobName + " 发生错误!!! ");
            }
        });
        thread.setName("手动执行 " + jobName + " 的多线程入口");
        thread.start();

        return AjaxUtils.success("手动执行 " + jobName + " 已开始... ");
    }

    @GetMapping("start")
    public AjaxUtils quartzStart(String jobName) {
        int i = quartzService.startTask(jobName);
        return switch (i) {
            case -2 -> AjaxUtils.failed(jobName + " 不存在! ", -1);
            case -1 -> AjaxUtils.failed(jobName + " 启动失败! ", -1);
            case 0 -> AjaxUtils.success(jobName + " 已被启动! ", 1);
            case 1 -> AjaxUtils.success(jobName + " 启动成功! ", 1);
            default -> AjaxUtils.success(jobName + " 被小鲨鱼吃了! ", 0);
        };
    }

    @GetMapping("stop")
    public AjaxUtils quartzStop(String jobName) {
        int i = quartzService.stopTask(jobName);
        return switch (i) {
            case -2 -> AjaxUtils.failed(jobName + " 不存在! ", -1);
            case -1 -> AjaxUtils.failed(jobName + " 停止失败! ", -1);
            case 0 -> AjaxUtils.success(jobName + " 已被停止! ", 1);
            case 1 -> AjaxUtils.success(jobName + " 停止成功! ", 1);
            default -> AjaxUtils.success(jobName + " 被小鲨鱼吃了! ", 0);
        };
    }
}
