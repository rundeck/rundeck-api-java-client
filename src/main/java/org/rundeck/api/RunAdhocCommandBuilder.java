package org.rundeck.api;

import java.util.Properties;

/**
 * Builder for adhoc command request
 */
public class RunAdhocCommandBuilder {
    private DefaultRunAdhocCommand command;

    public RunAdhocCommandBuilder() {
        command = new DefaultRunAdhocCommand();
    }

    public static RunAdhocCommandBuilder builder() {
        return new RunAdhocCommandBuilder();
    }

    public RunAdhocCommandBuilder setProject(String project) {
        command.setProject(project);

        return this;
    }

    public RunAdhocCommandBuilder setCommand(String commandString) {
        command.setCommand(commandString);
        return this;
    }

    public RunAdhocCommandBuilder setNodeFilters(Properties nodeFilters) {
        command.setNodeFilters(nodeFilters);
        return this;
    }

    public RunAdhocCommandBuilder setNodeThreadcount(Integer nodeThreadcount) {
        command.setNodeThreadcount(nodeThreadcount);
        return this;
    }

    public RunAdhocCommandBuilder setNodeKeepgoing(Boolean nodeKeepgoing) {
        command.setNodeKeepgoing(nodeKeepgoing);
        return this;
    }

    public RunAdhocCommandBuilder setAsUser(String asUser) {
        command.setAsUser(asUser);
        return this;
    }

    public RunAdhocCommand build() {
        DefaultRunAdhocCommand built = command;
        command = new DefaultRunAdhocCommand();
        return built;
    }
}
