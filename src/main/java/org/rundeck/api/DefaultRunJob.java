package org.rundeck.api;

import java.util.Properties;

/**
 * impl of {@link RunJob}
 */
class DefaultRunJob implements RunJob {
    private String jobId;
    private Properties options;
    private Properties nodeFilters;
    private String asUser;


    @Override
    public String getJobId() {
        return jobId;
    }

    @Override
    public Properties getOptions() {
        return options;
    }

    @Override
    public Properties getNodeFilters() {
        return nodeFilters;
    }

    @Override
    public String getAsUser() {
        return asUser;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setOptions(Properties options) {
        this.options = options;
    }

    public void setNodeFilters(Properties nodeFilters) {
        this.nodeFilters = nodeFilters;
    }

    public void setAsUser(String asUser) {
        this.asUser = asUser;
    }
}
