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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckExecution.ExecutionStatus;
import org.rundeck.api.domain.RundeckNodeIdentity;

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

    @Test
    public void parseV9Execution() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-running-v9.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecution execution = new ExecutionParser("result/executions/execution").parseXmlNode(document);
        RundeckJob job = execution.getJob();

        Assert.assertEquals(null, job);
        Assert.assertEquals(new Long(119), execution.getId());
        Assert.assertEquals("http://localhost:4440/execution/follow/119", execution.getUrl());
        Assert.assertEquals(ExecutionStatus.RUNNING, execution.getStatus());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(new Date(1377104570966L), execution.getStartedAt());
        Assert.assertEquals(null, execution.getEndedAt());
        Assert.assertEquals(null, execution.getDurationInMillis());
        Assert.assertEquals(null, execution.getDuration());
        Assert.assertEquals(null, execution.getAbortedBy());
        Assert.assertEquals("echo asdf; sleep 120", execution.getDescription());
        Assert.assertEquals(null, execution.getArgstring());
        Assert.assertEquals("test", execution.getProject());


    }

    @Test
    public void parseV10Execution() throws Exception {
        InputStream input = getClass().getResourceAsStream("execution-result-v10.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckExecution execution = new ExecutionParser("result/executions/execution").parseXmlNode(document);
        RundeckJob job = execution.getJob();

        Assert.assertNotNull(job);
        Assert.assertEquals(new Long(146), execution.getId());
        Assert.assertEquals("http://dignan.local:4440/execution/follow/146", execution.getUrl());
        Assert.assertEquals(ExecutionStatus.SUCCEEDED, execution.getStatus());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(new Date(1389894502959L), execution.getStartedAt());
        Assert.assertEquals(new Date(1389894504561L), execution.getEndedAt());
        Assert.assertEquals((Long)(1389894504561L- 1389894502959L), execution.getDurationInMillis());
        Assert.assertEquals(null, execution.getAbortedBy());
        Assert.assertEquals("fdfd", execution.getProject());

        Assert.assertNotNull(execution.getSuccessfulNodes());
        Assert.assertEquals(3, execution.getSuccessfulNodes().size());

        HashSet<String> expectedSuccess = new HashSet<String>();
        expectedSuccess.addAll(Arrays.asList(
                "node-111.qa.subgroup.mycompany.com",
                "node-6.qa.subgroup.mycompany.com",
                "node-14.qa.subgroup.mycompany.com"));
        for (RundeckNodeIdentity rundeckNodeIdentity : execution.getSuccessfulNodes()) {
            Assert.assertTrue(expectedSuccess.contains(rundeckNodeIdentity.getName()));
        }

        Assert.assertNotNull(execution.getFailedNodes());
        Assert.assertEquals(3, execution.getFailedNodes().size());
        HashSet<String> expectedFailure = new HashSet<String>();
        expectedFailure.addAll(Arrays.asList(
                "node-112.qa.subgroup.mycompany.com",
                "node-62.qa.subgroup.mycompany.com",
                "node-12.qa.subgroup.mycompany.com"));
        for (RundeckNodeIdentity rundeckNodeIdentity : execution.getFailedNodes()) {
            Assert.assertTrue(expectedFailure.contains(rundeckNodeIdentity.getName()));
        }
    }

}
