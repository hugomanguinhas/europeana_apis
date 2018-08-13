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
public class FakeFacetCountTest extends FieldCountTest
{

    public FakeFacetCountTest(String filter, float tolerance)
    {
        super(filter, tolerance);
    }

    public TestResult run(TestResult result, TestConfig cfg)
    {
        String url = createQuery(cfg, result.instance, "*:*"
                               , "0", null, this._filter, result.field + ":*");
        result.value = getTotals(url, cfg);
        result.label = renderHL(url, result.value);
        return result;
    }
}
