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
import org.rundeck.api.domain.*;
import org.rundeck.api.query.ExecutionQuery;
import org.rundeck.api.util.PagedResults;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;


/**
 * Test the {@link RundeckClient}. Uses betamax to unit-test HTTP requests without a live RunDeck instance.
 * 
 * @author Vincent Behar
 */
public class RundeckClientTest {

    /**
     * Note: to add a new test, do the following:
      * 1) start a rundeck server, available at hostname "rundeck.local"
      * 2) login as test or admin user, and generate a token
      * 3) add the test token as a new TEST_TOKEN_X, and use that in your test
      * 4) run 'mvn test', and commit the new yml file generated at src/test/resources/betamax/tapes
     * @see #createClient(String)
     */

    public static final String TEST_TOKEN_0 = "PVnN5K3OPc5vduS3uVuVnEsD57pDC5pd";
    public static final String TEST_TOKEN_1 = "0UUNkeRp4d58EDeCs7S6UdODp334DvK9";
    public static final String TEST_TOKEN_2 = "PP4s4SdCRO6KUoNPd1D303Dc304ORN87";
    public static final String TEST_TOKEN_3 = "9RdEosesKP3se4oV9EKOd4s3RUeUS3ON";
    public static final String TEST_TOKEN_4 = "sN5RRSNvu15DnV6EcNDdc2CkdPcv3s32";
    public static final String TEST_TOKEN_5 = "C3O6d5O98Kr6Dpv71sdE4ERdCuU12P6d";
    public static final String TEST_TOKEN_6 = "Do4d3NUD5DKk21DR4sNK755RcPk618vn";

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

        RundeckClient client = createClient(TEST_TOKEN_1, 5);


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
        RundeckClient client = createClient(TEST_TOKEN_1, 5);
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
        RundeckClient client = createClient(TEST_TOKEN_2, 5);

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
        RundeckClient client = createClient(TEST_TOKEN_2, 5);

        final RundeckJobDeleteBulk deleteTest
            = client.deleteJobs(Arrays.asList("does-not-exist"));
        Assert.assertFalse(deleteTest.isAllsuccessful());
        Assert.assertEquals(1, deleteTest.getRequestCount());
        Assert.assertEquals(1, deleteTest.getResults().size());

        final RundeckJobDelete delete = deleteTest.getResults().get(0);
        Assert.assertFalse(delete.isSuccessful());
        Assert.assertNotNull(delete.getError());
        Assert.assertEquals("notfound", delete.getErrorCode());
        Assert.assertNull(delete.getMessage());
        Assert.assertEquals("does-not-exist", delete.getId());
    }
    @Test
    @Betamax(tape = "bulk_delete_unauthorized")
    public void bulkDeleteFailUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_2, 5);

        final RundeckJobDeleteBulk deleteTest
            = client.deleteJobs(Arrays.asList("3a6d16be-4268-4d26-86a9-cebc1781f768"));
        Assert.assertFalse(deleteTest.isAllsuccessful());
        Assert.assertEquals(1, deleteTest.getRequestCount());
        Assert.assertEquals(1, deleteTest.getResults().size());

        final RundeckJobDelete delete = deleteTest.getResults().get(0);
        Assert.assertFalse(delete.isSuccessful());
        Assert.assertNotNull(delete.getError());
        Assert.assertEquals("unauthorized", delete.getErrorCode());
        Assert.assertNull(delete.getMessage());
        Assert.assertEquals("3a6d16be-4268-4d26-86a9-cebc1781f768", delete.getId());
    }
    @Test
    @Betamax(tape = "trigger_job_basic")
    public void triggerJobDeprecatedBasic() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
            = client.triggerJob("3170ba0e-6093-4b58-94d2-52988aefbfc9", null, null, null);

        Assert.assertEquals((Long) 19L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo hi there ${job.username} ; sleep 90", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());

    }
    @Test
    @Betamax(tape = "trigger_job_basic")
    public void triggerJobBasic() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
            = client.triggerJob(RunJobBuilder.builder().setJobId("3170ba0e-6093-4b58-94d2-52988aefbfc9").build());

        Assert.assertEquals((Long) 19L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo hi there ${job.username} ; sleep 90", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());

    }
    @Test
    @Betamax(tape = "trigger_job_as_user")
    public void triggerJobDeprecatedAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
            = client.triggerJob("3170ba0e-6093-4b58-94d2-52988aefbfc9", null, null, "api-java-client-user-test1");

        Assert.assertEquals((Long) 20L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo hi there ${job.username} ; sleep 90", test.getDescription());
        Assert.assertEquals("api-java-client-user-test1", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());

    }
    @Test
    @Betamax(tape = "trigger_job_as_user")
    public void triggerJobAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
            = client.triggerJob(RunJobBuilder.builder()
                .setJobId("3170ba0e-6093-4b58-94d2-52988aefbfc9")
                .setAsUser("api-java-client-user-test1")
                .build());

        Assert.assertEquals((Long)20L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo hi there ${job.username} ; sleep 90", test.getDescription());
        Assert.assertEquals("api-java-client-user-test1", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());

    }
    @Test
    @Betamax(tape = "trigger_job_as_user_unauthorized")
    public void triggerJobDeprecatedAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test;
        try {
            test = client.triggerJob("3170ba0e-6093-4b58-94d2-52988aefbfc9",null,null,"api-java-client-user-test2");
            Assert.fail("should not succeed");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Not authorized for action \"Run as User\" for Job ID 3170ba0e-6093-4b58-94d2-52988aefbfc9", e.getMessage());
        }
    }
    @Test
    @Betamax(tape = "trigger_job_as_user_unauthorized")
    public void triggerJobAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test;
        try {
            test = client.triggerJob(RunJobBuilder.builder()
                    .setJobId("3170ba0e-6093-4b58-94d2-52988aefbfc9")
                    .setAsUser("api-java-client-user-test2")
                    .build());
            Assert.fail("should not succeed");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Not authorized for action \"Run as User\" for Job ID 3170ba0e-6093-4b58-94d2-52988aefbfc9", e.getMessage());
        }
    }

    @Test
    @Betamax(tape = "trigger_adhoc_command")
    public void triggerAdhocCommandDeprecated() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
                = client.triggerAdhocCommand("test", "echo test trigger_adhoc_command");

        Assert.assertEquals((Long) 23L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo test trigger_adhoc_command", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, test.getStatus());
    }

    @Test
    @Betamax(tape = "trigger_adhoc_command")
    public void triggerAdhocCommand() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
                = client.triggerAdhocCommand(RunAdhocCommandBuilder.builder()
                .setProject("test")
                .setCommand("echo test trigger_adhoc_command")
                .build());

        Assert.assertEquals((Long) 23L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo test trigger_adhoc_command", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, test.getStatus());
    }

    @Test
    @Betamax(tape = "trigger_adhoc_command_as_user")
    public void triggerAdhocCommandDeprecatedAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
                = client.triggerAdhocCommand("test", "echo test trigger_adhoc_command_as_user",null,null,null,"api-java-client-test-run-command-as-user1");

        Assert.assertEquals((Long) 24L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo test trigger_adhoc_command_as_user", test.getDescription());
        Assert.assertEquals("api-java-client-test-run-command-as-user1", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_command_as_user")
    public void triggerAdhocCommandAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test
                = client.triggerAdhocCommand(
                RunAdhocCommandBuilder.builder()
                        .setProject("test")
                        .setCommand("echo test trigger_adhoc_command_as_user")
                        .setAsUser("api-java-client-test-run-command-as-user1")
                        .build()
                );

        Assert.assertEquals((Long) 24L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo test trigger_adhoc_command_as_user", test.getDescription());
        Assert.assertEquals("api-java-client-test-run-command-as-user1", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_command_as_user_unauthorized")
    public void triggerAdhocCommandDeprecatedAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test;
        try {
            test = client.triggerAdhocCommand("test", "echo test trigger_adhoc_command_as_user",null,null,null,"api-java-client-test-run-command-as-user1");
            Assert.fail("should not succeed");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Not authorized for action \"Run as User\" for Run Adhoc", e.getMessage());
        }
    }
    @Test
    @Betamax(tape = "trigger_adhoc_command_as_user_unauthorized")
    public void triggerAdhocCommandAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        final RundeckExecution test;
        try {
            test = client.triggerAdhocCommand(
                    RunAdhocCommandBuilder.builder()
                            .setProject("test")
                            .setCommand("echo test trigger_adhoc_command_as_user")
                            .setAsUser("api-java-client-test-run-command-as-user1")
                            .build()
            );
            Assert.fail("should not succeed");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Not authorized for action \"Run as User\" for Run Adhoc", e.getMessage());
        }
    }

    @Test
    @Betamax(tape = "trigger_adhoc_script")
    public void triggerAdhocScriptDeprecated() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        final RundeckExecution test
                = client.triggerAdhocScript("test", byteArrayInputStream,(Properties) null, null, null, null, null);

        Assert.assertEquals((Long) 25L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_script")
    public void triggerAdhocScript() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        final RundeckExecution test
                = client.triggerAdhocScript(RunAdhocScriptBuilder.builder().setProject("test").setScript
                (byteArrayInputStream).build());

        Assert.assertEquals((Long) 25L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_script_as_user")
    public void triggerAdhocScriptDeprecatedAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        final RundeckExecution test
                = client.triggerAdhocScript("test", byteArrayInputStream, (Properties) null, null, null, null, "api-java-client-test-adhoc-script-as-user1");

        Assert.assertEquals((Long) 26L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("api-java-client-test-adhoc-script-as-user1", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_script_as_user")
    public void triggerAdhocScriptAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        final RundeckExecution test
                = client.triggerAdhocScript(RunAdhocScriptBuilder.builder().setProject("test").setScript
                (byteArrayInputStream).setAsUser("api-java-client-test-adhoc-script-as-user1").build());

        Assert.assertEquals((Long) 26L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("api-java-client-test-adhoc-script-as-user1", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_script_as_user_unauthorized")
    public void triggerAdhocScriptDeprecatedAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        try{
        final RundeckExecution test
                = client.triggerAdhocScript("test", byteArrayInputStream, (Properties) null, null, null, null, "api-java-client-test-adhoc-script-as-user1");
            Assert.fail("should not succeed");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Not authorized for action \"Run as User\" for Run Adhoc", e.getMessage());
        }

    }
    @Test
    @Betamax(tape = "trigger_adhoc_script_as_user_unauthorized")
    public void triggerAdhocScriptAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        try{
        final RundeckExecution test
                = client.triggerAdhocScript(RunAdhocScriptBuilder.builder().setProject("test").setScript
                (byteArrayInputStream).setAsUser("api-java-client-test-adhoc-script-as-user1").build());
            Assert.fail("should not succeed");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Not authorized for action \"Run as User\" for Run Adhoc", e.getMessage());
        }

    }

    @Test
    @Betamax(tape = "trigger_adhoc_script_interpreter")
    public void triggerAdhocScriptInpterpreter() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 8);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());
        final RundeckExecution test
                = client.triggerAdhocScript(RunAdhocScriptBuilder.builder()
                .setProject("test").setScript(byteArrayInputStream)
                .setScriptInterpreter("sudo -c bob")
                .build());

        Assert.assertEquals((Long) 27L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    @Test
    @Betamax(tape = "abort_execution")
    public void abortExecution() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        long executionId = 21L;
        final RundeckAbort test = client.abortExecution(executionId);

        Assert.assertEquals((Long) executionId,test.getExecution().getId());
        Assert.assertEquals(RundeckAbort.AbortStatus.PENDING,test.getStatus());
    }
    @Test
    @Betamax(tape = "abort_execution_as_user")
    public void abortExecutionAsUser() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        long executionId = 22L;
        final RundeckAbort test = client.abortExecution(executionId,"api-java-client-test-abort-as-user1");

        Assert.assertEquals((Long) executionId,test.getExecution().getId());
        Assert.assertEquals(RundeckAbort.AbortStatus.PENDING,test.getStatus());
    }
    @Test
    @Betamax(tape = "abort_execution_as_user_unauthorized")
    public void abortExecutionAsUserUnauthorized() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_3, 5);

        long executionId = 28L;
        final RundeckAbort test = client.abortExecution(executionId, "api-java-client-test-abort-as-user1");
        Assert.assertEquals(RundeckAbort.AbortStatus.FAILED, test.getStatus());
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

    /**
     * Import jobs, xml contains project context
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_context_project")
    public void importJobsContextProject() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_4, 8);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>job1</name>\n" +
                "    <context>\n" +
                "      <project>test</project>\n" +
                "    </context>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(0,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(1,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getSucceededJobs().get(0);
        Assert.assertEquals("job1", rundeckJob.getName());
        Assert.assertEquals("test", rundeckJob.getProject());
    }
    /**
     * Import jobs, xml no project defined
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_no_project")
    public void importJobsNoProject() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_4, 8);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>job2</name>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(1,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getFailedJobs().entrySet().iterator().next().getKey();
        String reason = rundeckJobsImportResult.getFailedJobs().get(rundeckJob);
        Assert.assertEquals("job2", rundeckJob.getName());
        Assert.assertEquals(null, rundeckJob.getProject());
        Assert.assertTrue(reason.contains("Project was not specified"));
    }
    /**
     * Import jobs, using project parameter
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_project_param")
    public void importJobsProjectParam() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_4, 8);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>importJobsProjectParam</name>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .setProject("test")
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(0,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(1,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getSucceededJobs().get(0);
        Assert.assertEquals("importJobsProjectParam", rundeckJob.getName());
        Assert.assertEquals("test", rundeckJob.getProject());
    }
    /**
     * Import jobs, project parameter overrides xml
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_project_param_override")
    public void importJobsProjectParamOverride() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_4, 8);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>importJobsProjectParamOverride</name>\n" +
                "    <context>\n" +
                "      <project>testXYZ</project>\n" +
                "    </context>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .setProject("test")
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(0,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(1,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getSucceededJobs().get(0);
        Assert.assertEquals("importJobsProjectParamOverride", rundeckJob.getName());
        Assert.assertEquals("test", rundeckJob.getProject());
    }
    /**
     * Import jobs, project parameter v7 doesn' use parameter
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_project_param_v7")
    public void importJobsProjectParamV7() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_4, 7);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>importJobsProjectParamV7</name>\n" +
                "    <context>\n" +
                "      <project>testXYZ</project>\n" +
                "    </context>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .setProject("test")
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(0,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(1,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getSucceededJobs().get(0);
        Assert.assertEquals("importJobsProjectParamV7", rundeckJob.getName());
        Assert.assertEquals("testXYZ", rundeckJob.getProject());
    }
    /**
     * Import jobs, project parameter v7 doesn' use parameter
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_uuid_param_remove_v9")
    public void importJobsUUIDParamRemoveV9() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 9);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <uuid>testImportUUID</uuid>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>importJobsUUIDParamRemoveV9</name>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .setUUIDImportBehavior(RundeckJobsUUIDImportBehavior.REMOVE)
                .setProject("test")
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(0,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(1,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getSucceededJobs().get(0);
        Assert.assertEquals("importJobsUUIDParamRemoveV9", rundeckJob.getName());
        Assert.assertFalse("testImportUUID".equals(rundeckJob.getId()));
        Assert.assertEquals("b33c8bb1-4dfc-4661-a890-4164b264cc4a", rundeckJob.getId());
    }
    /**
     * Import jobs, project parameter v7 doesn' use parameter
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_uuid_param_preserve_v9")
    public void importJobsUUIDParamPreserveV9() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 9);
        InputStream stream=new ByteArrayInputStream(
                ("<joblist>\n" +
                "  <job>\n" +
                "    <uuid>testImportUUID</uuid>\n" +
                "    <loglevel>INFO</loglevel>\n" +
                "    <sequence keepgoing='false' strategy='node-first'>\n" +
                "      <command>\n" +
                "        <exec>echo hi</exec>\n" +
                "      </command>\n" +
                "    </sequence>\n" +
                "    <description></description>\n" +
                "    <name>importJobsUUIDParamPreserveV9</name>\n" +
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder.builder()
                .setStream(stream)
                .setFileType(FileType.XML)
                .setJobsImportMethod(RundeckJobsImportMethod.CREATE)
                .setUUIDImportBehavior(RundeckJobsUUIDImportBehavior.PRESERVE)
                .setProject("test")
                .build();
        RundeckJobsImportResult rundeckJobsImportResult = client.importJobs(jobsImport);
        Assert.assertEquals(0,rundeckJobsImportResult.getFailedJobs().size());
        Assert.assertEquals(0,rundeckJobsImportResult.getSkippedJobs().size());
        Assert.assertEquals(1,rundeckJobsImportResult.getSucceededJobs().size());
        RundeckJob rundeckJob = rundeckJobsImportResult.getSucceededJobs().get(0);
        Assert.assertEquals("importJobsUUIDParamPreserveV9", rundeckJob.getName());
        Assert.assertEquals("testImportUUID", rundeckJob.getId());
    }
    /**
     * Running executions for all projects using API v9
     * @throws Exception
     */
    @Test
    @Betamax(tape = "running_executions_v9")
    public void runningExecutionsV9() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_5, 9);
        List<RundeckExecution> runningExecutions = client.getRunningExecutions();
        Assert.assertEquals(2, runningExecutions.size());
        RundeckExecution exec1 = runningExecutions.get(0);
        Assert.assertEquals("test", exec1.getProject());
        RundeckExecution exec2 = runningExecutions.get(1);
        Assert.assertEquals("test2", exec2.getProject());
    }

    /**
     * Running executions for all projects using API v8, which queries for project list and then individually
     * queries each project's now running
     * @throws Exception
     */
    @Test
    @Betamax(tape = "running_executions_v8")
    public void runningExecutionsV8() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_5, 8);
        List<RundeckExecution> runningExecutions = client.getRunningExecutions();
        Assert.assertEquals(2, runningExecutions.size());
        RundeckExecution exec1 = runningExecutions.get(0);
        Assert.assertEquals("test", exec1.getProject());
        RundeckExecution exec2 = runningExecutions.get(1);
        Assert.assertEquals("test2", exec2.getProject());
    }

    @Before
    public void setUp() throws Exception {
        // not that you can put whatever here, because we don't actually connect to the RunDeck instance
        // but instead use betamax as a proxy to serve the previously recorded tapes (in src/test/resources)
        client = createClient(TEST_TOKEN_0, 5);
    }

    private RundeckClient createClient(final String token, int version) {
        return createBuilder(token).version(version).build();
    }

    private RundeckClient createClient(String token) {
        return createBuilder(token)
            .build();
    }

    private RundeckClientBuilder createBuilder(String token) {
        return RundeckClient.builder().url("http://rundeck.local:4440")
            .token(token);
    }

}
