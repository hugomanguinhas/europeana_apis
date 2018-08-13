/**
 * 
 */
package eu.europeana.api.solr.testing.tests;

import static eu.europeana.api.solr.testing.utils.TestUtils.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import eu.europeana.api.solr.testing.result.TestResult;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class FacetSumTest extends TestCase
{

    public FacetSumTest(String filter, float tolerance)
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
        result.label = renderHL(url, facets == null ? "?" : sum(facets));
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
        Map<String,Integer> values1 = getValues((List<Map>)l1);
        Map<String,Integer> values2 = getValues((List<Map>)l2);

        Collection<String> diff = new TreeSet();
        diff.addAll(values1.keySet());
        diff.addAll(values2.keySet());

        Iterator<String> iter = diff.iterator();
        while ( iter.hasNext() )
        {
            String value = iter.next();
            int c1 = getCount(values1, value);
            int c2 = getCount(values2, value);
            if ( c1 == c2 ) { iter.remove(); }
        }
        return diff.size();
    }

    private int getCount(Map<String,Integer> map, String value)
    {
        Integer count = map.get(value);
        return ( count == null ? 0 : count );
    }

    private Map<String,Integer> getValues(List<Map> l)
    {
        Map<String,Integer> values = new HashMap<String,Integer>();
        for ( Map m : l )
        { 
            values.put((String)m.get("label"), (Integer)m.get("count"));
        }
        return values;
    }

    private Long sum(List<Map> l)
    {
        long sum = 0l;
        for ( Map m : l ) { sum += (Integer)m.get("count"); }
        return sum;
    }
}
