package io.devcamp.blog.websocket;

import io.devcamp.blog.cdi.qualifier.Created;
import io.devcamp.blog.model.Post;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Daniel Sachse
 * @date 05.02.14 00:05
 */

@ServerEndpoint("/websocket")
public class WebSocketEndpoint {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    private Logger logger;

    @OnOpen
    public void onOpen(final Session session) {
        logger.info("Added Session");
        sessions.add(session);
    }

    @OnClose
    public void onClose(final Session session) {
        logger.info("Closed Session");
        sessions.remove(session);
    }

    public void catchNewPosts(@Observes @Created Post post) {
        logger.info("WebSocketEndpoint catched new post");

        try {
            for (Session s : sessions) {
                s.getBasicRemote().sendText("new-post");
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
        }
    }
}