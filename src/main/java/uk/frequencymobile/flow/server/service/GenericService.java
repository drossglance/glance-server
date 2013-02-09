package uk.frequencymobile.flow.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import uk.frequencymobile.flow.server.data.GenericDAO;
import uk.frequencymobile.flow.server.model.GenericEntity;
import uk.frequencymobile.flow.server.transfer.GenericDTO;


/**
 * @author Victor Basso
 * TODO inject the DAOs through spring
 */
public abstract class GenericService<T extends GenericEntity, U extends GenericDTO> {

	GenericDAO<T> dao;
	
	@Context UriInfo uriInfo;
	@Context Request request;
	
	public GenericService(GenericDAO<T> dao) {
        this.dao = dao;
     }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<U> findAll(){
		List<T> list = dao.findAll();
		return toDTO(list);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public U findById(@PathParam("id") long id) {
		T entity = dao.findById(id);
		return toDTO(entity);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response create(U dto){
		T entity = fromDTO(dto);
		dao.makePersistent(entity);
		URI uri = uriInfo.getBaseUriBuilder().path(""+entity.getId()).build(); //TODO missing the enitity type in path
		return Response.created(uri).build();
	}
	
	protected List<U> toDTO(List<T> list){
		List<U> dto = new ArrayList<U>();
		for(T entity : list){
			dto.add(toDTO(entity));
		}
		return dto;
	}
	
	protected abstract U toDTO(T entity);
	
	protected abstract T fromDTO(U dto);
	
}