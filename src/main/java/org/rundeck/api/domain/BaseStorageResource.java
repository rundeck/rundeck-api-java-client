package org.rundeck.api.domain;

import java.util.List;
import java.util.Map;

/**
 * BaseStorageResource is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class BaseStorageResource implements StorageResource {
    private String path;
    private String url;
    private String name;
    private Map<String,String> metadata;
    private boolean directory;
    private List<? extends StorageResource> directoryContents;

    public BaseStorageResource() {
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    @Override
    public List<? extends StorageResource> getDirectoryContents() {
        return directoryContents;
    }

    public void setDirectoryContents(List<? extends StorageResource> directoryContents) {
        this.directoryContents = directoryContents;
    }

    @Override
    public String toString() {
        return "BaseStorageResource{" +
                "path='" + path + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", directory=" + directory +
                '}';
    }
}
