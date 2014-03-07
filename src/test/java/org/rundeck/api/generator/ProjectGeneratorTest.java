package org.rundeck.api.generator;

import junit.framework.Assert;
import org.dom4j.Document;
import org.junit.Test;
import org.rundeck.api.domain.RundeckProject;

/**
 * ProjectGeneratorTest is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectGeneratorTest {
    @Test
    public void generate() {
        RundeckProject project = new RundeckProject();
        project.setName("monkey1");

        Document doc = new ProjectGenerator(project).generateXmlDocument();
        Assert.assertEquals("project", doc.getRootElement().getName());
        Assert.assertNotNull(doc.selectSingleNode("/project/name"));
        Assert.assertEquals("monkey1", doc.selectSingleNode("/project/name").getText());
    }
}
