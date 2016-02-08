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
package org.rundeck.api.parser;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckSystemInfo;

/**
 * Parser for a single {@link RundeckSystemInfo}
 * 
 * @author Vincent Behar
 */
public class SystemInfoParser extends BaseXpathParser<RundeckSystemInfo> {


    public SystemInfoParser(final String xpath) {
        super(xpath);
    }

    @Override
    public RundeckSystemInfo parse(Node infoNode) {
        RundeckSystemInfo info = new RundeckSystemInfo();

        String timestamp = StringUtils.trimToNull(infoNode.valueOf("timestamp/@epoch"));
        if (timestamp != null) {
            info.setDate(new Date(Long.valueOf(timestamp)));
        }
        info.setVersion(StringUtils.trimToNull(infoNode.valueOf("rundeck/version")));
        info.setBuild(StringUtils.trimToNull(infoNode.valueOf("rundeck/build")));
        info.setNode(StringUtils.trimToNull(infoNode.valueOf("rundeck/node")));
        info.setBaseDir(StringUtils.trimToNull(infoNode.valueOf("rundeck/base")));
        info.setOsArch(StringUtils.trimToNull(infoNode.valueOf("os/arch")));
        info.setOsName(StringUtils.trimToNull(infoNode.valueOf("os/name")));
        info.setOsVersion(StringUtils.trimToNull(infoNode.valueOf("os/version")));
        info.setJvmName(StringUtils.trimToNull(infoNode.valueOf("jvm/name")));
        info.setJvmVendor(StringUtils.trimToNull(infoNode.valueOf("jvm/vendor")));
        info.setJvmVersion(StringUtils.trimToNull(infoNode.valueOf("jvm/version")));
        String startDate = StringUtils.trimToNull(infoNode.valueOf("stats/uptime/since/@epoch"));
        if (startDate != null) {
            info.setStartDate(new Date(Long.valueOf(startDate)));
        }
        info.setUptimeInMillis(Long.valueOf(infoNode.valueOf("stats/uptime/@duration")));
        info.setCpuLoadAverage(StringUtils.trimToNull(infoNode.valueOf("stats/cpu/loadAverage")));
        if (info.getCpuLoadAverage() != null) {
            info.setCpuLoadAverage(info.getCpuLoadAverage() + " %");
        }
        info.setMaxMemoryInBytes(Long.valueOf(infoNode.valueOf("stats/memory/max")));
        info.setFreeMemoryInBytes(Long.valueOf(infoNode.valueOf("stats/memory/free")));
        info.setTotalMemoryInBytes(Long.valueOf(infoNode.valueOf("stats/memory/total")));
        info.setRunningJobs(Integer.valueOf(infoNode.valueOf("stats/scheduler/running")));
        info.setActiveThreads(Integer.valueOf(infoNode.valueOf("stats/threads/active")));

        return info;
    }
}
