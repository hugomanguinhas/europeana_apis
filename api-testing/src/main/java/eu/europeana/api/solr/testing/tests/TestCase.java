/**
 * 
 */
package eu.europeana.api.solr.testing.tests;

import eu.europeana.api.solr.testing.result.TestResult;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public abstract class TestCase
{
    protected String _filter;
    protected float  _tolerance;

    public TestCase(String filter, float tolerance)
    {
        _filter    = filter;
        _tolerance = tolerance;
    }

    public String getFilter() { return _filter; }

    public abstract TestResult run(TestResult result, TestConfig cfg);

    public abstract TestResult compare(TestResult rs1, TestResult rs2
                                     , TestResult result, TestConfig cfg);
}
