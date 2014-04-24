package org.rundeck.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseKeyResource is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class BaseKeyResource extends BaseStorageResource implements KeyResource {
    private boolean privateKey;

    public BaseKeyResource() {
    }


    public boolean isPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(boolean privateKey) {
        this.privateKey = privateKey;
    }

    ArrayList<KeyResource> keyResources = new ArrayList<KeyResource>();

    @Override
    public void setDirectoryContents(List<? extends StorageResource> directoryContents) {
        for (StorageResource directoryContent : directoryContents) {
            keyResources.add(from(directoryContent));
        }
    }

    @Override
    public List<KeyResource> getDirectoryContents() {
        return keyResources;
    }

    public static BaseKeyResource from(final StorageResource source) {
        final BaseKeyResource baseSshKeyResource = new BaseKeyResource();
        baseSshKeyResource.setDirectory(source.isDirectory());
        baseSshKeyResource.setPath(source.getPath());
        baseSshKeyResource.setName(source.getName());
        baseSshKeyResource.setMetadata(source.getMetadata());
        baseSshKeyResource.setUrl(source.getUrl());
        if (!baseSshKeyResource.isDirectory()) {
            baseSshKeyResource.setPrivateKey(
                    null != baseSshKeyResource.getMetadata() && "private".equals(baseSshKeyResource.getMetadata().get
                            ("Rundeck-key-type"))
            );
        } else if (null != source.getDirectoryContents()) {
            baseSshKeyResource.setDirectoryContents(source.getDirectoryContents());
        }
        return baseSshKeyResource;
    }
}
