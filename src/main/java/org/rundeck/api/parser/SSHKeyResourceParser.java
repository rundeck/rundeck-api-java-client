package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.RundeckClient;
import org.rundeck.api.domain.BaseSSHKeyResource;
import org.rundeck.api.domain.SSHKeyResource;

/**
 * SSHKeyResourceParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class SSHKeyResourceParser extends BaseXpathParser<SSHKeyResource> implements XmlNodeParser<SSHKeyResource> {
    public SSHKeyResourceParser() {
    }

    public SSHKeyResourceParser(String xpath) {
        super(xpath);
    }

    @Override
    public SSHKeyResource parse(Node node) {
        return BaseSSHKeyResource.from(new StorageResourceParser().parse(node));
    }
}
