package org.rundeck.api.parser;

import junit.framework.Assert;
import org.dom4j.Document;
import org.junit.Test;
import org.rundeck.api.domain.RundeckExecutionState;
import org.rundeck.api.domain.RundeckNodeIdentity;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 5:42 PM
 */
public class ExecutionStateParserTest {
    @Test
    public void testBasic(){
        InputStream input = getClass().getResourceAsStream("execution-state1.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecutionState execution = new ExecutionStateParser("/result/executionState").parseXmlNode
                (document);

        Assert.assertEquals(149L, execution.getExecutionId());

        HashSet<String> expectedTargetNodes = new HashSet<String>(Arrays.asList(
                "node-111.qa.subgroup.mycompany.com",
                "node-14.qa.subgroup.mycompany.com",
                "node-6.qa.subgroup.mycompany.com"
        ));

        Assert.assertEquals(3, execution.getAllNodes().size());
        for (RundeckNodeIdentity rundeckNodeIdentity : execution.getAllNodes()) {
            Assert.assertTrue(expectedTargetNodes.contains(rundeckNodeIdentity.getName()));
        }

        Assert.assertEquals(3,execution.getNodeStates().size());
        for (String s : execution.getNodeStates().keySet()) {
            Assert.assertTrue(expectedTargetNodes.contains(s));
        }
    }
}
