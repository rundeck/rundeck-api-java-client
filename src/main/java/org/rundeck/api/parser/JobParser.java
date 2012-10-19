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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckJob;

/**
 * Parser for a single {@link RundeckJob}
 * 
 * @author Vincent Behar
 */
public class JobParser implements XmlNodeParser<RundeckJob> {

    private String xpath;

    public JobParser() {
        super();
    }

    /**
     * @param xpath of the job element if it is not the root node
     */
    public JobParser(String xpath) {
        super();
        this.xpath = xpath;
    }

    @Override
    public RundeckJob parseXmlNode(Node node) {
        Node jobNode = xpath != null ? node.selectSingleNode(xpath) : node;

        RundeckJob job = new RundeckJob();

        job.setName(StringUtils.trimToNull(jobNode.valueOf("name")));
        job.setDescription(StringUtils.trimToNull(jobNode.valueOf("description")));
        job.setGroup(StringUtils.trimToNull(jobNode.valueOf("group")));

        // ID is either an attribute or an child element...
        String jobId = null;
        jobId = jobNode.valueOf("id");
        if (StringUtils.isBlank(jobId)) {
            jobId = jobNode.valueOf("@id");
        }
        job.setId(jobId);

        String averageDuration = StringUtils.trimToNull(jobNode.valueOf("@averageDuration"));
        if (averageDuration != null) {
            job.setAverageDuration(Long.valueOf(averageDuration));
        }

        // project is either a nested element of context, or just a child element
        Node contextNode = jobNode.selectSingleNode("context");
        if (contextNode != null) {
            job.setProject(StringUtils.trimToNull(contextNode.valueOf("project")));
        } else {
            job.setProject(StringUtils.trimToNull(jobNode.valueOf("project")));
        }

        return job;
    }

}
