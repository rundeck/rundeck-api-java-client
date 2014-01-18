package org.rundeck.api.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 5:41 PM
 */
public class RundeckExecutionState extends WorkflowState{
    private long executionId;
    private Set<RundeckNodeIdentity> allNodes;
    private Map<String, List<WorkflowStepContextState>> nodeStates;

    public Set<RundeckNodeIdentity> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(Set<RundeckNodeIdentity> allNodes) {
        this.allNodes = allNodes;
    }

    public Map<String, List<WorkflowStepContextState>> getNodeStates() {
        return nodeStates;
    }

    public void setNodeStates(Map<String, List<WorkflowStepContextState>> nodeStates) {
        this.nodeStates = nodeStates;
    }

    public long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(long executionId) {
        this.executionId = executionId;
    }
}
