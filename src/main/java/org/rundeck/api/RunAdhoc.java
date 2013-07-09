package org.rundeck.api;

import java.util.Properties;

/**
 * Super interface for adhoc executions
 */
public interface RunAdhoc {
    /**
     * Project name
     * @return
     */
    String getProject();

    /**
     * Node filters
     * @return
     */
    Properties getNodeFilters();

    /**
     * Threadcount
     * @return
     */
    Integer getNodeThreadcount();

    /**
     * Keepgoing
     * @return
     */
    Boolean getNodeKeepgoing();

    /**
     * As User
     * @return
     */
    String getAsUser();
}
