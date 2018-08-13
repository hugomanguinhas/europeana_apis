/**
 * 
 */
package eu.europeana.api.solr;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 17 Jul 2017
 */
public class SchemaFieldParser
{
    private DocumentBuilder    _builder;
    private XPath              _xpath;
    private XPathExpression    _eField;
    private XPathExpression    _eDynamicField;
    private Collection<String> _langs;

    public SchemaFieldParser(String... langs) throws Exception
    {
        this(Arrays.asList(langs));
    }

    public SchemaFieldParser(Collection<String> langs) throws Exception
    {
        _builder       = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        _xpath         = XPathFactory.newInstance().newXPath();
        _eField        = _xpath.compile("/schema/fields/field/@name");
        _eDynamicField = _xpath.compile("/schema/fields/dynamicField/@name");
        _langs         = langs;
    }

    public Collection<String> parse(InputStream is)
            throws SAXException, IOException, XPathExpressionException
    {
        return parse(is, new TreeSet<String>());
    }

    public Collection<String> parse(InputStream is, Collection<String> col)
           throws SAXException, IOException, XPathExpressionException
    {
        Document doc = _builder.parse(is);
        return processFields(doc, col);
    }

    private Collection<String> processFields(Document doc
                                           , Collection<String> fields)
            throws XPathExpressionException
    {
        NodeList list;
        list = (NodeList)_eField.evaluate(doc, XPathConstants.NODESET);
        for ( int i = 0; i < list.getLength(); i++)
        {
            Attr attr = (Attr)list.item(i);
            fields.add(attr.getNodeValue());
        }
        list = (NodeList)_eDynamicField.evaluate(doc, XPathConstants.NODESET);
        for ( int i = 0; i < list.getLength(); i++)
        {
            processDynamicField((Attr)list.item(i), fields);
        }
        return fields;
    }

    private void processDynamicField(Attr attr, Collection<String> fields)
    {
        String value = attr.getNodeValue();
        if ( value.equals("timestamp_*") )
        {
            fields.add("timestamp_created");
            fields.add("timestamp_update");
            return;
        }

        if ( !value.endsWith(".*") ) { return; }

        for ( String lang : _langs )
        {
            fields.add(value.substring(0, value.length()-1) + lang);
        }
    }

    public static final void main(String[] args) 
           throws Exception
    {
        InputStream is = new URL("http://sol7.eanadev.org:9191/solr/search_production_publish_1_shard2_replica1/admin/file?file=schema.xml&contentType=text/xml;charset=utf-8").openConnection().getInputStream();
        for ( String field : new SchemaFieldParser("en", "fr").parse(is) )
        {
            System.out.println(field);
        }
    }
}
