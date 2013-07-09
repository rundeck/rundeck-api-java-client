package org.rundeck.api;

/**
 * A command to execute
 */
public interface RunAdhocCommand extends RunAdhoc {

    /**
     * Command to execute
     * @return
     */
    String getCommand();

}
