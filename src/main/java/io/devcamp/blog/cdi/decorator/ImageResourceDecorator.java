package io.devcamp.blog.cdi.decorator;

import io.devcamp.blog.model.Post;
import io.devcamp.blog.rest.api.ImageResource;
import io.devcamp.blog.rest.api.PostResource;
import io.devcamp.blog.security.AuthenticationManager;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
public abstract class ImageResourceDecorator implements ImageResource {

    @Inject
    @Delegate
    @Any
    private ImageResource imageResource;

    @Inject
    private AuthenticationManager authenticationManager;

    @Override
    public Response addImage(@Context HttpHeaders headers, MultipartFormDataInput multipartFormDataInput) {
        if (authenticationManager.validateBasicAuth(headers)) {
            return imageResource.addImage(headers, multipartFormDataInput);
        }

        return unauthorized();
    }

    private Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}