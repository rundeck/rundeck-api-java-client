package org.rundeck.api;

import java.util.Properties;

/**
 * Job run request builder
 */
public class RunJobBuilder {
    private DefaultRunJob jobRun;

    public static RunJobBuilder builder() {
        return new RunJobBuilder();
    }

    public RunJobBuilder() {
        jobRun = new DefaultRunJob();
    }

    public RunJobBuilder setJobId(String jobId) {
        jobRun.setJobId(jobId);
        return this;
    }

    public RunJobBuilder setOptions(Properties options) {
        jobRun.setOptions(options);
        return this;
    }

    public RunJobBuilder setNodeFilters(Properties nodeFilters) {
        jobRun.setNodeFilters(nodeFilters);
        return this;
    }

    public RunJobBuilder setAsUser(String asUser) {
        jobRun.setAsUser(asUser);
        return this;
    }

    public RunJob build() {
        RunJob built = jobRun;
        jobRun = new DefaultRunJob();
        return built;
    }
}
