package org.rundeck.api.generator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.rundeck.api.domain.ProjectConfig;
import org.rundeck.api.domain.RundeckProject;

/**
 * ProjectGenerator is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectGenerator {
    RundeckProject project;

    public ProjectGenerator(RundeckProject project) {
        this.project = project;
    }

    public Document generate() {
        Document projectDom = DocumentFactory.getInstance().createDocument();
        Element rootElem = projectDom.addElement("project");
        rootElem.addElement("name").setText(project.getName());
        ProjectConfig configuration = project.getProjectConfig();
        if (null != configuration) {

            Element config = rootElem.addElement("config");
            for (String s : configuration.getProperties().keySet()) {
                Element property = config.addElement("property");
                property.addAttribute("key", s);
                property.addAttribute("value", configuration.getProperties().get(s));
            }
        }
        return projectDom;
    }
}
