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
* BulkDeleteParser.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 10/1/12 3:55 PM
* 
*/
package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckJobDelete;
import org.rundeck.api.domain.RundeckJobDeleteBulk;

import java.util.*;


/**
 * BulkDeleteParser is ...
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class BulkDeleteParser extends BaseXpathParser<RundeckJobDeleteBulk> {


    public BulkDeleteParser(final String xpath) {
        super(xpath);
    }

    @Override
    public RundeckJobDeleteBulk parse(Node subnode) {
        final ArrayList<RundeckJobDelete> deletes = new ArrayList<RundeckJobDelete>();
        final List results = subnode.selectNodes("(succeeded|failed)/deleteJobResult");
        final DeleteParser parser = new DeleteParser();
        if (null != results && results.size() > 0) {
            for (final Object o : results) {
                deletes.add(parser.parseXmlNode((Node) o));
            }
        }

        final String requestcount = StringUtils.trimToNull(subnode.valueOf("@requestCount"));
        final String allsuccessString = StringUtils.trimToNull(subnode.valueOf("@allsuccessful"));
        int count = 0;
        boolean allsuccess = false;
        if (null != requestcount) {
            try {
                count = Integer.parseInt(requestcount);
            } catch (NumberFormatException e) {

            }
        }
        if (null != allsuccessString) {
            allsuccess = Boolean.parseBoolean(allsuccessString);
        }

        return new RundeckJobDeleteBulk(deletes, count, allsuccess);
    }
}
