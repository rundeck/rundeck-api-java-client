package org.rundeck.api.generator;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.rundeck.api.domain.ConfigProperty;

/**
 * ProjectConfigPropertyGenerator generates a {@literal <property/>} element representing a configuration property.
 *
 * @author greg
 * @since 2014-03-07
 */
public class ProjectConfigPropertyGenerator extends BaseDocGenerator {
    private ConfigProperty property;

    public ProjectConfigPropertyGenerator(ConfigProperty property) {
        this.property = property;
    }

    @Override
    public Element generateXmlElement() {
        Element propElem = DocumentFactory.getInstance().createElement("property");
        propElem.addAttribute("key", property.getKey());
        propElem.addAttribute("value", property.getValue());

        return propElem;
    }
}
