package io.devcamp.blog.rest;

import io.devcamp.blog.cdi.interceptor.Logging;
import io.devcamp.blog.cdi.qualifier.Created;
import io.devcamp.blog.model.Post;
import io.devcamp.blog.rest.api.PostResource;
import io.devcamp.blog.rest.model.GetPostResponse;
import org.apache.commons.codec.binary.Hex;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

/**
 * @author Daniel Sachse
 * @date 06.01.14 20:00
 */

@Path("/posts/")
@Stateless
@Logging
public class PostResourceService implements PostResource {
    @PersistenceContext
    private EntityManager em;

    @Context
    private UriInfo uri;

    @Inject
    @Created
    private Event<Post> createdPostEvent;

    @Override
    public Response getAllPosts(@QueryParam("page") @DefaultValue("1") int currentPage, @QueryParam("perPage") @DefaultValue("5") int pageSize) {
        long totalPosts = em.createNamedQuery(Post.countPosts, Long.class).getSingleResult();
        long totalPages = totalPosts / pageSize;

        if(totalPosts % pageSize > 0) {
            totalPages++;
        }

        if(currentPage > totalPages) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<Post> posts = em.createNamedQuery(Post.findPosts, Post.class)
                                .setFirstResult((currentPage - 1) * pageSize)
                                .setMaxResults(pageSize)
                                .getResultList();

        for(Post post : posts) {
            Calendar createdAt = Calendar.getInstance();
            createdAt.setTime(post.getCreationTimestamp());

            if(post.getImageUUID() != null) {
                post.setImageURI(uri.getBaseUriBuilder().path("images").path(post.getImageUUID()).build().toString());
            }

            post.setUrl(uri.getAbsolutePathBuilder()
                            .path(String.valueOf(createdAt.get(Calendar.YEAR)))
                            .path(String.valueOf(createdAt.get(Calendar.MONTH) + 1))
                            .path(post.getId() + "-" + post.getTitle())
                            .build().toString());
        }

        return Response.ok(new GetPostResponse(currentPage, totalPages , posts)).build();
    }

    @Override
    public Response getPost(@PathParam("year") int year, @PathParam("month") int month, @PathParam("id") long id, @PathParam("title") String title) throws URISyntaxException {
        Post post = em.find(Post.class, id);

        if(post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        post.setUrl(uri.getAbsolutePath().toString());

        if(post.getImageUUID() != null) {
            post.setImageURI(uri.getBaseUriBuilder().path("images").path(post.getImageUUID()).build().toString());
        }

        if(!title.equals(post.getTitle())) {
            String newUri = uri.getAbsolutePath().toString().replaceAll("-" + title, "-" + post.getTitle());

            return Response.status(Response.Status.MOVED_PERMANENTLY).location(new URI(newUri)).build();
        }

        return Response.ok(post).build();
    }

    @Override
    public Response deletePost(@Context HttpHeaders headers, @PathParam("year") int year, @PathParam("month") int month, @PathParam("id") long id, @PathParam("title") String title) throws URISyntaxException {
        // TODO: check Basic auth header

        Post post = em.find(Post.class, id);

        if(post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.remove(post);

        return Response.noContent().build();
    }

    @Override
    public Response addNewPost(@Context HttpHeaders headers, Post post) {
        // TODO: check Basic auth header

        if(post.getImageURI() != null) {
            String[] parts = post.getImageURI().split("/");
            String uuid = parts[parts.length - 1];
            post.setImageUUID(uuid);
        }

        em.persist(post);

        UriBuilder uriBuilder = UriBuilder.fromUri(uri.getRequestUri());
        uriBuilder.path("{id}");
        URI uri = uriBuilder.build(post.getId());
        EntityTag etag = new EntityTag(Hex.encodeHexString(post.toString().getBytes()));

        post.setUrl(uri.toString());

        createdPostEvent.fire(post);

        return Response.created(uri)
                .tag(etag)
                .entity(post)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public Response updatePost(@Context HttpHeaders headers, Post post, @PathParam("id") long id) {
        // TODO: check Basic auth header

        Post dbPost = em.find(Post.class, id);

        dbPost.setTitle(post.getTitle());
        dbPost.setSubtitle(post.getSubtitle());
        dbPost.setContent(post.getContent());

        if(post.getImageURI() != null) {
            String[] parts = post.getImageURI().split("/");
            String uuid = parts[parts.length - 1];
            dbPost.setImageUUID(uuid);
        }

        dbPost = em.merge(dbPost);

        UriBuilder uriBuilder = UriBuilder.fromUri(uri.getRequestUri());
        uriBuilder.path("{id}");
        URI uri = uriBuilder.build(dbPost.getId());
        EntityTag etag = new EntityTag(Hex.encodeHexString(dbPost.toString().getBytes()));

        dbPost.setUrl(uri.toString());

        return Response.ok(uri)
                .tag(etag)
                .entity(dbPost)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}