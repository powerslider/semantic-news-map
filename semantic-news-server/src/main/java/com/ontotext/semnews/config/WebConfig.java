package com.ontotext.semnews.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Application Spring MVC configuration.
 *
 * @author Tsvetan Dimitrov <tsvetan.dimitrov@ontotext.com>
 * @since 30-May-2016
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Maps all AngularJS /news-* routes to index so that they work with direct linking.
     */
    @Controller
    static class NewsRoutes {

        @RequestMapping("/news-*")
        public String index() {
            return "forward:/index.html";
        }
    }
}
