package org.rundeck.api;

import java.util.Properties;

/**
 * $INTERFACE is ... User: greg Date: 7/9/13 Time: 10:38 AM
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

    public RunAdhocCommand create() {
        DefaultRunAdhocCommand built = command;
        command = new DefaultRunAdhocCommand();
        return built;
    }
}
