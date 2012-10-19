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
import org.apache.commons.lang.StringUtils;

/**
 * Represents a RunDeck job
 * 
 * @author Vincent Behar
 */
public class RundeckJob implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String group;

    private String project;

    private String description;

    private long averageDuration = -1L;

    /**
     * @return the fullname : group + name (exact format is : "group/name")
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (StringUtils.isNotBlank(group)) {
            fullName.append(group).append("/");
        }
        fullName.append(name);
        return fullName.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RundeckJob [id=" + id + ", name=" + name + ", group=" + group + ", project=" + project
               + ", description=" + description + ", averageDuration="+averageDuration+"]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        result = prime * result + (int) (averageDuration ^ (averageDuration >>> 32));
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
        RundeckJob other = (RundeckJob) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        if (averageDuration != other.averageDuration) {
            return false;
        }
        return true;
    }


    public long getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(long averageDuration) {
        this.averageDuration = averageDuration;
    }
}
