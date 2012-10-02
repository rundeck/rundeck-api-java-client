/*
 * Copyright 2012 DTO Labs, Inc. (http://dtolabs.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
* RundeckJobDelete.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 10/1/12 3:53 PM
* 
*/
package org.rundeck.api.domain;

import java.util.*;


/**
 * RundeckJobDelete represents a result of a job delete request.
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class RundeckJobDelete {
    private String id;
    private String error;
    private boolean successful;
    private String errorCode;
    private String message;

    /**
     * Job ID
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Error message if the job could not be deleted
     */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /**
     * True if the job was successfully deleted
     */
    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    /**
     * Error code string if there was a failure
     */
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Success message if it was successful
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        RundeckJobDelete delete = (RundeckJobDelete) o;

        if (successful != delete.successful) { return false; }
        if (error != null ? !error.equals(delete.error) : delete.error != null) { return false; }
        if (errorCode != null ? !errorCode.equals(delete.errorCode) : delete.errorCode != null) { return false; }
        if (id != null ? !id.equals(delete.id) : delete.id != null) { return false; }
        if (message != null ? !message.equals(delete.message) : delete.message != null) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (successful ? 1 : 0);
        result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RundeckJobDelete{" +
               "id='" + id + '\'' +
               ", error='" + error + '\'' +
               ", successful=" + successful +
               ", errorCode='" + errorCode + '\'' +
               ", message='" + message + '\'' +
               '}';
    }
}
