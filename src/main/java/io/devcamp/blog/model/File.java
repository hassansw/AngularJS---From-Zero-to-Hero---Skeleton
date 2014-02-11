package io.devcamp.blog.model;

import javax.persistence.*;

/**
 * @author Daniel Sachse
 * @date 22.01.14 21:17
 */

@NamedQueries({
        @NamedQuery(name = File.findFileByUUID, query = "SELECT f FROM File f WHERE f.uuid = :uuid")
})
@Entity
public class File extends AbstractEntity {
    public static final String findFileByUUID = "findFileByUUID";

    private String uuid;
    private String filename;
    private String contentType;

    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] content;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}