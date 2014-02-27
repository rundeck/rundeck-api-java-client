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
import java.util.List;

/**
 * Represents a RunDeck node (server on which RunDeck can execute jobs and commands)
 * 
 * @author Vincent Behar
 */
public class RundeckNode implements Serializable, RundeckNodeIdentity {

    private static final long serialVersionUID = 1L;

    /** The node name. This is a logical identifier from the node. (required) */
    private String name;

    /** The node type, such as "Node". (required) */
    private String type;

    /** A brief description about the node. (optional) */
    private String description;

    /** List of filtering tags. (optional) */
    private List<String> tags;

    /** The hostname or IP address of the remote host. (required) */
    private String hostname;

    /** The operating system architecture. (optional) */
    private String osArch;

    /** The operating system family, such as unix or windows. (optional) */
    private String osFamily;

    /** The operating system name such as Linux or Mac OS X. (optional) */
    private String osName;

    /** The operating system version. (optional) */
    private String osVersion;

    /** The username used for the remote connection. (required) */
    private String username;

    /** URL to an external resource model editor service (optional) */
    private String editUrl;

    /** URL to an external resource model service. (optional) */
    private String remoteUrl;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    @Override
    public String toString() {
        return "RundeckNode [name=" + name + ", hostname=" + hostname + ", description=" + description + ", tags="
               + tags + ", type=" + type + ", username=" + username + ", osArch=" + osArch + ", osFamily=" + osFamily
               + ", osName=" + osName + ", osVersion=" + osVersion + ", editUrl=" + editUrl + ", remoteUrl="
               + remoteUrl + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((editUrl == null) ? 0 : editUrl.hashCode());
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((osArch == null) ? 0 : osArch.hashCode());
        result = prime * result + ((osFamily == null) ? 0 : osFamily.hashCode());
        result = prime * result + ((osName == null) ? 0 : osName.hashCode());
        result = prime * result + ((osVersion == null) ? 0 : osVersion.hashCode());
        result = prime * result + ((remoteUrl == null) ? 0 : remoteUrl.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        RundeckNode other = (RundeckNode) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (editUrl == null) {
            if (other.editUrl != null)
                return false;
        } else if (!editUrl.equals(other.editUrl))
            return false;
        if (hostname == null) {
            if (other.hostname != null)
                return false;
        } else if (!hostname.equals(other.hostname))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (osArch == null) {
            if (other.osArch != null)
                return false;
        } else if (!osArch.equals(other.osArch))
            return false;
        if (osFamily == null) {
            if (other.osFamily != null)
                return false;
        } else if (!osFamily.equals(other.osFamily))
            return false;
        if (osName == null) {
            if (other.osName != null)
                return false;
        } else if (!osName.equals(other.osName))
            return false;
        if (osVersion == null) {
            if (other.osVersion != null)
                return false;
        } else if (!osVersion.equals(other.osVersion))
            return false;
        if (remoteUrl == null) {
            if (other.remoteUrl != null)
                return false;
        } else if (!remoteUrl.equals(other.remoteUrl))
            return false;
        if (tags == null) {
            if (other.tags != null)
                return false;
        } else if (!tags.equals(other.tags))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
