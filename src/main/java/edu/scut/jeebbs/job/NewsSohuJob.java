package edu.scut.jeebbs.job;

import edu.scut.jeebbs.config.properties.SpiderProperties;
import edu.scut.jeebbs.repository.NewsRepository;
import edu.scut.jeebbs.spider.pipeline.NewsRepositoryPipeline;
import edu.scut.jeebbs.spider.processor.SohuNewsPageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import us.codecraft.webmagic.Spider;

import javax.validation.Validator;

@EnableConfigurationProperties(SpiderProperties.class)
@Slf4j
public class NewsSohuJob implements Job {

    private static final String URL = "http://business.sohu.com/";

    @Autowired
    private NewsRepository repository;

    @Autowired
    private Validator validator;

    @Autowired
    private SpiderProperties spiderProperties;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start 'news_sohu_job'");
        Spider.create(new SohuNewsPageProcessor(spiderProperties))
                .addUrl(URL)
                .addPipeline(new NewsRepositoryPipeline(repository, validator))
                .thread(spiderProperties.getThread());
                //.run();
        log.info("finished 'news_sohu_job'");

    }
}
