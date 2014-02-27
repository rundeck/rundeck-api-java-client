package org.rundeck.api.domain;

import java.util.List;
import java.util.Map;

/**
 * Represents the state of a step in a workflow
 */
public class WorkflowStepState extends WorkflowStepContextState {
    private boolean nodeStep;
    private WorkflowState subWorkflow;
    private Map<String, WorkflowStepContextState> nodeStates;

    /**
     * Return true if this step runs on each target node
     * @return
     */
    public boolean isNodeStep() {
        return nodeStep;
    }

    public void setNodeStep(boolean nodeStep) {
        this.nodeStep = nodeStep;
    }

    /**
     * Return sub workflow if this step has one
     * @return
     */
    public WorkflowState getSubWorkflow() {
        return subWorkflow;
    }

    public void setSubWorkflow(WorkflowState subWorkflow) {
        this.subWorkflow = subWorkflow;
    }

    /**
     * Return the state of each target node if this step runs on each target node
     * @return
     */
    public Map<String, WorkflowStepContextState> getNodeStates() {
        return nodeStates;
    }

    public void setNodeStates(Map<String, WorkflowStepContextState> nodeStates) {
        this.nodeStates = nodeStates;
    }
}
