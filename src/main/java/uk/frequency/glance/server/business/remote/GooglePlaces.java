package uk.frequency.glance.server.business.remote;

import java.util.ArrayList;
import java.util.List;

import uk.frequency.glance.server.business.logic.LatLngGeometryUtil;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Position;
import static uk.frequency.glance.server.business.remote.GooglePlacesModel.*;
import static uk.frequency.glance.server.business.remote.GooglePlacesModel.Results.Status.*;

public class GooglePlaces {

	static final String rootUrl = "https://maps.googleapis.com/maps/api/place/";
	
	static final String key = EventDataFinder.googleAPIsKey;
	
	private RemoteAPIClient client = new RemoteAPIClient(rootUrl);
	
	private Position requestedPosition;
	private List<Place> results;
	
	//TEST
	public static void main(String[] args) {
		int radius = 100;
		showDistancesAndRatings("Victor's",					new Position(51.586896,-0.01582), 	radius);
		showDistancesAndRatings("Fionn's", 					new Position(51.535036,-0.252256), 	radius);
		showDistancesAndRatings("George's", 				new Position(51.519866,-0.201101), 	radius);
		showDistancesAndRatings("Google Campus", 			new Position(51.522628,-0.085699), 	radius);
		showDistancesAndRatings("Chelsea Stadium", 			new Position(51.48141,-0.19136), 	radius);
		showDistancesAndRatings("Liverpool St. Station", 	new Position(51.517129,-0.08244), 	radius);
		showDistancesAndRatings("Tesco", 					new Position(51.522802,-0.086995), 	radius);
		showDistancesAndRatings("Sainsbury's", 				new Position(51.524871,-0.087443), 	radius);
		showDistancesAndRatings("O2 Brixton Academy", 		new Position(51.465652,-0.115013), 	radius);
		showDistancesAndRatings("Passing Clouds", 			new Position(51.54231,-0.075569), 	radius);
		showDistancesAndRatings("Portobello Rd", 			new Position(51.517262,-0.206122), 	radius);
	}
	
	//TEST
	static void showDistancesAndRatings(String title, Position pos, int radius){
		System.out.printf("#%s (%f,%f)\n", title, pos.getLat(), pos.getLng());
		GooglePlaces places = new GooglePlaces(pos, radius);
		places.showDistancesAndRatings();
	}
	
	//TEST
	void showDistancesAndRatings(){
		for(int i=0; i<results.size(); i++){
			requestPlaceDetails(i);
			DetailedPlace place = (DetailedPlace)results.get(i);
			int dist = (int)LatLngGeometryUtil.distanceInMeters(place.geometry.location, requestedPosition);
			System.out.printf("%d\t%.1f\t%s\n", dist, place.rating, place.name);
		}
		System.out.println();
	}
	
	public GooglePlaces(Position position, int radius) {
		this.requestedPosition = position;
		requestPlaceSearch(position, radius);
	}

	private void requestPlaceSearch(Position pos, int radius){
		String searchPath = "nearbysearch/json?" +
				"location=" + pos.getLat() + "," + pos.getLng() +
				"&radius=" + radius +
				"&sensor=true" +
				"&key=" + key;
		PlaceSearch search = client.getJson(searchPath, PlaceSearch.class);

		if(search.status == OK){
			this.results = search.results;
		}else{
			throw new RuntimeException("GooglePlaces returned an error: " + search.status);
		}
	}
	
	private void requestPlaceDetails(int index){
		String ref = results.get(index).reference;
		String detailsPath = "details/json?" +
				"reference=" + ref +
				"&sensor=true" +
				"&key=" + key;
		PlaceDetails details = client.getJson(detailsPath, PlaceDetails.class);
		
		if(details.status == OK){
			results.set(index, details.result);
		}else{
			throw new RuntimeException("GooglePlaces returned an error: " + details.status);
		}
	}
	
	private String buildImageUrl(String reference, int maxHeight){
		String url = rootUrl + "photo?" +
				"photoreference=" + reference +
				"&sensor=true" +
				"&maxheight=" + maxHeight +
				"&key=" + key;
		return url;
	}
	
	public List<Position> getResultPositions(){
		List<Position> positions = new ArrayList<Position>();
		for(Place place : results){
			positions.add(place.geometry.location);
		}
		return positions;
	}
	
	public Location getLocation(int index){
		Place place = getDetailedPlace(index);
		Location location = new Location();
		location.setName(place.name);
		location.setAddress(place.vicinity);
		location.setPosition(place.geometry.location);
		return location;
	}
	
	public String getImageUrl(int index, int maxHeight){
		Place place = getDetailedPlace(index);
		if(place.photos != null && !place.photos.isEmpty()){
			String ref = place.photos.get(0).photo_reference;
			return buildImageUrl(ref, maxHeight);
		}else{
			return null;
		}
	}
	
	private DetailedPlace getDetailedPlace(int index){
		if(!(results.get(index) instanceof DetailedPlace)){
			requestPlaceDetails(index);
		}
		return (DetailedPlace)results.get(index);
	}
	
}
