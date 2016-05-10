package com.ontotext.semnews.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov@ontotext.com>
 * @since 10-May-2016
 */
@ConfigurationProperties(prefix = "semnews")
public class RepositoryConfiguration {

    private String sesameServer;

    private String repositoryId;


    public String getSesameServer() {
        return sesameServer;
    }

    public void setSesameServer(String sesameServer) {
        this.sesameServer = sesameServer;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
