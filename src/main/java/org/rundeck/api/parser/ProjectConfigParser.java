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
public class ProjectConfigParser extends BaseXpathParser<ProjectConfig> {
    private String xpath;

    public ProjectConfigParser() {
        super();
    }

    public ProjectConfigParser(String xpath) {
        super(xpath);
    }

    @Override
    public ProjectConfig parse(Node config1) {
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

}
