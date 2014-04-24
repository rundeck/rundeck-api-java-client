package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.ConfigProperty;

/**
 * ProjectConfigPropertyParser parses a {@literal <property/>} element representing
 * a configuration property.
 *
 * @author greg
 * @since 2014-03-07
 */
public class ProjectConfigPropertyParser implements XmlNodeParser<ConfigProperty> {
    private String xpath;

    public ProjectConfigPropertyParser() {
    }

    public ProjectConfigPropertyParser(final String xpath) {
        this.setXpath(xpath);
    }

    @Override
    public ConfigProperty parseXmlNode(final Node node) {
        final Node propnode = getXpath() != null ? node.selectSingleNode(getXpath()) : node;
        final String key = propnode.valueOf("@key");
        final String value = propnode.valueOf("@value");
        final ConfigProperty config = new ConfigProperty();
        config.setKey(key);
        config.setValue(value);
        return config;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(final String xpath) {
        this.xpath = xpath;
    }
}
