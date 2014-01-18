package org.rundeck.api.domain;

/**
 * $INTERFACE is ... User: greg Date: 1/17/14 Time: 11:27 AM
 */
public enum RundeckWFExecState {
    /**
     * Waiting to start running
     */
    WAITING,
    /**
     * Currently running
     */
    RUNNING,
    /**
     * Running error handler
     */
    RUNNING_HANDLER,
    /**
     * Finished running successfully
     */
    SUCCEEDED,
    /**
     * Finished with a failure
     */
    FAILED,
    /**
     * Execution was aborted
     */
    ABORTED,
    /**
     * Partial success for some nodes
     */
    NODE_PARTIAL_SUCCEEDED,
    /**
     * Mixed states among nodes
     */
    NODE_MIXED,
    /**
     * After waiting the execution did not start
     */
    NOT_STARTED,;
}
