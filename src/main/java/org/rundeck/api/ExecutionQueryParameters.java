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
* ExecutionQueryParameters.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 9/13/12 3:54 PM
* 
*/
package org.rundeck.api;

import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.query.ExecutionQuery;


/**
 * ExecutionQueryParameters adds parameters to a {@link ApiPathBuilder} from a {@link ExecutionQuery} object.
 *
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
class ExecutionQueryParameters extends QueryParameterBuilder {
    ExecutionQuery query;

    ExecutionQueryParameters(final ExecutionQuery query) {
        this.query = query;
    }

    public boolean buildParameters(final ApiPathBuilder builder, final boolean doPost) {
        boolean seen = false;

        seen |= visit("jobFilter", query.getJob(), doPost, builder);
        seen |= visit("jobExactFilter", query.getJobExact(), doPost, builder);
        seen |= visit("excludeJobFilter", query.getExcludeJob(), doPost, builder);
        seen |= visit("excludeJobExactFilter", query.getExcludeJobExact(), doPost, builder);
        seen |= visit("project", query.getProject(), doPost, builder);
        seen |= visit("groupPath", query.getGroupPath(), doPost, builder);
        seen |= visit("groupPathExact", query.getGroupPathExact(), doPost, builder);
        seen |= visit("excludeGroupPath", query.getExcludeGroupPath(), doPost, builder);
        seen |= visit("excludeGroupPathExact", query.getExcludeGroupPathExact(), doPost, builder);
        seen |= visit("descFilter", query.getDescription(), doPost, builder);
        seen |= visit("userFilter", query.getUser(), doPost, builder);
        if(null!=query.getAdhoc()){
            seen |= visit("adhoc", query.getAdhoc(), doPost, builder);
        }

        seen |= visit("statusFilter", stringVal(query.getStatus()), doPost, builder);
        seen |= visit("abortedbyFilter", query.getAbortedby(), doPost, builder);
        seen |= visit("jobListFilter", query.getJobList(), doPost, builder);
        seen |= visit("jobIdListFilter", query.getJobIdList(), doPost, builder);
        seen |= visit("excludeJobIdListFilter", query.getExcludeJobIdList(), doPost, builder);
        seen |= visit("excludeJobListFilter", query.getExcludeJobList(), doPost, builder);
        seen |= visit("begin", query.getBegin(), doPost, builder);
        seen |= visit("end", query.getEnd(), doPost, builder);
        seen |= visit("recentFilter", query.getRecent(), doPost, builder);

        return seen;
    }

    public static String stringVal(final RundeckExecution.ExecutionStatus status) {
        return null != status ? status.toString().toLowerCase() : null;
    }

}
