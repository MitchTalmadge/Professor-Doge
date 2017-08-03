package com.mitchtalmadge.apps.discord.professor_doge;

import com.mitchtalmadge.apps.discord.professor_doge.event.EventDistributor;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

public class ProfessorDoge {

    private static final String DISCORD_TOKEN = System.getenv("DISCORD_TOKEN");

    public static void main(String... args) {

        if (DISCORD_TOKEN == null || DISCORD_TOKEN.isEmpty()) {
            System.err.println("ERROR: Discord Token not found. Cannot start. Exiting.");
            System.exit(-1);
        }

        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(DISCORD_TOKEN)
                    .addEventListener(new EventDistributor())
                    .buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }

    }

}
