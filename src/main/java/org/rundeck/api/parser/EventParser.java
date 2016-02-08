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
import org.rundeck.api.domain.RundeckEvent;
import org.rundeck.api.domain.RundeckEvent.EventStatus;
import org.rundeck.api.domain.RundeckEvent.NodeSummary;

/**
 * Parser for a single {@link RundeckEvent}
 *
 * @author Vincent Behar
 */
public class EventParser extends BaseXpathParser<RundeckEvent> {


    public EventParser(final String xpath) {
        super(xpath);
    }

    public EventParser() {
        super();
    }

    @Override
    public RundeckEvent parse(Node eventNode) {

        RundeckEvent event = new RundeckEvent();

        event.setTitle(StringUtils.trimToNull(eventNode.valueOf("title")));
        try {
            event.setStatus(EventStatus.valueOf(StringUtils.upperCase(eventNode.valueOf("status"))));
        } catch (IllegalArgumentException e) {
            event.setStatus(null);
        }
        event.setSummary(StringUtils.trimToNull(eventNode.valueOf("summary")));

        NodeSummary nodeSummary = new NodeSummary();
        nodeSummary.setSucceeded(Integer.valueOf(eventNode.valueOf("node-summary/@succeeded")));
        nodeSummary.setFailed(Integer.valueOf(eventNode.valueOf("node-summary/@failed")));
        nodeSummary.setTotal(Integer.valueOf(eventNode.valueOf("node-summary/@total")));
        event.setNodeSummary(nodeSummary);

        event.setUser(StringUtils.trimToNull(eventNode.valueOf("user")));
        event.setProject(StringUtils.trimToNull(eventNode.valueOf("project")));
        String startedAt = StringUtils.trimToNull(eventNode.valueOf("@starttime"));
        if (startedAt != null) {
            event.setStartedAt(new Date(Long.valueOf(startedAt)));
        }
        String endedAt = StringUtils.trimToNull(eventNode.valueOf("@endtime"));
        if (endedAt != null) {
            event.setEndedAt(new Date(Long.valueOf(endedAt)));
        }
        event.setAbortedBy(StringUtils.trimToNull(eventNode.valueOf("abortedby")));
        try {
            event.setExecutionId(Long.valueOf(eventNode.valueOf("execution/@id")));
        } catch (NumberFormatException e) {
            event.setExecutionId(null);
        }
        event.setJobId(StringUtils.trimToNull(eventNode.valueOf("job/@id")));

        return event;
    }

}
