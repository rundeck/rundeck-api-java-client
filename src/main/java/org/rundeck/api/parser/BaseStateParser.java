package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.BaseState;
import org.rundeck.api.domain.RundeckWFExecState;

/**
 * $INTERFACE is ... User: greg Date: 1/17/14 Time: 12:19 PM
 */
public class BaseStateParser implements XmlNodeParser<BaseState> {
    public static void parseBaseState(Node targetNode, BaseState state) {
        state.setEndTime(WorkflowStateParser.parseDate(StringUtils.trimToNull(targetNode.valueOf("endTime"))));
        state.setStartTime(WorkflowStateParser.parseDate(StringUtils.trimToNull(targetNode.valueOf("startTime"))));
        state.setUpdateTime(WorkflowStateParser.parseDate(StringUtils.trimToNull(targetNode.valueOf("updateTime"))));

        try {
            state.setExecutionState(RundeckWFExecState.valueOf(StringUtils.upperCase(targetNode.valueOf
                    ("executionState"))));
        } catch (IllegalArgumentException e) {
            state.setExecutionState(null);
        }
    }

    private String xpath;

    public BaseStateParser() {
    }

    public BaseStateParser(String xpath) {

        this.xpath = xpath;
    }

    @Override
    public BaseState parseXmlNode(Node node) {
        Node targetNode = xpath != null ? node.selectSingleNode(xpath) : node;
        BaseState baseState = new BaseState();
        parseBaseState(targetNode, baseState);
        return baseState;
    }
}
