package com.mitchtalmadge.apps.discord.professor_doge;

import com.mitchtalmadge.apps.discord.professor_doge.service.DiscordService;
import com.mitchtalmadge.apps.discord.professor_doge.util.InheritedComponent;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "com.mitchtalmadge.apps.discord.professor_doge",
        includeFilters = @ComponentScan.Filter(InheritedComponent.class))
@SpringBootApplication
public class ProfessorDoge {

    public static void main(String... args) {
        try {
            ConfigurableApplicationContext applicationContext = SpringApplication.run(ProfessorDoge.class, args);
        } catch (BeanCreationException e) {
            if (e.getBeanName().equalsIgnoreCase(DiscordService.class.getSimpleName())) {
                System.err.println("ERROR: DiscordService failed to start. Is DISCORD_TOKEN defined?");
                System.exit(-1);
            } else {
                e.printStackTrace();
            }
        }
    }

}
