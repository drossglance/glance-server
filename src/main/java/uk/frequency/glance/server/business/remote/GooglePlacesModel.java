package uk.frequency.glance.server.business.remote;

import java.util.ArrayList;
import java.util.List;

import uk.frequency.glance.server.model.component.Location;
import uk.frequency.glance.server.model.component.Position;

@SuppressWarnings("unused")
public class GooglePlacesModel {

	static abstract class Results{
		static enum Status {OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST, UNKNOWN_ERROR, NOT_FOUND}
		Status status;
		String next_page_token;
	}
	
	static class PlaceSearch extends Results{
		List<Place> results;
	}
	
	static class PlaceDetails extends Results{
		DetailedPlace result;
	}
	
	static class Place{
		String formatted_address; //useful: maybe for the event details page
		Geometry geometry;
		String name; //useful
		String reference;
		List<String> types; //useful: for distinguishing pub, work, gym, etc
		String vicinity;
		String icon;
		List<Photo> photos;
	}
	
	static class DetailedPlace extends Place{
		List<AddressComponent> address_components;
		double rating;
		String url;
		String website;  //useful: maybe for the event details page
	}
	
	static class Photo{
		int height;
		int width;
		List<String> html_attributions;
		String photo_reference;
		static class HtmlAttributions{
			String USER = "From a Google User";
		}
	}
	
	static class Geometry{
		Position location; //useful: consistent position for a given place
	}

	static class AddressComponent{
		String long_name;
		String short_name;
		List<String> types;
	}
	
	
}
