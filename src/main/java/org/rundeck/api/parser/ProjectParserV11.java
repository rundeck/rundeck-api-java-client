package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.RundeckProject;

/**
 * ProjectParserV11 supports embedded "config" element.
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectParserV11 extends ProjectParser {
    public ProjectParserV11() {
    }

    public ProjectParserV11(final String xpath) {
        super(xpath);
    }

    @Override
    public RundeckProject parse(final Node projectNode) {
        final RundeckProject rundeckProject = super.parse(projectNode);
        final Node config1 = projectNode.selectSingleNode("config");
        if (config1 == null) {
            return rundeckProject;
        }

        rundeckProject.setProjectConfig(new ProjectConfigParser().parseXmlNode(config1));

        return rundeckProject;
    }
}
