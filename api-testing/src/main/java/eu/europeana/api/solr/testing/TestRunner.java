/**
 * 
 */
package eu.europeana.api.solr.testing;

import java.util.List;

import eu.europeana.api.solr.testing.result.FieldTestResult;
import eu.europeana.api.solr.testing.result.TestResult;
import eu.europeana.api.solr.testing.result.TestResultModel;
import eu.europeana.api.solr.testing.tests.TestCase;
import eu.europeana.api.solr.testing.tests.TestConfig;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class TestRunner
{
    public TestResultModel runTests(TestConfig cfg)
    {
        TestResultModel model = new TestResultModel();
        for ( String field : cfg.fields )
        {
            model.fields.put(field, runTest(field, cfg.testsField, cfg));
        }
        
        for ( String query : cfg.facets )
        {
            model.facets.put(query, runTest(query, cfg.testsFacets, cfg));
        }

        for ( String query : cfg.queries )
        {
            model.queries.put(query, runTest(query, cfg.testsQuery, cfg));
        }

        return model;
    }

    protected FieldTestResult runTest(String field, List<TestCase> tests
                                    , TestConfig cfg)
    {
        FieldTestResult results = new FieldTestResult(field);
        for ( String instance : cfg.endpoints )
        {
            for ( TestCase tc : tests )
            {
                results.add(tc.run(new TestResult(instance, field), cfg));
            }
        }

        int size = tests.size();
        int i = 0;
        for ( TestCase tc : tests )
        {
            TestResult tr1    = results.get(i);
            TestResult tr2    = results.get(i + size);
            TestResult result = new TestResult(null, tr2.field);
            results.add(tc.compare(tr1, tr2, result, cfg));
            i++;
        }

        return results;
    }
}
