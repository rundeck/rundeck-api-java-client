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
* DeleteParser.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 10/1/12 3:52 PM
* 
*/
package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckJobDelete;

import java.util.*;


/**
 * DeleteParser is ...
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class DeleteParser implements XmlNodeParser<RundeckJobDelete> {
    private String xpath;

    public DeleteParser(String xpath) {
        this.xpath = xpath;
    }

    public DeleteParser() {
    }

    @Override
    public RundeckJobDelete parseXmlNode(Node node) {
        Node resultNode = xpath != null ? node.selectSingleNode(xpath) : node;

        final RundeckJobDelete delete = new RundeckJobDelete();
        delete.setError(StringUtils.trimToNull(resultNode.valueOf("error")));
        delete.setErrorCode(StringUtils.trimToNull(resultNode.valueOf("@errorCode")));
        delete.setId(StringUtils.trimToNull(resultNode.valueOf("@id")));
        delete.setMessage(StringUtils.trimToNull(resultNode.valueOf("message")));
        delete.setSuccessful(null == delete.getError() && null == delete.getErrorCode());

        return delete;
    }
}
