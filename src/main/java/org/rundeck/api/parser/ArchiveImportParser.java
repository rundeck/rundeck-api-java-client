package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.ArchiveImport;

import java.util.ArrayList;

/**
 * ArchiveImportParser is ...
 *
 * @author greg
 * @since 2014-03-09
 */
public class ArchiveImportParser implements XmlNodeParser<ArchiveImport> {
    String xpath;

    public ArchiveImportParser() {
    }

    public ArchiveImportParser(final String xpath) {
        this.xpath = xpath;
    }

    @Override
    public ArchiveImport parseXmlNode(final Node node) {
        final Node importNode = xpath != null ? node.selectSingleNode(xpath) : node;

        boolean issuccess = "successful".equals(importNode.valueOf("/import/@status"));
        final ArrayList<String> messages = new ArrayList<String>();
        for (final Object o : importNode.selectNodes("/import/errors/error")) {
            messages.add(((Node) o).getText());
        }

        return new ArchiveImport(issuccess, messages);
    }
}
