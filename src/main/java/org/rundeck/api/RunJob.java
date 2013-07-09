package org.rundeck.api;

import java.util.Properties;

/**
 * Job run request
 */
public interface RunJob {
    /**
     * Identifier of the job - mandatory
     * @return
     */
    String getJobId();

    /**
     * Options of the job - optional. See {@link OptionsBuilder}.
     * @return
     */
    Properties getOptions();

    /**
     * Node filters for overriding the nodes on which the job will be executed - optional. See
     *            {@link NodeFiltersBuilder}
     *
     * @return
     */
    Properties getNodeFilters();

    /**
     * Specify a user name to run the job as, must have 'runAs' permission
     * @return
     */
    String getAsUser();
}
