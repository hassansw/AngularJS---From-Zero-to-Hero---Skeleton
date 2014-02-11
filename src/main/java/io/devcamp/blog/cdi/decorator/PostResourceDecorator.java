package io.devcamp.blog.cdi.decorator;

import io.devcamp.blog.model.Post;
import io.devcamp.blog.rest.api.PostResource;
import io.devcamp.blog.security.AuthenticationManager;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

/**
 * @author Daniel Sachse
 * @date 10.02.14 22:02
 */

@Decorator
public abstract class PostResourceDecorator implements PostResource {

    @Inject
    @Delegate
    @Any
    private PostResource postResource;

    @Inject
    private AuthenticationManager authenticationManager;

    @Override
    public Response deletePost(@Context HttpHeaders headers, int year, int month, long id, String title) throws URISyntaxException {
        if (authenticationManager.validateBasicAuth(headers)) {
            return postResource.deletePost(headers, year, month, id, title);
        }

        return unauthorized();
    }

    @Override
    public Response addNewPost(@Context HttpHeaders headers, Post post) {
        if (authenticationManager.validateBasicAuth(headers)) {
            return postResource.addNewPost(headers, post);
        }

        return unauthorized();
    }

    @Override
    public Response updatePost(@Context HttpHeaders headers, Post post, long id) {
        if (authenticationManager.validateBasicAuth(headers)) {
            return postResource.updatePost(headers, post, id);
        }

        return unauthorized();
    }

    private Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}