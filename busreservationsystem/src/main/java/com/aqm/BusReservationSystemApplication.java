package com.aqm;

import com.aqm.console.ConsoleRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BusReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusReservationSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ConsoleRunner consoleRunner) {
        return args -> consoleRunner.run();
    }
}

