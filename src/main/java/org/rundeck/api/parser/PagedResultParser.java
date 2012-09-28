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
* PagedResultParser.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 9/28/12 10:37 AM
* 
*/
package org.rundeck.api.parser;

import org.dom4j.Element;
import org.dom4j.Node;
import org.rundeck.api.util.PagedResults;

import java.util.*;


/**
 * PagedResultParser extracts paging data from an xpath node, and includes the List result from a ListParser
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class PagedResultParser<T> implements XmlNodeParser<PagedResults<T>> {
    ListParser<T> itemParser;
    String xpath;

    /**
     * Create a PagedResultParser
     *
     * @param itemParser the list parser
     * @param xpath      xpath for list container containing paging attributes
     */
    public PagedResultParser(ListParser<T> itemParser, String xpath) {
        this.itemParser = itemParser;
        this.xpath = xpath;
    }

    @Override
    public PagedResults<T> parseXmlNode(Node node) {
        Node pagedNodeContainer = node.selectSingleNode(xpath);

        Element el = (Element) pagedNodeContainer;
        final int max = integerAttribute(el, "max", -1);
        final int offset = integerAttribute(el, "offset", -1);
        final int total = integerAttribute(el, "total", -1);
        final int count = integerAttribute(el, "count", -1);

        final List<T> ts = itemParser.parseXmlNode(pagedNodeContainer);


        return new PagedResults<T>() {
            @Override
            public int getMax() {
                return max;
            }

            @Override
            public int getOffset() {
                return offset;
            }

            @Override
            public int getTotal() {
                return total;
            }

            @Override
            public List<T> getResults() {
                return ts;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public Iterator<T> iterator() {
                return ts.iterator();
            }
        };
    }

    /**
     * Return an integer value of an attribute of an element, or a default value if it is not found or not an integer.
     *
     * @param el        the element
     * @param attribute attribute name
     * @param defValue  default value to return
     */
    private int integerAttribute(Element el, final String attribute, final int defValue) {
        int parseMax = defValue;
        try {
            final String max1 = null != el.attribute(attribute) ? el.attribute(attribute).getStringValue() : null;
            parseMax = null != max1 ? Integer.parseInt(max1) : defValue;
        } catch (NumberFormatException e) {
        }
        return parseMax;
    }
}
