package org.rundeck.api.parser;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Utility to handle API v11 responses with &lt;result&gt; wrapper element.
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-11-10
 */
public class APIV11Helper {

    /**
     * Detect and remove extra &lt;result&gt; wrapper around xml response.
     * @param parser
     * @param xpath
     * @param <T>
     * @return
     */
    public static <T> XmlNodeParser<T> unwrapIfNeeded(
            final XmlNodeParser<T> parser,
            final String xpath
    ) {
        return new NodeParser_unwrap<T>(parser, xpath);
    }

    static class NodeParser_unwrap<T> implements XmlNodeParser<T> {
        XmlNodeParser<T> parser;
        String           xpath;

        public NodeParser_unwrap(final XmlNodeParser<T> parser, final String xpath) {
            this.parser = parser;
            this.xpath = xpath;
        }

        @Override public T parseXmlNode(final Node node) {

            Node sourceNode = unwrapResultElement(node, xpath);
            return parser.parseXmlNode(sourceNode);
        }
    }

    /**
     * Test the node for matching the xpath string, if it doesnt match, attempt to prefix it with
     * "result" and match that. If that matches, return the first child of the 'result' element.
     *
     * @param node
     * @param xpath
     *
     * @return
     */
    public static Node unwrapResultElement(final Node node, final String xpath) {
        Node sourceNode = node;
        final Node tested = sourceNode.selectSingleNode(xpath);
        if (null == tested && !xpath.startsWith("result")) {
            //prepend /result
            if (null != sourceNode.selectSingleNode("result" + xpath)) {
                Node resultNode = sourceNode.selectSingleNode("result");
                if (resultNode instanceof Element) {
                    Element result = (Element) resultNode;
                    if (result.elements().size() == 1) {

                        Node node1 = (Node) result.elements().get(0);
                        if (node1 instanceof Element) {
                            sourceNode = node1;

                            sourceNode.getParent().remove(sourceNode);
                            DocumentHelper.createDocument((Element) sourceNode);
                        }
                    }
                }
            }
        }
        return sourceNode;
    }
}
