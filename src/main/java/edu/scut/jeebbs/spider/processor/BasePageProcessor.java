package edu.scut.jeebbs.spider.processor;

import edu.scut.jeebbs.config.properties.SpiderProperties;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class BasePageProcessor implements PageProcessor {
    private Site site;
    private SpiderProperties spiderProperties;

    public BasePageProcessor(SpiderProperties properties) {
        this.spiderProperties = properties;
        this.site = Site.me().setDomain(getDomain())
                .setSleepTime(getSleepTime())
                .setRetryTimes(getRetryTimes())
                .setRetrySleepTime(getRetrySleepTimes())
                .setTimeOut(getTimeout())
                .setCharset(getCharset())
                .setUseGzip(isUseGzip())
                .setDisableCookieManagement(isDisableCookieManagement());
    }

    public abstract String getDomain();

    @Override
    public Site getSite() {
        return site;
    }

    public SpiderProperties getSpiderProperties() {
        return spiderProperties;
    }

    public int getSleepTime() {
        return spiderProperties.getSleepTime();
    }

    public int getRetryTimes() {
        return spiderProperties.getRetryTimes();
    }

    public int getRetrySleepTimes() {
        return spiderProperties.getRetrySleepTimes();
    }

    public int getTimeout() {
        return spiderProperties.getTimeout();
    }

    public String getCharset() {
        return spiderProperties.getCharset();
    }

    public boolean isUseGzip() {
        return spiderProperties.isUseGzip();
    }

    public boolean isDisableCookieManagement() {
        return spiderProperties.isDisableCookieManagement();
    }
}
