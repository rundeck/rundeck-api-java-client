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
import java.util.Date;
import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckExecution.ExecutionStatus;

/**
 * Test the {@link ExecutionParser}
 * 
 * @author Vincent Behar
 */
public class ExecutionParserTest {

    @Test
    public void parseRunningExecution() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-running.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecution execution = new ExecutionParser("result/executions/execution").parseXmlNode(document);
        RundeckJob job = execution.getJob();

        Assert.assertEquals(new Long(1), execution.getId());
        Assert.assertEquals("http://localhost:4440/execution/follow/1", execution.getUrl());
        Assert.assertEquals(ExecutionStatus.RUNNING, execution.getStatus());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(new Date(1302183830082L), execution.getStartedAt());
        Assert.assertEquals(null, execution.getEndedAt());
        Assert.assertEquals(null, execution.getDurationInMillis());
        Assert.assertEquals(null, execution.getDuration());
        Assert.assertEquals(null, execution.getAbortedBy());
        Assert.assertEquals("ls ${option.dir}", execution.getDescription());
        Assert.assertEquals("-arg1 value -arg2 value", execution.getArgstring());

        Assert.assertEquals("1", job.getId());
        Assert.assertEquals("ls", job.getName());
        Assert.assertEquals("system", job.getGroup());
        Assert.assertEquals("test", job.getProject());
        Assert.assertEquals("list files", job.getDescription());
    }

    @Test
    public void parseSucceededExecution() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-succeeded.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecution execution = new ExecutionParser("result/executions/execution").parseXmlNode(document);
        RundeckJob job = execution.getJob();

        Assert.assertEquals(new Long(1), execution.getId());
        Assert.assertEquals("http://localhost:4440/execution/follow/1", execution.getUrl());
        Assert.assertEquals(ExecutionStatus.SUCCEEDED, execution.getStatus());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(new Date(1308322895104L), execution.getStartedAt());
        Assert.assertEquals(new Date(1308322959420L), execution.getEndedAt());
        Assert.assertEquals(new Long(64316), execution.getDurationInMillis());
        Assert.assertEquals("1 minute 4 seconds", execution.getDuration());
        Assert.assertEquals(null, execution.getAbortedBy());
        Assert.assertEquals("ls ${option.dir}", execution.getDescription());
        Assert.assertEquals("-argA some -argB thing", execution.getArgstring());

        Assert.assertEquals("1", job.getId());
        Assert.assertEquals("ls", job.getName());
        Assert.assertEquals("system", job.getGroup());
        Assert.assertEquals("test", job.getProject());
        Assert.assertEquals("list files", job.getDescription());
    }

    @Test
    public void parseAdhocExecution() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-adhoc.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecution execution = new ExecutionParser("result/executions/execution").parseXmlNode(document);
        RundeckJob job = execution.getJob();

        Assert.assertEquals(new Long(1), execution.getId());
        Assert.assertEquals("http://localhost:4440/execution/follow/1", execution.getUrl());
        Assert.assertEquals(ExecutionStatus.SUCCEEDED, execution.getStatus());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(new Date(1309857539137L), execution.getStartedAt());
        Assert.assertEquals(new Date(1309857539606L), execution.getEndedAt());
        Assert.assertEquals(new Long(469), execution.getDurationInMillis());
        Assert.assertEquals("0 seconds", execution.getDuration());
        Assert.assertEquals(null, execution.getAbortedBy());
        Assert.assertEquals("w", execution.getDescription());
        Assert.assertEquals("-monkey true", execution.getArgstring());

        Assert.assertNull(job);
    }

    @Test
    public void parseMinimalistExecution() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-minimalist.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecution execution = new ExecutionParser("result/execution").parseXmlNode(document);
        RundeckJob job = execution.getJob();

        Assert.assertEquals(new Long(1), execution.getId());
        Assert.assertNull(execution.getUrl());
        Assert.assertNull(execution.getStatus());
        Assert.assertNull(execution.getStartedBy());
        Assert.assertNull(execution.getStartedAt());
        Assert.assertNull(execution.getEndedAt());
        Assert.assertNull(execution.getDurationInMillis());
        Assert.assertNull(execution.getDuration());
        Assert.assertNull(execution.getAbortedBy());
        Assert.assertNull(execution.getDescription());
        Assert.assertNull(execution.getArgstring());

        Assert.assertNull(job);
    }

}
