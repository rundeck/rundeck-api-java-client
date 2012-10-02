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
* DeleteParserTest.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 10/1/12 4:36 PM
* 
*/
package org.rundeck.api.parser;

import junit.framework.TestCase;
import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.domain.RundeckAbort;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckJobDelete;

import java.io.InputStream;
import java.util.*;


/**
 * DeleteParserTest is ...
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class DeleteParserTest  {
    @Test
    public void successfulResult() throws Exception {
        InputStream input = getClass().getResourceAsStream("delete-job-bulk-test1.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJobDelete deleteResult = new DeleteParser("result/deleteJobs/succeeded/deleteJobResult[1]").parseXmlNode(document);

        Assert.assertTrue(deleteResult.toString(), deleteResult.isSuccessful());
        Assert.assertNull(deleteResult.getError());
        Assert.assertNull(deleteResult.getErrorCode());
        Assert.assertEquals("test1", deleteResult.getId());
        Assert.assertEquals("Success", deleteResult.getMessage());
    }
    @Test
    public void failedResult1() throws Exception {
        InputStream input = getClass().getResourceAsStream("delete-job-bulk-test1.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJobDelete deleteResult = new DeleteParser("result/deleteJobs/failed/deleteJobResult[1]").parseXmlNode(document);


        Assert.assertFalse(deleteResult.isSuccessful());
        Assert.assertEquals("Failed", deleteResult.getError());
        Assert.assertEquals("failed", deleteResult.getErrorCode());
        Assert.assertEquals("test2", deleteResult.getId());
        Assert.assertNull(deleteResult.getMessage());
    }
    @Test
    public void failedResult2() throws Exception {
        InputStream input = getClass().getResourceAsStream("delete-job-bulk-test1.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJobDelete deleteResult = new DeleteParser("result/deleteJobs/failed/deleteJobResult[2]").parseXmlNode(document);


        Assert.assertFalse(deleteResult.isSuccessful());
        Assert.assertEquals("Unauthorized", deleteResult.getError());
        Assert.assertEquals("unauthorized", deleteResult.getErrorCode());
        Assert.assertEquals("test3", deleteResult.getId());
        Assert.assertNull(deleteResult.getMessage());
    }
    @Test
    public void failedResult3() throws Exception {
        InputStream input = getClass().getResourceAsStream("delete-job-bulk-test1.xml");
        Document document = ParserHelper.loadDocument(input);

        RundeckJobDelete deleteResult = new DeleteParser("result/deleteJobs/failed/deleteJobResult[3]").parseXmlNode(document);


        Assert.assertFalse(deleteResult.isSuccessful());
        Assert.assertEquals("Not found", deleteResult.getError());
        Assert.assertEquals("notfound", deleteResult.getErrorCode());
        Assert.assertEquals("test4", deleteResult.getId());
        Assert.assertNull(deleteResult.getMessage());
    }
}
