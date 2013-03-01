package uk.frequency.glance.server.service;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.frequency.glance.server.business.TraceBL;
import uk.frequency.glance.server.model.trace.ListenTrace;
import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.transfer.trace.ListenTraceDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.TraceDTO;


@Path("/trace")
public class TraceSL extends GenericSL<Trace, TraceDTO>{

	TraceBL traceBl;

	public TraceSL() {
		super(new TraceBL());
		traceBl = (TraceBL)business;
	}
	
	@GET
	@Path("/user-{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TraceDTO> findByAuthor(@PathParam("id") long userId) {
		List<Trace> list = traceBl.findByUser(userId);
		List<TraceDTO> dto = toDTO(list);
		traceBl.flush();
		return dto;
	}
	
	@Override
	protected TraceDTO toDTO(Trace trace) {
		
		TraceDTO dto = null;
		
		if(trace instanceof PositionTrace){
			PositionTrace pos = (PositionTrace)trace;
			PositionTraceDTO posDto = new PositionTraceDTO();
			posDto.setPosition(pos.getPosition());
			posDto.setSpeed(pos.getSpeed());
			dto = posDto;
		}else if(trace instanceof ListenTrace){
			ListenTrace listen = (ListenTrace)trace;
			ListenTraceDTO listenDto = new ListenTraceDTO();
			listenDto.setSongMetadata(listen.getSongMetadata());
			dto = listenDto;
		}else{
			throw new AssertionError();
		}
		
		initToDTO(trace, dto);
		dto.setUserId(trace.getUser().getId());
		dto.setTime(trace.getTime().getTime());
		return dto;
	}
	
	@Override
	protected Trace fromDTO(TraceDTO dto) {

		User user = new User();
		user.setId(dto.getUserId());
		
		Trace trace = null;
		if(dto instanceof PositionTraceDTO){
			PositionTraceDTO posDto = (PositionTraceDTO)dto;
			PositionTrace pos = new PositionTrace();
			pos.setPosition(posDto.getPosition());
			pos.setSpeed(posDto.getSpeed());
			trace = pos;
		}else if(dto instanceof ListenTraceDTO){
			ListenTraceDTO listenDto = (ListenTraceDTO)dto;
			ListenTrace listen = new ListenTrace();
			listen.setSongMetadata(listenDto.getSongMetadata());
			trace = listen;
		}else{
			throw new AssertionError();
		}

		initFromDTO(dto, trace);
		trace.setUser(user);
		trace.setTime(new Date(dto.getTime()));
		
		return trace;
	}
	
}
