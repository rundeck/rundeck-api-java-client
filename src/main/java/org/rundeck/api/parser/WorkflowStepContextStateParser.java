package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.WorkflowStepContextState;
import org.rundeck.api.domain.WorkflowStepState;

/**
 * $INTERFACE is ... User: greg Date: 1/17/14 Time: 12:39 PM
 */
public class WorkflowStepContextStateParser implements XmlNodeParser<WorkflowStepContextState> {
    WorkflowStepContextState inherit;

    public WorkflowStepContextStateParser(WorkflowStepContextState inherit) {
        this.inherit = inherit;
    }

    @Override
    public WorkflowStepContextState parseXmlNode(Node node) {
        WorkflowStepContextState workflowStepState = new WorkflowStepContextState();
        if(null!=inherit) {
            workflowStepState.setStepNum(inherit.getStepNum());
            workflowStepState.setStepContextId(inherit.getStepContextId());
        }
        BaseStateParser.parseBaseState(node, workflowStepState);
        return workflowStepState;
    }
}
