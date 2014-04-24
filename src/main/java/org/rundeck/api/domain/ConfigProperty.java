package org.rundeck.api.domain;

import java.io.Serializable;

/**
 * ConfigProperty is a single configuration property key and value.
 *
 * @author greg
 * @since 2014-03-07
 */
public class ConfigProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    private String key;
    private String value;

    public ConfigProperty() {
    }

    public ConfigProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigProperty)) return false;

        ConfigProperty that = (ConfigProperty) o;

        if (!key.equals(that.key)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ConfigProperty{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
