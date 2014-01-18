package org.rundeck.api.parser;

import junit.framework.Assert;
import org.dom4j.Document;
import org.junit.Test;
import org.rundeck.api.domain.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 5:47 PM
 */
public class WorkflowStateParserTest {
    @Test
    public void parseBasic(){
        InputStream input = getClass().getResourceAsStream("execution-state1.xml");
        Document document = ParserHelper.loadDocument(input);

        WorkflowState execution = new WorkflowStateParser("result/executionState").parseXmlNode(document);
        Assert.assertEquals(1390066159000L, execution.getStartTime().getTime());
        Assert.assertEquals(1390066160000L, execution.getEndTime().getTime());
        Assert.assertEquals(1390066160000L, execution.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED, execution.getExecutionState());
        Assert.assertEquals(1, execution.getStepCount());
        Assert.assertEquals(3, execution.getTargetNodes().size());
        HashSet<String> expectedTargetNodes = new HashSet<String>(Arrays.asList(
                "node-111.qa.subgroup.mycompany.com",
                "node-14.qa.subgroup.mycompany.com",
                "node-6.qa.subgroup.mycompany.com"
                ));
        for (RundeckNodeIdentity rundeckNodeIdentity : execution.getTargetNodes()) {
            Assert.assertTrue(expectedTargetNodes.contains(rundeckNodeIdentity.getName()));
        }

        //
        Assert.assertEquals(1,execution.getSteps().size());
        WorkflowStepState step1 = execution.getSteps().get(0);
    }
    @Test
    public void parse(){
        InputStream input = getClass().getResourceAsStream("execution-state2.xml");
        Document document = ParserHelper.loadDocument(input);

        WorkflowState execution = new WorkflowStateParser("result/executionState").parseXmlNode(document);
        Assert.assertEquals(1390066061000L, execution.getStartTime().getTime());
        Assert.assertEquals(null, execution.getEndTime());
        Assert.assertEquals(1390066067000L, execution.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.RUNNING, execution.getExecutionState());
        Assert.assertEquals(2, execution.getStepCount());
        Assert.assertEquals(1, execution.getTargetNodes().size());
        HashSet<String> expectedTargetNodes = new HashSet<String>(Arrays.asList(
                "dignan"
                ));
        for (RundeckNodeIdentity rundeckNodeIdentity : execution.getTargetNodes()) {
            Assert.assertTrue(expectedTargetNodes.contains(rundeckNodeIdentity.getName()));
        }

        //
        Assert.assertEquals(2,execution.getSteps().size());
        WorkflowStepState step1 = execution.getSteps().get(0);
    }
}
