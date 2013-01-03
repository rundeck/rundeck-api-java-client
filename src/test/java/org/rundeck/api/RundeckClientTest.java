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

import betamax.Betamax;
import betamax.MatchRule;
import betamax.Recorder;
import betamax.TapeMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.rundeck.api.domain.RundeckEvent;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckHistory;
import org.rundeck.api.domain.RundeckJobDelete;
import org.rundeck.api.domain.RundeckJobDeleteBulk;
import org.rundeck.api.domain.RundeckProject;
import org.rundeck.api.query.ExecutionQuery;
import org.rundeck.api.util.PagedResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Test the {@link RundeckClient}. Uses betamax to unit-test HTTP requests without a live RunDeck instance.
 * 
 * @author Vincent Behar
 */
public class RundeckClientTest {

    public static final String TEST_TOKEN_0 = "PVnN5K3OPc5vduS3uVuVnEsD57pDC5pd";
    public static final String TEST_TOKEN_1 = "0UUNkeRp4d58EDeCs7S6UdODp334DvK9";
    public static final String TEST_TOKEN_2 = "PP4s4SdCRO6KUoNPd1D303Dc304ORN87";

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

        RundeckClient client = createClient(TEST_TOKEN_1);


        final String projectName = "blah";
        final PagedResults<RundeckExecution> jobTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .job("test job")
                                                                        .build(),
                                                                    2L,
                                                                    0L);
        assertPageResults(jobTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> jobExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                             .project(projectName)
                                                                             .jobExact("test job")
                                                                             .build(),
                                                                         2L,
                                                                         0L);
        assertPageResults(jobExactTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> excludeJobTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .excludeJob("test job")
                                                                        .build(),
                                                                    2L,
                                                                    0L);
        assertPageResults(excludeJobTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> excludeJobExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                             .project(projectName)
                                                                             .excludeJobExact("test job")
                                                                             .build(),
                                                                         2L,
                                                                         0L);
        assertPageResults(excludeJobExactTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> descriptionTest = client.getExecutions(ExecutionQuery.builder()
                                                                                .project(projectName)
                                                                                .description("a description")
                                                                                .build(), 2L, 0L);
        assertPageResults(descriptionTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> abortedbyTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .abortedby("admin")
                                                                              .build(),
                                                                          2L,
                                                                          0L);
        assertPageResults(abortedbyTest, 1, 1, 2, 0, 1);
        final PagedResults<RundeckExecution> beginTest = client.getExecutions(ExecutionQuery.builder()
                                                                          .project(projectName)
                                                                          .begin(new Date(1347581178168L))
                                                                          .build(), 2L, 0L);
        assertPageResults(beginTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> endTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .end(new Date(1347581178168L))
                                                                        .build(), 2L, 0L);
        assertPageResults(endTest, 2, 2, 2, 0, 2);
        final List<String> excludeJobIdList = Arrays.asList("123", "456");
        final PagedResults<RundeckExecution> excludeJobIdListTest = client.getExecutions(ExecutionQuery.builder()
                                                                                     .project(projectName)
                                                                                     .excludeJobIdList(excludeJobIdList)
                                                                                     .build(), 2L, 0L);
        assertPageResults(excludeJobIdListTest, 2, 2, 2, 0, 2);
        final List<String> jobList = Arrays.asList("fruit/mango", "fruit/lemon");
        final PagedResults<RundeckExecution> jobListTest = client.getExecutions(ExecutionQuery.builder()
                                                                            .project(projectName)
                                                                            .jobList(jobList)
                                                                            .build(), 2L, 0L);
        assertPageResults(jobListTest, 2, 2, 2, 0, 2);
        final List<String> excludeJobList = Arrays.asList("a/path/job1", "path/to/job2");
        final PagedResults<RundeckExecution> excludeJobListTest = client.getExecutions(ExecutionQuery.builder()
                                                                                   .project(projectName)
                                                                                   .excludeJobList(excludeJobList)
                                                                                   .build(), 2L, 0L);
        assertPageResults(excludeJobListTest, 2, 2, 2, 0, 2);
        final List<String> list = Arrays.asList("1f4415d7-3b52-4fc8-ba42-b6ac97508bff",
                                                "d9fc5ee6-f1db-4d24-8808-feda18345bab");
        final PagedResults<RundeckExecution> jobIdListTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .jobIdList(list)
                                                                              .build(), 2L, 0L);
        assertPageResults(jobIdListTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> groupPathTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .groupPath("fruit")
                                                                              .build(),
                                                                          2L,
                                                                          0L);
        assertPageResults(groupPathTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> groupPathExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                                   .project(projectName)
                                                                                   .groupPathExact("fruit")
                                                                                   .build(), 2L, 0L);
        assertPageResults(groupPathExactTest, 2, 2, 2, 0, 2);

        final PagedResults<RundeckExecution> excludeGroupPathTest = client.getExecutions(ExecutionQuery.builder()
                                                                              .project(projectName)
                                                                              .excludeGroupPath("fruit")
                                                                              .build(),
                                                                          2L,
                                                                          0L);
        assertPageResults(excludeGroupPathTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> excliudeGroupPathExactTest = client.getExecutions(ExecutionQuery.builder()
                                                                                   .project(projectName)
                                                                                   .excludeGroupPathExact("fruit")
                                                                                   .build(), 2L, 0L);
        assertPageResults(excliudeGroupPathExactTest, 2, 2, 2, 0, 2);

        final PagedResults<RundeckExecution> recentTest = client.getExecutions(ExecutionQuery.builder()
                                                                           .project(projectName)
                                                                           .recent("1h").build(), 2L, 0L);
        assertPageResults(recentTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> statusTest = client.getExecutions(ExecutionQuery.builder()
                                                                           .project(projectName)
                                                                           .status(RundeckExecution.ExecutionStatus.SUCCEEDED)
                                                                           .build(), 2L, 0L);
        assertPageResults(statusTest, 2, 2, 2, 0, 2);
        final PagedResults<RundeckExecution> adhocTest = client.getExecutions(ExecutionQuery.builder()
                                                                           .project(projectName)
                                                                           .adhoc(true)
                                                                           .build(), 2L, 0L);
        assertPageResults(adhocTest, 2, 2, 2, 0, 2);
    }

    /**
     * Test paging values from results
     */
    @Test
    @Betamax(tape = "get_executions_paging")
    public void getExecutionsPaging() throws Exception{
        RundeckClient client = createClient(TEST_TOKEN_1);
        final String projectName = "blah";
        //2 max, 1 offset
        final PagedResults<RundeckExecution> adhocTest = client.getExecutions(ExecutionQuery.builder()
                                                                                  .project(projectName)
                                                                                  .excludeGroupPathExact("fruit")
                                                                                  .build(), 2L, 1L);
        //expect 2 count, 3 total
        assertPageResults(adhocTest, 2, 2, 2, 1, 3);

        //FAKE results, testing paging attributes parsing

        //1 max, 1 offset
        final PagedResults<RundeckExecution> test2 = client.getExecutions(ExecutionQuery.builder()
                                                                                  .project(projectName)
                                                                                  .excludeGroupPathExact("fruit")
                                                                                  .build(), 1L, 1L);
        //expect no paging attributes
        assertPageResults(test2, 2, -1, -1, -1, -1);

        //1 max, 2 offset
        final PagedResults<RundeckExecution> test3 = client.getExecutions(ExecutionQuery.builder()
                                                                                  .project(projectName)
                                                                                  .excludeGroupPathExact("fruit")
                                                                                  .build(), 1L, 2L);
        //invalid value for count
        assertPageResults(test3, 2, -1, -1, -1, -1);
    }
    @Test
    @Betamax(tape = "bulk_delete")
    public void bulkDelete() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_2);

        final RundeckJobDeleteBulk deleteTest
            = client.deleteJobs(Arrays.asList("0ce457b5-ba84-41ca-812e-02b31da355a4"));
        Assert.assertTrue(deleteTest.isAllsuccessful());
        Assert.assertEquals(1, deleteTest.getRequestCount());
        Assert.assertEquals(1, deleteTest.getResults().size());

        final RundeckJobDelete delete = deleteTest.getResults().get(0);
        Assert.assertTrue(delete.isSuccessful());
        Assert.assertNull(delete.getError());
        Assert.assertNull(delete.getErrorCode());
        Assert.assertNotNull(delete.getMessage());
        Assert.assertEquals("0ce457b5-ba84-41ca-812e-02b31da355a4", delete.getId());
    }
    @Test
    @Betamax(tape = "bulk_delete_dne")
    public void bulkDeleteFailDNE() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_2);

        final RundeckJobDeleteBulk deleteTest
            = client.deleteJobs(Arrays.asList("does-not-exist"));
        Assert.assertFalse(deleteTest.isAllsuccessful());
        Assert.assertEquals(1, deleteTest.getRequestCount());
        Assert.assertEquals(1, deleteTest.getResults().size());

        final RundeckJobDelete delete = deleteTest.getResults().get(0);
        Assert.assertFalse(delete.isSuccessful());
        Assert.assertNotNull(delete.getError());
        Assert.assertEquals("notfound",delete.getErrorCode());
        Assert.assertNull(delete.getMessage());
        Assert.assertEquals("does-not-exist", delete.getId());
    }
    @Test
    @Betamax(tape = "bulk_delete_unauthorized")
    public void bulkDeleteFailUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_2);

        final RundeckJobDeleteBulk deleteTest
            = client.deleteJobs(Arrays.asList("3a6d16be-4268-4d26-86a9-cebc1781f768"));
        Assert.assertFalse(deleteTest.isAllsuccessful());
        Assert.assertEquals(1, deleteTest.getRequestCount());
        Assert.assertEquals(1, deleteTest.getResults().size());

        final RundeckJobDelete delete = deleteTest.getResults().get(0);
        Assert.assertFalse(delete.isSuccessful());
        Assert.assertNotNull(delete.getError());
        Assert.assertEquals("unauthorized",delete.getErrorCode());
        Assert.assertNull(delete.getMessage());
        Assert.assertEquals("3a6d16be-4268-4d26-86a9-cebc1781f768", delete.getId());
    }

    private void assertPageResults(PagedResults<RundeckExecution> jobTest,
                                   final int size,
                                   final int count, final int max, final int offset, final int total) {
        Assert.assertEquals(size, jobTest.getResults().size());
        Assert.assertEquals(count, jobTest.getCount());
        Assert.assertEquals(max, jobTest.getMax());
        Assert.assertEquals(offset, jobTest.getOffset());
        Assert.assertEquals(total, jobTest.getTotal());
    }

    @Before
    public void setUp() throws Exception {
        // not that you can put whatever here, because we don't actually connect to the RunDeck instance
        // but instead use betamax as a proxy to serve the previously recorded tapes (in src/test/resources)
        client = createClient(TEST_TOKEN_0);
    }

    private RundeckClient createClient(final String token) {
        return RundeckClient.builder().url("http://rundeck.local:4440")
            .token(token)
            .version(5)
            .build();
    }

}
