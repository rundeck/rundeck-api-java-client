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
* RundeckJobDeleteBulk.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 10/1/12 4:12 PM
* 
*/
package org.rundeck.api.domain;

import java.util.*;


/**
 * RundeckJobDeleteBulk represents the result of a bulk job delete request and contains
 * a list of {@link RundeckJobDelete} objects.
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class RundeckJobDeleteBulk implements Iterable<RundeckJobDelete> {
    private List<RundeckJobDelete> results;
    private int requestCount;
    private boolean allsuccessful;

    public RundeckJobDeleteBulk(List<RundeckJobDelete> results, int requestCount, boolean allsuccessful) {
        this.results = results;
        this.requestCount = requestCount;
        this.allsuccessful = allsuccessful;
    }

    public List<RundeckJobDelete> getResults() {
        return results;
    }

    /**
     * The number of job delete requests processed.
     */
    public int getRequestCount() {
        return requestCount;
    }

    /**
     * True if all job delete requests were successful
     */
    public boolean isAllsuccessful() {
        return allsuccessful;
    }

    @Override
    public Iterator<RundeckJobDelete> iterator() {
        if(null!=results){
            return results.iterator();
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        return "RundeckJobDeleteBulk{" +
               "results=" + results +
               ", requestCount=" + requestCount +
               ", allsuccessful=" + allsuccessful +
               '}';
    }
}
