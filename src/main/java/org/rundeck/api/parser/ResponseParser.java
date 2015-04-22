package org.rundeck.api.parser;

import java.io.InputStream;

/**
 * Parse inputstream to some content
 */
public interface ResponseParser<T> {

    /**
     * Parse the given content
     *
     * @param response input
     * @return any object holding the converted value
     */
    T parseResponse(InputStream response);
}
