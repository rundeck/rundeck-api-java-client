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
package org.rundeck.api.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckJobsImportResult;

/**
 * Parser for a single {@link RundeckJobsImportResult}
 *
 * @author Vincent Behar
 */
public class JobsImportResultParser extends BaseXpathParser<RundeckJobsImportResult> {


    public JobsImportResultParser(final String xpath) {
        super(xpath);
    }

    @Override
    public RundeckJobsImportResult parse(Node resultNode) {

        RundeckJobsImportResult result = new RundeckJobsImportResult();

        @SuppressWarnings("unchecked")
        List<Node> succeededJobsNodes = resultNode.selectNodes("succeeded/job");
        if (succeededJobsNodes != null) {
            for (Node succeededJobNode : succeededJobsNodes) {
                RundeckJob job = new JobParser().parseXmlNode(succeededJobNode);
                result.addSucceededJob(job);
            }
        }

        @SuppressWarnings("unchecked")
        List<Node> skippedJobsNodes = resultNode.selectNodes("skipped/job");
        if (skippedJobsNodes != null) {
            for (Node skippedJobNode : skippedJobsNodes) {
                RundeckJob job = new JobParser().parseXmlNode(skippedJobNode);
                result.addSkippedJob(job);
            }
        }

        @SuppressWarnings("unchecked")
        List<Node> failedJobsNodes = resultNode.selectNodes("failed/job");
        if (failedJobsNodes != null) {
            for (Node failedJobNode : failedJobsNodes) {
                RundeckJob job = new JobParser().parseXmlNode(failedJobNode);
                result.addFailedJob(job, failedJobNode.valueOf("error"));
            }
        }

        return result;
    }

}
