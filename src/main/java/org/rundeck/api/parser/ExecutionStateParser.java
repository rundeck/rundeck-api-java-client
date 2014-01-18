package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 5:42 PM
 */
public class ExecutionStateParser implements XmlNodeParser<RundeckExecutionState> {
    private String xpath;

    public ExecutionStateParser() {
        super();
    }

    /**
     * @param xpath of the execution element if it is not the root node
     */
    public ExecutionStateParser(String xpath) {
        this();
        this.xpath = xpath;
    }

    @Override
    public RundeckExecutionState parseXmlNode(Node node) {
        Node targetNode = xpath != null ? node.selectSingleNode(xpath) : node;
        RundeckExecutionState rundeckExecutionState = new RundeckExecutionState();
        rundeckExecutionState.setExecutionId(Long.valueOf(targetNode.valueOf("@id")));

        WorkflowStateParser.parseWorkflowState(targetNode, rundeckExecutionState);


        final List<RundeckNode> rundeckNodes =
                new ListParser<RundeckNode>(new NodeParser(), "allNodes/nodes/node").parseXmlNode(targetNode);
        rundeckExecutionState.setAllNodes(new HashSet<RundeckNodeIdentity>(rundeckNodes));


        //node states
        HashMap<String, List<WorkflowStepContextState>> nodeStates = new HashMap<String, List<WorkflowStepContextState>>();

        for (Object o : targetNode.selectNodes("nodes/node")) {
            final Node nodeStateNode = (Node) o;
            final String nodeName = StringUtils.trimToNull(nodeStateNode.valueOf("@name"));
            if (null != nodeName) {
                ListParser<WorkflowStepContextState> workflowStepStateListParser
                        = new ListParser<WorkflowStepContextState>(new IndexedWorkflowStepStateParser(rundeckExecutionState, nodeName)
                        , "steps/step");
                nodeStates.put(nodeName, workflowStepStateListParser.parseXmlNode(nodeStateNode));
            }
        }
        rundeckExecutionState.setNodeStates(nodeStates);

        return rundeckExecutionState;
    }
}
