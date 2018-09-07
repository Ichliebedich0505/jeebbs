package edu.scut.jeebbs;


import edu.scut.jeebbs.domain.News;
import edu.scut.jeebbs.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
import java.util.Map;

import static java.lang.Long.parseLong;
import static org.springframework.util.StringUtils.isEmpty;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ActiveProfiles("dev")
@Slf4j
public class SpiderTest {

    class SohuNewsPageProcessor implements PageProcessor {
        private static final String ARTICAL_SOURCE = "souhu";
        private static final String ARTICAL_URL = "(//www\\.sohu\\.com/a/\\w+)";
        private static final String ARTICAL_XPATH = "//div[@class='wrapper-box']" +
                "/div[@id='article-container']" +
                "/div[@class='left main']" +
                "/div[@class='text']";
        private Site site = Site.me()
                .setDomain("business.sohu.com")
                .setSleepTime(1000)
                .setRetryTimes(5)
                .setCharset("utf-8")
                .setTimeOut(30000);

        @Override
        public void process(Page page) {
            page.addTargetRequests(page.getHtml().links().regex(ARTICAL_URL).all());
            page.putField("source", ARTICAL_SOURCE);
            page.putField("title", page.getHtml()
                    .xpath(ARTICAL_XPATH +
                    "/div[@class='text-title']" +
                    "/h1/text()").toString());
            if (page.getResultItems().get("title") == null){
                //skip this page
                page.setSkip(true);
            }
            page.putField("href", page.getUrl().toString());
            page.putField("abstracts", page.getHtml()
                    .xpath("//head/meta[@name='description']/@content")
                    .toString());
            String timestamp = page.getHtml()
                    .xpath(ARTICAL_XPATH +
                    "/div[@class=article-info]" +
                    "/span/@data-val").toString();
            page.putField("date", isEmpty(timestamp) ? new Date() : new Date(parseLong(timestamp)));
        }

        @Override
        public Site getSite() {
            return site;
        }
    }



    class MysqlPipeline<T> implements Pipeline {
        CrudRepository repository;
        Class<T> clazz;

        MysqlPipeline(Class<T> clazz, CrudRepository repository) {
            this.clazz = clazz;
            this.repository = repository;
        }

        /**
         * Process extracted results.
         *
         * @param resultItems resultItems
         * @param task        quartz
         */
        @Override
        public void process(ResultItems resultItems, Task task) {
            T bean = instance();
            Assert.notNull(bean, "can't create an instance of class: " + clazz.getName());
            BeanMap beanMap = BeanMap.create(bean);
            Map<String, Object> map = resultItems.getAll();
            beanMap.putAll(map);
            repository.save(bean);
        }

        private T instance() {
            return ReflectionUtils.createInstanceIfPresent(clazz.getName(), null);
        }
    }

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void testSohuSpider() {
        log.info("test");
        Spider.create(new SohuNewsPageProcessor())
                .addUrl("http://business.sohu.com/")
//                .addPipeline(new ConsolePipeline())
                .addPipeline(new MysqlPipeline<>(News.class, newsRepository))
                .thread(5)
                .run();

        newsRepository.findAll().forEach(System.out::println);
    }
}
