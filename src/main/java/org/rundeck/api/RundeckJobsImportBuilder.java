package org.rundeck.api;

import org.rundeck.api.domain.RundeckJobsImportMethod;
import org.rundeck.api.domain.RundeckJobsUUIDImportBehavior;

import java.io.File;
import java.io.InputStream;

/**
 * Builds a {@link RundeckJobsImport}
 */
public class RundeckJobsImportBuilder {
    private DefaultJobsImport jobsImport;

    public RundeckJobsImportBuilder() {
        jobsImport = new DefaultJobsImport();
    }

    public RundeckJobsImportBuilder(final RundeckJobsImport original) {
        jobsImport = new DefaultJobsImport();
        jobsImport.setStream(original.getStream());
        jobsImport.setFile(original.getFile());
        jobsImport.setFileType(original.getFileType());
        jobsImport.setProject(original.getProject());
        jobsImport.setImportMethod(original.getImportMethod());
        jobsImport.setUuidImportBehavior(original.getUuidImportBehavior());
    }

    /**
     * Create a new builder
     * @return
     */
    public static RundeckJobsImportBuilder builder() {
        return new RundeckJobsImportBuilder();
    }

    /**
     * Create a builder initialized with an existing {@link RundeckJobsImport}
     * @param original
     * @return
     */
    public static RundeckJobsImportBuilder builder(final RundeckJobsImport original) {
        return new RundeckJobsImportBuilder(original);
    }

    public RundeckJobsImportBuilder setProject(final String project) {
        jobsImport.setProject(project);
        return this;
    }

    public RundeckJobsImportBuilder setStream(final InputStream stream) {
        jobsImport.setStream(stream);
        return this;
    }
    public RundeckJobsImportBuilder setFile(final File file) {
        jobsImport.setFile(file);
        return this;
    }

    public RundeckJobsImportBuilder setFileType(final FileType filetype) {
        jobsImport.setFileType(filetype);
        return this;
    }

    public RundeckJobsImportBuilder setJobsImportMethod(final RundeckJobsImportMethod behavior) {
        jobsImport.setImportMethod(behavior);
        return this;
    }
    public RundeckJobsImportBuilder setUUIDImportBehavior(final RundeckJobsUUIDImportBehavior behavior) {
        jobsImport.setUuidImportBehavior(behavior);
        return this;
    }

    public RundeckJobsImport build() {
        final RundeckJobsImport built = jobsImport;
        jobsImport = new DefaultJobsImport();
        return built;
    }
}
