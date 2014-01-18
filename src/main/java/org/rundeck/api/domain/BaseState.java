package org.rundeck.api.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * $INTERFACE is ... User: greg Date: 1/17/14 Time: 11:26 AM
 */
public class BaseState {
    private Date startTime;
    private Date endTime;
    private Date updateTime;
    private RundeckWFExecState executionState;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public RundeckWFExecState getExecutionState() {
        return executionState;
    }

    public void setExecutionState(RundeckWFExecState executionState) {
        this.executionState = executionState;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
