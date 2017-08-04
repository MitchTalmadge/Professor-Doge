package com.mitchtalmadge.apps.discord.professor_doge;

import com.mitchtalmadge.apps.discord.professor_doge.util.InheritedComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "com.mitchtalmadge.apps.discord.professor_doge",
        includeFilters = @ComponentScan.Filter(InheritedComponent.class))
@SpringBootApplication
public class ProfessorDoge {

    /**
     * The ApplicationContext for the running Spring Boot application.
     */
    private static ApplicationContext applicationContext;

    public static void main(String... args) {
        applicationContext = SpringApplication.run(ProfessorDoge.class, args);
    }

    /**
     * Shuts the application down gracefully.
     */
    public static void shutdown() {
        SpringApplication.exit(applicationContext);
    }

}
