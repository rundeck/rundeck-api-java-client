package org.rundeck.api;

import org.rundeck.api.domain.RundeckJobsImportMethod;

import java.io.InputStream;

/**
 * Request to import a set of Job definitions
 */
public interface RundeckJobsImport {
    /**
     * Return the input stream
     * @return
     */
    public InputStream getStream();

    /**
     * Return the file type
     * @return
     */
    public FileType getFileType();

    /**
     * Return the import behavior
     * @return
     */
    public RundeckJobsImportMethod getImportMethod();

    /**
     * Return the project name
     * @return
     */
    public String getProject();
}
