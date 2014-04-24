package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.RundeckToken;

/**
 * RundeckTokenParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class RundeckTokenParser implements XmlNodeParser<RundeckToken> {
    String xpath;

    public RundeckTokenParser() {
    }

    public RundeckTokenParser(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public RundeckToken parseXmlNode(Node node) {
        Node targetNode = xpath != null ? node.selectSingleNode(xpath) : node;
        RundeckToken rundeckToken = new RundeckToken();
        String token = targetNode.valueOf("@id");
        String user = targetNode.valueOf("@user");
        rundeckToken.setToken(token);
        rundeckToken.setUser(user);

        return rundeckToken;
    }
}
