package org.rundeck.api.parser;

import org.dom4j.Element;
import org.dom4j.Node;
import org.rundeck.api.domain.BaseStorageResource;
import org.rundeck.api.domain.StorageResource;

import java.util.HashMap;

/**
 * StorageResourceParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class StorageResourceParser extends BaseXpathParser<StorageResource> {
    private BaseStorageResource holder;
    public StorageResourceParser() {

    }
    public StorageResourceParser(BaseStorageResource holder){
        this.holder=holder;
    }

    public StorageResourceParser(String xpath) {
        super(xpath);
    }

    @Override
    public StorageResource parse(Node node) {
        String path = node.valueOf("@path");
        String type = node.valueOf("@type");
        String url = node.valueOf("@url");
        BaseStorageResource storageResource = null == holder ? new BaseStorageResource() : holder;
        storageResource.setDirectory("directory".equals(type));
        storageResource.setPath(path);
        storageResource.setUrl(url);

        if (storageResource.isDirectory()) {
            if (node.selectSingleNode("contents") != null) {
                storageResource.setDirectoryContents(new ListParser<StorageResource>(new StorageResourceParser(),
                        "contents/resource").parseXmlNode(node));
            }
        } else {
            String name = node.valueOf("@name");
            storageResource.setName(name);

            Node meta = node.selectSingleNode("resource-meta");
            HashMap<String, String> metamap = new HashMap<String, String>();
            if (null != meta) {
                Element metaEl = (Element) meta;
                for (Object o : metaEl.elements()) {
                    Element sub = (Element) o;
                    metamap.put(sub.getName(), sub.getText().trim());
                }
            }
            storageResource.setMetadata(metamap);
        }
        return storageResource;
    }
}
