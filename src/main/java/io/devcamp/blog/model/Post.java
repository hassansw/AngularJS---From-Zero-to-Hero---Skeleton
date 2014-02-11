package io.devcamp.blog.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 * @author Daniel Sachse
 * @date 06.01.14 20:07
 */

@NamedQueries({
        @NamedQuery(name = Post.findPosts, query = "SELECT p FROM Post p ORDER BY p.creationTimestamp desc"),
        @NamedQuery(name = Post.countPosts, query = "SELECT COUNT(*) FROM Post p")
})
@Entity
public class Post extends AbstractEntity {
    public static final String findPosts = "findPosts";
    public static final String countPosts = "countPosts";

    private String title;
    private String subtitle;
    private String content;

    @JsonIgnore
    private String imageUUID;

    @Transient
    private String imageURI;

    @Transient
    private String url;

    protected Post() {}

    public Post(String title, String subtitle, String content, String imageUUID, String url) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.imageUUID = imageUUID;
        this.url = url;
    }

    public String getImageUUID() {
        return imageUUID;
    }

    public void setImageUUID(String imageUUID) {
        this.imageUUID = imageUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}