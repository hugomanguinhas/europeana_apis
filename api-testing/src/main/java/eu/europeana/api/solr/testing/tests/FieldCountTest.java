/**
 * 
 */
package eu.europeana.api.solr.testing.tests;

import static eu.europeana.api.solr.testing.utils.TestUtils.*;
import eu.europeana.api.solr.testing.result.TestResult;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class FieldCountTest extends TestCase
{

    public FieldCountTest(String filter, float tolerance)
    {
        super(filter, tolerance);
    }

    public String getName() { return "Counts"; }

    public TestResult run(TestResult result, TestConfig cfg)
    {
        String url = createQuery(cfg, result.instance, result.field + ":*"
                               , "0", null, this._filter);
        result.value = getTotals(url, cfg);
        result.label = renderHL(url, result.value);
        return result;
    }

    public TestResult compare(TestResult rs1, TestResult rs2
                            , TestResult result, TestConfig cfg)
    {
        Integer value = compareValues((Integer)rs1.value, (Integer)rs2.value);
        result.value = value;
        result.label = String.valueOf(value);
        return result;
    }
}
