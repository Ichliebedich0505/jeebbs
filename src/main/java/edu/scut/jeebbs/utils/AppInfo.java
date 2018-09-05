package edu.scut.jeebbs.utils;

import com.jcabi.manifests.Manifests;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tool for gathering application info
 */
public class AppInfo {

    /**
     * extract '${project.artifactId}' from manifest(Implementation-Title) or other place
     * @return '${project.artifactId}'
     */
    public static String getApplicationName() {
        return getValue("jeebbs-info-name");
    }

    /**
     * extract '${project.version}' from manifest(Implementation-Version) or other place
     * @return '${project.version}'
     */
    public static String getApplicationVersion() {
        return getValue("jeebbs-info-version");
    }

    /**
     * extract '${buildNumber}' from manifest or other place
     * @return '${buildNumber}'
     */
    public static String getBuildRevision() {
        return getValue("jeebbs-info-buildRevision");
    }

    /**
     * extract '${maven.build.timestamp}' from manifest or other place
     * @return '${maven.build.timestamp}'
     */
    public static OffsetDateTime getBuildTime() {
        try {
            return OffsetDateTime.parse(getValue("jeebbs-info-date"), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeException e) {
            return OffsetDateTime.now();
        }
    }

    private static String getValue(String key) {
        try {
            return Manifests.read(key);
        } catch (IllegalArgumentException e) {
            return "MANIFEST_WAS_NOT_FOUND";
        }
    }
}
