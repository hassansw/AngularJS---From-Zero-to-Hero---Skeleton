package io.devcamp.blog.cdi.event;

import io.devcamp.blog.model.Post;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author Daniel Sachse
 * @date 04.02.14 23:24
 */
public class EventListener {
    @Inject
    private Logger logger;

    public void catchAllPosts(@Observes Post post) {
        logger.info("Catched new Post #" + post.getId() + " with title: " + post.getTitle());
    }
}