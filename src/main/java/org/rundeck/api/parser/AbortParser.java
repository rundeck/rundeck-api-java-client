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
import org.rundeck.api.domain.RundeckAbort;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckAbort.AbortStatus;

/**
 * Parser for a single {@link RundeckAbort}
 * 
 * @author Vincent Behar
 */
public class AbortParser extends BaseXpathParser<RundeckAbort>  {

    public AbortParser(final String xpath) {
        super(xpath);
    }

    @Override
    public RundeckAbort parse(Node abortNode) {
        RundeckAbort abort = new RundeckAbort();

        try {
            abort.setStatus(AbortStatus.valueOf(StringUtils.upperCase(abortNode.valueOf("@status"))));
        } catch (IllegalArgumentException e) {
            abort.setStatus(null);
        }

        Node execNode = abortNode.selectSingleNode("execution");
        if (execNode != null) {
            RundeckExecution execution = new ExecutionParser().parseXmlNode(execNode);
            abort.setExecution(execution);
        }

        return abort;
    }

}
