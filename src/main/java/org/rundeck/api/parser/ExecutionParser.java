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

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckExecution.ExecutionStatus;

/**
 * Parser for a single {@link RundeckExecution}
 * 
 * @author Vincent Behar
 */
public class ExecutionParser implements XmlNodeParser<RundeckExecution> {

    private String xpath;

    public ExecutionParser() {
        super();
    }

    /**
     * @param xpath of the execution element if it is not the root node
     */
    public ExecutionParser(String xpath) {
        super();
        this.xpath = xpath;
    }

    @Override
    public RundeckExecution parseXmlNode(Node node) {
        Node execNode = xpath != null ? node.selectSingleNode(xpath) : node;

        RundeckExecution execution = new RundeckExecution();

        execution.setId(Long.valueOf(execNode.valueOf("@id")));
        execution.setUrl(StringUtils.trimToNull(execNode.valueOf("@href")));
        try {
            execution.setStatus(ExecutionStatus.valueOf(StringUtils.upperCase(execNode.valueOf("@status"))));
        } catch (IllegalArgumentException e) {
            execution.setStatus(null);
        }
        execution.setDescription(StringUtils.trimToNull(execNode.valueOf("description")));
        execution.setArgstring(StringUtils.trimToNull(execNode.valueOf("argstring")));
        execution.setStartedBy(StringUtils.trimToNull(execNode.valueOf("user")));
        execution.setAbortedBy(StringUtils.trimToNull(execNode.valueOf("abortedby")));
        String startedAt = StringUtils.trimToNull(execNode.valueOf("date-started/@unixtime"));
        if (startedAt != null) {
            execution.setStartedAt(new Date(Long.valueOf(startedAt)));
        }
        String endedAt = StringUtils.trimToNull(execNode.valueOf("date-ended/@unixtime"));
        if (endedAt != null) {
            execution.setEndedAt(new Date(Long.valueOf(endedAt)));
        }

        Node jobNode = execNode.selectSingleNode("job");
        if (jobNode != null) {
            RundeckJob job = new JobParser().parseXmlNode(jobNode);
            execution.setJob(job);
        }

        return execution;
    }

}
