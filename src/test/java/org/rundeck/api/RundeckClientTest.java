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
import java.util.List;

import betamax.MatchRule;
import betamax.TapeMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.rundeck.api.domain.RundeckEvent;
import org.rundeck.api.domain.RundeckHistory;
import org.rundeck.api.domain.RundeckProject;
import betamax.Betamax;
import betamax.Recorder;

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

    @Before
    public void setUp() throws Exception {
        // not that you can put whatever here, because we don't actually connect to the RunDeck instance
        // but instead use betamax as a proxy to serve the previously recorded tapes (in src/test/resources)
        client = new RundeckClient("http://rundeck.local:4440", "PVnN5K3OPc5vduS3uVuVnEsD57pDC5pd");
    }

}
