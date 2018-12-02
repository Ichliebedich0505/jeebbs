package edu.scut.jeebbs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Component
@ConfigurationProperties(prefix = "jeebbs.spider")
@Data
public class SpiderProperties {
    /**
     * Set the interval between the processing of two pages.<br>
     * Time unit is micro seconds.<br>
     */
    private int sleepTime = 1000;

    /**
     * Get retry times immediately when download fail, 0 by default.<br>
     */
    private int retryTimes = 5;

    /**
     * Set retry sleep times when download fail. <br>
     */
    private int retrySleepTimes = 1000;

    /**
     * set timeout for downloader in ms
     */
    private int timeout = 30000;

    /**
     * Set charset of page manually.<br>
     * When charset is not set or set to null, it can be auto detected by Http header.
     */
    private String charset = "utf-8";

    /**
     * Whether use gzip. <br>
     * Default is true, you can set it to false to disable gzip.
     */
    private boolean useGzip = true;

    /**
     * Downloader is supposed to store response cookie.
     * Disable it to ignore all cookie fields and stay clean.
     * Warning: Set cookie will still NOT work if disableCookieManagement is true.
     */
    private boolean disableCookieManagement = false;

    /**
     * start with more than one threads
     */
    private int thread = 5;
}
