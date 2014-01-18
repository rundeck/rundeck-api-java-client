package org.rundeck.api.domain;

/**
 * A state for a particular step
 */
public class WorkflowStepContextState extends BaseState {
    private String stepContextId;
    private String stepNum;

    public String getStepContextId() {
        return stepContextId;
    }

    public void setStepContextId(String stepContextId) {
        this.stepContextId = stepContextId;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }
}
