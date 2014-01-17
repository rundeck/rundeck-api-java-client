package org.rundeck.api.domain;

import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

import org.rundeck.api.domain.RundeckExecution.ExecutionStatus;


/**
 * Represents a RunDeck output
 * 
 */
public class RundeckOutput implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long executionId;
	
	//private String message = null;

	//private String error = null;
	
	private Boolean unmodified = false;
	
	private Boolean empty = false;
	
	private int offset;
	
	private Boolean completed = false;
	
	private Boolean execCompleted = false;
	
	private Boolean hasFailedNodes = false;
	
	private RundeckExecution.ExecutionStatus status = null;
	
	private Long lastModified;
	
	private Long execDuration;
	
	private Float percentLoaded;
	
	private int totalSize;
	
	List<RundeckOutputEntry> logEntries = null;

    private String filterNode;
    private String filterStep;

	public Long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}

	/*public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}*/

	public Boolean isUnmodified() {
		return unmodified;
	}

	public void setUnmodified(Boolean unmodified) {
		this.unmodified = unmodified;
	}

	public Boolean isEmpty() {
		return empty;
	}

	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Boolean isCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Boolean isExecCompleted() {
		return execCompleted;
	}

	public void setExecCompleted(Boolean execCompleted) {
		this.execCompleted = execCompleted;
	}

	public Boolean isHasFailedNodes() {
		return hasFailedNodes;
	}

	public void setHasFailedNodes(Boolean hasFailedNodes) {
		this.hasFailedNodes = hasFailedNodes;
	}

	public RundeckExecution.ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(RundeckExecution.ExecutionStatus status) {
		this.status = status;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}

	public Long getExecDuration() {
		return execDuration;
	}

	public void setExecDuration(Long execDuration) {
		this.execDuration = execDuration;
	}

	public Float getPercentLoaded() {
		return percentLoaded;
	}

	public void setPercentLoaded(Float percentLoaded) {
		this.percentLoaded = percentLoaded;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public List<RundeckOutputEntry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(List<RundeckOutputEntry> logEntries) {
		this.logEntries = logEntries;
	}
	
	public void addLogEntry(RundeckOutputEntry entry){
		if(logEntries == null){
			logEntries = new ArrayList<RundeckOutputEntry>();
		}
		logEntries.add(entry);
	}

    @Override
    public String toString() {
        return "RundeckOutput [executionId=" + executionId + /*", message=" + message + 
        	", error=" + error +*/ ", unmodified=" + unmodified + ", empty=" + empty + 
        	", offset=" + offset + ", completed=" + completed +
        	", execCompleted=" + execCompleted + ", hasFailedNodes=" + hasFailedNodes +
        	", status=" + status + ", lastModified=" + lastModified +
        	", execDuration=" + execDuration + ", percentLoaded=" + percentLoaded +
        	", filterNode=" + filterNode + ", filterStep=" + filterStep +
        	", totalSize=" + totalSize + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((executionId == null) ? 0 : executionId.hashCode());
       // result = prime * result + ((message == null) ? 0 : message.hashCode());
       // result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + ((unmodified == null) ? 0 : unmodified.hashCode());
        result = prime * result + ((empty == null) ? 0 : empty.hashCode());
        result = prime * result + offset;
        result = prime * result + ((completed == null) ? 0 : completed.hashCode());
        result = prime * result + ((execCompleted == null) ? 0 : execCompleted.hashCode());
        result = prime * result + ((hasFailedNodes == null) ? 0 : hasFailedNodes.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
        result = prime * result + ((execDuration == null) ? 0 : execDuration.hashCode());
        result = prime * result + ((percentLoaded == null) ? 0 : percentLoaded.hashCode());
        result = prime * result + totalSize;
        result = prime * result + ((logEntries == null) ? 0 : logEntries.hashCode());
        result = prime * result + ((filterNode == null) ? 0 : filterNode.hashCode());
        result = prime * result + ((filterStep == null) ? 0 : filterStep.hashCode());
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
        RundeckOutput other = (RundeckOutput) obj;
        if (executionId == null) {
            if (other.executionId != null)
                return false;
        } else if (!executionId.equals(other.executionId))
            return false;
/*        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (error == null) {
            if (other.error != null)
                return false;
        } else if (!error.equals(other.error))
            return false;
*/
        if (unmodified == null) {
            if (other.unmodified != null)
                return false;
        } else if (!unmodified.equals(other.unmodified))
            return false;
        if (empty == null) {
            if (other.empty != null)
                return false;
        } else if (!empty.equals(other.empty))
            return false;
        if (offset != other.offset)
            return false;
        if (completed == null) {
            if (other.completed != null)
                return false;
        } else if (!completed.equals(other.completed))
            return false;
        if (execCompleted == null) {
            if (other.execCompleted != null)
                return false;
        } else if (!execCompleted.equals(other.execCompleted))
            return false;
        if (hasFailedNodes == null) {
            if (other.hasFailedNodes != null)
                return false;
        } else if (!hasFailedNodes.equals(other.hasFailedNodes))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (lastModified == null) {
            if (other.lastModified != null)
                return false;
        } else if (!lastModified.equals(other.lastModified))
            return false;
        if (execDuration == null) {
            if (other.execDuration != null)
                return false;
        } else if (!execDuration.equals(other.execDuration))
            return false;
        if (percentLoaded == null) {
            if (other.percentLoaded != null)
                return false;
        } else if (!percentLoaded.equals(other.percentLoaded))
            return false;
        if (totalSize != other.totalSize)
            return false;
        if (logEntries == null) {
            if (other.logEntries != null)
                return false;
        } else if (!logEntries.equals(other.logEntries))
            return false;
        if (filterNode == null) {
            if (other.filterNode != null)
                return false;
        } else if (!filterNode.equals(other.filterNode))
            return false;
        if (filterStep == null) {
            if (other.filterStep != null)
                return false;
        } else if (!filterStep.equals(other.filterStep))
            return false;
        return true;
    }

    public String getFilterNode() {
        return filterNode;
    }

    /**
     * return the node name used to filter this output
     * @param filterNode
     */
    public void setFilterNode(String filterNode) {
        this.filterNode = filterNode;
    }

    /**
     * Return the step context used to filter this output
     * @return
     */
    public String getFilterStep() {
        return filterStep;
    }

    public void setFilterStep(String filterStep) {
        this.filterStep = filterStep;
    }
}
