/*
 * Copyright 2011 Vincent Behar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rundeck.api.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Represents a Rundeck event
 * 
 * @author Vincent Behar
 */
public class RundeckEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private EventStatus status;

    private String summary;

    private NodeSummary nodeSummary;

    private String user;

    private String project;

    private Date startedAt;

    private Date endedAt;

    /** only if the execution was aborted */
    private String abortedBy;

    /** only if associated with an execution */
    private Long executionId;

    /** only if associated with a job */
    private String jobId;

    /**
     * @return the duration of the event in milliseconds (or null if the dates are invalid)
     */
    public Long getDurationInMillis() {
        if (startedAt == null || endedAt == null) {
            return null;
        }
        return endedAt.getTime() - startedAt.getTime();
    }

    /**
     * @return the duration of the event in seconds (or null if the dates are invalid)
     */
    public Long getDurationInSeconds() {
        Long durationInMillis = getDurationInMillis();
        return durationInMillis != null ? TimeUnit.MILLISECONDS.toSeconds(durationInMillis) : null;
    }

    /**
     * @return the duration of the event, as a human-readable string : "3 minutes 34 seconds" (or null if the dates are
     *         invalid)
     */
    public String getDuration() {
        Long durationInMillis = getDurationInMillis();
        return durationInMillis != null ? DurationFormatUtils.formatDurationWords(durationInMillis, true, true) : null;
    }

    /**
     * @return the duration of the event, as a "short" human-readable string : "0:03:34.187" (or null if the dates are
     *         invalid)
     */
    public String getShortDuration() {
        Long durationInMillis = getDurationInMillis();
        return durationInMillis != null ? DurationFormatUtils.formatDurationHMS(durationInMillis) : null;
    }

    /**
     * @return true if this event is for an ad-hoc command or script, false otherwise (for a job)
     */
    public boolean isAdhoc() {
        return "adhoc".equals(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the status of the event - see {@link EventStatus}
     */
    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the node summary - see {@link NodeSummary}
     */
    public NodeSummary getNodeSummary() {
        return nodeSummary;
    }

    public void setNodeSummary(NodeSummary nodeSummary) {
        this.nodeSummary = nodeSummary;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Date getStartedAt() {
        return (startedAt != null) ? new Date(startedAt.getTime()) : null;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = ((startedAt != null) ? new Date(startedAt.getTime()) : null);
    }

    public Date getEndedAt() {
        return (endedAt != null) ? new Date(endedAt.getTime()) : null;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = ((endedAt != null) ? new Date(endedAt.getTime()) : null);
    }

    public String getAbortedBy() {
        return abortedBy;
    }

    public void setAbortedBy(String abortedBy) {
        this.abortedBy = abortedBy;
    }

    /**
     * @return the ID of the execution associated with this event, or null if there is not
     */
    public Long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    /**
     * @return the ID of the job associated with this event, or null in the case of an ad-hoc command or script
     */
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "RundeckEvent [abortedBy=" + abortedBy + ", endedAt=" + endedAt + ", executionId=" + executionId
               + ", jobId=" + jobId + ", nodeSummary=" + nodeSummary + ", project=" + project + ", startedAt="
               + startedAt + ", status=" + status + ", summary=" + summary + ", title=" + title + ", user=" + user
               + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abortedBy == null) ? 0 : abortedBy.hashCode());
        result = prime * result + ((endedAt == null) ? 0 : endedAt.hashCode());
        result = prime * result + ((executionId == null) ? 0 : executionId.hashCode());
        result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
        result = prime * result + ((nodeSummary == null) ? 0 : nodeSummary.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        result = prime * result + ((startedAt == null) ? 0 : startedAt.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((summary == null) ? 0 : summary.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        RundeckEvent other = (RundeckEvent) obj;
        if (abortedBy == null) {
            if (other.abortedBy != null)
                return false;
        } else if (!abortedBy.equals(other.abortedBy))
            return false;
        if (endedAt == null) {
            if (other.endedAt != null)
                return false;
        } else if (!endedAt.equals(other.endedAt))
            return false;
        if (executionId == null) {
            if (other.executionId != null)
                return false;
        } else if (!executionId.equals(other.executionId))
            return false;
        if (jobId == null) {
            if (other.jobId != null)
                return false;
        } else if (!jobId.equals(other.jobId))
            return false;
        if (nodeSummary == null) {
            if (other.nodeSummary != null)
                return false;
        } else if (!nodeSummary.equals(other.nodeSummary))
            return false;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        if (startedAt == null) {
            if (other.startedAt != null)
                return false;
        } else if (!startedAt.equals(other.startedAt))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (summary == null) {
            if (other.summary != null)
                return false;
        } else if (!summary.equals(other.summary))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    /**
     * Summary for nodes
     */
    public static class NodeSummary implements Serializable {

        private static final long serialVersionUID = 1L;

        private int succeeded;

        private int failed;

        private int total;

        public int getSucceeded() {
            return succeeded;
        }

        public void setSucceeded(int succeeded) {
            this.succeeded = succeeded;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "NodeSummary [succeeded=" + succeeded + ", failed=" + failed + ", total=" + total + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + failed;
            result = prime * result + succeeded;
            result = prime * result + total;
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
            NodeSummary other = (NodeSummary) obj;
            if (failed != other.failed)
                return false;
            if (succeeded != other.succeeded)
                return false;
            if (total != other.total)
                return false;
            return true;
        }

    }

    /**
     * The status of an event
     */
    public static enum EventStatus {
        SUCCEEDED, FAILED, ABORTED;
    }

}
