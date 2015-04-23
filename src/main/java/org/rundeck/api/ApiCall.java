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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.rundeck.api.RundeckApiException.RundeckApiLoginException;
import org.rundeck.api.RundeckApiException.RundeckApiTokenException;
import org.rundeck.api.parser.ParserHelper;
import org.rundeck.api.parser.ResponseParser;
import org.rundeck.api.parser.XmlNodeParser;
import org.rundeck.api.util.AssertUtil;
import org.rundeck.api.util.DocumentContentProducer;

import java.io.*;
import java.net.ProxySelector;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Class responsible for making the HTTP API calls
 *
 * @author Vincent Behar
 */
class ApiCall {

    /** Rundeck HTTP header for the auth-token (in case of token-based authentication) */
    private static final transient String AUTH_TOKEN_HEADER = "X-Rundeck-Auth-Token";

    /** Rundeck HTTP header for the setting session cookie (in case of session-based authentication) */
    private static final transient String COOKIE_HEADER = "Cookie";

    /** {@link RundeckClient} instance holding the Rundeck url and the credentials */
    private final RundeckClient client;

    /**
     * Build a new instance, linked to the given Rundeck client
     *
     * @param client holding the Rundeck url and the credentials
     * @throws IllegalArgumentException if client is null
     */
    public ApiCall(RundeckClient client) throws IllegalArgumentException {
        super();
        this.client = client;
        AssertUtil.notNull(client, "The Rundeck Client must not be null !");
    }

    /**
     * Try to "ping" the Rundeck instance to see if it is alive
     *
     * @throws RundeckApiException if the ping fails
     */
    public void ping() throws RundeckApiException {
        CloseableHttpClient httpClient = instantiateHttpClient();
        try {
            HttpResponse response = httpClient.execute(new HttpGet(client.getUrl()));
            if (response.getStatusLine().getStatusCode() / 100 != 2) {
                throw new RundeckApiException("Invalid HTTP response '" + response.getStatusLine() + "' when pinging "
                                              + client.getUrl());
            }
        } catch (IOException e) {
            throw new RundeckApiException("Failed to ping Rundeck instance at " + client.getUrl(), e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Test the authentication on the Rundeck instance. Will delegate to either {@link #testLoginAuth()} (in case of
     * login-based auth) or {@link #testTokenAuth()} (in case of token-based auth).
     *
     * @return the login session ID if using login-based auth, otherwise null
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     * @see #testLoginAuth()
     * @see #testTokenAuth()
     */
    public String testAuth() throws RundeckApiLoginException, RundeckApiTokenException {
        String sessionID = null;
        if (client.getToken() != null || client.getSessionID() != null) {
            testTokenAuth();
        } else {
            sessionID = testLoginAuth();
        }
        return sessionID;
    }

    /**
     * Test the login-based authentication on the Rundeck instance
     *
     * @throws RundeckApiLoginException if the login fails
     * @see #testAuth()
     */
    public String testLoginAuth() throws RundeckApiLoginException {
        String sessionID = null;
        try (CloseableHttpClient httpClient = instantiateHttpClient()){
            sessionID = login(httpClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionID;
    }

    /**
     * Test the token-based authentication on the Rundeck instance
     *
     * @throws RundeckApiTokenException if the token is invalid
     * @see #testAuth()
     */
    public void testTokenAuth() throws RundeckApiTokenException {
        try {
            execute(new HttpGet(client.getUrl() + client.getApiEndpoint() + "/system/info"));
        } catch (RundeckApiTokenException e) {
            throw e;
        } catch (RundeckApiException e) {
            throw new RundeckApiTokenException("Failed to verify token", e);
        }
    }

    /**
     * Execute an HTTP GET request to the Rundeck instance, on the given path. We will login first, and then execute the
     * API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T get(ApiPathBuilder apiPath, XmlNodeParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        HttpGet request = new HttpGet(client.getUrl() + client.getApiEndpoint() + apiPath);
        if (null != apiPath.getAccept()) {
            request.setHeader("Accept", apiPath.getAccept());
        }
        return execute(request, parser);
    }


    /**
     * Execute an HTTP GET request to the Rundeck instance, on the given path. We will login first, and then execute the
     * API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T get(ApiPathBuilder apiPath, ResponseParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        HttpGet request = new HttpGet(client.getUrl() + client.getApiEndpoint() + apiPath);
        if (null != apiPath.getAccept()) {
            request.setHeader("Accept", apiPath.getAccept());
        }
        return execute(request, new ContentHandler<T>(parser));
    }

    /**
     * Execute an HTTP GET request to the Rundeck instance, on the given path. We will login first, and then execute the
     * API call.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @return a new {@link InputStream} instance, not linked with network resources
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public InputStream get(ApiPathBuilder apiPath, boolean parseXml) throws RundeckApiException, RundeckApiLoginException,
            RundeckApiTokenException {
        HttpGet request = new HttpGet(client.getUrl() + client.getApiEndpoint() + apiPath);
        if (null != apiPath.getAccept()) {
            request.setHeader("Accept", apiPath.getAccept());
        }
        ByteArrayInputStream response = execute(request);

        // try to load the document, to throw an exception in case of error
        if(parseXml) {
            ParserHelper.loadDocument(response);
            response.reset();
        }

        return response;
    }

    /**
     * Execute an HTTP GET request to the Rundeck instance, on the given path. We will login first, and then execute the
     * API call without appending the API_ENDPOINT to the URL.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @return a new {@link InputStream} instance, not linked with network resources
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public InputStream getNonApi(ApiPathBuilder apiPath) throws RundeckApiException, RundeckApiLoginException,
            RundeckApiTokenException {
        HttpGet request = new HttpGet(client.getUrl() + apiPath);
        if (null != apiPath.getAccept()) {
            request.setHeader("Accept", apiPath.getAccept());
        }
        ByteArrayInputStream response = execute(request);
        response.reset();

        return response;
    }

    /**
     * Execute an HTTP POST or GET request to the Rundeck instance, on the given path, depend ing of the {@link
     * ApiPathBuilder} contains POST content or not (attachments or Form data). We will login first, and then execute
     * the API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser  used to parse the response
     *
     * @return the result of the call, as formatted by the parser
     *
     * @throws RundeckApiException      in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T postOrGet(ApiPathBuilder apiPath, XmlNodeParser<T> parser) throws RundeckApiException,
                                                                                   RundeckApiLoginException,
                                                                                   RundeckApiTokenException {
        if (apiPath.hasPostContent()) {
            return post(apiPath, parser);
        } else {
            return get(apiPath, parser);
        }
    }

    /**
     * Execute an HTTP POST request to the Rundeck instance, on the given path. We will login first, and then execute
     * the API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T post(ApiPathBuilder apiPath, XmlNodeParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        HttpPost httpPost = new HttpPost(client.getUrl() + client.getApiEndpoint() + apiPath);
        return requestWithEntity(apiPath, parser, httpPost);
    }
    /**
     * Execute an HTTP PUT request to the Rundeck instance, on the given path. We will login first, and then execute
     * the API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T put(ApiPathBuilder apiPath, XmlNodeParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        HttpPut httpPut = new HttpPut(client.getUrl() + client.getApiEndpoint() + apiPath);
        return requestWithEntity(apiPath, parser, httpPut);
    }

    /**
     * Execute an HTTP PUT request to the Rundeck instance, on the given path. We will login first, and then execute
     * the API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T put(ApiPathBuilder apiPath, ResponseParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        HttpPut httpPut = new HttpPut(client.getUrl() + client.getApiEndpoint() + apiPath);
        return requestWithEntity(apiPath, new ContentHandler<T>(parser), httpPut);
    }
    private <T> T requestWithEntity(ApiPathBuilder apiPath, XmlNodeParser<T> parser, HttpEntityEnclosingRequestBase
            httpPost) {
        return new ParserHandler<T>(parser).handle(requestWithEntity(apiPath, new ResultHandler(), httpPost));
    }
    private <T> T requestWithEntity(ApiPathBuilder apiPath, Handler<HttpResponse,T> handler, HttpEntityEnclosingRequestBase
            httpPost) {
        if(null!= apiPath.getAccept()) {
            httpPost.setHeader("Accept", apiPath.getAccept());
        }
        // POST a multi-part request, with all attachments
        if (apiPath.getAttachments().size() > 0) {
            MultipartEntityBuilder multipartEntityBuilder =
                    MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (Entry<String, InputStream> attachment : apiPath.getAttachments().entrySet()) {
                multipartEntityBuilder.addPart(
                        attachment.getKey(),
                        new InputStreamBody(attachment.getValue(), attachment.getKey())
                );
            }
            httpPost.setEntity(multipartEntityBuilder.build());
        } else if (apiPath.getForm().size() > 0) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(apiPath.getForm(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RundeckApiException("Unsupported encoding: " + e.getMessage(), e);
            }
        } else if (apiPath.getContentStream() != null && apiPath.getContentType() != null) {
            InputStreamEntity entity = new InputStreamEntity(
                    apiPath.getContentStream(),
                    ContentType.create(apiPath.getContentType())
            );
            httpPost.setEntity(entity);
        } else if (apiPath.getContents() != null && apiPath.getContentType() != null) {
            ByteArrayEntity bae = new ByteArrayEntity(
                    apiPath.getContents(),
                    ContentType.create(apiPath.getContentType())
            );

            httpPost.setEntity(bae);
        } else if (apiPath.getContentFile() != null && apiPath.getContentType() != null) {
            httpPost.setEntity(new FileEntity(apiPath.getContentFile(), ContentType.create(apiPath.getContentType())));
        } else if (apiPath.getXmlDocument() != null) {
            httpPost.setHeader("Content-Type", "application/xml");
            httpPost.setEntity(new EntityTemplate(new DocumentContentProducer(apiPath.getXmlDocument())));
        } else if (apiPath.isEmptyContent()) {
            //empty content
        } else {
            throw new IllegalArgumentException("No Form or Multipart entity for POST content-body");
        }

        return execute(httpPost, handler);
    }

    /**
     * Execute an HTTP DELETE request to the Rundeck instance, on the given path. We will login first, and then execute
     * the API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public <T> T delete(ApiPathBuilder apiPath, XmlNodeParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        return execute(new HttpDelete(client.getUrl() + client.getApiEndpoint() + apiPath), parser);
    }
    /**
     * Execute an HTTP DELETE request to the Rundeck instance, on the given path, and expect a 204 response.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public void delete(ApiPathBuilder apiPath) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {

        InputStream response = execute(new HttpDelete(client.getUrl() + client.getApiEndpoint() + apiPath));
        if(null!=response){
            throw new RundeckApiException("Unexpected Rundeck response content, expected no content!");
        }
    }

    /**
     * Execute an HTTP request to the Rundeck instance. We will login first, and then execute the API call. At the end,
     * the given parser will be used to convert the response to a more useful result object.
     *
     * @param request to execute. see {@link HttpGet}, {@link HttpDelete}, and so on...
     * @param parser used to parse the response
     * @return the result of the call, as formatted by the parser
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    private <T> T execute(HttpRequestBase request, XmlNodeParser<T> parser) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException {
        // execute the request
        return new ParserHandler<T>(parser).handle(execute(request, new ResultHandler()));
    }

    /**
     * Execute an HTTP GET request to the Rundeck instance, on the given path. We will login first, and then execute the
     * API call. At the end, the given parser will be used to convert the response to a more useful result object.
     *
     * @param apiPath on which we will make the HTTP request - see {@link ApiPathBuilder}
     * @param outputStream write output to this stream
     *
     * @return the result of the call, as formatted by the parser
     *
     * @throws RundeckApiException      in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    public int get(ApiPathBuilder apiPath, OutputStream outputStream) throws RundeckApiException,
            RundeckApiLoginException, RundeckApiTokenException, IOException {
        HttpGet request = new HttpGet(client.getUrl() + client.getApiEndpoint() + apiPath);
        if (null != apiPath.getAccept()) {
            request.setHeader("Accept", apiPath.getAccept());
        }
        final WriteOutHandler writeOutHandler = new WriteOutHandler(outputStream);
        Handler<HttpResponse,Integer> handler = writeOutHandler;
        if(null!=apiPath.getRequiredContentType()){
            handler = new RequireContentTypeHandler<Integer>(apiPath.getRequiredContentType(), handler);
        }
        final int wrote = execute(request, handler);
        if(writeOutHandler.thrown!=null){
            throw writeOutHandler.thrown;
        }
        return wrote;
    }
    /**
     * Execute an HTTP request to the Rundeck instance. We will login first, and then execute the API call.
     *
     * @param request to execute. see {@link HttpGet}, {@link HttpDelete}, and so on...
     * @return a new {@link InputStream} instance, not linked with network resources
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    private ByteArrayInputStream execute(HttpUriRequest request) throws RundeckApiException, RundeckApiLoginException,
            RundeckApiTokenException {
        return execute(request, new ResultHandler() );
    }

    /**
     * Handles one type into another
     * @param <T>
     * @param <V>
     */
    private static interface Handler<T,V>{
        public V handle(T response);
    }

    /**
     * Handles parsing inputstream via a parser
     * @param <S>
     */
    private static class ParserHandler<S> implements Handler<InputStream,S> {
        XmlNodeParser<S> parser;

        private ParserHandler(XmlNodeParser<S> parser) {
            this.parser = parser;
        }

        @Override
        public S handle(InputStream response) {
            // read and parse the response
            return parser.parseXmlNode(ParserHelper.loadDocument(response));
        }
    }

    /**
     * Converts to a string
     */
    public static class PlainTextHandler implements ResponseParser<String>{
        @Override
        public String parseResponse(final InputStream response) {
            StringWriter output = new StringWriter();
            try {
                IOUtils.copy(response, output);
            } catch (IOException e) {
                throw new RundeckApiException("Failed to consume text/plain input to string", e);
            }
            return output.toString();
        }
    }

    /**
     * Handles parsing response via a {@link ResponseParser}
     *
     * @param <S>
     */
    private static class ContentHandler<S> implements Handler<HttpResponse, S> {
        ResponseParser<S> parser;

        private ContentHandler(ResponseParser<S> parser) {
            this.parser = parser;
        }

        @Override
        public S handle(HttpResponse response) {
            // read and parse the response
            return parser.parseResponse(new ResultHandler().handle(response));
        }
    }

    /**
     * Handles writing response to an output stream
     */
    private static class ChainHandler<T> implements Handler<HttpResponse,T> {
        Handler<HttpResponse, T> chain;
        private ChainHandler(Handler<HttpResponse,T> chain) {
            this.chain=chain;
        }
        @Override
        public T handle(final HttpResponse response) {
            return chain.handle(response);
        }
    }

    /**
     * Handles writing response to an output stream
     */
    private static class RequireContentTypeHandler<T> extends ChainHandler<T> {
        String contentType;

        private RequireContentTypeHandler(final String contentType, final Handler<HttpResponse, T> chain) {
            super(chain);
            this.contentType = contentType;
        }

        @Override
        public T handle(final HttpResponse response) {
            final Header firstHeader = response.getFirstHeader("Content-Type");
            final String[] split = firstHeader.getValue().split(";");
            boolean matched=false;
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (this.contentType.equalsIgnoreCase(s.trim())) {
                    matched=true;
                    break;
                }
            }
            if(!matched) {
                throw new RundeckApiException.RundeckApiHttpContentTypeException(firstHeader.getValue(),
                        this.contentType);
            }
            return super.handle(response);
        }
    }

    /**
     * Handles writing response to an output stream
     */
    private static class WriteOutHandler implements Handler<HttpResponse,Integer> {
        private WriteOutHandler(OutputStream writeOut) {
            this.writeOut = writeOut;
        }

        OutputStream writeOut;
        IOException thrown;
        @Override
        public Integer handle(final HttpResponse response) {
            try {
                return IOUtils.copy(response.getEntity().getContent(), writeOut);
            } catch (IOException e) {
                thrown=e;
            }
            return -1;
        }
    }

    /**
     * Handles reading response into a byte array stream
     */
    private static class ResultHandler implements Handler<HttpResponse,ByteArrayInputStream> {
        @Override
        public ByteArrayInputStream handle(final HttpResponse response) {
            // return a new inputStream, so that we can close all network resources
            try {
                return new ByteArrayInputStream(EntityUtils.toByteArray(response.getEntity()));
            } catch (IOException e) {
                throw new RundeckApiException("Failed to consume entity and convert the inputStream", e);
            }
        }
    }
    /**
     * Execute an HTTP request to the Rundeck instance. We will login first, and then execute the API call.
     *
     * @param request to execute. see {@link HttpGet}, {@link HttpDelete}, and so on...
     * @return a new {@link InputStream} instance, not linked with network resources
     * @throws RundeckApiException in case of error when calling the API
     * @throws RundeckApiLoginException if the login fails (in case of login-based authentication)
     * @throws RundeckApiTokenException if the token is invalid (in case of token-based authentication)
     */
    private <T> T execute(HttpUriRequest request, Handler<HttpResponse,T> handler) throws RundeckApiException,
            RundeckApiLoginException,
            RundeckApiTokenException {
        try(CloseableHttpClient httpClient = instantiateHttpClient()) {
            // we only need to manually login in case of login-based authentication
            // note that in case of token-based auth, the auth (via an HTTP header) is managed by an interceptor.
            if (client.getToken() == null && client.getSessionID() == null) {
                login(httpClient);
            }

            // execute the HTTP request
            HttpResponse response = null;
            try {
                response = httpClient.execute(request);
            } catch (IOException e) {
                throw new RundeckApiException("Failed to execute an HTTP " + request.getMethod() + " on url : "
                                              + request.getURI(), e);
            }

            // in case of error, we get a redirect to /api/error
            // that we need to follow manually for POST and DELETE requests (as GET)
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode / 100 == 3) {
                String newLocation = response.getFirstHeader("Location").getValue();
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    throw new RundeckApiException("Failed to consume entity (release connection)", e);
                }
                request = new HttpGet(newLocation);
                try {
                    response = httpClient.execute(request);
                    statusCode = response.getStatusLine().getStatusCode();
                } catch (IOException e) {
                    throw new RundeckApiException("Failed to execute an HTTP GET on url : " + request.getURI(), e);
                }
            }

            // check the response code (should be 2xx, even in case of error : error message is in the XML result)
            if (statusCode / 100 != 2) {
                if (statusCode == 403 &&
                        (client.getToken() != null || client.getSessionID() != null)) {
                    throw new RundeckApiTokenException("Invalid Token or sessionID ! Got HTTP response '" + response.getStatusLine()
                                                       + "' for " + request.getURI());
                } else {
                    throw new RundeckApiException.RundeckApiHttpStatusException("Invalid HTTP response '" + response.getStatusLine() + "' for "
                                                  + request.getURI(), statusCode);
                }
            }
            if(statusCode==204){
                return null;
            }
            if (response.getEntity() == null) {
                throw new RundeckApiException("Empty Rundeck response ! HTTP status line is : "
                                              + response.getStatusLine());
            }
            return handler.handle(response);
        } catch (IOException e) {
            throw new RundeckApiException("failed closing http client", e);
        }
    }

    /**
     * Do the actual work of login, using the given {@link HttpClient} instance. You'll need to re-use this instance
     * when making API calls (such as running a job). Only use this in case of login-based authentication.
     *
     * @param httpClient pre-instantiated
     * @throws RundeckApiLoginException if the login failed
     */
    private String login(HttpClient httpClient) throws RundeckApiLoginException {
        String sessionID = null;

        // 1. call expected GET request
        String location = client.getUrl();

        try {
            HttpGet getRequest = new HttpGet(location);
            HttpResponse response = httpClient.execute(getRequest);

            // sessionID stored in case user wants to cache it for reuse
            Header cookieHeader = response.getFirstHeader("Set-Cookie");
            if (cookieHeader != null) {
                String cookieStr = cookieHeader.getValue();
                if (cookieStr != null) {
                    int i1 = cookieStr.indexOf("JSESSIONID=");
                    if (i1 >= 0) {
                        cookieStr = cookieStr.substring(i1 + "JSESSIONID=".length());
                        int i2 = cookieStr.indexOf(";");
                        if (i2 >= 0) {
                            sessionID = cookieStr.substring(0, i2);
                        }
                    }
                }
            }

            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                throw new RundeckApiLoginException("Failed to consume entity (release connection)", e);
            }
        } catch (IOException e) {
            throw new RundeckApiLoginException("Failed to get request on " + location, e);
        }

        // 2. then call POST login request
        location += "/j_security_check";

        while (true) {
            try {
                HttpPost postLogin = new HttpPost(location);
                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("j_username", client.getLogin()));
                params.add(new BasicNameValuePair("j_password", client.getPassword()));
                params.add(new BasicNameValuePair("action", "login"));
                postLogin.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
                HttpResponse response = httpClient.execute(postLogin);

                if (response.getStatusLine().getStatusCode() / 100 == 3) {
                    // HTTP client refuses to handle redirects (code 3xx) for POST, so we have to do it manually...
                    location = response.getFirstHeader("Location").getValue();
                    try {
                        EntityUtils.consume(response.getEntity());
                    } catch (IOException e) {
                        throw new RundeckApiLoginException("Failed to consume entity (release connection)", e);
                    }
                    continue;
                }

                if (response.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new RundeckApiLoginException("Invalid HTTP response '" + response.getStatusLine() + "' for "
                            + location);
                }

                try {
                    String content = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                    if (StringUtils.contains(content, "j_security_check")) {
                        throw new RundeckApiLoginException("Login failed for user " + client.getLogin());
                    }
                    try {
                        EntityUtils.consume(response.getEntity());
                    } catch (IOException e) {
                        throw new RundeckApiLoginException("Failed to consume entity (release connection)", e);
                    }
                    break;
                } catch (IOException io) {
                    throw new RundeckApiLoginException("Failed to read Rundeck result", io);
                } catch (ParseException p) {
                    throw new RundeckApiLoginException("Failed to parse Rundeck response", p);
                }
            } catch (IOException e) {
                throw new RundeckApiLoginException("Failed to post login form on " + location, e);
            }
        }

        return sessionID;
    }


    /**
     * Instantiate a new {@link HttpClient} instance, configured to accept all SSL certificates
     *
     * @return an {@link HttpClient} instance - won't be null
     */
    private CloseableHttpClient instantiateHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().useSystemProperties();

        // configure user-agent
        httpClientBuilder.setUserAgent( "Rundeck API Java Client " + client.getApiVersion());

        if (client.isSslHostnameVerifyAllowAll()) {
            httpClientBuilder.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
        if (client.isSslCertificateTrustAllowSelfSigned()) {
            // configure SSL
            try {
                httpClientBuilder.setSslcontext(
                        new SSLContextBuilder()
                                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                                .build());
            } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        }
        if(client.isSystemProxyEnabled()) {
            // configure proxy (use system env : http.proxyHost / http.proxyPort)
            httpClientBuilder.setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()));
        }
        // in case of token-based authentication, add the correct HTTP header to all requests via an interceptor
        httpClientBuilder.addInterceptorFirst(
                new HttpRequestInterceptor() {

                    @Override
                    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                        if (client.getToken() != null) {
                            request.addHeader(AUTH_TOKEN_HEADER, client.getToken());
                            //System.out.println("httpClient adding token header");
                        } else if (client.getSessionID() != null) {
                            request.addHeader(COOKIE_HEADER, "JSESSIONID=" + client.getSessionID());
                            //System.out.println("httpClient adding session header, sessionID="+client.getSessionID());
                        }
                    }
                }
        );

        return httpClientBuilder.build();
    }
}
