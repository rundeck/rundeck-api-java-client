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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Result of importing some jobs into Rundeck
 * 
 * @author Vincent Behar
 */
public class RundeckJobsImportResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<RundeckJob> succeededJobs = new ArrayList<RundeckJob>();

    private final List<RundeckJob> skippedJobs = new ArrayList<RundeckJob>();

    private final Map<RundeckJob, String> failedJobs = new HashMap<RundeckJob, String>();

    public void addSucceededJob(RundeckJob job) {
        succeededJobs.add(job);
    }

    public void addSkippedJob(RundeckJob job) {
        skippedJobs.add(job);
    }

    public void addFailedJob(RundeckJob job, String errorMessage) {
        failedJobs.put(job, errorMessage);
    }

    public List<RundeckJob> getSucceededJobs() {
        return succeededJobs;
    }

    public List<RundeckJob> getSkippedJobs() {
        return skippedJobs;
    }

    public Map<RundeckJob, String> getFailedJobs() {
        return failedJobs;
    }

    @Override
    public String toString() {
        return "RundeckJobsImportResult [succeededJobs=" + succeededJobs + ", skippedJobs=" + skippedJobs
               + ", failedJobs=" + failedJobs + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((failedJobs == null) ? 0 : failedJobs.hashCode());
        result = prime * result + ((skippedJobs == null) ? 0 : skippedJobs.hashCode());
        result = prime * result + ((succeededJobs == null) ? 0 : succeededJobs.hashCode());
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
        RundeckJobsImportResult other = (RundeckJobsImportResult) obj;
        if (failedJobs == null) {
            if (other.failedJobs != null)
                return false;
        } else if (!failedJobs.equals(other.failedJobs))
            return false;
        if (skippedJobs == null) {
            if (other.skippedJobs != null)
                return false;
        } else if (!skippedJobs.equals(other.skippedJobs))
            return false;
        if (succeededJobs == null) {
            if (other.succeededJobs != null)
                return false;
        } else if (!succeededJobs.equals(other.succeededJobs))
            return false;
        return true;
    }

}
