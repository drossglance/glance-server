package uk.frequency.glance.server.business.remote;

import java.util.List;

import uk.frequency.glance.server.model.component.Position;

@SuppressWarnings("unused")
public class GooglePlaces {

	static final String placesUrl = "https://maps.googleapis.com/maps/api/place/";
	static final String key = GoogleAPIs.key; //TODO get from properties
	static final String imageSize = GoogleAPIs.imageSize; //TODO find better place to define it
	
	private RemoteAPIClient client = new RemoteAPIClient(placesUrl);
	
	private List<Place> results;
	
	public static void main(String[] args) {
		GooglePlaces gp = new GooglePlaces();
		Position pos = new Position();
		pos.setLat(51.524666);
		pos.setLng(-0.086839);
		gp.requestPlaceInfo(pos, 20);
	}
	
	public void requestPlaceInfo(Position pos, int radius){
		String searchPath = "nearbysearch/json?" +
				"location=" + pos.getLat() + "," + pos.getLng() +
				"&radius=" + radius +
				"&sensor=true" +
				"&key=" + key;
		PlaceSearch results = client.getJson(searchPath, PlaceSearch.class);
		//TODO check status
	}
	
	public void requestPlaceDetails(int index){
		String ref = results.get(index).reference;
		String detailsPath = "details/json?" +
				"reference=" + ref +
				"&sensor=true" +
				"&key=" + key;
		PlaceDetails details = client.getJson(detailsPath, PlaceDetails.class);
		//TODO check status
		results.set(index, details.result);
	}
	
	public List<String> getResultNames(){
		//TODO
		return null;
	}
	
	private static abstract class Results{
		String status;
		String next_page_token;
		static enum Status {OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST, UNKNOWN_ERROR, NOT_FOUND}
	}
	
	private static class PlaceSearch extends Results{
		List<Place> results;
	}
	
	private static class PlaceDetails extends Results{
		DetailedPlace result;
	}
	
	private static class Place{
		String formatted_address;
		Geometry geometry;
		String name;
		String reference;
		List<String> types;
		String vicinity;
		String icon;
		List<Photo> photos;
	}
	
	private static class DetailedPlace extends Place{
		List<AddressComponent> address_components;
		double rating;
		String url;
		String website;
	}
	
	private static class Photo{
		int height;
		int width;
		List<String> html_attributions;
		String photo_reference;
		static class HtmlAttributions{
			String USER = "From a Google User";
		}
	}
	
	private static class Geometry{
		Position location;
	}

	private static class AddressComponent{
		String long_name;
		String short_name;
		List<String> types;
	}
	
	
}
