package org.rundeck.api.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The state of an Execution
 */
public class RundeckExecutionState extends WorkflowState{
    private long executionId;
    private Set<RundeckNodeIdentity> allNodes;
    private Map<String, List<WorkflowStepContextState>> nodeStates;

    /**
     * Return the set of all rundeck nodes targeted in this execution
     * @return
     */
    public Set<RundeckNodeIdentity> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(Set<RundeckNodeIdentity> allNodes) {
        this.allNodes = allNodes;
    }

    /**
     * Return the map of node name to step state list
     * @return
     */
    public Map<String, List<WorkflowStepContextState>> getNodeStates() {
        return nodeStates;
    }

    public void setNodeStates(Map<String, List<WorkflowStepContextState>> nodeStates) {
        this.nodeStates = nodeStates;
    }

    /**
     * Return this execution's ID
     * @return
     */
    public long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(long executionId) {
        this.executionId = executionId;
    }
}
