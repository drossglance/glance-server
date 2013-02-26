package uk.frequency.glance.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.business.GenericBL;
import uk.frequency.glance.server.model.GenericEntity;
import uk.frequency.glance.server.transfer.GenericDTO;


/**
 * @author Victor Basso
 * TODO inject the DALs through spring
 */
public abstract class GenericSL<T extends GenericEntity, U extends GenericDTO> {

	GenericBL<T> business;
	
	@Context UriInfo uriInfo;
	@Context Request request;

	public GenericSL(GenericBL<T> dao) {
		this.business = dao;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<U> findAll(){
		List<T> list = business.findAll();
		return toDTO(list);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") long id) {
		try{
			T entity = business.findById(id);
			return Response.status(Status.OK)
					.entity(toDTO(entity)).build();
		}catch(ObjectNotFoundException e){
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response create(U dto){
		try{
			T entity = fromDTO(dto);
			business.create(entity); //TODO capture ConstraintViolationExceptions
			URI uri = uriInfo.getAbsolutePathBuilder().path("{index}").build(entity.getId());
			return Response.created(uri).build();
		}catch(ConstraintViolationException e){
			return Response.status(Status.CONFLICT)
					.entity("Object contains an invalid reference.").build();
		}catch(TransientObjectException e){
			return Response.status(Status.CONFLICT)
					.entity("Object lacks a required reference.").build();
		}
	}
	
	protected List<U> toDTO(List<? extends T> list){
		List<U> dto = new ArrayList<U>();
		for(T entity : list){
			dto.add(toDTO(entity));
		}
		return dto;
	}
	
	protected abstract U toDTO(T entity);
	
	protected abstract T fromDTO(U dto);
	
}