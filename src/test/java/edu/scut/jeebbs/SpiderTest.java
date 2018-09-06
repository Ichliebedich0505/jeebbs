package edu.scut.jeebbs;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class SpiderTest {

    private static final String articalUrl = "//www\\.sohu\\.com/a/\\d+";

    class SohuNewsPageProcessor implements PageProcessor {
        private Site site = Site.me()
                .setDomain("http://business.sohu.com/")
                .setSleepTime(1000)
                .setRetryTimes(5)
                .setCharset("utf-8")
                .setTimeOut(30000);

        @Override
        public void process(Page page) {
            page.putField("href", page.getUrl().regex(articalUrl).toString());
        }

        @Override
        public Site getSite() {
            return null;
        }
    }
}
