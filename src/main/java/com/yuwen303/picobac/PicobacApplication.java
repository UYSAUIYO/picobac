package com.yuwen303.picobac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,DataSourceAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class PicobacApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicobacApplication.class, args);
    }

}
