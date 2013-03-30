package uk.frequency.glance.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
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
		List<U> dto = toDTO(list);
		business.flush();
		return dto;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public U findById(@PathParam("id") long id) {
		try{
			T entity = business.findById(id);
			U dto = toDTO(entity);
			business.flush();
			return dto;
		}catch(ObjectNotFoundException e){
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@GET
	@Path("/created_after-{time}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<U> findCreatedAfter(@PathParam("time") long time) {
		try{
			List<T> entity = business.findCreatedAfter(new Date(time));
			List<U> dto = toDTO(entity);
			business.flush();
			return dto;
		}catch(ObjectNotFoundException e){
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(U dto){
		try{
			T entity = fromDTO(dto);
			business.create(entity);
			U newDto = toDTO(entity); //needed to capture data created on the server side (like id, creationTime, updateTime)
			URI uri = uriInfo.getAbsolutePathBuilder().path("{index}").build(entity.getId());
			business.flush();
			return Response.created(uri).
					entity(newDto).build();
		}catch(ConstraintViolationException e){
			return Response.status(Status.CONFLICT)
					.entity(e.getMessage()).build();
		}catch(TransientObjectException e){
			return Response.status(Status.CONFLICT)
					.entity(e.getMessage()).build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") long id){
		try{
			business.deleteById(id);
			business.flush();
			return Response.status(Status.NO_CONTENT).build();
		}catch(ObjectNotFoundException e){
			throw new WebApplicationException(Status.NOT_FOUND);
		}catch(ConstraintViolationException e){
			return Response.status(Status.CONFLICT)
					.entity(e.getMessage()).build();
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