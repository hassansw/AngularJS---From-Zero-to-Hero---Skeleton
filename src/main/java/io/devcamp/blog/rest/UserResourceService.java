package io.devcamp.blog.rest;

import io.devcamp.blog.model.User;
import io.devcamp.blog.rest.api.UserResource;
import io.devcamp.blog.security.AuthenticationManager;
import io.devcamp.blog.security.BasicAuth;
import org.apache.commons.codec.binary.Hex;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author Daniel Sachse
 * @date 08.01.14 19:53
 */

@Stateless
@Path("/users/")
public class UserResourceService implements UserResource {
    @PersistenceContext
    private EntityManager em;

    @Context
    private UriInfo uri;

    @Inject
    private AuthenticationManager authenticationManager;

    @Override
    public Response registerAdmin(User user) {
        // TODO: What if a user already exists?
        em.persist(user);

        UriBuilder uriBuilder = UriBuilder.fromUri(uri.getRequestUri());
        uriBuilder.path("{id}");

        EntityTag etag = new EntityTag(Hex.encodeHexString(user.toString().getBytes()));

        return Response.created(uriBuilder.build(user.getId()))
                .tag(etag)
                .entity(user)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public Response getUserByID(@PathParam("id") Long id) {
        User user = em.find(User.class, id);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        UriBuilder uriBuilder = UriBuilder.fromUri(uri.getRequestUri());
        uriBuilder.path("{id}");

        EntityTag etag = new EntityTag(Hex.encodeHexString(user.toString().getBytes()));

        return Response.created(uriBuilder.build(user.getId()))
                .tag(etag)
                .entity(user)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public Response loginAdmin(@Context HttpHeaders headers) {
        if (authenticationManager.validateBasicAuth(headers)) {
            String[] credentials = BasicAuth.decode(headers.getRequestHeader("authorization").get(0));
            User user = em.createNamedQuery(User.findUserByEmail, User.class).setParameter("email", credentials[0]).getSingleResult();

            EntityTag etag = new EntityTag(Hex.encodeHexString(user.toString().getBytes()));
            return Response.ok(user).tag(etag).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}