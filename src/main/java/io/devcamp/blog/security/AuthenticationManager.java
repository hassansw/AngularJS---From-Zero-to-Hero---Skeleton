package io.devcamp.blog.security;

import io.devcamp.blog.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

/**
 * @author Daniel Sachse
 * @date 10.02.14 22:08
 */

@Stateless
public class AuthenticationManager {
    @PersistenceContext
    private EntityManager em;

    public boolean validateBasicAuth(HttpHeaders headers) {
        //Get the authentification passed in HTTP headers parameters
        List<String> header = headers.getRequestHeader("authorization");

        //If the user does not provide any HTTP Basic Auth)
        if (header == null || header.size() == 0) {
            return false;
        }

        String authorization = header.get(0);

        //lap : loginAndPassword
        String[] credentials = BasicAuth.decode(authorization);

        //If login or password fail
        if (credentials == null || credentials.length != 2) {
            return false;
        }

        try {
            User authentificationResult = em.createNamedQuery(User.findUserByEmail, User.class).setParameter("email", credentials[0]).getSingleResult();

            // User not found or passwords missmatch. IMPORTANT: Encrypt PW in real-world-application
            if (authentificationResult == null || !authentificationResult.getPassword().equals(credentials[1])) {
                return false;
            }
        } catch (NoResultException nre) {
            return false;
        }

        return true;
    }
}
