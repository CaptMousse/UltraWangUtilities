package wang.ultra.my_utilities.blog.scheduler;

import wang.ultra.my_utilities.blog.runnable.BlogImageCleanRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlogImageCleanScheduler {


    public void imageCleaner() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new BlogImageCleanRunnable(), 0, 1, TimeUnit.DAYS);
    }
}
