package edu.scut.jeebbs.common.quartz;

import edu.scut.jeebbs.job.NewsSohuJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

@Order(1)
public class InitCornJobRunner implements ApplicationRunner {

    @Value("${jeebbs.quartz.news.cron:0/5 * * * * ?}")
    private String newsJobCron;

    @Autowired
    private QuartzManager quartzManager;

    /**
     * init quartz tasks
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        quartzManager.addJob("news_sohu_job",
                "group_news_job",
                "news_trigger",
                "group_news_trigger",
                NewsSohuJob.class,
                newsJobCron);
    }
}
