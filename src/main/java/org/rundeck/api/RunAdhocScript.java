package org.rundeck.api;

import java.io.InputStream;

/**
 * An adhoc script to be executed by Rundeck
 */
public interface RunAdhocScript extends RunAdhoc {

    /**
     * Stream containing script
     * @return
     */
    InputStream getScript();

    /**
     * Arguments to the script
     * @return
     */
    String getArgString();

}
