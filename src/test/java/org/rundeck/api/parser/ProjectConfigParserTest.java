package org.rundeck.api.parser;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.ProjectConfig;
import org.rundeck.api.domain.RundeckProject;

import java.io.InputStream;

/**
 * ProjectConfigParserTest is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectConfigParserTest {
    @Test
    public void parseProject() throws Exception {
        InputStream input = getClass().getResourceAsStream("projectv11.xml");
        Document document = ParserHelper.loadDocument(input);

        ProjectConfig config = new ProjectConfigParser("project/config").parseXmlNode(document);

        Assert.assertEquals(10, config.getProperties().size());
        Assert.assertEquals("ziggy", config.getProperties().get("project.name"));
        Assert.assertEquals("false", config.getProperties().get("resources.source.1.config.requireFileExists"));
        Assert.assertEquals("privateKey", config.getProperties().get("project.ssh-authentication"));
        Assert.assertEquals("jsch-ssh", config.getProperties().get("service.NodeExecutor.default.provider"));
        Assert.assertEquals("false", config.getProperties().get("resources.source.1.config.includeServerNode"));
        Assert.assertEquals("false", config.getProperties().get("resources.source.1.config.generateFileAutomatically"));
        Assert.assertEquals("/var/rundeck/projects/${project.name}/etc/resources.xml",
                config.getProperties().get("resources.source.1.config.file"));
        Assert.assertEquals("/var/lib/rundeck/.ssh/id_rsa", config.getProperties().get("project.ssh-keypath"));
        Assert.assertEquals("jsch-scp", config.getProperties().get("service.FileCopier.default.provider"));
        Assert.assertEquals("file", config.getProperties().get("resources.source.1.type"));
        /*
        <property key='project.name' value='ziggy'/>
        <property key='resources.source.1.config.requireFileExists' value='false'/>
        <property key='project.ssh-authentication' value='privateKey'/>
        <property key='service.NodeExecutor.default.provider' value='jsch-ssh'/>
        <property key='resources.source.1.config.includeServerNode' value='false'/>
        <property key='resources.source.1.config.generateFileAutomatically' value='false'/>
        <property key='resources.source.1.config.file'
                  value='/var/rundeck/projects/${project.name}/etc/resources.xml'/>
        <property key='project.ssh-keypath' value='/var/lib/rundeck/.ssh/id_rsa'/>
        <property key='service.FileCopier.default.provider' value='jsch-scp'/>
        <property key='resources.source.1.type' value='file'/>
         */
    }
}
