package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.WorkflowState;
import org.rundeck.api.domain.WorkflowStepContextState;
import org.rundeck.api.domain.WorkflowStepState;

/**
 * Returns a WorkflowStepContextState by looking up the given Rundeck node's state in the workflow, using the step
 * context path of the "stepctx" element of the selected DOM node.
 */
public class IndexedWorkflowStepStateParser implements XmlNodeParser<WorkflowStepContextState> {
    private final WorkflowState workflowState;
    private String rundeckNodeName;

    @Override
    public WorkflowStepContextState parseXmlNode(final Node node) {
        //look for workflow step state based on node name and stepctx found on the node
        final String stepctx = StringUtils.trimToNull(node.valueOf("stepctx"));
        final WorkflowStepState foundStep = lookupContext(stepctx, workflowState);
        //look up node state for this node
        if (null != foundStep
                && null != foundStep.getNodeStates()
                && null != foundStep.getNodeStates().get(rundeckNodeName)) {
            return foundStep.getNodeStates().get(rundeckNodeName);
        }


        return null;
    }

    /**
     * look up the workflow step state for the step context, from the root workflow
     *
     * @param stepctx
     * @param initial
     *
     * @return
     */
    public static WorkflowStepState lookupContext(final String stepctx, final WorkflowState initial) {
        final String[] parts = stepctx.split("/");
        //descend workflow steps to find correct step
        WorkflowState current = initial;
        WorkflowStepState currentStep = null;
        for (int i = 0; i < parts.length; i++) {
            final String part = parts[i];
            final WorkflowStepState workflowStepState = current.getSteps().get(Integer.parseInt(part) - 1);
            currentStep = workflowStepState;
            if (i < parts.length - 1) {
                current = currentStep.getSubWorkflow();
            }
        }
        return currentStep;
    }

    public IndexedWorkflowStepStateParser(final WorkflowState workflowState, final String rundeckNodeName) {
        this.workflowState = workflowState;
        this.rundeckNodeName = rundeckNodeName;
    }
}
