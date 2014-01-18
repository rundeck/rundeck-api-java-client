package org.rundeck.api.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the state of a workflow of steps
 */
public class WorkflowState extends BaseState {
    private int stepCount;
    private Set<RundeckNodeIdentity> targetNodes;
    private List<WorkflowStepState> steps;

    /**
     * Return the number of steps in this workflow
     * @return
     */
    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    /**
     * Identify the target nodes of this workflow
     * @return
     */
    public Set<RundeckNodeIdentity> getTargetNodes() {
        return targetNodes;
    }

    public void setTargetNodes(Set<RundeckNodeIdentity> targetNodes) {
        this.targetNodes = targetNodes;
    }

    /**
     * Return the list of steps for this workflow
     * @return
     */
    public List<WorkflowStepState> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStepState> steps) {
        this.steps = steps;
    }

}
