package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;

import org.rundeck.api.domain.RundeckOutputEntry;
import org.rundeck.api.domain.RundeckOutputEntry.RundeckLogLevel;

public class OutputEntryParser implements XmlNodeParser<RundeckOutputEntry> {


    private String xpath;

    public OutputEntryParser() {
        super();
    }

    /**
     * @param xpath of the event element if it is not the root node
     */
    public OutputEntryParser(String xpath) {
        super();
        this.xpath = xpath;
    }

    @Override
    public RundeckOutputEntry parseXmlNode(Node node) {
        Node entryNode = xpath != null ? node.selectSingleNode(xpath) : node;

        RundeckOutputEntry outputEntry = new RundeckOutputEntry();
        
        outputEntry.setTime(StringUtils.trimToNull(entryNode.valueOf("@time")));
        try {
    		outputEntry.setLevel(RundeckLogLevel.valueOf(StringUtils.upperCase(entryNode.valueOf("@level"))));
        } catch (IllegalArgumentException e) {
            outputEntry.setLevel(null);
        }

        outputEntry.setUser(StringUtils.trimToNull(entryNode.valueOf("@user")));
        outputEntry.setCommand(StringUtils.trimToNull(entryNode.valueOf("@command")));
        outputEntry.setNode(StringUtils.trimToNull(entryNode.valueOf("@node")));
        outputEntry.setMessage(StringUtils.trimToNull(entryNode.getStringValue()));
        
        return outputEntry;
    }

}