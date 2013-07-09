package org.rundeck.api;

/**
 * Implementation of {@link RunAdhocCommand}
 */
class DefaultRunAdhocCommand extends DefaultRunAdhoc implements RunAdhocCommand {
    private String command;

    @Override
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
