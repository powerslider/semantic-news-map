package com.ontotext.semnews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main Spring Boot application class for the Semantic News Map application.
 *
 * @author Tsvetan Dimitrov <tsvetan.dimitrov@ontotext.com>
 * @since 15-Apr-2016
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SemanticNewsMapApplication {

    public static void main(String args[]) {
        SpringApplication.run(SemanticNewsMapApplication.class, args);
    }
}
