package org.rundeck.api;

import org.rundeck.api.domain.RundeckJobsImportMethod;
import org.rundeck.api.domain.RundeckJobsUUIDImportBehavior;

import java.io.InputStream;

/**
 * $INTERFACE is ... User: greg Date: 8/6/13 Time: 10:18 AM
 */
public class DefaultJobsImport implements RundeckJobsImport{
    private InputStream stream;
    private FileType fileType;
    private RundeckJobsImportMethod importMethod;
    private RundeckJobsUUIDImportBehavior uuidImportBehavior;
    private String project;

    public InputStream getStream() {
        return stream;
    }

    public void setStream(final InputStream stream) {
        this.stream = stream;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(final FileType fileType) {
        this.fileType = fileType;
    }

    public RundeckJobsImportMethod getImportMethod() {
        return importMethod;
    }

    public void setImportMethod(final RundeckJobsImportMethod importMethod) {
        this.importMethod = importMethod;
    }

    public String getProject() {
        return project;
    }

    public void setProject(final String project) {
        this.project = project;
    }

    public RundeckJobsUUIDImportBehavior getUuidImportBehavior() {
        return uuidImportBehavior;
    }

    public void setUuidImportBehavior(RundeckJobsUUIDImportBehavior uuidImportBehavior) {
        this.uuidImportBehavior = uuidImportBehavior;
    }
}
