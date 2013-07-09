package org.rundeck.api;

import java.io.InputStream;

/**
 * Bean implementing {@link RunAdhocScript}
 */
class DefaultRunAdhocScript extends DefaultRunAdhoc implements RunAdhocScript {
    private InputStream script;
    private String argString;
    private String scriptInterpreter;
    private Boolean interpreterArgsQuoted;

    @Override
    public InputStream getScript() {
        return script;
    }

    public void setScript(InputStream script) {
        this.script = script;
    }

    public String getArgString() {
        return argString;
    }

    public void setArgString(String argString) {
        this.argString = argString;
    }

    public String getScriptInterpreter() {
        return scriptInterpreter;
    }

    public void setScriptInterpreter(String scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    public Boolean getInterpreterArgsQuoted() {
        return interpreterArgsQuoted;
    }

    public void setInterpreterArgsQuoted(Boolean interpreterArgsQuoted) {
        this.interpreterArgsQuoted = interpreterArgsQuoted;
    }
}
