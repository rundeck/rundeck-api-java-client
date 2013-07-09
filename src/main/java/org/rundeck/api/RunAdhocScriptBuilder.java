package org.rundeck.api;

import java.io.InputStream;
import java.util.Properties;

/**
 * A builder to create a {@link RunAdhocScript}, use the {@link #builder()} to create a builder, then {@link #create()}
 * to build an RunAdhocScript
 */
public class RunAdhocScriptBuilder {
    private DefaultRunAdhocScript script;

    private RunAdhocScriptBuilder() {
        script = new DefaultRunAdhocScript();
    }

    public RunAdhocScriptBuilder(final RunAdhocScript oldScript) {
        script = new DefaultRunAdhocScript();
        setAsUser(oldScript.getAsUser());
        setArgString(oldScript.getArgString());
        setProject(oldScript.getProject());
        setScript(oldScript.getScript());
        setNodeFilters(oldScript.getNodeFilters());
        setNodeKeepgoing(oldScript.getNodeKeepgoing());
        setNodeThreadcount(oldScript.getNodeThreadcount());
    }

    /**
     * Create a new builder
     *
     * @return
     */
    public static RunAdhocScriptBuilder builder() {
        return new RunAdhocScriptBuilder();
    }

    /**
     * Create a builder initialized with an existing {@link RunAdhocScript}
     *
     * @param script
     *
     * @return this builder
     */
    public static RunAdhocScriptBuilder builder(final RunAdhocScript script) {
        return new RunAdhocScriptBuilder(script);
    }

    public RunAdhocScriptBuilder setProject(final String project) {
        script.setProject(project);
        return this;
    }

    public RunAdhocScriptBuilder setScript(final InputStream stream) {
        script.setScript(stream);
        return this;
    }

    public RunAdhocScriptBuilder setArgString(final String argString) {
        script.setArgString(argString);
        return this;
    }

    public RunAdhocScriptBuilder setNodeFilters(final Properties nodeFilters) {
        script.setNodeFilters(nodeFilters);
        return this;
    }

    public RunAdhocScriptBuilder setNodeThreadcount(final Integer nodeThreadcount) {
        script.setNodeThreadcount(nodeThreadcount);
        return this;
    }

    public RunAdhocScriptBuilder setNodeKeepgoing(final Boolean nodeKeepgoing) {
        script.setNodeKeepgoing(nodeKeepgoing);
        return this;
    }

    public RunAdhocScriptBuilder setAsUser(final String asUser) {
        script.setAsUser(asUser);
        return this;
    }

    public RunAdhocScript create() {
        final DefaultRunAdhocScript built = script;
        script = new DefaultRunAdhocScript();
        return built;
    }
}
