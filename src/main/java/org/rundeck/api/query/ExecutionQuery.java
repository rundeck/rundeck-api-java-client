/*
 * Copyright 2012 DTO Labs, Inc. (http://dtolabs.com)
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
 *
 */

/*
* ExecutionQuery.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 9/13/12 3:18 PM
* 
*/
package org.rundeck.api.query;

import org.apache.commons.lang.StringUtils;
import org.rundeck.api.domain.RundeckExecution;

import java.util.*;


/**
 * ExecutionQuery contains the query parameters for the /executions api, create one with the {@link Builder} class, e.g.
 * using the {@link #builder()} method.
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class ExecutionQuery  {
    private RundeckExecution.ExecutionStatus status;
    private String abortedby;
    private List<String> jobList;
    private List<String> jobIdList;
    private List<String> excludeJobList;
    private List<String> excludeJobIdList;
    private Date begin;
    private Date end;
    private String recent;
    private String job;
    private String jobExact;
    private String project;
    private String groupPath;
    private String groupPathExact;
    private String user;
    private String description;
    private Boolean adhoc;

    private ExecutionQuery() {
    }

    private ExecutionQuery(ExecutionQuery query) {
        this.status = query.getStatus();
        this.abortedby = query.getAbortedby();
        this.jobList = query.getJobList();
        this.jobIdList = query.getJobIdList();
        this.excludeJobList = query.getExcludeJobList();
        this.excludeJobIdList = query.getExcludeJobIdList();
        this.begin = query.getBegin();
        this.end = query.getEnd();
        this.recent = query.getRecent();
        this.job = query.getJob();
        this.jobExact = query.getJobExact();
        this.project = query.getProject();
        this.groupPath = query.getGroupPath();
        this.groupPathExact = query.getGroupPathExact();
        this.description = query.getDescription();
        this.user = query.getUser();
        this.adhoc = query.getAdhoc();
    }

    /**
     * Return true if some parameter is set
     */
    public boolean notBlank() {
        return null!=status ||
               !StringUtils.isBlank(abortedby) ||
               !StringUtils.isBlank(recent) ||
               !StringUtils.isBlank(job) ||
               !StringUtils.isBlank(jobExact) ||
               !StringUtils.isBlank(groupPath) ||
               !StringUtils.isBlank(groupPathExact) ||
               !StringUtils.isBlank(description) ||
               //boolean
               (null!=adhoc) ||
               //lists
               (null != jobList && jobList.size() > 0) ||
               (null != jobIdList && jobIdList.size() > 0) ||
               (null != excludeJobList && excludeJobList.size() > 0) ||
               (null != excludeJobIdList && excludeJobIdList.size() > 0) ||
               //dates
               (null != begin ) ||
               (null != end )
            ;
    }

    public RundeckExecution.ExecutionStatus getStatus() {
        return status;
    }

    public String getAbortedby() {
        return abortedby;
    }

    public List<String> getJobList() {
        return jobList;
    }

    public List<String> getExcludeJobList() {
        return excludeJobList;
    }

    public List<String> getExcludeJobIdList() {
        return excludeJobIdList;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public String getRecent() {
        return recent;
    }

    public String getJob() {
        return job;
    }

    public String getJobExact() {
        return jobExact;
    }

    public String getProject() {
        return project;
    }

    public String getGroupPath() {
        return groupPath;
    }

    public String getGroupPathExact() {
        return groupPathExact;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getJobIdList() {
        return jobIdList;
    }

    /**
     * Create a Builder for an ExecutionQuery
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getUser() {
        return user;
    }

    public Boolean getAdhoc() {
        return adhoc;
    }


    /**
     * Builder for ExecutionQueries
     */
    public static class Builder {
        ExecutionQuery query;

        public Builder() {
            query = new ExecutionQuery();
        }

        /**
         * Execution status to query
         */
        public Builder status(RundeckExecution.ExecutionStatus status) {
            query.status = status;
            return this;
        }

        /**
         * Username
         */
        public Builder abortedby(String abortedby) {
            query.abortedby = abortedby;
            return this;
        }

        /**
         * List of job "group/name" to query
         */
        public Builder jobList(List<String> list) {
            query.jobList = list;
            return this;
        }
        /**
         * List of job IDs to query
         */
        public Builder jobIdList(List<String> list) {
            query.jobIdList = list;
            return this;
        }

        /**
         * List of job "group/name" to exclude from query
         */
        public Builder excludeJobList(List<String> list) {
            query.excludeJobList = list;
            return this;
        }

        /**
         * List of job IDs to exclude from query
         */
        public Builder excludeJobIdList(List<String> list) {
            query.excludeJobIdList = list;
            return this;
        }

        /**
         * Start date/time for execution completion time
         */
        public Builder begin(Date begin) {
            query.begin = begin;
            return this;
        }

        /**
         * End date/time for execution completion time
         */
        public Builder end(Date end) {
            query.end = end;
            return this;
        }

        /**
         * Recent time filter, in the for "XY", where X is a number, and Y
         * is one of: "h" (hour), "d" (day), "w" (week), "m" (month), "y" (year)
         */
        public Builder recent(String recent) {
            query.recent = recent;
            return this;
        }

        /**
         * Job name filter, which can match any part of the name
         */
        public Builder job(String job) {
            query.job = job;
            return this;
        }

        /**
         * Job exact name filter, much match exactly
         */
        public Builder jobExact(String exact) {
            query.jobExact = exact;
            return this;
        }

        /**
         * Project name
         */
        public Builder project(String project) {
            query.project = project;
            return this;
        }

        /**
         * Group path or super path, which will include any jobs within the subtree.
         */
        public Builder groupPath(String path) {
            query.groupPath = path;
            return this;
        }

        /**
         * Exact group path match
         */
        public Builder groupPathExact(String exact) {
            query.groupPathExact = exact;
            return this;
        }

        /**
         * Job description match
         */
        public Builder description(String description) {
            query.description = description;
            return this;
        }

        /**
         * Name of username that started the execution
         */
        public Builder user(String user) {
            query.user = user;
            return this;
        }

        /**
         * True to query only adhoc executions, false to query only Job executions, null for both
         */
        public Builder adhoc(Boolean adhoc) {
            query.adhoc = adhoc;
            return this;
        }

        /**
         * Build the query
         */
        public ExecutionQuery build() {
            return new ExecutionQuery(query);
        }
    }
}
