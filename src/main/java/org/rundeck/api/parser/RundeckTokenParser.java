package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.RundeckToken;

/**
 * RundeckTokenParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class RundeckTokenParser extends BaseXpathParser<RundeckToken> {


    public RundeckTokenParser(final String xpath) {
        super(xpath);
    }

    public RundeckTokenParser() {

    }

    @Override
    public RundeckToken parse(Node targetNode) {
        RundeckToken rundeckToken = new RundeckToken();
        String token = targetNode.valueOf("@id");
        String user = targetNode.valueOf("@user");
        rundeckToken.setToken(token);
        rundeckToken.setUser(user);

        return rundeckToken;
    }
}
