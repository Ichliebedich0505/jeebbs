package edu.scut.jeebbs.config;

import edu.scut.jeebbs.common.quartz.InitCornJobRunner;
import edu.scut.jeebbs.common.quartz.QuartzManager;
import edu.scut.jeebbs.common.quartz.SpringJobFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfiguration {

    @Bean
    public SpringJobFactory springJobFactory() {
        return new SpringJobFactory();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(SpringJobFactory springJobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(springJobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public QuartzManager quartzManager(SchedulerFactoryBean schedulerFactoryBean) {
        return new QuartzManager(schedulerFactoryBean);
    }

    @Bean
    public InitCornJobRunner initQuartzTaskRunner() {
        return new InitCornJobRunner();
    }
}
