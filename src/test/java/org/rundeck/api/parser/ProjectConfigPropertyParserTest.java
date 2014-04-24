package org.rundeck.api.parser;

import junit.framework.Assert;
import org.dom4j.Document;
import org.junit.Test;
import org.junit.runners.JUnit4;
import org.rundeck.api.domain.ConfigProperty;
import org.rundeck.api.domain.ProjectConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Test
 *
 * @author greg
 * @since 2014-03-07
 */
public class ProjectConfigPropertyParserTest {
    @Test
    public void parseFromProject() throws Exception {
        InputStream input = getClass().getResourceAsStream("projectv11.xml");
        Document document = ParserHelper.loadDocument(input);

        ConfigProperty config = new ProjectConfigPropertyParser("project/config/property[1]").parseXmlNode(document);
        Assert.assertEquals("project.name", config.getKey());
        Assert.assertEquals("ziggy", config.getValue());
        /**
         * <property key='project.name' value='ziggy'/>
         <property key='resources.source.1.config.requireFileExists' value='false'/>

         */
    }
    @Test
    public void parseProperty() throws Exception {
        Document document = ParserHelper.loadDocument(new ByteArrayInputStream(("<property key='project.name' " +
                "value='ABC' />").getBytes()));

        ConfigProperty config = new ProjectConfigPropertyParser("/property").parseXmlNode(document);
        Assert.assertEquals("project.name", config.getKey());
        Assert.assertEquals("ABC", config.getValue());
        /**
         * <property key='project.name' value='ziggy'/>
         <property key='resources.source.1.config.requireFileExists' value='false'/>

         */
    }
}
