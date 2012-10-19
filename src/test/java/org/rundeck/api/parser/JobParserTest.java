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
import org.rundeck.api.domain.RundeckJob;

/**
 * Test the {@link JobParser}
 * 
 * @author Vincent Behar
 */
public class JobParserTest {

    @Test
    public void parseJob() throws Exception {
        InputStream input = getClass().getResourceAsStream("job.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJob job = new JobParser("joblist/job").parseXmlNode(document);

        Assert.assertEquals("1", job.getId());
        Assert.assertEquals("job-name", job.getName());
        Assert.assertEquals("job description", job.getDescription());
        Assert.assertEquals("group-name", job.getGroup());
        Assert.assertEquals("project-name", job.getProject());
    }

    @Test
    public void parseExecutionJob1() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-running.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJob job = new JobParser("result/executions/execution/job").parseXmlNode(document);

        Assert.assertEquals("1", job.getId());
        Assert.assertEquals("ls", job.getName());
        Assert.assertEquals("list files", job.getDescription());
        Assert.assertEquals("system", job.getGroup());
        Assert.assertEquals("test", job.getProject());
        Assert.assertEquals(10000L, job.getAverageDuration());
    }
    @Test
    public void parseExecutionJob2() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-succeeded.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJob job = new JobParser("result/executions/execution/job").parseXmlNode(document);

        Assert.assertEquals("1", job.getId());
        Assert.assertEquals("ls", job.getName());
        Assert.assertEquals("list files", job.getDescription());
        Assert.assertEquals("system", job.getGroup());
        Assert.assertEquals("test", job.getProject());
        Assert.assertEquals(-1, job.getAverageDuration());
    }

}
