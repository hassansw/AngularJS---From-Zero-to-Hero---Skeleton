package io.devcamp.blog.rest;

import io.devcamp.blog.Marker;
import io.devcamp.blog.model.User;
import io.devcamp.blog.rest.api.UserResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RunWith(Arquillian.class)
public class UserResourceServiceTest {
    @Inject
    private UserResource userResource;

    private Client client;

    public UserResourceServiceTest() {
        this.client = ClientBuilder.newClient();
    }

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, Marker.class.getPackage())
                .addAsLibraries(Maven.resolver().resolve("commons-codec:commons-codec:1.9").withTransitivity().asFile())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");
    }

    @Test
    @RunAsClient
    public void testRegisterAdmin() {
        User admin = new User("Daniel", "Sachse", "mail@wombatsoftware.de", "sup3rs3cr3t");

        Response response = client.target("http://localhost:8080/test/rest/users/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(admin));

        Assert.assertEquals(201, response.getStatusInfo().getStatusCode());
    }

    @Test
    public void testGetUserByID() {

    }

    @Test
    public void testLoginAdmin() {

    }
}