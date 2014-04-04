package org.rundeck.api.domain;

import java.util.List;

/**
 * SSHKeyResource represents a directory or an SSH key file
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public interface SSHKeyResource extends StorageResource {
    /**
     * Return true if this is a file and is a private SSH key file.
     * @return
     */
    public boolean isPrivateKey();

    /**
     * Return the list of SSH Key resources if this is a directory
     * @return
     */
    public List<SSHKeyResource> getDirectoryContents();
}
