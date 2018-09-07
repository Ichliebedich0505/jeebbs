package edu.scut.jeebbs.spider.processor;

import edu.scut.jeebbs.config.properties.SpiderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.Date;

import static java.lang.Long.parseLong;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@EnableConfigurationProperties(SpiderProperties.class)
public class SohuNewsPageProcessor extends BasePageProcessor {
    private static final String ARTICAL_SOURCE = "souhu";
    private static final String ARTICAL_URL = "(//www\\.sohu\\.com/a/\\w+)";
    private static final String ARTICAL_XPATH = "//div[@class='wrapper-box']" +
            "/div[@id='article-container']" +
            "/div[@class='left main']" +
            "/div[@class='text']";

    @Autowired
    public SohuNewsPageProcessor(SpiderProperties properties) {
        super(properties);
    }

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
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
    public String getDomain() {
        return "business.sohu.com";
    }
}
