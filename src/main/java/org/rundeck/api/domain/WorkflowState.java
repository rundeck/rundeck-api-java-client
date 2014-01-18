package org.rundeck.api.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 5:44 PM
 */
public class WorkflowState  extends BaseState{
    private int stepCount;
    private Set<RundeckNodeIdentity> targetNodes;
    private List<WorkflowStepState> steps;

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public Set<RundeckNodeIdentity> getTargetNodes() {
        return targetNodes;
    }

    public void setTargetNodes(Set<RundeckNodeIdentity> targetNodes) {
        this.targetNodes = targetNodes;
    }

    public List<WorkflowStepState> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStepState> steps) {
        this.steps = steps;
    }

}
