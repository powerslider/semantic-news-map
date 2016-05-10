package com.ontotext.semnews;

import com.ontotext.semnews.model.RepositoryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov@ontotext.com>
 * @since 15-Apr-2016
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SemanticNewsMapApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SemanticNewsMapApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(SemanticNewsMapApplication.class, args);
    }
}
