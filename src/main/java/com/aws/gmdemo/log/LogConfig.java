package com.aws.gmdemo.log;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @Bean
    public WebServerFactoryCustomizer embeddedServletContainerCustomizer() {
        return container -> {
            if (container instanceof TomcatServletWebServerFactory) {
                ((TomcatServletWebServerFactory) container).addContextCustomizers((TomcatContextCustomizer) context -> {
                    context.getPipeline().addValve( new LogbackValve());
                });
            }
        };
    }
}