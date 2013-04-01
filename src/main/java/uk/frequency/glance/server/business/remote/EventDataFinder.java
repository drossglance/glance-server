package uk.frequency.glance.server.business.remote;

import java.util.List;

import uk.frequency.glance.server.business.logic.geometry.LatLngGeometryUtil;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Position;

public class EventDataFinder {

	static final String googleAPIsKey = "AIzaSyByhzaP3j5iMrMSw_hnMQUTugiVH0cTldc"; //TODO get from properties
	static final String imageSize = "200x200"; //TODO find better place to define it
	static final int imageMaxHeight = 200;
	static final int searchRadius = 100;
	
//	public static void main(String[] args) {
//		Position p = new Position();
//		p.setLat(51.552873);
//		p.setLng(-0.08287);
//		new EventDataFinder(p);
//	}
	
	Location location;
	String imageUrl;
	
	public EventDataFinder(Position pos) {
		loadInfo(pos);
	}

	private void loadInfo(Position pos){
		GooglePlaces places = new GooglePlaces(pos, searchRadius);
		List<Position> results = places.getResultPositions();
		int index = LatLngGeometryUtil.findCloser(pos, results);
		location = places.getLocation(index);
		imageUrl = places.getImageUrl(index, imageMaxHeight);
		
		if(imageUrl == null){
			imageUrl = GoogleStreetView.getImageUrl(pos);
		}
		
		//TEST
//		double dist = LatLngGeometryUtil.distance(pos, results.get(index));
//		String streetViewImage = GoogleStreetView.getStreetViewImageUrl(pos);
//		String geocodingName = GoogleGeocoding.getLocationName(pos);
//		System.out.println(location.getName());
//		System.out.println(location.getAddress());
//		System.out.println(location.getPosition());
//		System.out.println(imageUrl);
//		System.out.println(LatLngGeometryUtil.degreesToMeters(dist) + "m");
//		System.out.println(geocodingName);
//		System.out.println(streetViewImage);
	}
	
	public Location getLocation(){
		return location;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	
}
