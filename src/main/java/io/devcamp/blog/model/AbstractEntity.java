package io.devcamp.blog.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private Long id = null;

    @Version
    @Column(name = "version")
    @JsonIgnore
    private int version = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @PrePersist
    private void prePersist() {
        creationTimestamp = new Date();
    }

    @PreUpdate
    private void preUpdate() {
        updateTimestamp = new Date();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";

        if (id != null) {
            result += "id: " + id;
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AbstractEntity)) {
            return false;
        }

        AbstractEntity other = (AbstractEntity) obj;

        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }
}