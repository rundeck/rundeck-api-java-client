package org.rundeck.api.generator;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.rundeck.api.domain.ProjectConfig;

import java.util.List;
import java.util.Set;

/**
 * DeleteExecutionsGenerator is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-11-06
 */
public class DeleteExecutionsGenerator extends BaseDocGenerator {
    private Set<Long> executionIds;

    public DeleteExecutionsGenerator(final Set<Long> executionIds) {
        this.executionIds = executionIds;
    }

    @Override public Element generateXmlElement() {
        Element rootElem = DocumentFactory.getInstance().createElement("executions");
        for (Long executionId : executionIds) {
            rootElem.addElement("execution").addAttribute("id", Long.toString(executionId));
        }
        return rootElem;
    }

    public Set<Long> getExecutionIds() {
        return executionIds;
    }

    public void setExecutionIds(final Set<Long> executionIds) {
        this.executionIds = executionIds;
    }
}
