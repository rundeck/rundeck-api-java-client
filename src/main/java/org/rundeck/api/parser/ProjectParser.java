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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.rundeck.api.domain.RundeckProject;

/**
 * Parser for a single {@link RundeckProject}
 * 
 * @author Vincent Behar
 */
public class ProjectParser implements XmlNodeParser<RundeckProject> {

    private String xpath;

    public ProjectParser() {
        super();
    }

    /**
     * @param xpath of the project element if it is not the root node
     */
    public ProjectParser(String xpath) {
        super();
        this.xpath = xpath;
    }

    @Override
    public RundeckProject parseXmlNode(Node node) {
        Node projectNode = getXpath() != null ? node.selectSingleNode(getXpath()) : node;

        RundeckProject project = new RundeckProject();

        project.setName(StringUtils.trimToNull(projectNode.valueOf("name")));
        project.setDescription(StringUtils.trimToNull(projectNode.valueOf("description")));
        project.setResourceModelProviderUrl(StringUtils.trimToNull(projectNode.valueOf("resources/providerURL")));

        return project;
    }

    protected String getXpath() {
        return xpath;
    }
}
