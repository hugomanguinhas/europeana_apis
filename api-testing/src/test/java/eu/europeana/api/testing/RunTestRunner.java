/**
 * 
 */
package eu.europeana.api.testing;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;

import eu.europeana.api.solr.testing.ParallelTestRunner;
import eu.europeana.api.solr.testing.TestRunner;
import eu.europeana.api.solr.testing.result.TestResultModel;
import eu.europeana.api.solr.testing.tests.DummyTest;
import eu.europeana.api.solr.testing.tests.FacetSumTest;
import eu.europeana.api.solr.testing.tests.FacetValueTest;
import eu.europeana.api.solr.testing.tests.FakeFacetCountTest;
import eu.europeana.api.solr.testing.tests.FieldCountTest;
import eu.europeana.api.solr.testing.tests.QueryBasedTest;
import eu.europeana.api.solr.testing.tests.TestConfig;
import static eu.europeana.api.solr.testing.utils.TestUtils.*;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class RunTestRunner
{
    public static List<String> load(String src)
    {
        Class c = RunTestRunner.class;
        try { return IOUtils.readLines(c.getResourceAsStream(src)); }
        catch (IOException e) { return Collections.EMPTY_LIST; }
    }

    public static List<String> getFields()  { return load("/etc/fields.csv");  }
    public static List<String> getFacets()  { return load("/etc/facets.csv");  }
    public static List<String> getQueries() { return load("/etc/queries.csv"); }
    public static List<String> getTestSet() { return load("/etc/testset.csv"); }


    public static TestConfig getTestConfig()
    {
        TestConfig cfg = new TestConfig();

        cfg.fields  = getFields();
        cfg.facets  = getFacets();
        cfg.queries = getQueries();
        cfg.testSet = getTestSet();

        cfg.endpoints.add("https://www.europeana.eu");
        cfg.endpoints.add("https://metis-preview-api-prod.eanadev.org");
//        cfg.endpoints.add("https://metis-publish-api-prod.eanadev.org");

//        cfg.testSet.add("/000006/UEDIN_214");
//        cfg.testSet.add("/000002/_UEDIN_214");
//        cfg.testSet.add("/9200506/https___handrit_is_rdf_cho_AM02_0113f");

        String filter = getTestSetFilter("europeana_id", cfg.testSet);

//        cfg.testsField.add(new FieldCountTest(null, cfg.tolerance));
//        cfg.testsField.add(new FacetValueTest(null, cfg.tolerance));
//        cfg.testsField.add(new FacetSumTest(null, cfg.tolerance));
        cfg.testsField.add(new FieldCountTest(filter, 0));
        cfg.testsField.add(new FacetValueTest(filter, 0));
        cfg.testsField.add(new FacetSumTest(filter, 0));

//        cfg.testsFacets.add(new FakeFacetCountTest(null, 0));
//        cfg.testsFacets.add(new FacetValueTest(null, cfg.tolerance));
//        cfg.testsFacets.add(new FacetSumTest(null, cfg.tolerance));
        cfg.testsFacets.add(new FakeFacetCountTest(filter, 0));
        cfg.testsFacets.add(new FacetValueTest(filter, 0));
        cfg.testsFacets.add(new FacetSumTest(filter, 0));

//        cfg.testsQuery.add(new QueryBasedTest(null, 0));
//        cfg.testsQuery.add(new DummyTest());
//        cfg.testsQuery.add(new DummyTest());
        cfg.testsQuery.add(new QueryBasedTest(filter, 0));
        cfg.testsQuery.add(new DummyTest());
        cfg.testsQuery.add(new DummyTest());


        return cfg;
    }

    public static final void main(String[] args) throws IOException
    {
        TestResultModel model = new ParallelTestRunner(20).runTests(getTestConfig());
        model.print(new CSVPrinter(new PrintStream("D:\\work\\incoming\\migration\\solr.csv"), CSVFormat.EXCEL));
    }
}
