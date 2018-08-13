/**
 * 
 */
package eu.europeana.api.solr.testing.result;

import java.io.IOException;

import org.apache.commons.csv.CSVPrinter;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class TestResult
{
    public String  instance;
    public String  field;
    public Object  value;
    public String  label;

    public TestResult(String instance, String field)
    {
        this.instance = instance;
        this.field    = field;
    }

    public void print(CSVPrinter p) throws IOException
    {
        p.print(label);
    }
}
