package uk.frequency.glance.server.business.remote;

import uk.frequency.glance.server.model.component.Position;

public class GoogleStreetView {

	static final String rootUrl = "http://maps.googleapis.com/maps/api/streetview";
	
	static final String imageSize = EventDataFetcher.imageSize;
	static final String key = EventDataFetcher.googleAPIsKey;
	
	public static String getStreetViewImageUrl(double lat, double lng, int heading){
		String url = rootUrl + 
				"?size=" + imageSize + 
				"&location=" + lat + ",%20" + lng +
				"&heading=" + heading + 
				"&sensor=true" +
				"&key="+ key;
		return url;
	}
	
	public static String getStreetViewImageUrl(Position pos){
		String url = rootUrl + 
				"?size=" + imageSize + 
				"&location=" + pos.getLat() + ",%20" + pos.getLng() +
				"&sensor=true" +
				"&key="+ key;
		return url;
	}
	
}
