package io.devcamp.blog.rest;

import io.devcamp.blog.model.File;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Daniel Sachse
 * @date 22.01.14 21:22
 */

@Stateless
@Path("/images/")
public class ImageResourceService implements io.devcamp.blog.rest.api.ImageResource {
    @PersistenceContext
    private EntityManager em;

    @Context
    private UriInfo uri;

    @Override
    public Response addImage(@Context HttpHeaders headers, MultipartFormDataInput multipartFormDataInput) {
        String fileName = "";
        String fileURL = "";
        URI newUri = null;

        Map<String, List<InputPart>> uploadForm = multipartFormDataInput.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);
                String contentType = header.getFirst("Content-Type");

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                byte[] bytes = IOUtils.toByteArray(inputStream);

                String uuid = UUID.randomUUID().toString();

                //constructs upload file path
                UriBuilder uriBuilder = UriBuilder.fromUri(uri.getRequestUri());
                uriBuilder.path("{uuid}");

                File file = new File();
                file.setUuid(uuid);
                file.setContent(bytes);
                file.setFilename(fileName);
                file.setContentType(contentType);

                em.persist(file);

                newUri = uriBuilder.build(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Response.created(newUri).build();
    }

    @Override
    public Response getImage(@PathParam("uuid") String uuid) {
        File file = em.createNamedQuery(File.findFileByUUID, File.class).setParameter("uuid", uuid).getSingleResult();

        Response.ResponseBuilder response = Response.ok(file.getContent());
        response.header("Content-Disposition", "attachment; filename=" + file.getFilename());
        response.type(file.getContentType());

        return response.build();
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }

        return "unknown";
    }
}