package org.rundeck.api;

import java.io.InputStream;

/**
 * An adhoc script to be executed by Rundeck
 */
public interface RunAdhocScript extends RunAdhoc {

    /**
     * InputStream for reading the script to be executed - mandatory
     * @return
     */
    InputStream getScript();

    /**
     * Arguments to the script
     * @return
     */
    String getArgString();

}
