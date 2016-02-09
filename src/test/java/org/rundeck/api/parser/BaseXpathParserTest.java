package org.rundeck.api.parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.RundeckApiException;

import java.io.InputStream;

/**
 * Created by greg on 2/9/16.
 */
public class BaseXpathParserTest {
    class BaseTest1 extends BaseXpathParser<String>{
        public BaseTest1(final String xpath) {
            super(xpath);
        }
        @Override
        public String parse(final Node node) {
            return node.getPath();
        }
    }
    private Document getResultErrorDocument() {
        InputStream input = getClass().getResourceAsStream("error.xml");
        return loadDocument(input);
    }
    private Document getNonResultDocument() {
        InputStream input = getClass().getResourceAsStream("event-adhoc.xml");
        return loadDocument(input);
    }

    private Document loadDocument(final InputStream inputStream) {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");

        Document document;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            throw new RundeckApiException("Failed to read Rundeck response", e);
        }
        document.setXMLEncoding("UTF-8");
        return document;
    }

    @Test
    public void emptyXpath_resultdoc_removesResult(){
        Document document = getResultErrorDocument();
        BaseTest1 baseState = new BaseTest1(null);
        Assert.assertEquals("/result/error",baseState.parseXmlNode(document));
    }
    @Test
    public void emptyXpath_noresultdoc_withoutResult(){
        Document document = getNonResultDocument();
        BaseTest1 baseState = new BaseTest1(null);
        Assert.assertEquals("/",baseState.parseXmlNode(document));
    }
    @Test
    public void unemptyXpath_resultdoc_withoutResult(){
        Document document = getResultErrorDocument();
        BaseTest1 baseState = new BaseTest1("error");
        Assert.assertEquals("/result/error",baseState.parseXmlNode(document));
    }
    @Test
    public void slashresultXpath_resultdoc_withoutResult(){
        Document document = getResultErrorDocument();
        BaseTest1 baseState = new BaseTest1("/result/error");
        Assert.assertEquals("/result/error",baseState.parseXmlNode(document));
    }
    @Test
    public void resultXpath_resultdoc_withoutResult(){
        Document document = getResultErrorDocument();
        BaseTest1 baseState = new BaseTest1("result/error");
        Assert.assertEquals("/result/error",baseState.parseXmlNode(document));
    }
    @Test
    public void unemptyXpath_noresultdoc_withoutResult(){
        Document document = getNonResultDocument();
        BaseTest1 baseState = new BaseTest1("event");
        Assert.assertEquals("/event",baseState.parseXmlNode(document));
    }
    @Test
    public void resultXpath_noresultdoc_withoutResult(){
        Document document = getNonResultDocument();
        BaseTest1 baseState = new BaseTest1("result/event");
        Assert.assertEquals("/event",baseState.parseXmlNode(document));
    }
    @Test
    public void slashresultXpath_noresultdoc_withoutResult(){
        Document document = getNonResultDocument();
        BaseTest1 baseState = new BaseTest1("/result/event");
        Assert.assertEquals("/event",baseState.parseXmlNode(document));
    }
    @Test
    public void unemptyXpath_resultdoc_withResult(){
        Document document = getResultErrorDocument();
        BaseTest1 baseState = new BaseTest1("result/error");
        Assert.assertEquals("/result/error",baseState.parseXmlNode(document));
    }
    @Test
    public void unemptyXpath_noresultdoc_removesResult(){
        Document document = getNonResultDocument();
        BaseTest1 baseState = new BaseTest1("result/event");
        Assert.assertEquals("/event",baseState.parseXmlNode(document));
    }

}