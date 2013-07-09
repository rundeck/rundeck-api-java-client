package org.rundeck.api;

import java.io.InputStream;
import java.util.Properties;

/**
 * A builder to create a {@link RunAdhocScript}, use the {@link #builder()} to create a builder, then {@link #create()} to
 * build an RunAdhocScript
 */
public class RunAdhocScriptBuilder {
    private DefaultRunAdhocScript script;

    public RunAdhocScriptBuilder() {
        script = new DefaultRunAdhocScript();
    }

    public static RunAdhocScriptBuilder builder() {
        return new RunAdhocScriptBuilder();
    }

    public RunAdhocScriptBuilder setProject(String project) {
        script.setProject(project);

        return this;
    }

    public RunAdhocScriptBuilder setScript(InputStream stream) {
        script.setScript(stream);
        return this;
    }

    public RunAdhocScriptBuilder setArgString(String argString) {
        script.setArgString(argString);
        return this;
    }

    public RunAdhocScriptBuilder setNodeFilters(Properties nodeFilters) {
        script.setNodeFilters(nodeFilters);
        return this;
    }

    public RunAdhocScriptBuilder setNodeThreadcount(Integer nodeThreadcount) {
        script.setNodeThreadcount(nodeThreadcount);
        return this;
    }

    public RunAdhocScriptBuilder setNodeKeepgoing(Boolean nodeKeepgoing) {
        script.setNodeKeepgoing(nodeKeepgoing);
        return this;
    }

    public RunAdhocScriptBuilder setAsUser(String asUser) {
        script.setAsUser(asUser);
        return this;
    }

    public RunAdhocScript create() {
        DefaultRunAdhocScript built = script;
        script = new DefaultRunAdhocScript();
        return built;
    }
}
