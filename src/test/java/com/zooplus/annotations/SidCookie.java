package com.zooplus.annotations;

import com.zooplus.extensions.SidCookieExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({SidCookieExtension.class})
public @interface SidCookie {

    String sidCookieValue();
}
