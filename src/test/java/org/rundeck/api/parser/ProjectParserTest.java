/*
 * Copyright 2011 Vincent Behar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rundeck.api.parser;

import java.io.InputStream;
import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckProject;

/**
 * Test the {@link ProjectParser}
 * 
 * @author Vincent Behar
 */
public class ProjectParserTest {

    @Test
    public void parseProject() throws Exception {
        InputStream input = getClass().getResourceAsStream("project.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckProject project = new ProjectParser("result/projects/project").parseXmlNode(document);

        Assert.assertEquals("test", project.getName());
        Assert.assertEquals("test project", project.getDescription());
    }
    @Test
    public void parseProject_noresult() throws Exception {
        InputStream input = getClass().getResourceAsStream("project.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckProject project = new ProjectParser("projects/project").parseXmlNode(document);

        Assert.assertEquals("test", project.getName());
        Assert.assertEquals("test project", project.getDescription());
    }

}
