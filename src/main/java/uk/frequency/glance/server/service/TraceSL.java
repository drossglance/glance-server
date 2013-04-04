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
import uk.frequency.glance.server.model.trace.SleepTrace;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.transfer.trace.ListenTraceDTO;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;
import uk.frequency.glance.server.transfer.trace.SleepTraceDTO;
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
	public List<TraceDTO> findByUser(@PathParam("id") long userId) {
//		List<Trace> list = traceBl.findByUser(userId);
		List<Trace> list = traceBl.findRecent(userId);
		List<TraceDTO> dto = toDTO(list);
		traceBl.flush();
		return dto;
	}
	
	@GET
	@Path("/user-{id}/{begin}to{end}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TraceDTO> findByUser(
			@PathParam("id") long userId, 
			@PathParam("begin") long begin, 
			@PathParam("end") long end) {
		List<Trace> list = traceBl.find(userId, new Date(begin), new Date(end));
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
			posDto.position = pos.getPosition();
			posDto.speed = pos.getSpeed();
			dto = posDto;
		}else if(trace instanceof ListenTrace){
			ListenTrace listen = (ListenTrace)trace;
			ListenTraceDTO listenDto = new ListenTraceDTO();
			listenDto.songMetadata = listen.getSongMetadata();
			dto = listenDto;
		}else if(trace instanceof SleepTrace){
			SleepTrace sleep = (SleepTrace)trace;
			SleepTraceDTO sleepDto = new SleepTraceDTO();
			sleepDto.begin = sleep.isBegin();
			dto = sleepDto;
		}else{
			throw new AssertionError();
		}
		
		dto.initFromEntity(trace);
		dto.userId = trace.getUser().getId();
		dto.time = trace.getTime().getTime();
		return dto;
	}
	
	@Override
	protected Trace fromDTO(TraceDTO dto) {

		User user = new User();
		user.setId(dto.userId);
		
		Trace trace = null;
		if(dto instanceof PositionTraceDTO){
			PositionTraceDTO posDto = (PositionTraceDTO)dto;
			PositionTrace pos = new PositionTrace();
			pos.setPosition(posDto.position);
			pos.setSpeed(posDto.speed);
			trace = pos;
		}else if(dto instanceof ListenTraceDTO){
			ListenTraceDTO listenDto = (ListenTraceDTO)dto;
			ListenTrace listen = new ListenTrace();
			listen.setSongMetadata(listenDto.songMetadata);
			trace = listen;
		}else if(dto instanceof SleepTraceDTO){
			SleepTraceDTO sleepDto = (SleepTraceDTO)dto;
			SleepTrace sleep = new SleepTrace();
			sleep.setBegin(sleepDto.begin);
			trace = sleep;
		}else{
			throw new AssertionError();
		}

		dto.initEntity(trace);
		trace.setUser(user);
		trace.setTime(new Date(dto.time));
		
		return trace;
	}
	
}
