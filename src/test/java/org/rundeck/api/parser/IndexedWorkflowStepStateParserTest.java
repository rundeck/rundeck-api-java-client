package org.rundeck.api.parser;

import junit.framework.Assert;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.rundeck.api.domain.WorkflowState;
import org.rundeck.api.domain.WorkflowStepContextState;
import org.rundeck.api.domain.WorkflowStepState;

import java.util.Arrays;
import java.util.HashMap;

/**
 * $INTERFACE is ... User: greg Date: 1/18/14 Time: 9:57 AM
 */
public class IndexedWorkflowStepStateParserTest {
    @Test
    public void testLookupContextSimple1(){
        WorkflowState workflowState = new WorkflowState();
        WorkflowStepState step1 = new WorkflowStepState();
        workflowState.setSteps(Arrays.asList(step1));
        WorkflowStepState stepState = IndexedWorkflowStepStateParser.lookupContext("1", workflowState);
        Assert.assertEquals(step1,stepState);
    }
    @Test
    public void testLookupContextSimple2(){
        WorkflowState workflowState = new WorkflowState();
        WorkflowStepState step1 = new WorkflowStepState();
        WorkflowStepState step2 = new WorkflowStepState();
        workflowState.setSteps(Arrays.asList(step1,step2));
        WorkflowStepState stepState = IndexedWorkflowStepStateParser.lookupContext("2", workflowState);
        Assert.assertEquals(step2,stepState);
    }
    @Test
    public void testLookupContextDescend1(){
        WorkflowState workflowState = new WorkflowState();
        WorkflowStepState step1 = new WorkflowStepState();
        WorkflowStepState step2 = new WorkflowStepState();
        WorkflowState sub1 = new WorkflowState();
        step2.setSubWorkflow(sub1);
        workflowState.setSteps(Arrays.asList(step1,step2));

        WorkflowStepState step21 = new WorkflowStepState();
        sub1.setSteps(Arrays.asList(step21));

        WorkflowStepState stepState = IndexedWorkflowStepStateParser.lookupContext("2/1", workflowState);
        Assert.assertEquals(step21,stepState);
    }
    @Test
    public void testParse1() throws DocumentException {
        WorkflowState workflowState = new WorkflowState();
        WorkflowStepState step1 = new WorkflowStepState();
        WorkflowStepState step2 = new WorkflowStepState();
        WorkflowState sub1 = new WorkflowState();
        step2.setSubWorkflow(sub1);
        workflowState.setSteps(Arrays.asList(step1,step2));

        WorkflowStepState step21 = new WorkflowStepState();
        sub1.setSteps(Arrays.asList(step21));
        HashMap<String, WorkflowStepContextState> nodeStates = new HashMap<String, WorkflowStepContextState>();
        WorkflowStepContextState nodeState1 = new WorkflowStepContextState();
        nodeStates.put("dignan", nodeState1);
        step21.setNodeStates(nodeStates);

        Document document = DocumentHelper.parseText("<stepctx>2/1</stepctx>");

        WorkflowStepContextState result = new IndexedWorkflowStepStateParser(workflowState,"dignan").parseXmlNode(document);
        Assert.assertEquals(nodeState1,result);
    }
}
