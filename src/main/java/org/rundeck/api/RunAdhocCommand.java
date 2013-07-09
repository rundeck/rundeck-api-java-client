package org.rundeck.api;

/**
 * A command to execute
 */
public interface RunAdhocCommand extends RunAdhoc {

    /**
     * Command to executed
     * @return
     */
    String getCommand();

}
