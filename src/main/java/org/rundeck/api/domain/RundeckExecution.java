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
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Represents a RunDeck execution, usually triggered by an API call. An execution could be a {@link RundeckJob}
 * execution or an "ad-hoc" execution.
 * 
 * @author Vincent Behar
 */
public class RundeckExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String url;

    private ExecutionStatus status;

    /** Optional - only if it is a job execution */
    private RundeckJob job;

    private String startedBy;

    private Date startedAt;

    /** only if the execution has ended */
    private Date endedAt;

    /** only if the execution was aborted */
    private String abortedBy;

    private String description;
    private String argstring;
    private String project;
    private Set<RundeckNodeIdentity> successfulNodes;
    private Set<RundeckNodeIdentity> failedNodes;

    /**
     * @return the duration of the execution in milliseconds (or null if the duration is still running, or has been
     *         aborted)
     */
    public Long getDurationInMillis() {
        if (startedAt == null || endedAt == null) {
            return null;
        }
        return endedAt.getTime() - startedAt.getTime();
    }

    /**
     * @return the duration of the execution in seconds (or null if the execution is still running, or has been aborted)
     */
    public Long getDurationInSeconds() {
        Long durationInMillis = getDurationInMillis();
        return durationInMillis != null ? TimeUnit.MILLISECONDS.toSeconds(durationInMillis) : null;
    }

    /**
     * @return the duration of the execution, as a human-readable string : "3 minutes 34 seconds" (or null if the
     *         execution is still running, or has been aborted)
     */
    public String getDuration() {
        Long durationInMillis = getDurationInMillis();
        return durationInMillis != null ? DurationFormatUtils.formatDurationWords(durationInMillis, true, true) : null;
    }

    /**
     * @return the duration of the execution, as a "short" human-readable string : "0:03:34.187" (or null if the
     *         execution is still running, or has been aborted)
     */
    public String getShortDuration() {
        Long durationInMillis = getDurationInMillis();
        return durationInMillis != null ? DurationFormatUtils.formatDurationHMS(durationInMillis) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the status of the execution - see {@link ExecutionStatus}
     */
    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    /**
     * @return the {@link RundeckJob} associated with this execution, or null in the case of an ad-hoc execution
     *         (command or script)
     */
    public RundeckJob getJob() {
        return job;
    }

    public void setJob(RundeckJob job) {
        this.job = job;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RundeckExecution [id=" + id + ", description=" + description + ", url=" + url + ", status=" + status
               + ", argstring=" + argstring
               + ", startedBy=" + startedBy + ", startedAt=" + startedAt + ", endedAt=" + endedAt
               + ", durationInSeconds=" + getDurationInSeconds() + ", abortedBy=" + abortedBy + ", job=" + job
               + ", successfulNodes=" + getSuccessfulNodes()
                + ", failedNodes=" + getFailedNodes()
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abortedBy == null) ? 0 : abortedBy.hashCode());
        result = prime * result + ((argstring == null) ? 0 : argstring.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((endedAt == null) ? 0 : endedAt.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((job == null) ? 0 : job.hashCode());
        result = prime * result + ((startedAt == null) ? 0 : startedAt.hashCode());
        result = prime * result + ((startedBy == null) ? 0 : startedBy.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
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
        RundeckExecution other = (RundeckExecution) obj;
        if (abortedBy == null) {
            if (other.abortedBy != null)
                return false;
        } else if (!abortedBy.equals(other.abortedBy))
            return false;
        if (argstring == null) {
            if (other.argstring != null)
                return false;
        } else if (!argstring.equals(other.argstring))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (endedAt == null) {
            if (other.endedAt != null)
                return false;
        } else if (!endedAt.equals(other.endedAt))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (job == null) {
            if (other.job != null)
                return false;
        } else if (!job.equals(other.job))
            return false;
        if (startedAt == null) {
            if (other.startedAt != null)
                return false;
        } else if (!startedAt.equals(other.startedAt))
            return false;
        if (startedBy == null) {
            if (other.startedBy != null)
                return false;
        } else if (!startedBy.equals(other.startedBy))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        return true;
    }

    /**
     * the argument string for the execution
     */
    public String getArgstring() {
        return argstring;
    }

    public void setArgstring(String argstring) {
        this.argstring = argstring;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Set<RundeckNodeIdentity> getSuccessfulNodes() {
        return successfulNodes;
    }

    public void setSuccessfulNodes(Set<RundeckNodeIdentity> successfulNodes) {
        this.successfulNodes = successfulNodes;
    }

    public Set<RundeckNodeIdentity> getFailedNodes() {
        return failedNodes;
    }

    public void setFailedNodes(Set<RundeckNodeIdentity> failedNodes) {
        this.failedNodes = failedNodes;
    }

    /**
     * The status of an execution
     */
    public static enum ExecutionStatus {
        RUNNING, SUCCEEDED, FAILED, ABORTED;
    }

}
