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

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.rundeck.api.domain.*;
import org.rundeck.api.query.ExecutionQuery;
import org.rundeck.api.util.PagedResults;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.MatchRule;
import co.freeside.betamax.Recorder;
import co.freeside.betamax.TapeMode;

import java.io.*;
import java.util.*;


/**
 * Test the {@link RundeckClient}. Uses betamax to unit-test HTTP requests without a live Rundeck instance.
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
    public static final String TEST_TOKEN_7 = "8Dp9op111ER6opsDRkddvE86K9sE499s";
    public static final String TEST_TOKEN_8 = "GG7uj1y6UGahOs7QlmeN2sIwz1Y2j7zI";
    public static final String TEST_TOKEN_9 = "FGHg4cKAKOSXK51yGt9WhfZcX4VQJsxM";

    @Rule
    public Recorder recorder = new Recorder();

    private RundeckClient client;

    @Test
    public void apiVersionDefaultLatest() {
        RundeckClient blah = createClient("blah", 0);
        Assert.assertEquals("/api/" + RundeckClient.API_VERSION, blah.getApiEndpoint());
        Assert.assertEquals(RundeckClient.API_VERSION, blah.getApiVersion());
        blah.setApiVersion(0);
        Assert.assertEquals(RundeckClient.API_VERSION, blah.getApiVersion());
        blah.setApiVersion(-1);
        Assert.assertEquals(RundeckClient.API_VERSION, blah.getApiVersion());
        blah.setApiVersion(RundeckClient.Version.V9.getVersionNumber());
        Assert.assertEquals(RundeckClient.Version.V9.getVersionNumber(), blah.getApiVersion());
    }
    @Test
    @Betamax(tape = "get_projects", mode = TapeMode.READ_ONLY)
    public void getProjects() throws Exception {
        List<RundeckProject> projects = client.getProjects();
        Assert.assertEquals(1, projects.size());
        Assert.assertEquals("test", projects.get(0).getName());
        Assert.assertNull(projects.get(0).getDescription());
    }
    @Test
    @Betamax(tape = "get_project_v10")
    public void getProject_v10() throws Exception {

        RundeckProject project = createClient("V4yhukF67G3tSOEvWYEh1ijROKfrULVN",10).getProject("test");
        Assert.assertNotNull(project);
        Assert.assertEquals("abc", project.getDescription());
        Assert.assertEquals("test", project.getName());
        Assert.assertNull(project.getProjectConfig());
    }
    @Test
    @Betamax(tape = "get_project_v11")
    public void getProject_v11() throws Exception {

        RundeckProject project = createClient("V4yhukF67G3tSOEvWYEh1ijROKfrULVN",11).getProject("test");
        Assert.assertNotNull(project);
        Assert.assertEquals("abc", project.getDescription());
        Assert.assertEquals("test", project.getName());
        Assert.assertNotNull(project.getProjectConfig());
    }
    @Test
    @Betamax(tape = "create_projectv11")
    public void createProject() throws Exception {

        RundeckProject project = createClient(TEST_TOKEN_6,11).createProject("monkey1", null);
        Assert.assertEquals("monkey1", project.getName());
        Assert.assertEquals(null, project.getDescription());
        Assert.assertNotNull(project.getProjectConfig());

    }
    @Test
    @Betamax(tape = "delete_projectv11")
    public void deleteProject() throws Exception {
        RundeckClient client1 = createClient(TEST_TOKEN_6, 11);
        client1.deleteProject("delete_me");
        RundeckProject delete_me = null;
        try {
            delete_me = client1.getProject("delete_me");
            Assert.fail();
        } catch (RundeckApiException.RundeckApiHttpStatusException e) {
            Assert.assertEquals(404,e.getStatusCode());
        }

    }

    @Test
    @Betamax(tape = "get_project_configv11")
    public void getProjectConfig() throws Exception {
        ProjectConfig config = createClient(TEST_TOKEN_6, 11).getProjectConfig("monkey1");
        Assert.assertNotNull(config);
        Assert.assertNotNull(config.getProperties());
        Assert.assertEquals(9,config.getProperties().size());
        Assert.assertEquals("monkey1", config.getProperties().get("project.name"));
    }
    @Test
    @Betamax(tape = "set_project_configv11")
    public void setProjectConfig() throws Exception {
        HashMap<String, String> config = new HashMap<String, String>();
        config.put("alphabetty", "spaghetti");
        config.put("blha.blee", "a big amazing thingy so there.");
        ProjectConfig result = createClient(TEST_TOKEN_6, 11).setProjectConfig("monkey1", config);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getProperties());
        Assert.assertEquals(3, result.getProperties().size());
        Assert.assertEquals("monkey1", result.getProperties().get("project.name"));
        Assert.assertEquals("spaghetti", result.getProperties().get("alphabetty"));
        Assert.assertEquals("a big amazing thingy so there.", result.getProperties().get("blha.blee"));
    }

    @Test
    @Betamax(tape = "get_project_config_keyedv11")
    public void getProjectConfigKeyed() throws Exception {
        String value = createClient(TEST_TOKEN_6, 11).getProjectConfig("ABC", "project.name");
        Assert.assertNotNull(value);
        Assert.assertEquals("ABC", value);
    }
    @Test
    @Betamax(tape = "get_project_config_keyed_dne_v11")
    public void getProjectConfigKeyedDNE() throws Exception {
        String value = createClient(TEST_TOKEN_6, 11).getProjectConfig("ABC", "does-not-exist");
        Assert.assertNull(value);
    }
    @Test
    @Betamax(tape = "set_project_config_keyedv11")
    public void setProjectConfigKeyed() throws Exception {
        String value = createClient(TEST_TOKEN_6, 11).setProjectConfig("ABC", "monkey-burrito", "lemon pie");
        Assert.assertNotNull(value);
        Assert.assertEquals("lemon pie", value);
    }
    @Test
    @Betamax(tape = "delete_project_config_keyedv11")
    public void deleteProjectConfigKeyed() throws Exception {
        RundeckClient client1 = createClient(TEST_TOKEN_6, 11);
        Assert.assertEquals("7up", client1.setProjectConfig("ABC", "monkey-burrito", "7up"));
        client1.deleteProjectConfig("ABC", "monkey-burrito");
        String value=client1.getProjectConfig("ABC", "monkey-burrito");
        Assert.assertNull(value);
    }
    @Test
    @Betamax(tape = "export_projectv11")
    public void exportProject() throws Exception {
        RundeckClient client1 = createClient(TEST_TOKEN_6, 11);
        File temp = File.createTempFile("test-archive", ".zip");
        temp.deleteOnExit();
        int i = client1.exportProject("DEF1", temp);
        Assert.assertEquals(8705, i);
    }
    @Test
    @Betamax(tape = "import_project_suv11",mode = TapeMode.READ_ONLY)
    public void importProjectSuccess() throws Exception {
        RundeckClient client1 = createClient(TEST_TOKEN_6, 11);
        InputStream resourceAsStream = getClass().getResourceAsStream("test-archive.zip");
        File temp = File.createTempFile("test-archive", ".zip");
        temp.deleteOnExit();
        IOUtils.copy(resourceAsStream, new FileOutputStream(temp));
        ArchiveImport def1 = client1.importArchive("DEF2", temp, true, true);
        Assert.assertTrue(def1.isSuccessful());
        Assert.assertEquals(0, def1.getErrorMessages().size());

        ArchiveImport def2 = client1.importArchive("DEF2", temp, false, true);
        Assert.assertTrue(def2.isSuccessful());
        Assert.assertEquals(0, def2.getErrorMessages().size());

        ArchiveImport def3 = client1.importArchive("DEF2", temp, true, false);
        Assert.assertTrue(def3.isSuccessful());
        Assert.assertEquals(0, def3.getErrorMessages().size());
        temp.delete();
    }
    @Test
    @Betamax(tape = "import_project_failure_v11", mode = TapeMode.READ_ONLY)
    public void importProjectFailure() throws Exception {
        RundeckClient client1 = createClient(TEST_TOKEN_6, 11);
        InputStream resourceAsStream = getClass().getResourceAsStream("test-archive.zip");
        File temp = File.createTempFile("test-archive", ".zip");
        temp.deleteOnExit();
        IOUtils.copy(resourceAsStream, new FileOutputStream(temp));
        ArchiveImport def1 = client1.importArchive("DEF1", temp, false, true);
        Assert.assertFalse(def1.isSuccessful());
        Assert.assertEquals(10, def1.getErrorMessages().size());
        Assert.assertEquals("Job at index [1] at archive path: " +
                "rundeck-DEF1/jobs/job-6fd1808c-eafb-49ac-abf2-4de7ec75872f.xml had errors: Validation errors: Cannot" +
                " create a Job with UUID 6fd1808c-eafb-49ac-abf2-4de7ec75872f: a Job already exists with this UUID. " +
                "Change the UUID or delete the other Job.", def1.getErrorMessages().get(0));

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
    @Betamax(tape = "get_history_v14",
             mode=TapeMode.READ_ONLY
    )
    public void getHistory_v14() throws Exception {
        final RundeckHistory test = createClient("V4yhukF67G3tSOEvWYEh1ijROKfrULVN", 14).getHistory("test");
        Assert.assertEquals(20, test.getCount());
        Assert.assertEquals(20, test.getMax());
        Assert.assertEquals(0, test.getOffset());
        Assert.assertEquals(60, test.getTotal());
        final List<RundeckEvent> events = test.getEvents();
        Assert.assertEquals(20, events.size());
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
    @Betamax(tape="get_execution")
    public void getExecution() throws  Exception{
        RundeckClient client = createClient(TEST_TOKEN_7, 5);
        RundeckExecution execution = client.getExecution(945L);
        Assert.assertEquals("echo test trigger_adhoc_command", execution.getDescription());
        Assert.assertEquals("2 seconds", execution.getDuration());
        Assert.assertEquals("test", execution.getProject());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(null, execution.getJob());
        Assert.assertEquals(null, execution.getAbortedBy());
    }
    @Test
    @Betamax(tape="get_execution_v11")
    public void getExecution_v11() throws  Exception{
        RundeckClient client = createClient(TEST_TOKEN_7, 11);
        RundeckExecution execution = client.getExecution(945L);
        Assert.assertEquals("echo test trigger_adhoc_command", execution.getDescription());
        Assert.assertEquals("2 seconds", execution.getDuration());
        Assert.assertEquals("test", execution.getProject());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(null, execution.getJob());
        Assert.assertEquals(null, execution.getAbortedBy());
    }

    /**
     * Test incorrect &lt;result&gt; wrapper is handled correctly
     * @throws Exception
     */
    @Test
    @Betamax(tape="get_execution_v11_buggy")
    public void getExecution_v11_buggy() throws  Exception{
        RundeckClient client = createClient(TEST_TOKEN_7, 11);
        RundeckExecution execution = client.getExecution(945L);
        Assert.assertEquals("echo test trigger_adhoc_command", execution.getDescription());
        Assert.assertEquals("2 seconds", execution.getDuration());
        Assert.assertEquals("test", execution.getProject());
        Assert.assertEquals("admin", execution.getStartedBy());
        Assert.assertEquals(null, execution.getJob());
        Assert.assertEquals(null, execution.getAbortedBy());
    }
    @Test
    @Betamax(tape = "get_executions_v14"
             ,mode = TapeMode.READ_ONLY
    )
    public void getExecutionsV14() throws Exception {

        RundeckClient client = createClient("V4yhukF67G3tSOEvWYEh1ijROKfrULVN", 14);


        final PagedResults<RundeckExecution> jobTest = client.getExecutions(
                ExecutionQuery.builder()
                              .project("test")
                              .build(),
                10L,
                0L
        );
        assertPageResults(jobTest, 10, 10, 10, 0, 60);
    }
    @Test
    @Betamax(tape = "get_executions",
             mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
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
    @Test
    @Betamax(tape = "get_executions_v11",
             mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
    public void getExecutionsV11() throws Exception {

        RundeckClient client = createClient(TEST_TOKEN_7, 11);


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
        assertPageResults(beginTest, 2, 2, 2, 0, 6);
        final PagedResults<RundeckExecution> endTest = client.getExecutions(ExecutionQuery.builder()
                                                                        .project(projectName)
                                                                        .end(new Date(1415388156385L))
                                                                        .build(), 2L, 0L);
        assertPageResults(endTest, 2, 2, 2, 0, 4);
        final List<String> excludeJobIdList = Arrays.asList("123", "456");
        final PagedResults<RundeckExecution> excludeJobIdListTest = client.getExecutions(ExecutionQuery.builder()
                                                                                     .project(projectName)
                                                                                     .excludeJobIdList(excludeJobIdList)
                                                                                     .build(), 2L, 0L);
        assertPageResults(excludeJobIdListTest, 2, 2, 2, 0, 4);
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
        assertPageResults(excludeJobListTest, 2, 2, 2, 0, 4);
        final List<String> list = Arrays.asList("9aa33253-17a3-4dce-890c-e5f10f9f00d6",
                                                "2dd94199-00c4-4690-9b4d-beda4812bed0");
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
        assertPageResults(statusTest, 2, 2, 2, 0, 3);
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

    /**
     * API v11 request to trigger job, with expected xml response without &lt;result&gt; wrapper
     * @throws Exception
     */
    @Test
    @Betamax(tape = "trigger_job_basic_v11")
    public void triggerJobBasic_v11() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_7, 11);

        final RundeckExecution test
            = client.triggerJob(RunJobBuilder.builder().setJobId("bda8b956-43a5-4eef-9c67" +
                                                                 "-3f27cc0ee1a5").build());

        Assert.assertEquals((Long) 943L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo hi there ${job.username} ; sleep 90", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }

    /**
     * Response for API v11 incorrectly includes &lt;result&gt;, but we should handle this case
     * @throws Exception
     */
    @Test
    @Betamax(tape = "trigger_job_basic_v11_patch")
    public void triggerJobBasic_v11_patch() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_7, 11);

        final RundeckExecution test
            = client.triggerJob(RunJobBuilder.builder().setJobId("bda8b956-43a5-4eef-9c67" +
                                                                 "-3f27cc0ee1a5").build());

        Assert.assertEquals((Long) 944L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo hi there ${job.username} ; sleep 90", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
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
    @Betamax(tape = "trigger_adhoc_command_v11_buggy")
    public void triggerAdhocCommand_v11_buggy() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_7, 11);

        final RundeckExecution test
                = client.triggerAdhocCommand(RunAdhocCommandBuilder.builder()
                .setProject("test")
                .setCommand("echo test trigger_adhoc_command")
                .build());

        Assert.assertEquals((Long) 945L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo test trigger_adhoc_command", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    @Test
    @Betamax(tape = "trigger_adhoc_command_v11")
    public void triggerAdhocCommand_v11() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_7, 11);

        final RundeckExecution test
                = client.triggerAdhocCommand(RunAdhocCommandBuilder.builder()
                .setProject("test")
                .setCommand("echo test trigger_adhoc_command")
                .build());

        Assert.assertEquals((Long) 946L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("echo test trigger_adhoc_command", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
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


    /**
     * Handle incorrect &lt;result&gt; wrapper for v11 response
     * @throws Exception
     */
    @Test
    @Betamax(tape = "trigger_adhoc_script_v11_buggy")
    public void triggerAdhocScript_v11_buggy() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_7, 11);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        final RundeckExecution test
                = client.triggerAdhocScript(RunAdhocScriptBuilder.builder().setProject("test").setScript
                (byteArrayInputStream).build());

        Assert.assertEquals((Long) 947L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.RUNNING, test.getStatus());
    }
    /**
     * Handle v11 response without &lt;result&gt; wrapper
     * @throws Exception
     */
    @Test
    @Betamax(tape = "trigger_adhoc_script_v11")
    public void triggerAdhocScript_v11() throws Exception {
        RundeckClient client = createClient(TEST_TOKEN_7, 11);
        String script = "#!/bin/bash\n" +
                "echo test trigger_adhoc_script\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(script.getBytes());

        final RundeckExecution test
                = client.triggerAdhocScript(RunAdhocScriptBuilder.builder().setProject("test").setScript
                (byteArrayInputStream).build());

        Assert.assertEquals((Long) 948L, test.getId());
        Assert.assertEquals(null, test.getArgstring());
        Assert.assertEquals(null, test.getAbortedBy());
        Assert.assertEquals("#!/bin/bash\necho test trigger_adhoc_script", test.getDescription());
        Assert.assertEquals("admin", test.getStartedBy());
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


    @Test
    @Betamax(tape = "export_jobs_v14", mode = TapeMode.READ_ONLY)
    public void exportJobs_v14() throws Exception {
        RundeckClient client1 = createClient("V4yhukF67G3tSOEvWYEh1ijROKfrULVN", 14);
        InputStream inputStream = client1.exportJobs(FileType.XML, "test", "job1", "");

        File temp = File.createTempFile("test_export_jobs", ".xml");
        temp.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(temp)) {
            IOUtils.copy(inputStream, out);
        }
        long length = temp.length();
        temp.delete();
        Assert.assertEquals(448, length);
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
     * Import jobs api > v11
     * @throws Exception
     */
    @Test
    @Betamax(tape = "import_jobs_context_project_v13", mode = TapeMode.READ_ONLY)
    public void importJobsContextProject_v13() throws Exception {
        final RundeckClient client = createBuilder("V4yhukF67G3tSOEvWYEh1ijROKfrULVN")
                .version(13)
                .build();
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
                "  </job>\n" +
                "</joblist>").getBytes("utf-8"));

        final RundeckJobsImport jobsImport = RundeckJobsImportBuilder
                .builder()
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
    @Betamax(tape = "import_jobs_project_param", mode = TapeMode.READ_ONLY)
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
    @Betamax(tape = "import_jobs_project_param_override", mode = TapeMode.READ_ONLY)
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
    @Betamax(tape = "import_jobs_project_param_v7", mode = TapeMode.READ_ONLY)
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
    @Betamax(tape = "import_jobs_uuid_param_remove_v9", mode = TapeMode.READ_ONLY)
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
    @Betamax(tape = "import_jobs_uuid_param_preserve_v9", mode = TapeMode.READ_ONLY)
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
     * Running executions for all projects using API v14
     * @throws Exception
     */
    @Test
    @Betamax(tape = "running_executions_v14", mode = TapeMode.READ_ONLY)
    public void runningExecutionsV14() throws Exception {
        final RundeckClient client = createClient("V4yhukF67G3tSOEvWYEh1ijROKfrULVN", 14);
        List<RundeckExecution> runningExecutions = client.getRunningExecutions();
        Assert.assertEquals(2, runningExecutions.size());
        RundeckExecution exec1 = runningExecutions.get(0);
        Assert.assertEquals("test", exec1.getProject());
        RundeckExecution exec2 = runningExecutions.get(1);
        Assert.assertEquals("test", exec2.getProject());
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
    /**
     * Execution output
     */
    @Test
    @Betamax(tape = "execution_output_basic", mode = TapeMode.READ_ONLY)
    public void executionOutputBasic() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckOutput output = client.getExecutionOutput(146L,0,0L,-1);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals(null, output.getFilterNode());
        Assert.assertEquals(null, output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.57597), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(3, output.getLogEntries().size());
    }
    /**
     * Execution output for a node
     */
    @Test
    @Betamax(tape = "execution_output_fornode", mode = TapeMode.READ_ONLY)
    public void executionOutputForNode() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckOutput output = client.getExecutionOutputForNode(146L, "node-14.qa.subgroup.mycompany.com", 0, -1, 0L, -1);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals("node-14.qa.subgroup.mycompany.com", output.getFilterNode());
        Assert.assertEquals(null, output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.57597), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(1, output.getLogEntries().size());
    }
    /**
     * Execution output for a step
     */
    @Test
    @Betamax(tape = "execution_output_forstep", mode = TapeMode.READ_ONLY)
    public void executionOutputForStep() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckOutput output = client.getExecutionOutputForStep(146L, "1", 0, -1, 0L, -1);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals(null, output.getFilterNode());
        Assert.assertEquals("1", output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.57597), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(3, output.getLogEntries().size());
    }

    /**
     * Execution output for a node and step
     */
    @Test
    @Betamax(tape = "execution_output_fornodeandstep", mode = TapeMode.READ_ONLY)
    public void executionOutputForNodeAndStep() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckOutput output = client.getExecutionOutputForNodeAndStep(146L, "node-14.qa.subgroup.mycompany.com",
                "1", 0, -1, 0L, -1);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals("node-14.qa.subgroup.mycompany.com", output.getFilterNode());
        Assert.assertEquals("1", output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.57597), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(1, output.getLogEntries().size());
    }

    /**
     * Execution output state sequence
     */
    @Test
    @Betamax(tape = "execution_output_state", mode = TapeMode.READ_ONLY)
    public void executionOutputState() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckOutput output = client.getExecutionOutputState(146L, false, 0, 0L, -1);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals(null, output.getFilterNode());
        Assert.assertEquals(null, output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.57597), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(15, output.getLogEntries().size());
    }
    /**
     * Execution output state sequence
     */
    @Test
    @Betamax(tape = "execution_output_state_only", mode = TapeMode.READ_ONLY)
    public void executionOutputStateOnly() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckOutput output = client.getExecutionOutputState(146L, true, 0, 0L, -1);

        Assert.assertEquals(new Long(1602), output.getExecDuration());
        Assert.assertEquals(new Long(146), output.getExecutionId());
        Assert.assertEquals(new Long(1389894504000L), output.getLastModified());
        Assert.assertEquals(null, output.getFilterNode());
        Assert.assertEquals(null, output.getFilterStep());
        Assert.assertEquals(1409, output.getOffset());
        Assert.assertEquals(RundeckExecution.ExecutionStatus.SUCCEEDED, output.getStatus());
        Assert.assertEquals(new Float(99.57597), output.getPercentLoaded());
        Assert.assertEquals(1415, output.getTotalSize());
        Assert.assertEquals(true, output.isCompleted());
        Assert.assertEquals(true, output.isExecCompleted());
        Assert.assertEquals(false, output.isEmpty());
        Assert.assertEquals(false, output.isHasFailedNodes());
        Assert.assertEquals(false, output.isUnmodified());
        Assert.assertEquals(12, output.getLogEntries().size());
    }
    /**
     * Execution state structure
     */
    @Test
    @Betamax(tape = "execution_state", mode = TapeMode.READ_ONLY)
    public void executionState() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_6, 10);
        RundeckExecutionState output = client.getExecutionState(149L);

        Assert.assertEquals(149,output.getExecutionId());
        Assert.assertEquals(3,output.getTargetNodes().size());
        Assert.assertEquals(3,output.getAllNodes().size());
        Assert.assertEquals(1,output.getStepCount());
        Assert.assertEquals(1,output.getSteps().size());
        Assert.assertEquals(RundeckWFExecState.SUCCEEDED,output.getExecutionState());
    }

    /**
     * generate api token
     */
    @Test
    @Betamax(tape = "api_token_generate", mode = TapeMode.READ_ONLY)
    public void generateApiToken() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        String token = client.generateApiToken("bob");

        Assert.assertNotNull(token);
        Assert.assertEquals("MiquQjELTrEaugpmdgAKs1W3a7xonAwU", token);
    }
    /**
     * get api token
     */
    @Test
    @Betamax(tape = "api_token_get", mode = TapeMode.READ_ONLY)
    public void getApiToken() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        RundeckToken token = client.getApiToken("MiquQjELTrEaugpmdgAKs1W3a7xonAwU");

        Assert.assertNotNull(token);
        Assert.assertEquals("MiquQjELTrEaugpmdgAKs1W3a7xonAwU", token.getToken());
        Assert.assertEquals("bob", token.getUser());
    }
    /**
     * list api tokens for user
     */
    @Test
    @Betamax(tape = "api_tokens_list_user", mode = TapeMode.READ_ONLY)
    public void listApiTokens_user() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        List<RundeckToken> tokens = client.listApiTokens("bob");

        Assert.assertNotNull(tokens);
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals("hINp5eGzvYA9UePbUChaKHd5NiRkwWbx", tokens.get(0).getToken());
        Assert.assertEquals("bob", tokens.get(0).getUser());
        Assert.assertEquals("NaNnwVzAHAG83qOS7Wtwh6mjcXViyWUV", tokens.get(1).getToken());
        Assert.assertEquals("bob", tokens.get(1).getUser());
        Assert.assertEquals("MiquQjELTrEaugpmdgAKs1W3a7xonAwU", tokens.get(2).getToken());
        Assert.assertEquals("bob", tokens.get(2).getUser());
    }
    /**
     * list api tokens all
     */
    @Test
    @Betamax(tape = "api_tokens_list_all", mode = TapeMode.READ_ONLY)
    public void listApiTokens() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        List<RundeckToken> tokens = client.listApiTokens();

        Assert.assertNotNull(tokens);
        Assert.assertEquals(4, tokens.size());
        Assert.assertEquals("8Dp9op111ER6opsDRkddvE86K9sE499s", tokens.get(0).getToken());
        Assert.assertEquals("admin", tokens.get(0).getUser());
        Assert.assertEquals("hINp5eGzvYA9UePbUChaKHd5NiRkwWbx", tokens.get(1).getToken());
        Assert.assertEquals("bob", tokens.get(1).getUser());
        Assert.assertEquals("NaNnwVzAHAG83qOS7Wtwh6mjcXViyWUV", tokens.get(2).getToken());
        Assert.assertEquals("bob", tokens.get(2).getUser());
        Assert.assertEquals("MiquQjELTrEaugpmdgAKs1W3a7xonAwU", tokens.get(3).getToken());
        Assert.assertEquals("bob", tokens.get(3).getUser());
    }

    /**
     * get api token
     */
    @Test
    @Betamax(tape = "api_token_delete", mode = TapeMode.READ_ONLY)
    public void deleteApiToken() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);

        client.deleteApiToken("MiquQjELTrEaugpmdgAKs1W3a7xonAwU");

        //get should now return 404
        try {
            client.getApiToken("MiquQjELTrEaugpmdgAKs1W3a7xonAwU");
            Assert.fail("expected failure");
        } catch (RundeckApiException.RundeckApiHttpStatusException e) {
            Assert.assertEquals(404, e.getStatusCode());
        }
    }
    /**
     * Store ssh key
     */
    @Test
    @Betamax(tape = "key_store_private", mode = TapeMode.READ_ONLY)
    public void storeKey_private() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        File temp = File.createTempFile("test-key", ".tmp");
        temp.deleteOnExit();
        FileOutputStream out = new FileOutputStream(temp);
        try{
            out.write("test1".getBytes());
        }finally {
            out.close();
        }
        KeyResource storageResource = client.storeKey("keys/test/example/file1.pem", temp, true);
        Assert.assertNotNull(storageResource);
        Assert.assertFalse(storageResource.isDirectory());
        Assert.assertTrue(storageResource.isPrivateKey());
        Assert.assertEquals("file1.pem", storageResource.getName());
        Assert.assertEquals("keys/test/example/file1.pem", storageResource.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test/example/file1.pem",
                storageResource.getUrl());
        Assert.assertEquals(0, storageResource.getDirectoryContents().size());
        Map<String, String> metadata = storageResource.getMetadata();
        Assert.assertNotNull(metadata);
        Assert.assertEquals("application/octet-stream", metadata.get("Rundeck-content-type"));
        Assert.assertEquals("private", metadata.get("Rundeck-key-type"));
    }
    /**
     * Store ssh key
     */
    @Test
    @Betamax(tape = "key_store_public", mode = TapeMode.READ_ONLY)
    public void storeKey_public() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        File temp = File.createTempFile("test-key", ".tmp");
        temp.deleteOnExit();
        FileOutputStream out = new FileOutputStream(temp);
        try{
            out.write("test1".getBytes());
        }finally {
            out.close();
        }
        KeyResource storageResource = client.storeKey("keys/test/example/file2.pub", temp, false);
        Assert.assertNotNull(storageResource);
        Assert.assertFalse(storageResource.isDirectory());
        Assert.assertFalse(storageResource.isPrivateKey());
        Assert.assertEquals("file2.pub", storageResource.getName());
        Assert.assertEquals("keys/test/example/file2.pub", storageResource.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test/example/file2.pub",
                storageResource.getUrl());
        Assert.assertEquals(0, storageResource.getDirectoryContents().size());
        Map<String, String> metadata = storageResource.getMetadata();
        Assert.assertNotNull(metadata);
        Assert.assertEquals("application/pgp-keys", metadata.get("Rundeck-content-type"));
        Assert.assertEquals("public", metadata.get("Rundeck-key-type"));
    }
    /**
     * get ssh key
     */
    @Test
    @Betamax(tape = "key_get_public", mode = TapeMode.READ_ONLY)
    public void getKey_public() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        KeyResource storageResource = client.getKey("keys/test/example/file2.pub");
        Assert.assertNotNull(storageResource);
        Assert.assertFalse(storageResource.isDirectory());
        Assert.assertFalse(storageResource.isPrivateKey());
        Assert.assertEquals("file2.pub", storageResource.getName());
        Assert.assertEquals("keys/test/example/file2.pub", storageResource.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test/example/file2.pub",
                storageResource.getUrl());
        Assert.assertEquals(0, storageResource.getDirectoryContents().size());
        Map<String, String> metadata = storageResource.getMetadata();
        Assert.assertNotNull(metadata);
        Assert.assertEquals("application/pgp-keys", metadata.get("Rundeck-content-type"));
        Assert.assertEquals("public", metadata.get("Rundeck-key-type"));
    }
    /**
     * get ssh key
     */
    @Test
    @Betamax(tape = "key_get_private", mode = TapeMode.READ_ONLY)
    public void getKey_private() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        KeyResource storageResource = client.getKey("keys/test/example/file1.pem");
        Assert.assertNotNull(storageResource);
        Assert.assertFalse(storageResource.isDirectory());
        Assert.assertTrue(storageResource.isPrivateKey());
        Assert.assertEquals("file1.pem", storageResource.getName());
        Assert.assertEquals("keys/test/example/file1.pem", storageResource.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test/example/file1.pem",
                storageResource.getUrl());
        Assert.assertEquals(0, storageResource.getDirectoryContents().size());
        Map<String, String> metadata = storageResource.getMetadata();
        Assert.assertNotNull(metadata);
        Assert.assertEquals("application/octet-stream", metadata.get("Rundeck-content-type"));
        Assert.assertEquals("private", metadata.get("Rundeck-key-type"));
    }
    /**
     * get ssh key data
     */
    @Test
    @Betamax(tape = "key_get_data_private", mode = TapeMode.READ_ONLY)
    public void getKeyData_private() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        File temp = File.createTempFile("test-key", ".tmp");
        temp.deleteOnExit();
        try {
            int data = client.getPublicKeyContent("keys/test/example/file1.pem", temp);
            Assert.fail("expected failure");
        } catch (RundeckApiException e) {
            Assert.assertEquals("Requested Key path was not a Public key: keys/test/example/file1.pem",
                    e.getMessage());
        }
    }
    /**
     * get ssh key data
     */
    @Test
    @Betamax(tape = "key_get_data_public", mode = TapeMode.READ_ONLY)
    public void getKeyData_public() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        File temp = File.createTempFile("test-key", ".tmp");
        temp.deleteOnExit();
        int length = client.getPublicKeyContent("keys/test/example/file2.pub", temp);
        Assert.assertEquals(5, length);
    }
    /**
     * list directory
     */
    @Test
    @Betamax(tape = "key_list_directory", mode = TapeMode.READ_ONLY)
    public void listKeyDirectory() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        List<KeyResource> list = client.listKeyDirectory("keys/test/example");
        Assert.assertEquals(2, list.size());
        KeyResource storageResource1 = list.get(0);
        KeyResource storageResource2 = list.get(1);

        Assert.assertFalse(storageResource2.isDirectory());
        Assert.assertTrue(storageResource2.isPrivateKey());
        Assert.assertEquals("file1.pem", storageResource2.getName());
        Assert.assertEquals("keys/test/example/file1.pem", storageResource2.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test/example/file1.pem", storageResource2.getUrl());
        Assert.assertNotNull(storageResource2.getMetadata());

        Assert.assertEquals("application/octet-stream", storageResource2.getMetadata().get("Rundeck-content-type"));
        Assert.assertEquals("private", storageResource2.getMetadata().get("Rundeck-key-type"));

        Assert.assertFalse(storageResource1.isDirectory());
        Assert.assertFalse(storageResource1.isPrivateKey());
        Assert.assertEquals("file2.pub", storageResource1.getName());
        Assert.assertEquals("keys/test/example/file2.pub", storageResource1.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test/example/file2.pub",
                storageResource1.getUrl());
        Assert.assertNotNull(storageResource1.getMetadata());
        Assert.assertEquals("application/pgp-keys", storageResource1.getMetadata().get("Rundeck-content-type"));
        Assert.assertEquals("public", storageResource1.getMetadata().get("Rundeck-key-type"));
    }
    /**
     * list root
     */
    @Test
    @Betamax(tape = "key_list_root", mode = TapeMode.READ_ONLY)
    public void listKeyDirectoryRoot() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        List<KeyResource> list = client.listKeyDirectoryRoot();
        Assert.assertEquals(2, list.size());
        KeyResource storageResource0 = list.get(0);
        KeyResource storageResource1 = list.get(1);

        Assert.assertFalse(storageResource0.isDirectory());
        Assert.assertTrue(storageResource0.isPrivateKey());
        Assert.assertEquals("test1.pem", storageResource0.getName());
        Assert.assertEquals("keys/test1.pem", storageResource0.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test1.pem", storageResource0.getUrl());
        Assert.assertNotNull(storageResource0.getMetadata());

        Assert.assertEquals("application/octet-stream", storageResource0.getMetadata().get("Rundeck-content-type"));
        Assert.assertEquals("private", storageResource0.getMetadata().get("Rundeck-key-type"));

        Assert.assertTrue(storageResource1.toString(), storageResource1.isDirectory());
        Assert.assertEquals(null, storageResource1.getName());
        Assert.assertEquals("keys/test", storageResource1.getPath());
        Assert.assertEquals("http://dignan.local:4440/api/11/storage/keys/test",
                storageResource1.getUrl());
        Assert.assertNull(storageResource1.getMetadata());


    }

    /**
     * delete ssh key
     */
    @Test
    @Betamax(tape = "key_delete", mode = TapeMode.READ_ONLY)
    public void deleteKey() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        client.deleteKey("keys/test/example/file2.pub");

        try {
            client.getKey("keys/test/example/file2.pub");
            Assert.fail("expected failure");
        } catch (RundeckApiException.RundeckApiHttpStatusException e) {
            Assert.assertEquals(404,e.getStatusCode());
        }
    }
    /**
     * delete job
     */
    @Test
    @Betamax(tape = "delete_job", mode = TapeMode.READ_ONLY)
    public void deleteJob() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        String result = client.deleteJob("api-test-job-run-scheduled");

        Assert.assertEquals("Job api-test-job-run-scheduled was deleted successfully", result);
    }
    /**
     * delete job (DNE)
     */
    @Test
    @Betamax(tape = "delete_job_not_found", mode = TapeMode.READ_ONLY)
    public void deleteJobNotFound() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_7, 11);
        try {
            String result = client.deleteJob("does-not-exist");
            Assert.fail();
        } catch (RundeckApiException.RundeckApiHttpStatusException e) {
            Assert.assertEquals(404, e.getStatusCode());
        }

    }

    /**
     * delete executions with failure
     */
    @Test
    @Betamax(tape = "delete_executions_unauthorized", mode = TapeMode.READ_ONLY)
    public void deleteExecutionsUnauthorized() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        final DeleteExecutionsResponse response = client.deleteExecutions(
                new HashSet<Long>() {{
                    add(640L);
                    add(641L);
                }}
        );
        Assert.assertEquals(2, response.getRequestCount());
        Assert.assertEquals(0, response.getSuccessCount());
        Assert.assertEquals(2, response.getFailedCount());
        Assert.assertFalse(response.isAllsuccessful());
        Assert.assertNotNull(response.getFailures());
        Assert.assertEquals(2, response.getFailures().size());
        Assert.assertEquals(Long.valueOf(641L), response.getFailures().get(0).getExecutionId());
        Assert.assertEquals(
                "Unauthorized: Delete execution in project test",
                response.getFailures().get(0).getMessage()
        );
        Assert.assertEquals(Long.valueOf(640L), response.getFailures().get(1).getExecutionId());
        Assert.assertEquals(
                "Unauthorized: Delete execution in project test",
                response.getFailures().get(1).getMessage()
        );
    }
    /**
     * delete executions with success
     */
    @Test
    @Betamax(tape = "delete_executions_success", mode = TapeMode.READ_ONLY)
    public void deleteExecutionsSuccess() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        final DeleteExecutionsResponse response = client.deleteExecutions(
                new HashSet<Long>() {{
                    add(640L);
                    add(641L);
                }}
        );
        Assert.assertEquals(2, response.getRequestCount());
        Assert.assertEquals(2, response.getSuccessCount());
        Assert.assertEquals(0, response.getFailedCount());
        Assert.assertTrue(response.isAllsuccessful());
        Assert.assertNotNull(response.getFailures());
        Assert.assertEquals(0, response.getFailures().size());
    }
    /**
     * delete executions mixed success
     */
    @Test
    @Betamax(tape = "delete_executions_mixed", mode = TapeMode.READ_ONLY)
    public void deleteExecutionsMixed() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        final DeleteExecutionsResponse response = client.deleteExecutions(
                new HashSet<Long>() {{
                    add(642L);
                    add(640L);
                    add(1640L);
                }}
        );
        Assert.assertEquals(3, response.getRequestCount());
        Assert.assertEquals(1, response.getSuccessCount());
        Assert.assertEquals(2, response.getFailedCount());
        Assert.assertFalse(response.isAllsuccessful());
        Assert.assertNotNull(response.getFailures());
        Assert.assertEquals(2, response.getFailures().size());
        Assert.assertEquals(Long.valueOf(1640L), response.getFailures().get(0).getExecutionId());
        Assert.assertEquals(
                "Execution Not found: 1640",
                response.getFailures().get(0).getMessage()
        );
        Assert.assertEquals(Long.valueOf(640L), response.getFailures().get(1).getExecutionId());
        Assert.assertEquals(
                "Execution Not found: 640",
                response.getFailures().get(1).getMessage()
        );
    }
    /**
     * delete executions with failure
     */
    @Test
    @Betamax(tape = "delete_all_job_executions_unauthorized", mode = TapeMode.READ_ONLY)
    public void deleteAllJobExecutionsUnauthorized() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        try {
            final DeleteExecutionsResponse response = client.deleteAllJobExecutions(
                    "764c1209-68ed-4185-8d43-a739364bf156"
            );
            Assert.fail();
        } catch (RundeckApiException.RundeckApiTokenException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws Exception
     */
    @Test
    @Betamax(tape = "delete_all_job_executions_success", mode = TapeMode.READ_ONLY)
    public void deleteAllJobExecutionsSuccess() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        final DeleteExecutionsResponse response = client.deleteAllJobExecutions(
                "764c1209-68ed-4185-8d43-a739364bf156"
        );
        Assert.assertEquals(2, response.getRequestCount());
        Assert.assertEquals(2, response.getSuccessCount());
        Assert.assertEquals(0, response.getFailedCount());
        Assert.assertTrue(response.isAllsuccessful());
        Assert.assertNotNull(response.getFailures());
        Assert.assertEquals(0, response.getFailures().size());
    }

    /**
     * delete single execution success
     */
    @Test
    @Betamax(tape = "delete_execution_success", mode = TapeMode.READ_ONLY)
    public void deleteExecutionSuccess() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        client.deleteExecution(643L);
    }
    /**
     * delete single execution failure (does not exist)
     */
    @Test
    @Betamax(tape = "delete_execution_failure", mode = TapeMode.READ_ONLY)
    public void deleteExecutionFailure() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        try {
            client.deleteExecution(640L);
            Assert.fail();
        } catch (RundeckApiException.RundeckApiHttpStatusException e) {
            Assert.assertEquals(404, e.getStatusCode());
        }
    }
    /**
     * delete single execution null input
     */
    @Test
    public void deleteExecutionNullInput() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_8, 12);
        try {
            client.deleteExecution(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {

        }
    }


    /**
     * read project readme, motd
     */
    @Test
    @Betamax(tape = "read_project_file",mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
    public void readProjectFile() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);
        //does not exist
        String readme = client.readProjectFile("test", "readme.md");
        Assert.assertEquals(null, readme);
        //content
        String motd = client.readProjectFile("test", "motd.md");
        Assert.assertEquals("this is the motd\r\n\r\ntest motd", motd);
    }
    /**
     * read project readme, motd
     */
    @Test
    public void readProjectFileInvalidName() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);
        try {
            String readme = client.readProjectFile("test", "other.md");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("filename must be in the list"));
        }
    }
    /**
     * store project file
     */
    @Test
    public void storeProjectFileInvalidName() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);
        try {
            client.storeProjectFile("test", "other.md","blah");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("filename must be in the list"));
        }
    }
    /**
     * store project file
     */
    @Test
    public void deleteProjectFileInvalidName() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);
        try {
            client.deleteProjectFile("test", "other.md");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("filename must be in the list"));
        }
    }

    /**
     * store project readme
     */
    @Test
    @Betamax(tape = "store_project_file_readme",mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query, MatchRule.headers})
    public void storeProjectFileReadme() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);
        //does not exist
        client.storeProjectFile("test", "readme.md", "new readme.md content\n\n* test\n* test2");
        Assert.assertEquals("new readme.md content\n" +
                            "\n" +
                            "* test\n" +
                            "* test2", client.readProjectFile("test", "readme.md"));
    }

    /**
     * store project motd
     */
    @Test
    @Betamax(tape = "store_project_file_motd",mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query,MatchRule.headers})
    public void storeProjectFileMotd() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);


        client.storeProjectFile("test", "motd.md", "new motd.md content\n\n* test\n* test2");
        Assert.assertEquals("new motd.md content\n" +
                            "\n" +
                            "* test\n" +
                            "* test2", client.readProjectFile("test", "motd.md"));
    }
    /**
     * project DNE
     */
    @Test
    @Betamax(tape = "read_project_file_project_dne",mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
    public void readProjectFileProjectDNE() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);

        String readme = client.readProjectFile("project1", "readme.md");
        Assert.assertEquals(null, readme);

        String motd = client.readProjectFile("project1", "motd.md");
        Assert.assertEquals(null, motd);
    }
    /**
     * delete project file
     */
    @Test
    @Betamax(tape = "delete_project_file_readme",mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
    public void deleteProjectFileReadme() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);

        client.deleteProjectFile("test", "readme.md");
    }
    /**
     * delete project file
     */
    @Test
    @Betamax(tape = "delete_project_file_motd",mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
    public void deleteProjectFileMotd() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);

        client.deleteProjectFile("test", "motd.md");
    }

    /**
     * delete project file
     */
    @Test
    @Betamax(tape = "delete_project_file_projectDNE",
             mode = TapeMode.READ_ONLY,
             match = {MatchRule.uri, MatchRule.method, MatchRule.path, MatchRule.query})
    public void deleteProjectFileProjectDNE() throws Exception {
        final RundeckClient client = createClient(TEST_TOKEN_9, 13);

        try {
            client.deleteProjectFile("projectDNE", "motd.md");
            Assert.fail();
        } catch (RundeckApiException.RundeckApiHttpStatusException e) {
            Assert.assertEquals(404, e.getStatusCode());
        }
    }

    @Before
    public void setUp() throws Exception {
        // not that you can put whatever here, because we don't actually connect to the Rundeck instance
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
