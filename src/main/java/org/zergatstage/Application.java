package org.zergatstage;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * @author father
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.zergatstage.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
