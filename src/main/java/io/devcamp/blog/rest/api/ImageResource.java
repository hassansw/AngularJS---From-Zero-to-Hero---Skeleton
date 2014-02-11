package io.devcamp.blog.rest.api;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Daniel Sachse
 * @date 10.02.14 23:47
 */
public interface ImageResource {
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response addImage(@Context HttpHeaders headers, MultipartFormDataInput multipartFormDataInput);

    @GET
    @Path("/{uuid}/")
    Response getImage(@PathParam("uuid") String uuid);
}