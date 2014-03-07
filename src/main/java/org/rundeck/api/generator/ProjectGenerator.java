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
public class ProjectGenerator extends BaseDocGenerator {
    RundeckProject project;

    public ProjectGenerator(RundeckProject project) {
        this.project = project;
    }

    @Override
    public Element generateXmlElement() {
        Element rootElem = DocumentFactory.getInstance().createElement("project");
        rootElem.addElement("name").setText(project.getName());
        ProjectConfig configuration = project.getProjectConfig();
        if (null != configuration) {
            rootElem.add(new ProjectConfigGenerator(configuration).generateXmlElement());
        }
        return rootElem;
    }
}
