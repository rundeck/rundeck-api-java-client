package org.rundeck.api.parser;

import junit.framework.Assert;
import org.dom4j.Document;
import org.junit.Test;
import org.rundeck.api.domain.BaseState;
import org.rundeck.api.domain.RundeckWFExecState;

import java.io.InputStream;
import java.util.Date;

/**
 * $INTERFACE is ... User: greg Date: 1/18/14 Time: 8:33 AM
 */
public class BaseStateParserTest {
    @Test
    public void testBase1(){
        InputStream input = getClass().getResourceAsStream("execution-state1.xml");
        Document document = ParserHelper.loadDocument(input);
        BaseState baseState = new BaseState();
        BaseStateParser.parseBaseState(document.selectSingleNode("/result/executionState"), baseState);

        Assert.assertEquals(1390066160000L, baseState.getEndTime().getTime());
        Assert.assertEquals(1390066159000L, baseState.getStartTime().getTime());
        Assert.assertEquals(1390066160000L, baseState.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED, baseState.getExecutionState());
    }
    @Test
    public void testBase2(){
        InputStream input = getClass().getResourceAsStream("execution-state1.xml");
        Document document = ParserHelper.loadDocument(input);
        BaseState baseState = new BaseState();
        BaseStateParser.parseBaseState(document.selectSingleNode("/result/executionState/steps/step[1]"), baseState);

        Assert.assertEquals(1390066159000L, baseState.getStartTime().getTime());
        Assert.assertEquals(1390066160000L, baseState.getEndTime().getTime());
        Assert.assertEquals(1390066160000L, baseState.getUpdateTime().getTime());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED, baseState.getExecutionState());
    }
}
