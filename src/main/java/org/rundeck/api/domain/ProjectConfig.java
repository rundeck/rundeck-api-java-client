package org.rundeck.api.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ProjectConfig is ...
 *
 * @author greg
 * @since 2014-02-27
 */
public class ProjectConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();

    public ProjectConfig() {
    }

    public ProjectConfig(Map<String, String> properties) {
        setProperties(properties);
    }

    public void setProperty(final String key, final String value) {
        getProperties().put(key, value);
    }

    public void addProperties(final Map<String, String> values) {
        getProperties().putAll(values);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = new LinkedHashMap<String, String>(properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectConfig)) return false;

        ProjectConfig that = (ProjectConfig) o;

        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return properties != null ? properties.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
                "properties=" + properties +
                '}';
    }
}
