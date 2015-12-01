package org.blub.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DDocument.
 */

@Document(collection = "ddocument")
public class DDocument implements Serializable {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("directories")
    private String directories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectories() {
        return directories;
    }

    public void setDirectories(String directories) {
        this.directories = directories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DDocument dDocument = (DDocument) o;
        return Objects.equals(id, dDocument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DDocument{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", directories='" + directories + "'" +
            '}';
    }
}
