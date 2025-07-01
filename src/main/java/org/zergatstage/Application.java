package org.zergatstage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * @author father
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.zergatstage.repository")
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        log.info("Application started at: http://localhost:8080/quiz");
    }

}
