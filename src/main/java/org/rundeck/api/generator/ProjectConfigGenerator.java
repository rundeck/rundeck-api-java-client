package org.rundeck.api.generator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.rundeck.api.domain.ProjectConfig;

/**
 * ProjectConfigGenerator is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectConfigGenerator extends BaseDocGenerator {
    private ProjectConfig config;

    public ProjectConfigGenerator(ProjectConfig config) {
        this.config = config;
    }

    @Override
    public Element generateXmlElement() {
        Element configEl = DocumentFactory.getInstance().createElement("config");
        if (null != config.getProperties()) {
            for (String s : config.getProperties().keySet()) {
                Element property = configEl.addElement("property");
                property.addAttribute("key", s);
                property.addAttribute("value", config.getProperties().get(s));
            }
        }
        return configEl;
    }

}
