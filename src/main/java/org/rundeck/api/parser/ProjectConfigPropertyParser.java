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
public class ProjectConfigPropertyParser extends BaseXpathParser<ConfigProperty> {
    private String xpath;

    public ProjectConfigPropertyParser() {
        super();
    }

    public ProjectConfigPropertyParser(final String xpath) {
        super(xpath);
    }

    @Override
    public ConfigProperty parse(final Node propnode) {
        final String key = propnode.valueOf("@key");
        final String value = propnode.valueOf("@value");
        final ConfigProperty config = new ConfigProperty();
        config.setKey(key);
        config.setValue(value);
        return config;
    }

}
