/**
 * 
 */
package eu.europeana.api.solr.testing.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 17 Jul 2017
 */
public class SearchAPIHandler implements ResponseHandler<Object>
{
    private CloseableHttpClient _client;
    private String              _path;
    private Object              _def;

    public SearchAPIHandler()
    { 
        _client = HttpClientBuilder.create().build();
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    }

    public Object handle(String url, String path, Object def)
    {
        _path = path;
        _def  = def;
        try
        {
            HttpGet m = new HttpGet(url);
            try     { return _client.execute(m, this); }
            finally { m.releaseConnection();           }
        }
        catch (Throwable t)
        { 
            t.printStackTrace();
            return null;
        }
    }

    public Object handleResponse(HttpResponse rsp)
           throws ClientProtocolException, IOException
    {
        int code = rsp.getStatusLine().getStatusCode();
        if ( code != 200 ) { 
            System.out.println("HTTP error: " + code); 
            return null;
        }

        InputStream is = null;
        try
        {
            is = rsp.getEntity().getContent();
            return JsonPath.read(is, _path);
        }
        catch (PathNotFoundException e) { return _def; }
        catch (IOException e)  { e.printStackTrace();      }
        finally                { IOUtils.closeQuietly(is); }

        return null;
    }
}
