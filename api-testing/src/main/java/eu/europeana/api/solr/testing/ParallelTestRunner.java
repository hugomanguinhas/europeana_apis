/**
 * 
 */
package eu.europeana.api.solr.testing;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import eu.europeana.api.solr.testing.result.TestResultModel;
import eu.europeana.api.solr.testing.tests.TestConfig;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 31 Jul 2018
 */
public class ParallelTestRunner extends TestRunner
{
    private int _threads;

    public ParallelTestRunner(int threads)
    {
        _threads = threads;
    }

    public TestResultModel runTests(TestConfig cfg)
    {
        Phaser          phaser = new Phaser();
        TestResultModel model  = new TestResultModel();
        ExecutorService exec   = getExecutor();

        try
        {
            phaser.register();

            for ( String field : cfg.fields )
            {
                phaser.register();
                exec.submit(new FieldTestTask(field, cfg, model, phaser));
            }

            for ( String facet : cfg.facets )
            {
                phaser.register();
                exec.submit(new FacetTestTask(facet, cfg, model, phaser));
            }

            for ( String query : cfg.queries )
            {
                phaser.register();
                exec.submit(new QueryTestTask(query, cfg, model, phaser));
            }

            phaser.arriveAndAwaitAdvance();
        }
        finally { exec.shutdown(); }

        return model;
    }

    private ExecutorService getExecutor()
    {
        return new ThreadPoolExecutor(
            _threads, _threads, 0L, TimeUnit.MILLISECONDS
          , new ArrayBlockingQueue<Runnable>(_threads * 10)
          , new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private abstract class TestRunTask implements Runnable
    {
        protected Phaser          _phaser;
        protected String          _field;
        protected TestResultModel _model;
        protected TestConfig      _cfg;

        public TestRunTask(String field, TestConfig cfg, TestResultModel model
                         , Phaser phaser)
        { 
            _field  = field;
            _cfg    = cfg;
            _model  = model;
            _phaser = phaser;
        }
    }

    private class FieldTestTask extends TestRunTask
    {
        public FieldTestTask(String field, TestConfig cfg,
                TestResultModel model, Phaser phaser)
        {
            super(field, cfg, model, phaser);
        }

        public void run()
        {
            try
            {
                System.out.println("Running (START) : " + _field);
                _model.fields.put(_field, runTest(_field,_cfg.testsField,_cfg));
                System.out.println("Running (FINISH): " + _field);
            }
            finally { _phaser.arriveAndDeregister(); }
        }
    }

    private class FacetTestTask extends TestRunTask
    {
        public FacetTestTask(String field, TestConfig cfg,
                TestResultModel model, Phaser phaser)
        {
            super(field, cfg, model, phaser);
        }

        public void run()
        {
            try
            {
                System.out.println("Running (START) : " + _field);
                _model.facets.put(_field, runTest(_field,_cfg.testsFacets,_cfg));
                System.out.println("Running (FINISH): " + _field);
            }
            finally { _phaser.arriveAndDeregister(); }
        }
    }

    private class QueryTestTask extends TestRunTask
    {
        public QueryTestTask(String field, TestConfig cfg,
                TestResultModel model, Phaser phaser)
        {
            super(field, cfg, model, phaser);
        }

        public void run()
        {
            try
            {
                System.out.println("Running (START) : " + _field);
                _model.queries.put(_field, runTest(_field,_cfg.testsQuery,_cfg));
                System.out.println("Running (FINISH): " + _field);
            }
            finally { _phaser.arriveAndDeregister(); }
        }
    }
}
