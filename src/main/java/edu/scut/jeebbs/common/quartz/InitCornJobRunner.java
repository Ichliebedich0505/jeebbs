package edu.scut.jeebbs.common.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

@Order(1)
public class InitCornJobRunner implements ApplicationRunner {

    @Value("${jeebbs.quartz.news.corn:\"0 0/10 6-23 * * ?\"}")
    private String newsTaskCorn;

    @Autowired
    private QuartzManager quartzManager;

    /**
     * init quartz tasks
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
