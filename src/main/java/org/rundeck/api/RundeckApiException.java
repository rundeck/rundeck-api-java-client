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

/**
 * A generic (unchecked) exception when using the RunDeck API
 * 
 * @author Vincent Behar
 */
public class RundeckApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RundeckApiException(String message) {
        super(message);
    }

    public RundeckApiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Specific authentication-related error (either login or token).
     * 
     * @see RundeckApiLoginException
     * @see RundeckApiTokenException
     */
    public static class RundeckApiAuthException extends RundeckApiException {

        private static final long serialVersionUID = 1L;

        public RundeckApiAuthException(String message) {
            super(message);
        }

        public RundeckApiAuthException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Specific authentication-related error (in case of login-based authentication)
     */
    public static class RundeckApiLoginException extends RundeckApiAuthException {

        private static final long serialVersionUID = 1L;

        public RundeckApiLoginException(String message) {
            super(message);
        }

        public RundeckApiLoginException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Specific authentication-related error (in case of token-based authentication)
     */
    public static class RundeckApiTokenException extends RundeckApiAuthException {

        private static final long serialVersionUID = 1L;

        public RundeckApiTokenException(String message) {
            super(message);
        }

        public RundeckApiTokenException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    /**
     * Error due to unexpected HTTP status
     */
    public static class RundeckApiHttpStatusException extends RundeckApiAuthException {

        private static final long serialVersionUID = 1L;
        private int statusCode;

        public RundeckApiHttpStatusException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }

        public RundeckApiHttpStatusException(String message, Throwable cause, int statusCode) {
            super(message, cause);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    /**
     * Error due to unexpected HTTP content-type
     */
    public static class RundeckApiHttpContentTypeException extends RundeckApiAuthException {

        private static final long serialVersionUID = 1L;
        private String contentType;
        private String requiredContentType;

        public RundeckApiHttpContentTypeException(final String contentType,
                final String requiredContentType) {
            super("Unexpected content-type: '" + contentType + "', expected: '" + requiredContentType + "'");
            this.contentType = contentType;
            this.requiredContentType = requiredContentType;
        }
        public RundeckApiHttpContentTypeException(final String message, final String contentType,
                final String requiredContentType) {
            super(message);
            this.contentType = contentType;
            this.requiredContentType = requiredContentType;
        }

        public RundeckApiHttpContentTypeException(final String message, final Throwable cause, final String contentType,
                final String requiredContentType) {
            super(message, cause);
            this.contentType = contentType;
            this.requiredContentType = requiredContentType;
        }

        public String getContentType() {
            return contentType;
        }

        public String getRequiredContentType() {
            return requiredContentType;
        }
    }

}
