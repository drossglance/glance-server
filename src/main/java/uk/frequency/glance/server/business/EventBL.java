package uk.frequency.glance.server.business;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.business.logic.PresentationUtil;
import uk.frequency.glance.server.business.logic.event.EventGenerationLogic;
import uk.frequency.glance.server.business.logic.waveline.BasicColorPicker;
import uk.frequency.glance.server.business.logic.waveline.WavelineDataAdapter;
import uk.frequency.glance.server.business.logic.waveline.WavelineRenderer;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.ColorPicker;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.Layer;
import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.TraceDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.User;

public class EventBL extends GenericBL<Event>{

	private final EventDAL eventDal;
	private final TraceDAL traceDal;
	private final UserDAL userDal;

	public EventBL() {
		super(new EventDAL());
		eventDal = (EventDAL)dal;
		traceDal = new TraceDAL();
		userDal = new UserDAL();
	}
	
	public List<Event> findByUser(long userId) throws ObjectNotFoundException {
		return eventDal.findByUser(userId);
	}
	
	public List<Event> findRecent(long userId, int limit){
		return eventDal.findRecent(userId, limit);
	}

	public List<Event> findByTimeRange(long userId, Date start, Date end){
		return eventDal.findByTimeRange(userId, start, end);
	}
	
	public List<Event> findCreatedAfter(long userId, Date time) throws ObjectNotFoundException {
		return eventDal.findCreatedAfter(userId, time);
	}
	
	public Event findMostRecent(long userId){
		return eventDal.findMostRecent(userId);
	}
	
	public byte[] generateWaveline(long userId, int width, int height) throws IOException{
		List<Event> events = findByUser(userId);
		return generateWaveline(events, width, height);
	}
	
	public byte[] generateWaveline(long userId, long begin, long end, int width, int height) throws IOException{
		List<Event> events = findByTimeRange(userId, new Date(begin), new Date(end));
		return generateWaveline(events, width, height);
	}
	
	public byte[] generateWaveline(List<Event> events, int width, int height) throws IOException{
		Layer[] layers = new WavelineDataAdapter().buildLayers(events);
		ColorPicker coloring = new BasicColorPicker(PresentationUtil.WAVELINE_BLUE_SHADES);
		WavelineRenderer waveline = new WavelineRenderer(layers, coloring);
		RenderedImage img = waveline.render(width, height);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "png", out);
		out.flush();
		
		return out.toByteArray();
	}
	
	public void onTraceReceived(Trace trace){
		new EventGenerationLogic(trace, eventDal, traceDal, userDal).start();
	}
	
	public void onUserCreated(User user){
		StayEvent join = new StayEvent();
		join.setType(EventType.JOIN);
		join.setStartTime(user.getCreationTime());
		join.setEndTime(user.getCreationTime());
		join.setSingleImage(user.getProfileHistory().get(0).getImageUrl());
		join.setUser(user);
		eventDal.save(join);
	}
	
}
