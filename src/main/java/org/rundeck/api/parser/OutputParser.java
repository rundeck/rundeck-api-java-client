package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckOutput;
import org.rundeck.api.domain.RundeckOutputEntry;

import java.util.List;


public class OutputParser implements XmlNodeParser<RundeckOutput> {


    private String xpath;
    XmlNodeParser<RundeckOutputEntry> parser;

    public OutputParser(String xpath, XmlNodeParser<RundeckOutputEntry> parser) {
        this.xpath = xpath;
        if (null != parser) {
            this.parser = parser;
        }
    }

    @Override
    public RundeckOutput parseXmlNode(Node node) {
        Node entryNode = xpath != null ? node.selectSingleNode(xpath) : node;

        RundeckOutput output = new RundeckOutput();

        //output.setMessage(StringUtils.trimToNull(entryNode.valueOf("message")));
        //output.setError(StringUtils.trimToNull(entryNode.valueOf("error")));


        try {
            output.setExecutionId(Long.valueOf(entryNode.valueOf("id")));
        } catch (NumberFormatException e) {
            output.setExecutionId(null);
        }

        try {
            output.setOffset(Integer.valueOf(entryNode.valueOf("offset")));
        } catch (NumberFormatException e) {
            output.setOffset(-1);
        }

        output.setCompleted(Boolean.valueOf(entryNode.valueOf("completed")));
        output.setExecCompleted(Boolean.valueOf(entryNode.valueOf("execCompleted")));
        output.setHasFailedNodes(Boolean.valueOf(entryNode.valueOf("hasFailedNodes")));
        output.setUnmodified(Boolean.valueOf(entryNode.valueOf("unmodified")));

        try {
            output.setStatus(RundeckExecution.ExecutionStatus
                                 .valueOf(StringUtils.upperCase(entryNode.valueOf("execState"))));
        } catch (IllegalArgumentException e) {
            output.setStatus(null);
        }

        try {
            output.setLastModified(Long.valueOf(entryNode.valueOf("lastModified")));
        } catch (NumberFormatException e) {
            output.setLastModified(null);
        }

        try {
            output.setExecDuration(Long.valueOf(entryNode.valueOf("execDuration")));
        } catch (NumberFormatException e) {
            output.setExecDuration(null);
        }

        try {
            output.setPercentLoaded(Float.valueOf(entryNode.valueOf("percentLoaded")));
        } catch (NumberFormatException e) {
            output.setPercentLoaded(null);
        }

        try {
            output.setTotalSize(Integer.valueOf(entryNode.valueOf("totalSize")));
        } catch (NumberFormatException e) {
            output.setTotalSize(-1);
        }
        if(entryNode.selectSingleNode("filter")!=null){
            output.setFilterNode(StringUtils.trimToNull(entryNode.valueOf("filter/@nodename")));
            output.setFilterStep(StringUtils.trimToNull(entryNode.valueOf("filter/@stepctx")));
        }

        Node entriesListNode = entryNode.selectSingleNode("entries");

        if (entriesListNode != null) {
            @SuppressWarnings("unchecked")
            List<Node> entries = entriesListNode.selectNodes("entry");

            for (Node logEntryNode : entries) {
                RundeckOutputEntry outputEntry = parser.parseXmlNode(logEntryNode);
                output.addLogEntry(outputEntry);
            }
        }
        return output;
    }

}
