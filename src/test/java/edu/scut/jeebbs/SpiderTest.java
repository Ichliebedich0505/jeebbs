package edu.scut.jeebbs;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import static java.lang.Long.parseLong;
import static org.springframework.util.StringUtils.isEmpty;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SpiderTest {

    private static final String ARTICAL_SOURCE = "souhu";
    private static final String ARTICAL_URL = "(//www\\.sohu\\.com/a/\\w+)";
    private static final String ARTICAL_XPATH = "//div[@class='wrapper-box']" +
                                                "/div[@id='article-container']" +
                                                "/div[@class='left main']" +
                                                "/div[@class='text']";

    class SohuNewsPageProcessor implements PageProcessor {
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
            } else {
                page.putField("id", counter.incrementAndGet());
            }
            page.putField("href", page.getUrl().toString());
            page.putField("abstracts", page.getHtml()
                    .xpath("//head/meta[@name='description']/@content")
                    .toString()
                    .trim());
            String timestamp = page.getHtml()
                    .xpath(ARTICAL_XPATH +
                    "/div[@class=article-info]" +
                    "/span/@data-val").toString();
            page.putField("date", isEmpty(timestamp) ? new Date() : new Date(parseLong(timestamp)));
            log.info("{}", page.getResultItems().toString());
        }

        @Override
        public Site getSite() {
            return site;
        }
    }

    private AtomicInteger counter = new AtomicInteger();
    @Test
    public void testSohuSpider() {
        Spider.create(new SohuNewsPageProcessor())
                .addUrl("http://business.sohu.com/")
                .thread(5)
                .run();
    }
}
