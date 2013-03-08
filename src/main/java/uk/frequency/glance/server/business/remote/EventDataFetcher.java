package uk.frequency.glance.server.business.remote;

import uk.frequency.glance.server.business.logic.GeometryUtil;
import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.component.Position;

public class EventDataFetcher {

	static final String googleAPIsKey = "AIzaSyByhzaP3j5iMrMSw_hnMQUTugiVH0cTldc"; //TODO get from properties
	static final String imageSize = "200x200"; //TODO find better place to define it
	static final int imageMaxHeight = 200;
	static final int searchRadius = 100;
	
	public static void main(String[] args) {
		Position p = new Position();
		p.setLat(51.52261);
		p.setLng(-0.085541);
		EventDataFetcher fetcher = new EventDataFetcher(p);
		System.out.println(fetcher.getLocation().getName());
		System.out.println(fetcher.getLocation().getAddress());
		System.out.println(fetcher.getLocation().getPosition());
		System.out.println(fetcher.getImageUrl());
	}
	
	Location location;
	String imageUrl;
	
	public EventDataFetcher(Position pos) {
		loadInfo(pos);
	}

	private void loadInfo(Position pos){
		GooglePlaces places = new GooglePlaces(pos, searchRadius);
		int index = GeometryUtil.findCloser(pos, places.getResultPositions());
		location = places.getLocation(index);
		imageUrl = places.getImageUrl(index, imageMaxHeight);
		
		//TODO if no image, get from streetview. if no name, get from geocoding
//		String imageUrl = GoogleStreetView.getStreetViewImageUrl(pos);
//		String name = GoogleGeocoding.getLocationName(pos);
	}
	
	public Location getLocation(){
		return location;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	
}
