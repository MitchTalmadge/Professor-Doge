package com.mitchtalmadge.apps.discord.professor_doge.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandPattern {

    String[] value();

    boolean strict() default false;

}
