package org.rundeck.api.domain;

import java.util.List;

/**
 * ArchiveImport describes the result of an {@link org.rundeck.api.RundeckClient#importArchive(String, java.io.File,
 * boolean, boolean)} request.
 *
 * @author greg
 * @since 2014-03-09
 */
public class ArchiveImport {
    private boolean successful;
    private List<String> errorMessages;

    public ArchiveImport(final boolean successful, final List<String> errorMessages) {
        this.successful = successful;
        this.errorMessages = errorMessages;
    }

    /**
     * Return true if successful
     * @return
     */
    public boolean isSuccessful() {
        return successful;
    }


    /**
     * Return a list of error messages if unsuccessful
     * @return
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

}
