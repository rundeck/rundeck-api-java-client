/*
 * Copyright 2013 DTO Labs, Inc. (http://dtolabs.com)
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
 * OutputEntryParserV5.java
 * 
 * User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 * Created: 1/2/13 5:33 PM
 * 
 */
package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckOutputEntry;


/**
 * OutputEntryParserV5 parses message entry for API v5
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class OutputEntryParserV5 extends OutputEntryParser implements XmlNodeParser<RundeckOutputEntry>{
    public OutputEntryParserV5() {
    }

    public OutputEntryParserV5(String xpath) {
        super(xpath);
    }

    @Override
    protected String parseMessage(Node entryNode) {
        return StringUtils.trimToNull(entryNode.getStringValue());
    }
}
