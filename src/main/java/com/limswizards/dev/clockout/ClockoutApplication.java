package com.limswizards.dev.clockout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.*;

@SpringBootApplication
public class ClockoutApplication {

    private static final Logger logger = Logger.getLogger("Clockout.SpringApp");

    public static void main(String[] args) {
        SpringApplication.run(ClockoutApplication.class, args);
        logger.info("SpringApp running...");
    }

}
