package org.rundeck.api.parser;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckOutputEntry;

import java.io.InputStream;
import java.util.Date;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 4:35 PM
 */
public class OutputEntryParserTest {
    @Test
    public void testEntryBasic() {
        InputStream input = getClass().getResourceAsStream("output1.xml");
        Document document = ParserHelper.loadDocument(input);
        OutputEntryParser outputEntryParser = new OutputEntryParser("//entry[1]");

        RundeckOutputEntry rundeckOutputEntry = outputEntryParser.parseXmlNode(document);
        Assert.assertEquals("hi there", rundeckOutputEntry.getMessage());
        Assert.assertEquals(null, rundeckOutputEntry.getCommand());
        Assert.assertEquals(RundeckOutputEntry.RundeckLogLevel.NORMAL, rundeckOutputEntry.getLevel());
        Assert.assertEquals("node-111.qa.subgroup.mycompany.com", rundeckOutputEntry.getNode());
        Assert.assertEquals("09:48:23", rundeckOutputEntry.getTime());
        Assert.assertEquals(new Date(1389894503000L), rundeckOutputEntry.getAbsoluteTime());
        Assert.assertEquals(null, rundeckOutputEntry.getType());
        Assert.assertEquals("Raif", rundeckOutputEntry.getUser());
        Assert.assertNotNull(rundeckOutputEntry.getMetadata());
        Assert.assertNotNull(rundeckOutputEntry.getMetadata().get("stepctx"));
        Assert.assertEquals("1",rundeckOutputEntry.getMetadata().get("stepctx"));
    }
    @Test
    public void testEntryState() {
        InputStream input = getClass().getResourceAsStream("output-state.xml");
        Document document = ParserHelper.loadDocument(input);
        OutputEntryParser outputEntryParser = new OutputEntryParser("//entry[1]");

        RundeckOutputEntry rundeckOutputEntry = outputEntryParser.parseXmlNode(document);
        Assert.assertEquals(null, rundeckOutputEntry.getMessage());
        Assert.assertEquals(null, rundeckOutputEntry.getCommand());
        Assert.assertEquals(RundeckOutputEntry.RundeckLogLevel.NORMAL, rundeckOutputEntry.getLevel());
        Assert.assertEquals("dignan", rundeckOutputEntry.getNode());
        Assert.assertEquals("09:48:23", rundeckOutputEntry.getTime());
        Assert.assertEquals(new Date(1389894503000L), rundeckOutputEntry.getAbsoluteTime());
        Assert.assertEquals("stepbegin", rundeckOutputEntry.getType());
        Assert.assertEquals("admin", rundeckOutputEntry.getUser());
        Assert.assertNotNull(rundeckOutputEntry.getMetadata());
        Assert.assertNotNull(rundeckOutputEntry.getMetadata().get("stepctx"));
        Assert.assertEquals("1",rundeckOutputEntry.getMetadata().get("stepctx"));
        Assert.assertNotNull(rundeckOutputEntry.getMetadata().get("step"));
        Assert.assertEquals("1",rundeckOutputEntry.getMetadata().get("step"));
    }
}
