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

/**
 * Represents a RunDeck project
 * 
 * @author Vincent Behar
 */
public class RundeckProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String resourceModelProviderUrl;

    private ProjectConfig projectConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceModelProviderUrl() {
        return resourceModelProviderUrl;
    }

    public void setResourceModelProviderUrl(String resourceModelProviderUrl) {
        this.resourceModelProviderUrl = resourceModelProviderUrl;
    }

    public ProjectConfig getProjectConfig() {
        return projectConfig;
    }

    public void setProjectConfig(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    @Override
    public String toString() {
        return "RundeckProject [name=" + name + ", description=" + description
                + (null!=resourceModelProviderUrl? ", resourceModelProviderUrl=" + resourceModelProviderUrl : "")
                + ", config="
               + projectConfig + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((resourceModelProviderUrl == null) ? 0 : resourceModelProviderUrl.hashCode());
        result = prime * result + ((projectConfig == null) ? 0 : projectConfig.hashCode());
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
        RundeckProject other = (RundeckProject) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (resourceModelProviderUrl == null) {
            if (other.resourceModelProviderUrl != null)
                return false;
        } else if (!resourceModelProviderUrl.equals(other.resourceModelProviderUrl))
            return false;
        if (projectConfig == null) {
            if (other.projectConfig != null)
                return false;
        } else if (!projectConfig.equals(other.projectConfig))
            return false;
        return true;
    }
}
