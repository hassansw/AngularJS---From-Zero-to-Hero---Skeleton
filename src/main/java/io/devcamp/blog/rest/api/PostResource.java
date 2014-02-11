package io.devcamp.blog.rest.api;

import io.devcamp.blog.model.Post;
import io.devcamp.blog.rest.PostResourceService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

/**
 * @author Daniel Sachse
 * @date 10.02.14 22:56
 */
public interface PostResource {
    static final String getPostTemplate = "/{year}/{month}/{id}-{title}/";

    @GET
    @Produces("application/json")
    Response getAllPosts(@QueryParam("page") @DefaultValue("1") int currentPage, @QueryParam("perPage") @DefaultValue("5") int pageSize);

    @GET
    @Path(PostResourceService.getPostTemplate)
    @Produces("application/json")
    Response getPost(@PathParam("year") int year, @PathParam("month") int month, @PathParam("id") long id, @PathParam("title") String title) throws URISyntaxException;

    @DELETE
    @Path(PostResourceService.getPostTemplate)
    @Produces("application/json")
    Response deletePost(@Context HttpHeaders headers, @PathParam("year") int year, @PathParam("month") int month, @PathParam("id") long id, @PathParam("title") String title) throws URISyntaxException;

    @POST
    @Consumes("application/json")
    Response addNewPost(@Context HttpHeaders headers, Post post);

    @PUT
    @Path(PostResourceService.getPostTemplate)
    @Consumes("application/json")
    Response updatePost(@Context HttpHeaders headers, Post post, @PathParam("id") long id);
}
