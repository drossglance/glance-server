package uk.frequency.glance.server.service;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import uk.frequency.glance.server.business.UserBL;
import uk.frequency.glance.server.data_access.StaticResourcesLoader;

@Path("/static")
public class StaticResourcesService {

	@Context UriInfo uriInfo;
	@Context Request request;
	
	UserBL userBl;
	
	@GET
	@Path("/{path : .+}")
	@Produces("image/png")
	public Response loadStaticImage(@PathParam("path") String path) {
		try {
			byte[] out = StaticResourcesLoader.loadImage(path);
			return Response.ok(out).build();
		} catch (IOException e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}
	
}
