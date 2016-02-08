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
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckEvent;
import org.rundeck.api.domain.RundeckHistory;

/**
 * Parser for a single {@link RundeckHistory}
 * 
 * @author Vincent Behar
 */
public class HistoryParser extends BaseXpathParser<RundeckHistory> {


    public HistoryParser(final String xpath) {
        super(xpath);
    }

    @Override
    public RundeckHistory parse(Node eventsNode) {

        RundeckHistory history = new RundeckHistory();

        history.setCount(Integer.valueOf(eventsNode.valueOf("@count")));
        history.setTotal(Integer.valueOf(eventsNode.valueOf("@total")));
        history.setMax(Integer.valueOf(eventsNode.valueOf("@max")));
        history.setOffset(Integer.valueOf(eventsNode.valueOf("@offset")));

        @SuppressWarnings("unchecked")
        List<Node> eventNodes = eventsNode.selectNodes("event");
        EventParser eventParser = new EventParser();

        for (Node eventNode : eventNodes) {
            RundeckEvent event = eventParser.parseXmlNode(eventNode);
            history.addEvent(event);
        }

        return history;
    }

}
