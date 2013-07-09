package org.rundeck.api;

import java.util.Properties;

/**
 * Super interface for adhoc executions
 */
public interface RunAdhoc {
    /**
     * Project name, required
     * @return
     */
    String getProject();

    /**
     * Filters for selecting nodes on which the command will be executed. See {@link NodeFiltersBuilder}
     * @return
     */
    Properties getNodeFilters();

    /**
     * Thread count to use (for parallelizing when running on multiple nodes) - optional
     * @return
     */
    Integer getNodeThreadcount();

    /**
     * if true, continue executing on other nodes even if some fail - optional
     * @return
     */
    Boolean getNodeKeepgoing();

    /**
     * Specify a user name to run the job as, must have 'runAs' permission
     * @return
     */
    String getAsUser();
}
