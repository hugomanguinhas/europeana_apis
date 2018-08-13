/**
 * 
 */
package eu.europeana.api.solr.testing.tests;

import static eu.europeana.api.solr.testing.utils.TestUtils.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import eu.europeana.api.solr.testing.result.TestResult;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class FacetValueTest extends TestCase
{

    public FacetValueTest(String filter, float tolerance)
    {
        super(filter, tolerance);
    }

    public String getName() { return "Facet Values"; }

    public TestResult run(TestResult result, TestConfig cfg)
    {
        String url = createQuery(cfg, result.instance, "*"
                               , "0", result.field, this._filter);
        List facets = (List)getFacets(url, cfg);
        result.value = facets;
        result.label = renderHL(url, facets == null ? "?" : facets.size());
        return result;
    }

    public TestResult compare(TestResult rs1, TestResult rs2
                            , TestResult result, TestConfig cfg)
    {
        if ( rs1.value == null || rs2.value == null )
        {
            result.value = null;
            result.label = "?";
            return result;
        }
        Integer value = compareValues(rs1.value, rs2.value);
        result.value = value;
        result.label = String.valueOf(value);
        return result;
    }

    private int compareValues(Object l1, Object l2)
    {
        Collection<String> values1 = getValues((List<Map>)l1);
        Collection<String> values2 = getValues((List<Map>)l2);
        Collection<String> diff    = new HashSet();

        for ( String value : values1 )
        {
            if ( !values2.contains(value) ) { diff.add(value); }
        }
        for ( String value : values2 )
        {
            if ( !values1.contains(value) ) { diff.add(value); }
        }
        return diff.size();
    }

    private Collection<String> getValues(List<Map> l)
    {
        Collection<String> values = new HashSet<String>();
        for ( Map m : l ) { values.add((String)m.get("label")); }
        return values;
    }
}
