package org.rundeck.api.parser;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckOutput;
import org.rundeck.api.domain.RundeckProject;

import java.io.InputStream;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 4:24 PM
 */
public class OutputParserTest {
    @Test
    public void parseOutputBasic() throws Exception {
        InputStream input = getClass().getResourceAsStream("output1.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckOutput output = new OutputParser("result/output", new OutputEntryParser()).parseXmlNode(document);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals(null, output.getFilterNode());
        Assert.assertEquals(null, output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.9), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(3, output.getLogEntries().size());
    }
    @Test
    public void parseOutputFiltered() throws Exception {
        InputStream input = getClass().getResourceAsStream("output-filtered.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckOutput output = new OutputParser("result/output", new OutputEntryParser()).parseXmlNode(document);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals("node-111.qa.subgroup.mycompany.com", output.getFilterNode());
        Assert.assertEquals("1", output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.9), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(1, output.getLogEntries().size());
    }
    @Test
    public void parseOutputUnmodified() throws Exception {
        InputStream input = getClass().getResourceAsStream("output-unmodified.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckOutput output = new OutputParser("result/output", new OutputEntryParser()).parseXmlNode(document);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals(null, output.getFilterNode());
        Assert.assertEquals(null, output.getFilterStep());
        Assert.assertEquals(0, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(null, output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(true, output.isUnmodified());
        Assert.assertEquals(null, output.getLogEntries());
    }
}
