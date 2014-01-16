package org.rundeck.api.domain;


/**
 * Identifies a node by name
 */
public interface RundeckNodeIdentity {
    /**
     * Return the rundeck Node name
     *
     * @return
     */
    String getName();
}
