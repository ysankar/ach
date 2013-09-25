package com.ach.config;

import org.springframework.context.annotation.*;

@Configuration
@ImportResource(
        {
        "classpath:META-INF/spring/applicationContext.xml",
                "classpath:META-INF/spring/ach-batch.xml",
                "classpath:META-INF/spring/ach-domain.xml",
                "classpath:META-INF/spring/ach-repository-jpa.xml",
                "classpath:META-INF/spring/ach-service.xml"
        } )
public class AppConfig{

}
