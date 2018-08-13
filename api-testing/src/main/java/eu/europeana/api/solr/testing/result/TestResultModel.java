/**
 * 
 */
package eu.europeana.api.solr.testing.result;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVPrinter;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class TestResultModel
{
    public Map<String,FieldTestResult> fields
        = new TreeMap<String,FieldTestResult>();

    public Map<String,FieldTestResult> facets
        = new TreeMap<String,FieldTestResult>();

    public Map<String,FieldTestResult> queries
        = new TreeMap<String,FieldTestResult>();
    

    public void print(CSVPrinter p) throws IOException
    {
        for ( FieldTestResult tr : fields.values()  ) { tr.print(p); }
        for ( FieldTestResult tr : facets.values()  ) { tr.print(p); }
        for ( FieldTestResult tr : queries.values() ) { tr.print(p); }
    }
}
