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

import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.rundeck.api.RundeckApiException;

/**
 * Helper for parsing Rundeck responses
 * 
 * @author Vincent Behar
 */
public class ParserHelper {

    /**
     * Load an XML {@link Document} from the given {@link InputStream}
     * 
     * @param inputStream from an API call to Rundeck
     * @return an XML {@link Document}
     * @throws RundeckApiException if we failed to read the response, or if the response is an error
     */
    public static Document loadDocument(InputStream inputStream) throws RundeckApiException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");

        Document document;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            throw new RundeckApiException("Failed to read Rundeck response", e);
        }
        document.setXMLEncoding("UTF-8");

        Node result = document.selectSingleNode("result");
        if (result != null) {
            Boolean failure = Boolean.valueOf(result.valueOf("@error"));
            if (failure) {
                throw new RundeckApiException(result.valueOf("error/message"));
            }
        }

        return document;
    }

}
