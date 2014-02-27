package org.rundeck.api.parser;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckProject;

import java.io.InputStream;

/**
 * ProjectParserV11Test is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectParserV11Test {
    @Test
    public void parseProject() throws Exception {
        InputStream input = getClass().getResourceAsStream("projectv11.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckProject project = new ProjectParserV11("project").parseXmlNode(document);

        Assert.assertEquals("ziggy", project.getName());
        Assert.assertNull(project.getDescription());
        Assert.assertNotNull(project.getProjectConfig());
    }
}
