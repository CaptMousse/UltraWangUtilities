package wang.ultra.my_utilities.common.filter;

/**
 * 使用计数器算法的过滤器
 */
public class CurrentLimitingMonitorRunnable implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            CurrentLimitingFilter.setTimeLimiting(CurrentLimitingFilter.getTimeLimiting() - 10);
            if (CurrentLimitingFilter.getTimeLimiting() <= -2000000000l) {
                CurrentLimitingFilter.setTimeLimiting(0);
            }
        }
    }
}
