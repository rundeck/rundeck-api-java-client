/*
 * Copyright 2011 Vincent Behar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rundeck.api;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.rundeck.api.generator.XmlDocumentGenerator;
import org.rundeck.api.util.ParametersUtil;

/**
 * Builder for API paths
 *
 * @author Vincent Behar
 */
class ApiPathBuilder {

    /** Internally, we store everything in a {@link StringBuilder} */
    private final StringBuilder apiPath;

    private String accept="text/xml";

    /** When POSTing, we can add attachments */
    private final Map<String, InputStream> attachments;
    private final List<NameValuePair> form = new ArrayList<NameValuePair>();
    private Document xmlDocument;
    private InputStream contentStream;
    private File contentFile;
    private String contentType;
    private String requiredContentType;
    private boolean emptyContent = false;

    /** Marker for using the right separator between parameters ("?" or "&") */
    private boolean firstParamDone = false;

    /**
     * Build a new instance, for the given "path" (the "path" is the part before the parameters. The path and the
     * parameters are separated by a "?")
     *
     * @param paths elements of the path
     */
    public ApiPathBuilder(String... paths) {
        apiPath = new StringBuilder();
        attachments = new HashMap<String, InputStream>();
        paths(paths);
    }

    public ApiPathBuilder paths(String... paths) {
        if (paths != null) {
            for (String path : paths) {
                if (StringUtils.isNotBlank(path)) {
                    append(path);
                }
            }
        }
        return this;
    }

    /**
     * Set the accept header
     */
    public ApiPathBuilder accept(String mimeTypes) {
        accept=mimeTypes;
        return this;
    }
    /**
     * Visit a {@link BuildsParameters} and add the parameters
     */
    public ApiPathBuilder param(BuildsParameters params) {
        params.buildParameters(this, false);
        return this;
    }
    /**
     * Visit a {@link BuildsParameters} and add the parameters
     */
    public ApiPathBuilder field(BuildsParameters params) {
        params.buildParameters(this, true);
        return this;
    }
    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not blank (null, empty
     * or whitespace), and make sure to add the right separator ("?" or "&") before. The key and value will be separated
     * by the "=" character. Also, the value will be url-encoded.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null/empty/blank. Will be url-encoded.
     * @return this, for method chaining
     */
    public ApiPathBuilder param(String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            appendSeparator();
            append(key);
            append("=");
            append(ParametersUtil.urlEncode(value));
        }
        return this;
    }

    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not blank (null, empty
     * or whitespace), and make sure to add the right separator ("?" or "&") before. The key and value will be separated
     * by the "=" character. Also, the value will be url-encoded.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null/empty/blank. Will be url-encoded.
     * @return this, for method chaining
     */
    public ApiPathBuilder param(final String key, final Collection<String> values) {
        for(final String value: values){
            if (StringUtils.isNotBlank(value)) {
                appendSeparator();
                append(key);
                append("=");
                append(ParametersUtil.urlEncode(value));
            }
        }
        return this;
    }

    /**
     * Append multiple values for the given Form field. This will be appended if it is not blank (null, empty
     * or whitespace).  The form field values will only be used for a "post" request
     *
     * @param key of the field name. Must not be null or empty
     * @param values of the field. May be null/empty/blank. Will be url-encoded.
     * @return this, for method chaining
     */
    public ApiPathBuilder field(final String key, final Collection<String> values) {
        if (null!=values) {
            for(final String value: values){
                if (StringUtils.isNotBlank(value)) {
                    form.add(new BasicNameValuePair(key, value));
                }
            }
        }
        return this;
    }

    /**
     * Append a single value for the given Form field. This will be appended if it is not blank (null, empty
     * or whitespace).  The form field values will only be used for a "post" request
     *
     * @param key of the field name. Must not be null or empty
     * @param value of the field. May be null/empty/blank. Will be url-encoded.
     * @return this, for method chaining
     */
    public ApiPathBuilder field(final String key, final String value) {
        if (StringUtils.isNotBlank(value)) {
            form.add(new BasicNameValuePair(key, value));
        }
        return this;
    }

    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not null, and make sure
     * to add the right separator ("?" or "&") before. The key and value will be separated by the "=" character. Also,
     * the value will be converted to lower-case.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null
     * @return this, for method chaining
     */
    public ApiPathBuilder param(String key, Enum<?> value) {
        if (value != null) {
            param(key, StringUtils.lowerCase(value.toString()));
        }
        return this;
    }

    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not null, and make sure
     * to add the right separator ("?" or "&") before. The key and value will be separated by the "=" character.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null
     * @return this, for method chaining
     */
    public ApiPathBuilder param(String key, Date value) {
        if (value != null) {
            param(key, value.getTime());
        }
        return this;
    }

    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not null, and make sure
     * to add the right separator ("?" or "&") before. The key and value will be separated by the "=" character.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null
     * @return this, for method chaining
     */
    public ApiPathBuilder param(String key, Long value) {
        if (value != null) {
            param(key, value.toString());
        }
        return this;
    }

    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not null, and make sure
     * to add the right separator ("?" or "&") before. The key and value will be separated by the "=" character.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null
     * @return this, for method chaining
     */
    public ApiPathBuilder param(String key, Integer value) {
        if (value != null) {
            param(key, value.toString());
        }
        return this;
    }

    /**
     * Append the given parameter (key and value). This will only append the parameter if it is not null, and make sure
     * to add the right separator ("?" or "&") before. The key and value will be separated by the "=" character.
     *
     * @param key of the parameter. Must not be null or empty
     * @param value of the parameter. May be null
     * @return this, for method chaining
     */
    public ApiPathBuilder param(String key, Boolean value) {
        if (value != null) {
            param(key, value.toString());
        }
        return this;
    }

    /**
     * Append the given node filters, only if it is not null/empty
     *
     * @param nodeFilters may be null/empty
     * @return this, for method chaining
     * @see ParametersUtil#generateNodeFiltersString(Properties)
     */
    public ApiPathBuilder nodeFilters(Properties nodeFilters) {
        String filters = ParametersUtil.generateNodeFiltersString(nodeFilters);
        if (StringUtils.isNotBlank(filters)) {
            appendSeparator();
            append(filters);
        }
        return this;
    }

    /**
     * When POSTing a request, add the given {@link InputStream} as an attachment to the content of the request. This
     * will only add the stream if it is not null.
     *
     * @param name of the attachment. Must not be null or empty
     * @param stream. May be null
     * @return this, for method chaining
     */
    public ApiPathBuilder attach(String name, InputStream stream) {
        if (stream != null) {
            attachments.put(name, stream);
        }
        return this;
    }
    /**
     * When POSTing a request, use the given {@link InputStream} as the content of the request. This
     * will only add the stream if it is not null.
     *
     * @param contentType MIME content type ofr hte request
     * @param stream content stream
     * @return this, for method chaining
     */
    public ApiPathBuilder content(final String contentType, final InputStream stream) {
        if (stream != null && contentType != null) {
            this.contentStream=stream;
            this.contentType=contentType;
        }
        return this;
    }
    /**
     * When POSTing a request, use the given {@link File} as the content of the request. This
     * will only add the stream if it is not null.
     *
     * @param contentType MIME content type ofr hte request
     * @param file content from a file
     * @return this, for method chaining
     */
    public ApiPathBuilder content(final String contentType, final File file) {
        if (file != null && contentType != null) {
            this.contentFile=file;
            this.contentType=contentType;
        }
        return this;
    }
    /**
     * When POSTing a request, send an empty request.
     *
     * @return this, for method chaining
     */
    public ApiPathBuilder emptyContent() {
        this.emptyContent=true;
        return this;
    }
    /**
     * When POSTing a request, add the given XMl Document as the content of the request.
     *
     * @param document XMl document to send
     * @return this, for method chaining
     */
    public ApiPathBuilder xml(final Document document) {
        if (document != null) {
            xmlDocument = document;
        }
        return this;
    }
    /**
     * When POSTing a request, add the given XMl Document as the content of the request.
     *
     * @param document XMl document to send
     * @return this, for method chaining
     */
    public ApiPathBuilder xml(final XmlDocumentGenerator document) {
        if (document != null) {
            xmlDocument = document.generateXmlDocument();
        }
        return this;
    }

    /**
     * @return all attachments to be POSTed, with their names
     */
    public Map<String, InputStream> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        return apiPath.toString();
    }

    /**
     * Append the given string
     *
     * @param str to append
     */
    private void append(String str) {
        apiPath.append(str);
    }

    /**
     * Append the right separator "?" or "&" between 2 parameters
     */
    private void appendSeparator() {
        if (firstParamDone) {
            append("&");
        } else {
            append("?");
            firstParamDone = true;
        }
    }

    /**
     * Form fields for POST request
     */
    public List<NameValuePair> getForm() {
        return form;
    }

    /**
     * Return true if there are any Attachments or Form data for a POST request.
     */
    public boolean hasPostContent() {
        return getAttachments().size() > 0 || getForm().size() > 0 || null != xmlDocument;
    }

    /**
     * Accept header value, default "text/xml"
     */
    public String getAccept() {
        return accept;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public InputStream getContentStream() {
        return contentStream;
    }

    public String getContentType() {
        return contentType;
    }

    public File getContentFile() {
        return contentFile;
    }

    public boolean isEmptyContent() {
        return emptyContent;
    }

    public ApiPathBuilder requireContentType(String contentType) {
        this.requiredContentType=contentType;
        return this;
    }

    public String getRequiredContentType() {
        return requiredContentType;
    }

    /**
     * BuildsParameters can add URL or POST parameters to an {@link ApiPathBuilder}
     *
     * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
     */
    public static interface BuildsParameters {
        /**
         * Add the parameters or form fields to the ApiPathBuilder
         *
         * @param builder the builder
         * @param doPost  if true, use form fields, otherwise use query parameters
         */
        public boolean buildParameters(ApiPathBuilder builder, boolean doPost);
    }
}
