package io.devcamp.blog.rest.api;

import io.devcamp.blog.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * @author Daniel Sachse
 * @date 10.02.14 23:54
 */
public interface UserResource {
    @POST
    @Consumes("application/json")
    Response registerAdmin(User user);

    @GET
    @Path("/{id}/")
    Response getUserByID(@PathParam("id") Long id);

    @Path("/login/")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    Response loginAdmin(@Context HttpHeaders headers);
}
