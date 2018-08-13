/**
 * 
 */
package eu.europeana.api.solr.testing.tests;

import static eu.europeana.api.solr.testing.utils.TestUtils.compareValues;
import static eu.europeana.api.solr.testing.utils.TestUtils.createQuery;
import static eu.europeana.api.solr.testing.utils.TestUtils.getTotals;
import static eu.europeana.api.solr.testing.utils.TestUtils.renderHL;
import eu.europeana.api.solr.testing.result.TestResult;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 31 Jul 2018
 */
public class DummyTest extends TestCase
{
    private static TestResult EMPTY_RESULT = new TestResult(null, null);

    public DummyTest() { super(null, 0); }

    public String getName() { return ""; }

    public TestResult run(TestResult result, TestConfig cfg)
    {
        return EMPTY_RESULT;
    }

    public TestResult compare(TestResult rs1, TestResult rs2
                            , TestResult result, TestConfig cfg)
    {
        return EMPTY_RESULT;
    }
}
