package com.mitchtalmadge.apps.discord.professor_doge.service;

import com.mitchtalmadge.apps.discord.professor_doge.event.EventDistributor;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.security.auth.login.LoginException;

@Service
public class DiscordService {

    @Value("${DISCORD_TOKEN}")
    private String discordToken;

    /**
     * The JDA (Discord API) instance.
     */
    private JDA jda;

    private final LogService logService;
    private final ConfigurableApplicationContext applicationContext;
    private final EventDistributor eventDistributor;

    @Autowired
    public DiscordService(LogService logService,
                          ConfigurableApplicationContext applicationContext,
                          EventDistributor eventDistributor) {
        this.logService = logService;
        this.applicationContext = applicationContext;
        this.eventDistributor = eventDistributor;
    }

    @PostConstruct
    private void init() throws LoginException {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(discordToken)
                    .addEventListener(eventDistributor)
                    .buildBlocking();
        } catch (LoginException e) {
            throw e;
        } catch (InterruptedException e) {
            logService.logException(getClass(), e, "JDA was interrupted while logging in");
        } catch (RateLimitedException e) {
            logService.logException(getClass(), e, "JDA could not login due to rate limiting");
        }
    }

    @PreDestroy
    private void destroy() {
        if (jda != null)
            jda.shutdown();
    }

}
