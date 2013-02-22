package uk.frequency.glance.server.business;

import java.util.Date;
import java.util.List;

import org.hibernate.ObjectNotFoundException;

import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.data_access.UserDAL;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.Media.MediaType;
import uk.frequency.glance.server.model.User;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.event.Event.EventType;
import uk.frequency.glance.server.util.GoogleAPIs;

public class EventBL extends GenericBL<Event>{

	EventDAL eventDal;
	UserDAL userDal;

	public EventBL() {
		super(new EventDAL());
		eventDal = (EventDAL)dal;
		userDal = new UserDAL();
	}
	
	public List<Event> findByAuthor(long userId) throws ObjectNotFoundException {

		User user = userDal.findById(userId);
		user.getId(); //make hibernate do the actual select so it can throw ObjectNotFound. TODO understand why hibernate doesn't throw the exception without this, find appropriate way to accomplish this. 
		
		List<Event> list = eventDal.findByAuthor(userId);
		return list;
	}
	
	public long generateEvent(Location location, long userId){
		String imageUrl = GoogleAPIs.getStreetViewImageUrl(location.getPosition()); //TODO get more from google places and also streetview with different headings, choose best image somehow
		String address = GoogleAPIs.getLocationName(location.getPosition());
		String description = null; //TODO get from google places

		location.setAddress(address);
		location.setName(description);
		MediaType mediaType = MediaType.IMAGE;
		Media media = new Media();
		media.setType(mediaType);
		media.setUrl(imageUrl);
		User user = new User();
		user.setId(userId);
		StayEvent event = new StayEvent();
		event.setType(EventType.STAY);
		event.setCreationTime(new Date()); //TODO set this as default value directly on the database
		event.setAuthor(user);
		event.setStartTime(new Date());
		event.setEndTime(new Date());
		event.setLocation(location);
		event.setMedia(media);
		event.setScore(new EventScore());
		
		makePersistent(event);
		
		return event.getId();
	}
	
}
