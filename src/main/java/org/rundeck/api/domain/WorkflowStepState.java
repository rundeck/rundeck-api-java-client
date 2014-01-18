package org.rundeck.api.domain;

import java.util.List;
import java.util.Map;

/**
 * $INTERFACE is ... User: greg Date: 1/17/14 Time: 12:03 PM
 */
public class WorkflowStepState extends WorkflowStepContextState {
    private boolean nodeStep;
    private WorkflowState subWorkflow;
    private Map<String, WorkflowStepContextState> nodeStates;

    public boolean isNodeStep() {
        return nodeStep;
    }

    public void setNodeStep(boolean nodeStep) {
        this.nodeStep = nodeStep;
    }

    public WorkflowState getSubWorkflow() {
        return subWorkflow;
    }

    public void setSubWorkflow(WorkflowState subWorkflow) {
        this.subWorkflow = subWorkflow;
    }

    public Map<String, WorkflowStepContextState> getNodeStates() {
        return nodeStates;
    }

    public void setNodeStates(Map<String, WorkflowStepContextState> nodeStates) {
        this.nodeStates = nodeStates;
    }
}
