package com.mitchtalmadge.apps.discord.professor_doge.service;

import com.mitchtalmadge.apps.discord.professor_doge.ProfessorDoge;
import com.mitchtalmadge.apps.discord.professor_doge.event.EventDistributor;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.security.auth.login.LoginException;

@Service
public class DiscordService {

    /**
     * The token for the Discord Bot that is to be signed in.
     */
    private static final String DISCORD_TOKEN = System.getenv("DISCORD_TOKEN");

    /**
     * The JDA (Discord API) instance.
     */
    private JDA jda;

    private final EventDistributor eventDistributor;

    @Autowired
    public DiscordService(EventDistributor eventDistributor) {
        this.eventDistributor = eventDistributor;
    }

    @PostConstruct
    private void init() {
        try {
            // Can't start JDA if we don't have a token.
            if (DISCORD_TOKEN == null || DISCORD_TOKEN.isEmpty()) {
                System.err.println("ERROR: Discord Token not found. Cannot start. Exiting.");
                ProfessorDoge.shutdown();
            } else {
                jda = new JDABuilder(AccountType.BOT)
                        .setToken(DISCORD_TOKEN)
                        .addEventListener(eventDistributor)
                        .buildBlocking();
            }
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void destroy() {
        if (jda != null)
            jda.shutdown();
    }

}
