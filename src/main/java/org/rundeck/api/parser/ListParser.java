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

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Node;

/**
 * Parser for a {@link List} of elements
 * 
 * @author Vincent Behar
 */
public class ListParser<T> implements XmlNodeParser<List<T>> {

    private final XmlNodeParser<T> parser;

    private final String xpath;

    /**
     * @param parser for an individual element
     * @param xpath of the elements
     */
    public ListParser(XmlNodeParser<T> parser, String xpath) {
        super();
        this.parser = parser;
        this.xpath = xpath;
    }

    @Override
    public List<T> parseXmlNode(Node node) {
        List<T> elements = new ArrayList<T>();

        @SuppressWarnings("unchecked")
        List<Node> elementNodes = node.selectNodes(BaseXpathParser.unwrapXpath(node, xpath));

        for (Node elementNode : elementNodes) {
            T element = parser.parseXmlNode(elementNode);
            elements.add(element);
        }

        return elements;
    }

}
