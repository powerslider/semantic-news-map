package com.ontotext.semnews.service;

import com.google.common.collect.Maps;
import com.ontotext.semnews.model.RepositoryConfiguration;
import info.aduna.io.IOUtil;
import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 09-May-2016
 */
@Service
@EnableConfigurationProperties(RepositoryConfiguration.class)
public class SparqlService {

    public static final Logger LOG = LoggerFactory.getLogger(SparqlService.class);

    @Autowired
    private RepositoryConfiguration repositoryConfiguration;

    public Repository getRepository(){
        Repository repository = new HTTPRepository(
                repositoryConfiguration.getSesameServer(),
                repositoryConfiguration.getRepositoryId());
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

        protected Map<String, List<String>> executeQueryAndGetBindings(String sparqlFileName, Function<String, String> replacePlaceholdersOperator) {

            String queryString = null;
            try {
                queryString = readQueryString(sparqlFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            queryString = replacePlaceholdersOperator.apply(queryString);
            TupleQueryResult tqr = null;
            try {
                tqr = prepareAndEvaluate(queryString);
            } catch (QueryEvaluationException | MalformedQueryException e) {
                LOG.error("Error evaluating query", e);
            } catch (RepositoryException e) {
                LOG.error("Repository error", e);
            }
            return Optional.ofNullable(tqr)
                    .map(SparqlService.this::getResultsForAllQueryBindings)
                    .orElse(Maps.newHashMap());
        }

        protected abstract R doInConnection() throws RepositoryException;
    }

    /**
     * Read query string from SPARQL file.
     *
     * @param fileName file containing the SPARQL query
     * @return SPARQL query string
     * @throws IOException
     */
    public String readQueryString(String fileName) throws IOException {
        return IOUtil.readString(
                getClass().getResourceAsStream("/sparql-queries/" + fileName + ".sparql"));
    }

    /**
     * Gets all projected values of the query as key-value pairs (binding-collection of its values)
     *
     * @param tqr {@link TupleQueryResult} instance which contains a list of all projected values
     * @return query results a map of (binding-collection of its values)
     */
    public Map<String, List<String>> getResultsForAllQueryBindings(TupleQueryResult tqr) {
        Map<String, List<String>> resultMap = new HashMap<>();

        String currentBinding = null;
        try {
            List<String> bindings = tqr.getBindingNames();
            bindings.forEach(b -> resultMap.put(b, new ArrayList<>()));

            while (tqr.hasNext()) {
                BindingSet bs = tqr.next();
                for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
                    currentBinding = entry.getKey();
                    List<String> values = entry.getValue();
                    Value value = bs.getValue(currentBinding);
                    if (value != null) {
                        values.add(value.stringValue());
                    } else {
                        values.add(null);
                    }
                }
            }
        } catch (QueryEvaluationException e) {
            LOG.error("Failed to get results for binding ?{}. Query results corrupted?!", currentBinding);
        } finally {
            closeQueryResult(tqr);
        }
        return resultMap;
    }

    private void closeQueryResult(TupleQueryResult tqr) {
        try {
            if (tqr != null) {
                tqr.close();
            }
        } catch (QueryEvaluationException e) {
            LOG.error("Error closing tuple query result!");
        }
    }
}
