/**
 * 
 */
package eu.europeana.api.solr.testing.result;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVPrinter;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class FieldTestResult extends ArrayList<TestResult>
{
    public String field;

    public FieldTestResult(String field) { this.field = field; }

    public void print(CSVPrinter p) throws IOException
    {
        p.print(field);
        for ( TestResult tr : this ) { tr.print(p); }
        p.println();
    }
}
