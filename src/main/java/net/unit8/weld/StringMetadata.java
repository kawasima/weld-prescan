package net.unit8.weld;

import org.jboss.weld.bootstrap.spi.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kawasima
 */
public class StringMetadata implements Metadata<String> {
    private String value;
    private String location;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    static List<StringMetadata> from(List<Metadata<String>> metadatas) {
        List<StringMetadata> smList = new ArrayList<>(metadatas.size());
        for (Metadata<String> metadata : metadatas) {
            StringMetadata sm = new StringMetadata();
            sm.setLocation(metadata.getLocation());
            sm.setValue(metadata.getValue());
            smList.add(sm);
        }
        return smList;
    }
}
