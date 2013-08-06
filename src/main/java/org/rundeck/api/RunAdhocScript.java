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

    /**
     * Command string to invoke the script with arguments
     * @return
     */
    String getScriptInterpreter();

    /**
     * If true, quote the script filepath and args to the script interpreter.
     * @return
     */
    Boolean getInterpreterArgsQuoted();

}
