package com.mitchtalmadge.apps.discord.professor_doge.util;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface InheritedComponent {
}
