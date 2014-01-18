package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * $INTERFACE is ... User: greg Date: 1/16/14 Time: 5:44 PM
 */
public class WorkflowStateParser implements XmlNodeParser<WorkflowState> {
    private String xpath;

    public WorkflowStateParser() {
    }

    public WorkflowStateParser(String xpath) {
        this();
        this.xpath = xpath;
    }

    private static final ThreadLocal<DateFormat> w3cDateFormat = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            return fmt;
        }
    };
    public static Date parseDate(String s) {
        if (null == s) {
            return null;
        }
        try {
            Date parse = w3cDateFormat.get().parse(s);
            return parse;
        } catch (ParseException e) {
            return null;
        }
    }

    private static int integerValue(final String value, final int defValue) {
        int parseMax = defValue;
        try {
            parseMax = null != value ? Integer.parseInt(value) : defValue;
        } catch (NumberFormatException e) {
        }
        return parseMax;
    }

    @Override
    public WorkflowState parseXmlNode(Node node) {
        Node targetNode = xpath != null ? node.selectSingleNode(xpath) : node;
        WorkflowState state = new WorkflowState();
        parseWorkflowState(targetNode, state);


        return state;
    }

    /**
     * Parse the workflow state components from the given dom node
     * @param targetNode
     * @param state
     */
    public static void parseWorkflowState(Node targetNode, WorkflowState state) {
        BaseStateParser.parseBaseState(targetNode, state);

        state.setStepCount(integerValue(StringUtils.trimToNull(targetNode.valueOf("stepCount")), 0));

        final List<RundeckNode> rundeckNodes =
                new ListParser<RundeckNode>(new NodeParser(), "targetNodes/nodes/node").parseXmlNode(targetNode);
        state.setTargetNodes(new HashSet<RundeckNodeIdentity>(rundeckNodes));

        //steps
        state.setSteps(new ListParser<WorkflowStepState>(new WorkflowStepStateParser(),
                "steps/step").parseXmlNode(targetNode));
    }

}
