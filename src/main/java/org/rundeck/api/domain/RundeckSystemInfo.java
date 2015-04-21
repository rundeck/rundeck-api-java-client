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
import java.util.Date;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Represents the Rundeck system info
 * 
 * @author Vincent Behar
 */
public class RundeckSystemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date date;

    private String version;

    private String build;

    private String node;

    private String baseDir;

    private String osArch;

    private String osName;

    private String osVersion;

    private String jvmName;

    private String jvmVendor;

    private String jvmVersion;

    private Date startDate;

    private Long uptimeInMillis;

    private String cpuLoadAverage;

    private Long maxMemoryInBytes;

    private Long freeMemoryInBytes;

    private Long totalMemoryInBytes;

    private Integer runningJobs;

    private Integer activeThreads;

    /**
     * @return the uptime of the server, as a human-readable string : "42 days 7 hours 3 minutes 34 seconds"
     */
    public String getUptime() {
        return uptimeInMillis != null ? DurationFormatUtils.formatDurationWords(uptimeInMillis, true, true) : null;
    }

    public Date getDate() {
        return (date != null) ? new Date(date.getTime()) : null;
    }

    public void setDate(Date date) {
        this.date = ((date != null) ? new Date(date.getTime()) : null);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
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

    public String getJvmName() {
        return jvmName;
    }

    public void setJvmName(String jvmName) {
        this.jvmName = jvmName;
    }

    public String getJvmVendor() {
        return jvmVendor;
    }

    public void setJvmVendor(String jvmVendor) {
        this.jvmVendor = jvmVendor;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public Date getStartDate() {
        return (startDate != null) ? new Date(startDate.getTime()) : null;
    }

    public void setStartDate(Date startDate) {
        this.startDate = ((startDate != null) ? new Date(startDate.getTime()) : null);
    }

    public Long getUptimeInMillis() {
        return uptimeInMillis;
    }

    public void setUptimeInMillis(Long uptimeInMillis) {
        this.uptimeInMillis = uptimeInMillis;
    }

    public String getCpuLoadAverage() {
        return cpuLoadAverage;
    }

    public void setCpuLoadAverage(String cpuLoadAverage) {
        this.cpuLoadAverage = cpuLoadAverage;
    }

    public Long getMaxMemoryInBytes() {
        return maxMemoryInBytes;
    }

    public void setMaxMemoryInBytes(Long maxMemoryInBytes) {
        this.maxMemoryInBytes = maxMemoryInBytes;
    }

    public Long getFreeMemoryInBytes() {
        return freeMemoryInBytes;
    }

    public void setFreeMemoryInBytes(Long freeMemoryInBytes) {
        this.freeMemoryInBytes = freeMemoryInBytes;
    }

    public Long getTotalMemoryInBytes() {
        return totalMemoryInBytes;
    }

    public void setTotalMemoryInBytes(Long totalMemoryInBytes) {
        this.totalMemoryInBytes = totalMemoryInBytes;
    }

    public Integer getRunningJobs() {
        return runningJobs;
    }

    public void setRunningJobs(Integer runningJobs) {
        this.runningJobs = runningJobs;
    }

    public Integer getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(Integer activeThreads) {
        this.activeThreads = activeThreads;
    }

    @Override
    public String toString() {
        return "RundeckSystemInfo [activeThreads=" + activeThreads + ", baseDir=" + baseDir + ", build=" + build
               + ", cpuLoadAverage=" + cpuLoadAverage + ", date=" + date + ", freeMemoryInBytes=" + freeMemoryInBytes
               + ", jvmName=" + jvmName + ", jvmVendor=" + jvmVendor + ", jvmVersion=" + jvmVersion
               + ", maxMemoryInBytes=" + maxMemoryInBytes + ", node=" + node + ", osArch=" + osArch + ", osName="
               + osName + ", osVersion=" + osVersion + ", runningJobs=" + runningJobs + ", startDate=" + startDate
               + ", totalMemoryInBytes=" + totalMemoryInBytes + ", uptimeInMillis=" + uptimeInMillis + ", version="
               + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activeThreads == null) ? 0 : activeThreads.hashCode());
        result = prime * result + ((baseDir == null) ? 0 : baseDir.hashCode());
        result = prime * result + ((build == null) ? 0 : build.hashCode());
        result = prime * result + ((cpuLoadAverage == null) ? 0 : cpuLoadAverage.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((freeMemoryInBytes == null) ? 0 : freeMemoryInBytes.hashCode());
        result = prime * result + ((jvmName == null) ? 0 : jvmName.hashCode());
        result = prime * result + ((jvmVendor == null) ? 0 : jvmVendor.hashCode());
        result = prime * result + ((jvmVersion == null) ? 0 : jvmVersion.hashCode());
        result = prime * result + ((maxMemoryInBytes == null) ? 0 : maxMemoryInBytes.hashCode());
        result = prime * result + ((node == null) ? 0 : node.hashCode());
        result = prime * result + ((osArch == null) ? 0 : osArch.hashCode());
        result = prime * result + ((osName == null) ? 0 : osName.hashCode());
        result = prime * result + ((osVersion == null) ? 0 : osVersion.hashCode());
        result = prime * result + ((runningJobs == null) ? 0 : runningJobs.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((totalMemoryInBytes == null) ? 0 : totalMemoryInBytes.hashCode());
        result = prime * result + ((uptimeInMillis == null) ? 0 : uptimeInMillis.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        RundeckSystemInfo other = (RundeckSystemInfo) obj;
        if (activeThreads == null) {
            if (other.activeThreads != null)
                return false;
        } else if (!activeThreads.equals(other.activeThreads))
            return false;
        if (baseDir == null) {
            if (other.baseDir != null)
                return false;
        } else if (!baseDir.equals(other.baseDir))
            return false;
        if (build == null) {
            if (other.build != null)
                return false;
        } else if (!build.equals(other.build))
            return false;
        if (cpuLoadAverage == null) {
            if (other.cpuLoadAverage != null)
                return false;
        } else if (!cpuLoadAverage.equals(other.cpuLoadAverage))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (freeMemoryInBytes == null) {
            if (other.freeMemoryInBytes != null)
                return false;
        } else if (!freeMemoryInBytes.equals(other.freeMemoryInBytes))
            return false;
        if (jvmName == null) {
            if (other.jvmName != null)
                return false;
        } else if (!jvmName.equals(other.jvmName))
            return false;
        if (jvmVendor == null) {
            if (other.jvmVendor != null)
                return false;
        } else if (!jvmVendor.equals(other.jvmVendor))
            return false;
        if (jvmVersion == null) {
            if (other.jvmVersion != null)
                return false;
        } else if (!jvmVersion.equals(other.jvmVersion))
            return false;
        if (maxMemoryInBytes == null) {
            if (other.maxMemoryInBytes != null)
                return false;
        } else if (!maxMemoryInBytes.equals(other.maxMemoryInBytes))
            return false;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        if (osArch == null) {
            if (other.osArch != null)
                return false;
        } else if (!osArch.equals(other.osArch))
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
        if (runningJobs == null) {
            if (other.runningJobs != null)
                return false;
        } else if (!runningJobs.equals(other.runningJobs))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (totalMemoryInBytes == null) {
            if (other.totalMemoryInBytes != null)
                return false;
        } else if (!totalMemoryInBytes.equals(other.totalMemoryInBytes))
            return false;
        if (uptimeInMillis == null) {
            if (other.uptimeInMillis != null)
                return false;
        } else if (!uptimeInMillis.equals(other.uptimeInMillis))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
