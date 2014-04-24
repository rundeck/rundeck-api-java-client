package org.rundeck.api.domain;

import java.util.List;
import java.util.Map;

/**
 * StorageResource represents a directory or a file
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public interface StorageResource {
    /**
     * Return the storage path for this resource
     *
     * @return
     */
    public String getPath();

    /**
     * Return the URL for this resource
     *
     * @return
     */
    public String getUrl();

    /**
     * Return the file name if this is a file
     *
     * @return
     */
    public String getName();

    /**
     * Return the metadata for this file if this is a file
     *
     * @return
     */
    public Map<String, String> getMetadata();

    /**
     * Return true if this is a directory, false if this is a file
     *
     * @return
     */
    public boolean isDirectory();

    /**
     * Return the list of directory contents if this is a directory
     *
     * @return
     */
    public List<? extends StorageResource> getDirectoryContents();
}
