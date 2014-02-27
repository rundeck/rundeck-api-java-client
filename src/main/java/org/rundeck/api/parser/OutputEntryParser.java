package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;

import org.rundeck.api.domain.RundeckOutputEntry;
import org.rundeck.api.domain.RundeckOutputEntry.RundeckLogLevel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Parses output message content for API v6
 */
public class OutputEntryParser implements XmlNodeParser<RundeckOutputEntry> {


    private String xpath;

    public OutputEntryParser() {
        super();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private SimpleDateFormat dateFormat;
    /**
     * @param xpath of the event element if it is not the root node
     */
    public OutputEntryParser(String xpath) {
        this();
        this.xpath = xpath;
    }
    static HashSet<String> nonMetaAttributes = new HashSet<String>();
    static {
        nonMetaAttributes.add("time");
        nonMetaAttributes.add("level");
        nonMetaAttributes.add("user");
        nonMetaAttributes.add("node");
        nonMetaAttributes.add("type");
        nonMetaAttributes.add("log");
        nonMetaAttributes.add("absolute_type");
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
        if(null!=entryNode.valueOf("@absolute_time")) {
            outputEntry.setAbsoluteTime(parseDate(StringUtils.trimToNull(entryNode.valueOf("@absolute_time"))));
        }

        outputEntry.setUser(StringUtils.trimToNull(entryNode.valueOf("@user")));
        outputEntry.setNode(StringUtils.trimToNull(entryNode.valueOf("@node")));
        outputEntry.setType(StringUtils.trimToNull(entryNode.valueOf("@type")));

        HashMap<String, String> meta = new HashMap<String, String>();
        List list = entryNode.selectNodes("@*");
        for (Object node1 : list) {
            if(node1 instanceof Node) {
                Node child = (Node) node1;
                if (!nonMetaAttributes.contains(child.getName())) {
                    meta.put(child.getName(), child.getText());
                }
            }
        }
        if(meta.size()>0){
            outputEntry.setMetadata(meta);
        }
        outputEntry.setMessage(parseMessage(entryNode));
        
        return outputEntry;
    }

    private Date parseDate(String s) {
        if(null==s){
            return null;
        }
        try {
            Date parse = dateFormat.parse(s);
            return parse;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Parse the message content
     */
    protected String parseMessage(Node entryNode) {
        return StringUtils.trimToNull(entryNode.valueOf("@log"));
    }

}
