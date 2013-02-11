package uk.frequency.glance.server.business;

import java.awt.Image;
import java.util.List;

import uk.frequency.glance.server.data_access.EventDAL;
import uk.frequency.glance.server.model.Event;
import uk.frequency.glance.server.util.GoogleAPIs;

public class EventBL extends GenericBL<Event>{

	EventDAL eventDal;

	public EventBL() {
		super(new EventDAL());
		eventDal = (EventDAL)dal;
	}
	
	public List<Event> findByAuthor(long userId) {
		List<Event> list = eventDal.findByAuthor(userId);
		return list;
	}
	
	public void generateEvent(double lat, double lng){
		Image image = GoogleAPIs.requestStreetViewImage(lat, lng);
		
		//TODO
	}
	
}
