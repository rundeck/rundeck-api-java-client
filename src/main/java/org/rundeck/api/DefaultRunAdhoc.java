package org.rundeck.api;

import java.util.Properties;

/**
 * Base class for adhoc requests
 */
class DefaultRunAdhoc implements RunAdhoc{
    private String project;
    private Properties nodeFilters;
    private Integer nodeThreadcount;
    private Boolean nodeKeepgoing;
    private String asUser;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Properties getNodeFilters() {
        return nodeFilters;
    }

    public void setNodeFilters(Properties nodeFilters) {
        this.nodeFilters = nodeFilters;
    }

    public Integer getNodeThreadcount() {
        return nodeThreadcount;
    }

    public void setNodeThreadcount(Integer nodeThreadcount) {
        this.nodeThreadcount = nodeThreadcount;
    }

    public Boolean getNodeKeepgoing() {
        return nodeKeepgoing;
    }

    public void setNodeKeepgoing(Boolean nodeKeepgoing) {
        this.nodeKeepgoing = nodeKeepgoing;
    }

    public String getAsUser() {
        return asUser;
    }

    public void setAsUser(String asUser) {
        this.asUser = asUser;
    }
}
