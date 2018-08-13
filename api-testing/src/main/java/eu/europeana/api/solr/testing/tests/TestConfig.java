/**
 * 
 */
package eu.europeana.api.solr.testing.tests;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class TestConfig
{
    public String         apikey      = "api2demo";
    public int            facetLimit  = 1000;
    public float          tolerance   = 0.2f;

    public List<String>   fields      = new ArrayList();
    public List<String>   facets      = new ArrayList();
    public List<String>   queries     = new ArrayList();

    public List<String>   endpoints   = new ArrayList(2);
    public List<String>   testSet     = new ArrayList();

    public List<TestCase> testsField  = new ArrayList();
    public List<TestCase> testsFacets = new ArrayList();
    public List<TestCase> testsQuery  = new ArrayList();
}
