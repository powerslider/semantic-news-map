package com.ontotext.semnews.service;

import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 09-May-2016
 */
@Service
public class SparqlService {

    public static final Logger LOG = LoggerFactory.getLogger(SparqlService.class);

    @Value("${sesame.server}")
    private String sesameServer;

    @Value("${repository.id}")
    private String repositoryID;

    public Repository getRepository(){
        Repository repository = new HTTPRepository(sesameServer, repositoryID);
        try {
            repository.initialize();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return repository;
    }

    /**
     * Do an operation in an open connection. <p>A class that allows you to open a connection to a repository, do an
     * operation with the connection without caring about closing the connection properly.
     *
     * WithConnection will also close any QueryResults quietly if you foget to do so.
     *
     * Here is an example
     * <p/>
     * <pre>
     * {@code
     *      repository.new WithConnection<Void>() {
     *           @Override
     *           protected Void doInConnection() throws RepositoryException {
     *              begin();
     *              // do something in a transaction
     *              TupleQueryResult result = prepareAndEvaluate("select * { ?a ?b ?c }");
     *              commit();
     *              return null; // we have no result from here
     *           }
     *      }.run();
     * }
     * </pre>
     *
     * @param <R>
     */
    public abstract class WithConnection<R> {

        RepositoryConnection connection = null;

        List<QueryResult<?>> results = new ArrayList<>(4);

        public WithConnection() {
            super();
        }

        public R run() {
            try {
                connection = getRepository().getConnection();
                return doInConnection();
            } catch (RepositoryException e) {
                throw new RuntimeException("There was an error while communicating with the repository", e);
            } finally {
                for (QueryResult<?> result : results) {
                    try {
                        result.close();
                    } catch (QueryEvaluationException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (RepositoryException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
        }

        protected void begin() throws RepositoryException {
            connection.begin();
        }

        protected void commit() throws RepositoryException {
            connection.commit();
        }

        protected boolean prepareAndEvaluateBoolean(String q) throws MalformedQueryException, RepositoryException, QueryEvaluationException {
            return connection.prepareBooleanQuery(QueryLanguage.SPARQL, q).evaluate();
        }

        protected TupleQueryResult prepareAndEvaluate(String q) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
            TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, q);
            TupleQueryResult result = query.evaluate();
            results.add(result);
            return result;
        }

        protected GraphQueryResult prepareAndEvaluateGraphQuery(String q) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
            GraphQuery query = connection.prepareGraphQuery(QueryLanguage.SPARQL, q);
            GraphQueryResult result = query.evaluate();
            results.add(result);
            return result;
        }

        protected void prepareAndExecuteUpdate(String u) throws MalformedQueryException, RepositoryException, UpdateExecutionException {
            connection.prepareUpdate(QueryLanguage.SPARQL, u).execute();
        }

        protected abstract R doInConnection() throws RepositoryException;
    }
}
