/*
 * Copyright 2012 DTO Labs, Inc. (http://dtolabs.com)
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
 *
 */

/*
* QueryParameterBuilder.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 9/13/12 4:21 PM
* 
*/
package org.rundeck.api;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Abstract utility base class for building query parameters from a query object.
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
abstract class QueryParameterBuilder implements ApiPathBuilder.BuildsParameters {
    public static final String W3C_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    static final SimpleDateFormat format = new SimpleDateFormat(W3C_DATE_FORMAT);

    /**
     * Add a value to the builder for a given key
     *
     * @param key     parameter name
     * @param value   value which can be a String, Boolean, Date or collection of Strings. Other types will be converted
     *                via {@link Object#toString()}
     * @param doPost  if true, add POST field instead of query parameters
     * @param builder the builder
     *
     * @return true if the value was not null and was added to the builder
     */
    protected boolean visit(String key, Object value, boolean doPost, ApiPathBuilder builder) {
        if (null != value) {
            if (doPost) {
                if (value instanceof Collection) {
                    builder.field(key, (Collection<String>) value);
                } else {
                    builder.field(key, stringVal(value));
                }
                return true;
            } else {
                if (value instanceof Collection) {
                    builder.param(key, (Collection<String>) value);
                } else {
                    builder.param(key, stringVal(value));
                }
                return true;
            }
        }
        return false;
    }

    private String stringVal(Object value) {
        return value instanceof String ? (String) value :
               value instanceof Boolean ? Boolean.toString((Boolean) value) :
               value instanceof Date ? formatDate((Date) value)

                                     : value.toString();
    }

    private String formatDate(Date value) {
        return format.format(value);
    }
}
