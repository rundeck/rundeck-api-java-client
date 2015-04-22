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
package org.rundeck.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a portion of the Rundeck (events) history
 * 
 * @author Vincent Behar
 */
public class RundeckHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<RundeckEvent> events;

    private int count;

    private int total;

    private int max;

    private int offset;

    public void addEvent(RundeckEvent event) {
        if (events == null) {
            events = new ArrayList<RundeckEvent>();
        }
        events.add(event);
    }

    public List<RundeckEvent> getEvents() {
        return events;
    }

    public void setEvents(List<RundeckEvent> events) {
        this.events = events;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "RundeckHistory [count=" + count + ", max=" + max + ", offset=" + offset + ", total=" + total + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + ((events == null) ? 0 : events.hashCode());
        result = prime * result + max;
        result = prime * result + offset;
        result = prime * result + total;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RundeckHistory other = (RundeckHistory) obj;
        if (count != other.count)
            return false;
        if (events == null) {
            if (other.events != null)
                return false;
        } else if (!events.equals(other.events))
            return false;
        if (max != other.max)
            return false;
        if (offset != other.offset)
            return false;
        if (total != other.total)
            return false;
        return true;
    }

}
