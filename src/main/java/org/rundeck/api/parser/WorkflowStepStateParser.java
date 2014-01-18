package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.BaseState;
import org.rundeck.api.domain.WorkflowStepContextState;
import org.rundeck.api.domain.WorkflowStepState;

import java.util.HashMap;

/**
 * $INTERFACE is ... User: greg Date: 1/17/14 Time: 12:09 PM
 */
public class WorkflowStepStateParser implements XmlNodeParser<WorkflowStepState> {
    private String xpath;

    public WorkflowStepStateParser(final String xpath) {
        this.xpath = xpath;
    }

    public WorkflowStepStateParser() {
    }

    @Override
    public WorkflowStepState parseXmlNode(final Node node) {
        final Node targetNode = xpath != null ? node.selectSingleNode(xpath) : node;
        final WorkflowStepState state = new WorkflowStepState();

        BaseStateParser.parseBaseState(targetNode, state);
        state.setStepContextId(StringUtils.trimToNull(targetNode.valueOf("@stepctx")));
        state.setStepNum(Integer.valueOf(targetNode.valueOf("@id")));
        state.setNodeStep(Boolean.valueOf(StringUtils.trimToNull(targetNode.valueOf("nodeStep"))));
        if (Boolean.valueOf(StringUtils.trimToNull(targetNode.valueOf("hasSubworkflow")))) {
            //parse sub workflow
            state.setSubWorkflow(new WorkflowStateParser("workflow").parseXmlNode(targetNode));
        }
        if (Boolean.valueOf(StringUtils.trimToNull(targetNode.valueOf("nodeStep")))) {
            //node states
            final HashMap<String, WorkflowStepContextState> nodeStates = new HashMap<String, WorkflowStepContextState>();
            for (final Object o : targetNode.selectNodes("nodeStates/nodeState")) {
                final Node nodeStateNode = (Node) o;
                final String nodeName = StringUtils.trimToNull(nodeStateNode.valueOf("@name"));
                if (null != nodeName) {
                    nodeStates.put(nodeName, new WorkflowStepContextStateParser(state).parseXmlNode(nodeStateNode));
                }
            }
            state.setNodeStates(nodeStates);
        }
        return state;
    }
}
