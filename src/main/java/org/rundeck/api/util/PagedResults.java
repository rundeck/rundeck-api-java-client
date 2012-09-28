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
* PagedResults.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 9/28/12 10:32 AM
* 
*/
package org.rundeck.api.util;

import java.util.List;


/**
 * PagedResults contains a List of a certain type, and paging information.
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public interface PagedResults<T> extends Iterable<T>{
    /**
     * Max number of items returned in page
     */
    public int getMax();

    /**
     * Offset into all items
     */
    public int getOffset();

    /**
     * Total items to be paged
     */
    public int getTotal();

    /**
     * Number of items available in page
     */
    public int getCount();

    /**
     * Results list
     */
    public List<T> getResults();
}
