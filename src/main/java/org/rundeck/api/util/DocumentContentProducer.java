package org.rundeck.api.util;

import org.apache.http.entity.ContentProducer;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * DocumentContentProducer writes XML document to a stream
 *
 * @author greg
 * @since 2014-02-27
 */
public class DocumentContentProducer implements ContentProducer {
    Document document;
    private OutputFormat format;

    public DocumentContentProducer(final Document document, final OutputFormat format) {
        this.document = document;
        this.format = format;
    }

    public DocumentContentProducer(final Document document) {
        this.document = document;
        format = new OutputFormat("", false, "UTF-8");
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {

        final XMLWriter xmlWriter = new XMLWriter(outstream, format);
        xmlWriter.write(document);
        xmlWriter.flush();
    }
}
