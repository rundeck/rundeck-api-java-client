package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.BaseKeyResource;
import org.rundeck.api.domain.KeyResource;

/**
 * SSHKeyResourceParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class SSHKeyResourceParser extends BaseXpathParser<KeyResource> implements XmlNodeParser<KeyResource> {
    public SSHKeyResourceParser() {
    }

    public SSHKeyResourceParser(String xpath) {
        super(xpath);
    }

    @Override
    public KeyResource parse(Node node) {
        return BaseKeyResource.from(new StorageResourceParser().parse(node));
    }
}
