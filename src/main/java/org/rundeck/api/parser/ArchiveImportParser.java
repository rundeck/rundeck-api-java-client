package org.rundeck.api.parser;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.ArchiveImport;

import java.util.ArrayList;

/**
 * ArchiveImportParser is ...
 *
 * @author greg
 * @since 2014-03-09
 */
public class ArchiveImportParser extends BaseXpathParser<ArchiveImport> {


    @Override
    public ArchiveImport parse(final Node importNode) {

        boolean issuccess = "successful".equals(importNode.valueOf("/import/@status"));
        final ArrayList<String> messages = new ArrayList<String>();
        for (final Object o : importNode.selectNodes("/import/errors/error")) {
            messages.add(((Node) o).getText());
        }

        return new ArchiveImport(issuccess, messages);
    }
}
