package org.rundeck.api.domain;

import org.rundeck.api.RundeckClient;
import org.rundeck.api.parser.StorageResourceParser;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseSSHKeyResource is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class BaseSSHKeyResource extends BaseStorageResource implements SSHKeyResource {
    private boolean privateKey;

    public BaseSSHKeyResource() {
    }


    public boolean isPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(boolean privateKey) {
        this.privateKey = privateKey;
    }

    ArrayList<SSHKeyResource> sshKeyResources = new ArrayList<SSHKeyResource>();

    @Override
    public void setDirectoryContents(List<? extends StorageResource> directoryContents) {
        for (StorageResource directoryContent : directoryContents) {
            sshKeyResources.add(from(directoryContent));
        }
    }

    @Override
    public List<SSHKeyResource> getDirectoryContents() {
        return sshKeyResources;
    }

    public static BaseSSHKeyResource from(final StorageResource source) {
        final BaseSSHKeyResource baseSshKeyResource = new BaseSSHKeyResource();
        baseSshKeyResource.setDirectory(source.isDirectory());
        baseSshKeyResource.setPath(source.getPath());
        baseSshKeyResource.setName(source.getName());
        baseSshKeyResource.setMetadata(source.getMetadata());
        baseSshKeyResource.setUrl(source.getUrl());
        if (!baseSshKeyResource.isDirectory()) {
            baseSshKeyResource.setPrivateKey(
                    null != baseSshKeyResource.getMetadata() && "private".equals(baseSshKeyResource.getMetadata().get
                            ("Rundeck-ssh-key-type"))
            );
        } else if (null != source.getDirectoryContents()) {
            baseSshKeyResource.setDirectoryContents(source.getDirectoryContents());
        }
        return baseSshKeyResource;
    }
}
