package org.rundeck.api;

import java.io.InputStream;

/**
 * Bean implementing {@link RunAdhocScript}
 */
class DefaultRunAdhocScript extends DefaultRunAdhoc implements RunAdhocScript {
    private InputStream script;
    private String argString;

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
}
