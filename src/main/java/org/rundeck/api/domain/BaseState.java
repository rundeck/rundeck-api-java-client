package org.rundeck.api.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Base execution status for a step
 */
public class BaseState {
    private Date startTime;
    private Date endTime;
    private Date updateTime;
    private RundeckWFExecState executionState;

    /**
     * Time that the execution of this step started
     * @return
     */
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Time that the execution of this step finished, or null if it has not completed
     * @return
     */
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Current state of the execution
     * @return
     */
    public RundeckWFExecState getExecutionState() {
        return executionState;
    }

    public void setExecutionState(RundeckWFExecState executionState) {
        this.executionState = executionState;
    }

    /**
     * Time that this state was last updated
     * @return
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
