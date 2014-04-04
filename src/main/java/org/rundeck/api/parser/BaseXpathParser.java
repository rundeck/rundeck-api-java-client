package org.rundeck.api.parser;

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
    public T parseXmlNode(Node node) {
        Node selectedNode = xpath != null ? node.selectSingleNode(xpath) : node;
        return parse(selectedNode);
    }
}
