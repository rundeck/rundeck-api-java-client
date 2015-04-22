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
package org.rundeck.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for API parameters that should be passed in URLs.
 * 
 * @author Vincent Behar
 */
public class ParametersUtil {

    /**
     * URL-encode the given string
     * 
     * @param input string to be encoded
     * @return an url-encoded string
     */
    public static String urlEncode(String input) {
        if (StringUtils.isBlank(input)) {
            return input;
        }
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a Rundeck "argString" representing the given options. Format of the argString is
     * <code>"-key1 value1 -key2 'value 2 with spaces'"</code>. You might want to url-encode this string...
     * 
     * @param options to be converted
     * @return a string. null if options is null, empty if there are no valid options.
     */
    public static String generateArgString(Properties options) {
        if (options == null) {
            return null;
        }

        StringBuilder argString = new StringBuilder();
        for (Entry<Object, Object> option : options.entrySet()) {
            String key = String.valueOf(option.getKey());
            String value = String.valueOf(option.getValue());

            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                if (argString.length() > 0) {
                    argString.append(" ");
                }
                argString.append("-").append(key);
                argString.append(" ");
                if (value.indexOf(" ") >= 0
                    && !(0 == value.indexOf("'") && (value.length() - 1) == value.lastIndexOf("'"))) {
                    argString.append("'").append(value).append("'");
                } else {
                    argString.append(value);
                }
            }
        }
        return argString.toString();
    }

    /**
     * Generates an url-encoded string representing the given nodeFilters. Format of the string is
     * <code>"filter1=value1&filter2=value2"</code>.
     * 
     * @param nodeFilters to be converted
     * @return an url-encoded string. null if nodeFilters is null, empty if there are no valid filters.
     */
    public static String generateNodeFiltersString(Properties nodeFilters) {
        if (nodeFilters == null) {
            return null;
        }

        List<String> filters = new ArrayList<String>();
        for (Entry<Object, Object> filter : nodeFilters.entrySet()) {
            String key = String.valueOf(filter.getKey());
            String value = String.valueOf(filter.getValue());

            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                try {
                    filters.add(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return StringUtils.join(filters, "&");
    }

}
