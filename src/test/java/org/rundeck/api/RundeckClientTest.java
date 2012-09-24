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
package org.rundeck.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import betamax.MatchRule;
import betamax.TapeMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.rundeck.api.domain.RundeckEvent;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckHistory;
import org.rundeck.api.domain.RundeckProject;
import betamax.Betamax;
import betamax.Recorder;
import org.rundeck.api.query.ExecutionQuery;


/**
 * Test the {@link RundeckClient}. Uses betamax to unit-test HTTP requests without a live RunDeck instance.
 * 
 * @author Vincent Behar
 */
public class RundeckClientTest {

    @Rule
    public Recorder recorder = new Recorder();

    private RundeckClient client;

    @Test
    @Betamax(tape = "get_projects")
    public void getProjects() throws Exception {
        List<RundeckProject> projects = client.getProjects();
        Assert.assertEquals(1, projects.size());
        Assert.assertEquals("test", projects.get(0).getName());
        Assert.assertNull(projects.get(0).getDescription());
    }
    @Test
    @Betamax(tape = "get_history")
    public void getHistory() throws Exception {
        final RundeckHistory test = client.getHistory("test");
        Assert.assertEquals(3, test.getCount());
        Assert.assertEquals(20, test.getMax());
        Assert.assertEquals(0, test.getOffset());
        Assert.assertEquals(5, test.getTotal());
        final List<RundeckEvent> events = test.getEvents();
        Assert.assertEquals(3, events.size());
    }

    @Test
    @Betamax(tape = "get_history_joblist",
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query /*, MatchRule.body */})
    public void getHistoryJoblist() throws Exception {
        final List<String> jobNames = Arrays.asList("malk/blah", "malk/blah2");
        final RundeckHistory test = client.getHistory("demo", null, null, jobNames, null, null, null, null, null);
        Assert.assertEquals(2, test.getCount());
        Assert.assertEquals(20, test.getMax());
        Assert.assertEquals(0, test.getOffset());
        Assert.assertEquals(2, test.getTotal());
        final List<RundeckEvent> events = test.getEvents();
        Assert.assertEquals(2, events.size());
        final List<String> names = new ArrayList<String>();
        for (final RundeckEvent event : events) {
            names.add(event.getTitle());
        }
        Assert.assertEquals(Arrays.asList("malk/blah2", "malk/blah"), names);
    }

    @Test
    @Betamax(tape = "get_history_excludeJoblist",
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query /*, MatchRule.body */})
    public void getHistoryExcludeJoblist() throws Exception {
        final List<String> jobNames = Arrays.asList("malk/blah", "malk/blah2");
        final RundeckHistory test = client.getHistory("demo", null, null, null, jobNames, null, null, null, null);
        Assert.assertEquals(2, test.getCount());
        Assert.assertEquals(20, test.getMax());
        Assert.assertEquals(0, test.getOffset());
        Assert.assertEquals(2, test.getTotal());
        final List<RundeckEvent> events = test.getEvents();
        Assert.assertEquals(2, events.size());
        final List<String> names = new ArrayList<String>();
        for (final RundeckEvent event : events) {
            names.add(event.getTitle());
        }
        Assert.assertEquals(Arrays.asList("fliff", "malk/blah3"), names);
    }

    @Test
    @Betamax(tape = "get_history_user",
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query })
    public void getHistoryUser() throws Exception {
        final RundeckHistory test = client.getHistory("demo", "bob", (String)null, (List)null, null, null, null, 1L, 0L);
        Assert.assertEquals(1, test.getCount());
        Assert.assertEquals(20, test.getMax());
        Assert.assertEquals(0, test.getOffset());
        Assert.assertEquals(1, test.getTotal());
        final List<RundeckEvent> events = test.getEvents();
        Assert.assertEquals(1, events.size());
        final List<String> names = new ArrayList<String>();
        for (final RundeckEvent event : events) {
            names.add(event.getUser());
        }
        Assert.assertEquals(Arrays.asList("bob"), names);
    }

    @Test
    @Betamax(tape = "get_executions",
             mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.headers, MatchRule.method, MatchRule.path, MatchRule.query})
    public void getExecutions() throws Exception {

        RundeckClient client = new RundeckClient("http://rundeck.local:4440", "0UUNkeRp4d58EDeCs7S6UdODp334DvK9");


        final String projectName = "blah";
        final List<RundeckExecution> jobTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .job("test job")
                                                                        .build(),
                                                                    2L,
                                                                    0L);
        Assert.assertEquals(2, jobTest.size());
        final List<RundeckExecution> jobExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                             .project(projectName)
                                                                             .jobExact("test job")
                                                                             .build(),
                                                                         2L,
                                                                         0L);
        Assert.assertEquals(2, jobExactTest.size());
        final List<RundeckExecution> excludeJobTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .excludeJob("test job")
                                                                        .build(),
                                                                    2L,
                                                                    0L);
        Assert.assertEquals(2, excludeJobTest.size());
        final List<RundeckExecution> excludeJobExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                             .project(projectName)
                                                                             .excludeJobExact("test job")
                                                                             .build(),
                                                                         2L,
                                                                         0L);
        Assert.assertEquals(2, excludeJobExactTest.size());
        final List<RundeckExecution> descriptionTest = client.getExecutions(ExecutionQuery.builder()
                                                                                .project(projectName)
                                                                                .description("a description")
                                                                                .build(), 2L, 0L);
        Assert.assertEquals(2, descriptionTest.size());
        final List<RundeckExecution> abortedbyTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .abortedby("admin")
                                                                              .build(),
                                                                          2L,
                                                                          0L);
        Assert.assertEquals(1, abortedbyTest.size());
        final List<RundeckExecution> beginTest = client.getExecutions(ExecutionQuery.builder()
                                                                          .project(projectName)
                                                                          .begin(new Date(1347581178168L))
                                                                          .build(), 2L, 0L);
        Assert.assertEquals(2, beginTest.size());
        final List<RundeckExecution> endTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .end(new Date(1347581178168L))
                                                                        .build(), 2L, 0L);
        Assert.assertEquals(2, endTest.size());
        final List<String> excludeJobIdList = Arrays.asList("123", "456");
        final List<RundeckExecution> excludeJobIdListTest = client.getExecutions(ExecutionQuery.builder()
                                                                                     .project(projectName)
                                                                                     .excludeJobIdList(excludeJobIdList)
                                                                                     .build(), 2L, 0L);
        Assert.assertEquals(2, excludeJobIdListTest.size());
        final List<String> jobList = Arrays.asList("fruit/mango", "fruit/lemon");
        final List<RundeckExecution> jobListTest = client.getExecutions(ExecutionQuery.builder()
                                                                            .project(projectName)
                                                                            .jobList(jobList)
                                                                            .build(), 2L, 0L);
        Assert.assertEquals(2, jobListTest.size());
        final List<String> excludeJobList = Arrays.asList("a/path/job1", "path/to/job2");
        final List<RundeckExecution> excludeJobListTest = client.getExecutions(ExecutionQuery.builder()
                                                                                   .project(projectName)
                                                                                   .excludeJobList(excludeJobList)
                                                                                   .build(), 2L, 0L);
        Assert.assertEquals(2, excludeJobListTest.size());
        final List<String> list = Arrays.asList("1f4415d7-3b52-4fc8-ba42-b6ac97508bff",
                                                "d9fc5ee6-f1db-4d24-8808-feda18345bab");
        final List<RundeckExecution> jobIdListTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .jobIdList(list)
                                                                              .build(), 2L, 0L);
        Assert.assertEquals(2, jobIdListTest.size());
        final List<RundeckExecution> groupPathTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .groupPath("fruit")
                                                                              .build(),
                                                                          2L,
                                                                          0L);
        Assert.assertEquals(2, groupPathTest.size());
        final List<RundeckExecution> groupPathExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                                   .project(projectName)
                                                                                   .groupPathExact("fruit")
                                                                                   .build(), 2L, 0L);
        Assert.assertEquals(2, groupPathExactTest.size());

        final List<RundeckExecution> excludeGroupPathTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .excludeGroupPath("fruit")
                                                                              .build(),
                                                                          2L,
                                                                          0L);
        Assert.assertEquals(2, excludeGroupPathTest.size());
        final List<RundeckExecution> excliudeGroupPathExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                                   .project(projectName)
                                                                                   .excludeGroupPathExact("fruit")
                                                                                   .build(), 2L, 0L);
        Assert.assertEquals(2, excliudeGroupPathExactTest.size());

        final List<RundeckExecution> recentTest = client.getExecutions(ExecutionQuery.builder()
                                                                           .project(projectName)
                                                                           .recent("1h").build(), 2L, 0L);
        Assert.assertEquals(2, recentTest.size());
        final List<RundeckExecution> statusTest = client.getExecutions(ExecutionQuery.builder()
                                                                           .project(projectName)
                                                                           .status(RundeckExecution.ExecutionStatus.SUCCEEDED)
                                                                           .build(), 2L, 0L);
        Assert.assertEquals(2, statusTest.size());
        final List<RundeckExecution> adhocTest = client.getExecutions(ExecutionQuery.builder()
                                                                           .project(projectName)
                                                                           .adhoc(true)
                                                                           .build(), 2L, 0L);
        Assert.assertEquals(2, adhocTest.size());
    }

    @Before
    public void setUp() throws Exception {
        // not that you can put whatever here, because we don't actually connect to the RunDeck instance
        // but instead use betamax as a proxy to serve the previously recorded tapes (in src/test/resources)
        client = new RundeckClient("http://rundeck.local:4440", "PVnN5K3OPc5vduS3uVuVnEsD57pDC5pd");
    }

}
