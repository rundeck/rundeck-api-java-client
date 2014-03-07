package org.rundeck.api.generator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;

/**
 * BaseDocGenerator generates a document using the element as the root.
 *
 * @author greg
 * @since 2014-02-27
 */
public abstract class BaseDocGenerator implements XmlDocumentGenerator {
    @Override
    public Document generateXmlDocument() {
        return DocumentFactory.getInstance().createDocument(generateXmlElement());
    }

}
