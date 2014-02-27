package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.ProjectConfig;

import java.util.List;

/**
 * ProjectConfigParser parses project "config" element contents
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectConfigParser implements XmlNodeParser<ProjectConfig> {
    private String xpath;

    public ProjectConfigParser() {
    }

    public ProjectConfigParser(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public ProjectConfig parseXmlNode(Node node) {
        Node config1 = getXpath() != null ? node.selectSingleNode(getXpath()) : node;
        ProjectConfig config = new ProjectConfig();
        List property = config1.selectNodes("property");
        for (Object o : property) {
            Node propnode = (Node) o;
            String key = propnode.valueOf("@key");
            String value = propnode.valueOf("@value");
            if (null != key && null != value) {
                config.setProperty(key, value);
            }
        }
        return config;
    }

    public String getXpath() {
        return xpath;
    }
}
