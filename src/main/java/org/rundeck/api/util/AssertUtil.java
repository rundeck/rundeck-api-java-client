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

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for assertions
 *
 * @author Vincent Behar
 */
public class AssertUtil {

    /**
     * Test if the given value is in the list
     *
     * @param errorMessage message
     * @param value        value
     * @param list         list
     */
    public static void inList(String errorMessage, Object value, List<Object> list) {
        if (!list.contains(value)) {
            throw new IllegalArgumentException(errorMessage + ": " + list);
        }
    }

    /**
     * Test if the given value is in the list
     *
     * @param errorMessage message
     * @param value        value
     * @param list         list
     */
    public static void inList(String errorMessage, Object value, Object... list) {
        inList(errorMessage, value, Arrays.asList(list));
    }
    /**
     * Test if the given {@link Object} is null
     *
     * @param object
     * @param errorMessage to be used if the object is null
     * @throws IllegalArgumentException if the given object is null
     */
    public static void notNull(Object object, String errorMessage) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Test if the given {@link String} is blank (null, empty or only whitespace)
     *
     * @param input string
     * @param errorMessage to be used if the string is blank
     * @throws IllegalArgumentException if the given string is blank
     */
    public static void notBlank(String input, String errorMessage) throws IllegalArgumentException {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
