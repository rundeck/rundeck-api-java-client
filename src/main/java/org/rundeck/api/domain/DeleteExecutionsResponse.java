package org.rundeck.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * DeleteExecutionsResponse is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-11-06
 */
public class DeleteExecutionsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int                 failedCount;
    private int                 successCount;
    private boolean             allsuccessful;
    private int                 requestCount;
    private List<DeleteFailure> failures;

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(final int failedCount) {
        this.failedCount = failedCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(final int successCount) {
        this.successCount = successCount;
    }

    public boolean isAllsuccessful() {
        return allsuccessful;
    }

    public void setAllsuccessful(final boolean allsuccessful) {
        this.allsuccessful = allsuccessful;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(final int requestCount) {
        this.requestCount = requestCount;
    }

    public List<DeleteFailure> getFailures() {
        return failures;
    }

    public void setFailures(final List<DeleteFailure> failures) {
        this.failures = failures;
    }

    public static class DeleteFailure implements Serializable{

        private static final long serialVersionUID = 1L;
        private Long executionId;
        private String message;

        public Long getExecutionId() {
            return executionId;
        }

        public void setExecutionId(final Long executionId) {
            this.executionId = executionId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }
    }
}
