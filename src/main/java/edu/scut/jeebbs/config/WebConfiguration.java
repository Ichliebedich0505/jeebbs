package edu.scut.jeebbs.config;

import edu.scut.jeebbs.web.ApiErrorAttributes;
import edu.scut.jeebbs.web.ApiExceptionHandler;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class WebConfiguration {
    private final ServerProperties serverProperties;

    public WebConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    public ApiErrorAttributes apiErrorAttributes() {
        return new ApiErrorAttributes(
                this.serverProperties.getError().isIncludeException());
    }

    @Bean
    public ApiExceptionHandler apiExceptionHandler(ErrorAttributes errorAttributes) {
        return new ApiExceptionHandler(errorAttributes, serverProperties.getError());
    }
}
