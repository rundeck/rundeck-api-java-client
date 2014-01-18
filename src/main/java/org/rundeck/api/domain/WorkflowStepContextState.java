package org.rundeck.api.domain;

/**
 * A state for a particular step
 */
public class WorkflowStepContextState extends BaseState {
    private String stepContextId;
    private String stepNum;

    /**
     * The context id for the step in the form "#[/#[/#[...]]]" where "#" is a number
     * @return
     */
    public String getStepContextId() {
        return stepContextId;
    }

    public void setStepContextId(String stepContextId) {
        this.stepContextId = stepContextId;
    }

    /**
     * The step number of this step in the current workflow, 1 indexed.
     * @return
     */
    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }
}
