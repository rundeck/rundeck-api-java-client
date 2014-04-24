package org.rundeck.api.generator;

import junit.framework.Assert;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.rundeck.api.domain.ProjectConfig;
import org.rundeck.api.domain.RundeckProject;

import java.io.UnsupportedEncodingException;

/**
 * ProjectConfigGeneratorTest is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectConfigGeneratorTest {
    @Test
    public void generate() throws Exception {
        ProjectConfig config = new ProjectConfig();
        config.setProperty("abc", "123");
        config.setProperty("monkey.bonanza", "pale\ncomparison");

        Document doc = new ProjectConfigGenerator(config).generateXmlDocument();
        XMLWriter xmlWriter = new XMLWriter(System.out);
        xmlWriter.write(doc);
        xmlWriter.flush();
        Element configElement = doc.getRootElement();
        Assert.assertEquals("config", configElement.getName());
        Assert.assertNotNull(configElement.selectSingleNode("property[1]"));
        Assert.assertEquals("abc", configElement.selectSingleNode("property[1]/@key").getText());
        Assert.assertEquals("123", configElement.selectSingleNode("property[1]/@value").getText());

        Assert.assertNotNull(configElement.selectSingleNode("property[2]"));
        Assert.assertEquals("monkey.bonanza", configElement.selectSingleNode("property[2]/@key").getText());
        Assert.assertEquals("pale\ncomparison", configElement.selectSingleNode("property[2]/@value").getText());

    }
}
