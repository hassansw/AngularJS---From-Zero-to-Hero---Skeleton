package io.devcamp.blog.security;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

/**
 * @author Daniel Sachse
 * @date 10.02.14 22:06
 */
public class BasicAuth {
    public static String[] decode(String auth) {
        String[] decoded = null;

        auth = auth.substring("Basic ".length());
        decoded = new String(Base64.decodeBase64(auth)).split(":");

        return decoded;
    }
}