package edu.scut.jeebbs.common.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Slf4j
public class QuartzManager {

    private SchedulerFactoryBean schedulerFactoryBean;

    public QuartzManager(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    /**
     * Add a new cron job to quartz scheduler
     * @param jobName the name of job
     * @param jobGroupName the group name of job
     * @param triggerName the name of trigger
     * @param triggerGroupName the group name of trigger
     * @param jobClass the class of job
     * @param cron the cron
     */
    public void addJob(String jobName, String jobGroupName,
                       String triggerName, String triggerGroupName,
                       Class<? extends Job> jobClass, String cron) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            // create a job
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroupName).build();

            // create a job trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .startNow()
                    .build();

            // schedule the job
            scheduler.scheduleJob(jobDetail, trigger);

            // start job
            if (!scheduler.isShutdown()) {
                log.info("Add quartz cron task - {}, class: {}, cron: {}", jobName, jobClass.getName(), cron);
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
