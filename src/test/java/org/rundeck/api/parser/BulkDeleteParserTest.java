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
* BulkDeleteParserTest.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 10/1/12 5:03 PM
* 
*/
package org.rundeck.api.parser;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckJobDeleteBulk;

import java.io.InputStream;
import java.util.*;


/**
 * BulkDeleteParserTest is ...
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class BulkDeleteParserTest {
    @Test
    public void bulkParser() throws Exception{
        InputStream input = getClass().getResourceAsStream("delete-job-bulk-test1.xml");
        Document document = ParserHelper.loadDocument(input);
        final RundeckJobDeleteBulk deletes = new BulkDeleteParser("result/deleteJobs").parseXmlNode(document);
        Assert.assertEquals(14, deletes.getRequestCount());
        Assert.assertEquals(4, deletes.getResults().size());
        Assert.assertFalse(deletes.isAllsuccessful());
    }
    @Test
    public void bulkParserAllsuccessful() throws Exception{
        InputStream input = getClass().getResourceAsStream("delete-job-bulk-test2.xml");
        Document document = ParserHelper.loadDocument(input);
        final RundeckJobDeleteBulk deletes = new BulkDeleteParser("result/deleteJobs").parseXmlNode(document);
        Assert.assertEquals(2, deletes.getRequestCount());
        Assert.assertEquals(2, deletes.getResults().size());
        Assert.assertTrue(deletes.isAllsuccessful());
    }
}
