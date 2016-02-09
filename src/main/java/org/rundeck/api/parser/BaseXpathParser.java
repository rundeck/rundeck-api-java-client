package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;

/**
 * BaseXpathParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public abstract class BaseXpathParser<T> implements XmlNodeParser<T> {
    private String xpath;

    public BaseXpathParser() {
    }

    public BaseXpathParser(String xpath) {

        this.xpath = xpath;
    }

    public abstract T parse(Node node);

    @Override
    public final T parseXmlNode(Node node) {
        return parse(selectNode(node));
    }

    /**
     * Select appropriate node based on xpath, will automatically look for "result" top-level element and alter xpath if
     * necessary
     *
     * @param node
     *
     * @return
     */
    private Node selectNode(final Node node) {
        return selectNodeAndUnwrap(node, xpath);
    }

    /**
     * Select appropriate node based on xpath, will automatically look for "result" top-level element and alter xpath if
     * necessary
     * @param node
     * @param xpath
     * @return
     */
    public static Node selectNodeAndUnwrap(final Node node, final String xpath) {
        String useXpath = unwrapXpath(node, xpath);

        if (StringUtils.isNotEmpty(useXpath)) {
            return node.selectSingleNode(useXpath);
        } else {
            return node;
        }
    }

    /**
     * @param node
     * @param xpath
     * @return appropriate xpath to use based on whether node has a 'result' root element
     */
    public static String unwrapXpath(final Node node, final String xpath) {
        boolean rootResult = node.getName() == null && "result".equals(node.getDocument().getRootElement().getName());
        boolean isEmpty = StringUtils.isEmpty(xpath);
        String useXpath = xpath;
        if (isEmpty) {
            if (rootResult) {
                useXpath = "result/*";
            }
        } else if (rootResult) {
            if (!useXpath.startsWith("result") &&!useXpath.startsWith("/result") && !useXpath.startsWith("//")) {
                useXpath = "result/" + xpath;
            }
        } else if (useXpath.startsWith("result") ) {
            //starts with result/... but no result element
            useXpath = xpath.substring("result".length());
        }else if (useXpath.startsWith("/result")) {
            //starts with /result/... but no result element
            useXpath = xpath.substring("/result".length());
        }
        return useXpath;
    }
}
