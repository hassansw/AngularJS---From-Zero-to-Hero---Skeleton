package io.devcamp.blog.rest.model;

import io.devcamp.blog.model.Post;

import java.io.Serializable;
import java.util.List;

/**
 * @author Daniel Sachse
 * @date 11.01.14 15:10
 */
public class GetPostResponse implements Serializable {
    private long page;
    private long pageCount;
    private List<Post> posts;

    protected GetPostResponse(long page) {
        this.page = page;
    }

    public GetPostResponse(long page, long pageCount, List<Post> posts) {
        this.page = page;
        this.pageCount = pageCount;
        this.posts = posts;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}