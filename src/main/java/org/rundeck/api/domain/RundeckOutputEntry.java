package org.rundeck.api.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Represents a RunDeck output entry
 * 
 */
public class RundeckOutputEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private String time = null;
	private Date absoluteTime = null;

	private RundeckLogLevel level = null;
	
	private String message = null;
	
	private String user = null;
	
	private String command = null;
	
	private String node = null;
    private String type = null;

    private Map<String,String> metadata;


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public RundeckLogLevel getLevel() {
		return level;
	}

	public void setLevel(RundeckLogLevel level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}



    @Override
    public String toString() {
        return "RundeckOutputEntry [time=" + time + ", level=" + level + 
        	", message=" + message + ", user=" + user
                + ", command=" + command
                + ", type=" + type
                + ", metadata=" + metadata
                + ", node=" + node + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + ((node == null) ? 0 : node.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RundeckOutputEntry other = (RundeckOutputEntry) obj;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (level == null) {
            if (other.level != null)
                return false;
        } else if (!level.equals(other.level))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (command == null) {
            if (other.command != null)
                return false;
        } else if (!command.equals(other.command))
            return false;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        return true;
    }

    /**
     * type of entry
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Date getAbsoluteTime() {
        return absoluteTime;
    }

    public void setAbsoluteTime(Date absoluteTime) {
        this.absoluteTime = absoluteTime;
    }


    public static enum RundeckLogLevel {
        SEVERE, ERROR, WARNING, INFO, NORMAL, DEBUG, CONFIG, VERBOSE, FINEST,;
    }

}
