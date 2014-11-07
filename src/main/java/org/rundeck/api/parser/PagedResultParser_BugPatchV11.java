package org.rundeck.api.parser;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.rundeck.api.util.PagedResults;

/**
 * WRaps a {@link PagedResultParser} to detect whether the result XML incorrectly is wrapped with a
 * &lt;result&gt; element.
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-11-07
 */
public class PagedResultParser_BugPatchV11<T> implements XmlNodeParser<PagedResults<T>> {
    PagedResultParser<T> parser;

    public PagedResultParser_BugPatchV11(final PagedResultParser<T> parser) {
        this.parser = parser;
    }

    @Override public PagedResults<T> parseXmlNode(final Node node) {
        Node sourceNode = node;
        final Node tested = sourceNode.selectSingleNode(parser.getXpath());
        if (null == tested && !parser.getXpath().startsWith("result")) {
            //prepend /result
            if (null != sourceNode.selectSingleNode("result" + parser.getXpath())) {
                sourceNode = sourceNode.selectSingleNode("result" + parser.getXpath());
                sourceNode.getParent().remove(sourceNode);
                DocumentHelper.createDocument((Element) sourceNode);
            }
        }
        return parser.parseXmlNode(sourceNode);
    }
}
