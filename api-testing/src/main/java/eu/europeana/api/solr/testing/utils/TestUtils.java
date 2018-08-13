/**
 * 
 */
package eu.europeana.api.solr.testing.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import eu.europeana.api.solr.testing.tests.TestConfig;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 30 Jul 2018
 */
public class TestUtils
{
    private static int READ_TIMEOUT    = 5 * 60 * 1000;
    private static int DEFAULT_RETRIES = 5;
    

    public static String getTestSetFilter(String field, List<String> values)
    {
      int iL = values.size();
      if ( iL == 0 ) { return null; }

      String s = field + ":(" + encodeQuery(values.get(0));
      for ( int i = 1; i < iL; i++ ) { s += "+OR+" + encodeQuery(values.get(i)); }
      s += ")";
      return s;
    }

    private static String encodeQuery(String value)
    {
        return ( value.contains("*") ? value : "%22" + value + "%22");
    }

    public static String renderHL(String url, Object value)
    {
      value = (value == null ? "?" : value);
      return "=HYPERLINK(\"" + url + "\",\"" + value + "\")";
    }

    public static Integer compareValues(Integer v1, Integer v2)
    {
      return ( (v1 == null || v2 == null) ? null : v2 - v1 );
    }

    public static Object resolve(String req, String path, Object def, TestConfig cfg)
    {
        int retries = DEFAULT_RETRIES;
        try
        {
            HttpURLConnection conn = null;
            URL               url  = new URL(req);
            while ( retries-- > 0 )
            {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                int code = conn.getResponseCode();
                if ( code != 200 ) { continue; }

                return JsonPath.read(conn.getInputStream(), path);
            }
        }
        catch (PathNotFoundException e) { return def; }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public static Object resolve(String url, String path, Object def, TestConfig cfg)
    {
        return new SearchAPIHandler().handle(url, path, def);
    }
    */

    public static Integer getTotals(String url, TestConfig cfg)
    {
        return (Integer)resolve(url, "$.totalResults", null, cfg);
    }

    public static Object getFacets(String url, TestConfig cfg)
    {
        return resolve(url, "$.facets[0].fields", Collections.EMPTY_LIST, cfg);
    }

    public static String createQuery(
            TestConfig cfg, String endpoint, String query
          , String rows, String facet, String... fqs)
    {
      String pFacet = (facet == null ? "" : "&profile=facets&facet=" + facet
                                          + "&f." + facet + ".facet.limit="
                                          + cfg.facetLimit);
      String pFQ = "";
      for ( String fq : fqs )
      {
          if ( fq != null ) { pFQ +="&qf=" + fq; }
      }
      return endpoint + "/api/v2/search.json?wskey=" + cfg.apikey
                      + "&query=" + query + pFQ + "&rows=" + rows + pFacet
                      + "&sort=europeana_id+asc";
    }
}
