package org.rundeck.api.generator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * XmlDocumentGenerator is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public interface XmlDocumentGenerator {

    /**
     * Generate the XML {@link org.dom4j.Node}
     *
     * @return any object holding the converted value
     */
    Element generateXmlElement();
    /**
     * Generate the XML {@link org.dom4j.Node}
     *
     * @param node
     *
     * @return any object holding the converted value
     */
    Document generateXmlDocument();
}
