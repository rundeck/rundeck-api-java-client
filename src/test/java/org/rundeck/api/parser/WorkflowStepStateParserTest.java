package org.rundeck.api.parser;

import junit.framework.Assert;
import org.dom4j.Document;
import org.junit.Test;
import org.rundeck.api.domain.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

/**
 * $INTERFACE is ... User: greg Date: 1/18/14 Time: 9:00 AM
 */
public class WorkflowStepStateParserTest {

    @Test
    public void testParse1() {
        InputStream input = getClass().getResourceAsStream("execution-state1.xml");
        Document document = ParserHelper.loadDocument(input);
        WorkflowStepState stepState = new WorkflowStepStateParser().parseXmlNode(document.selectSingleNode
                ("/result/executionState/steps/step[1]"));

        Assert.assertNotNull(stepState);
        Assert.assertEquals(true, stepState.isNodeStep());
        Assert.assertEquals(null, stepState.getSubWorkflow());
        Assert.assertNotNull(stepState.getNodeStates());
        Assert.assertEquals("1", stepState.getStepContextId());
        Assert.assertEquals(1, stepState.getStepNum());
        Assert.assertEquals(1390066159000L, stepState.getStartTime().getTime());
        Assert.assertEquals(1390066160000L, stepState.getEndTime().getTime());
        Assert.assertEquals(1390066160000L, stepState.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED, stepState.getExecutionState());
        HashSet<String> expectedTargetNodes = new HashSet<String>(Arrays.asList(
                "node-111.qa.subgroup.mycompany.com",
                "node-14.qa.subgroup.mycompany.com",
                "node-6.qa.subgroup.mycompany.com"
        ));

        int i = 0;
        for (String s : stepState.getNodeStates().keySet()) {
            Assert.assertTrue(expectedTargetNodes.contains(s));
            WorkflowStepContextState workflowStepContextState = stepState.getNodeStates().get(s);
            Assert.assertEquals("1", workflowStepContextState.getStepContextId());
            Assert.assertEquals(1, workflowStepContextState.getStepNum());
            Assert.assertEquals(1390066159000L + (i * 1000), workflowStepContextState.getStartTime().getTime());
            Assert.assertEquals(1390066159000L + (i * 1000), workflowStepContextState.getEndTime().getTime());
            Assert.assertEquals(1390066159000L + (i * 1000), workflowStepContextState.getUpdateTime().getTime());
            Assert.assertEquals(RundeckWFExecState.SUCCEEDED, workflowStepContextState.getExecutionState());
            i++;
        }

    }
    @Test
    public void testParseRunning1() {
        InputStream input = getClass().getResourceAsStream("execution-state2.xml");
        Document document = ParserHelper.loadDocument(input);

        WorkflowStepState stepState = new WorkflowStepStateParser().parseXmlNode(document.selectSingleNode
                ("/result/executionState/steps/step[1]"));

        Assert.assertNotNull(stepState);
        Assert.assertEquals(true, stepState.isNodeStep());
        Assert.assertEquals(null, stepState.getSubWorkflow());
        Assert.assertEquals("1", stepState.getStepContextId());
        Assert.assertEquals(1, stepState.getStepNum());
        Assert.assertNotNull(stepState.getNodeStates());
        Assert.assertEquals(1390066061000L, stepState.getStartTime().getTime());
        Assert.assertEquals(1390066066000L, stepState.getEndTime().getTime());
        Assert.assertEquals(1390066061000L, stepState.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED, stepState.getExecutionState());
        HashSet<String> expectedTargetNodes = new HashSet<String>(Arrays.asList(
                "dignan"
        ));

        WorkflowStepContextState workflowStepContextState = stepState.getNodeStates().get("dignan");
        Assert.assertEquals("1", workflowStepContextState.getStepContextId());
        Assert.assertEquals(1, workflowStepContextState.getStepNum());
        Assert.assertEquals(1390066061000L, workflowStepContextState.getStartTime().getTime());
        Assert.assertEquals(1390066066000L, workflowStepContextState.getEndTime().getTime());
        Assert.assertEquals(1390066066000L, workflowStepContextState.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED, workflowStepContextState.getExecutionState());

    }
    @Test
    public void testParseRunning2() {
        InputStream input = getClass().getResourceAsStream("execution-state2.xml");
        Document document = ParserHelper.loadDocument(input);

        WorkflowStepState stepState = new WorkflowStepStateParser().parseXmlNode(document.selectSingleNode
                ("/result/executionState/steps/step[2]"));

        Assert.assertNotNull(stepState);
        Assert.assertEquals(false, stepState.isNodeStep());
        Assert.assertNotNull(stepState.getSubWorkflow());
        Assert.assertNull(stepState.getNodeStates());
        Assert.assertEquals("2", stepState.getStepContextId());
        Assert.assertEquals(2, stepState.getStepNum());
        Assert.assertEquals(1390066066000L, stepState.getStartTime().getTime());
        Assert.assertNull(stepState.getEndTime());
        Assert.assertEquals(1390066066000L, stepState.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.RUNNING, stepState.getExecutionState());


        //sub workflow
        WorkflowState subWorkflow = stepState.getSubWorkflow();
        Assert.assertEquals(1,subWorkflow.getSteps().size());
        Assert.assertEquals(1,subWorkflow.getTargetNodes().size());

        WorkflowStepState stepState1 = subWorkflow.getSteps().get(0);
        Assert.assertEquals(true, stepState1.isNodeStep());
        Assert.assertNull(stepState1.getSubWorkflow());
        Assert.assertNotNull(stepState1.getNodeStates());
        Assert.assertEquals("2/1", stepState1.getStepContextId());
        Assert.assertEquals(1, stepState1.getStepNum());
        Assert.assertEquals(1390066067000L, stepState1.getStartTime().getTime());
        Assert.assertNull(stepState1.getEndTime());
        Assert.assertEquals(1390066067000L, stepState1.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.RUNNING, stepState1.getExecutionState());

    }
}
