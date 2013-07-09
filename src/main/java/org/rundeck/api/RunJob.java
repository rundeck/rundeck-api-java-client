package org.rundeck.api;

import java.util.Properties;

/**
 * Job run request
 */
public interface RunJob {
    /**
     * Job ID
     * @return
     */
    String getJobId();

    /**
     * Options to job
     * @return
     */
    Properties getOptions();

    /**
     * Node filters
     * @return
     */
    Properties getNodeFilters();

    /**
     * as User
     * @return
     */
    String getAsUser();
}
