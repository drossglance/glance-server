package uk.frequency.glance.server.util;

import java.awt.Image;

public class GoogleAPIs {

	static String streetViewUrl = "http://maps.googleapis.com/maps/api/streetview";
	static String geocodingUrl = "http://maps.googleapis.com/maps/api/geocode/";
	static String placesUrl = "https://maps.googleapis.com/maps/api/place/";
	static String key = "AIzaSyByhzaP3j5iMrMSw_hnMQUTugiVH0cTldc";
	
	static String imageSize = "200x200"; 
	
	private static String streetViewImageUrl(double lat, double lng, int heading){
		String url = streetViewUrl + 
				"?size=" + imageSize + 
				"&location=" + lat + ",%20" + lng +
				"&heading=" + heading + 
				"&sensor=true" +
				"&key="+ key;
		return url;
	}
	
	private static String streetViewImageUrl(double lat, double lng){
		String url = streetViewUrl + 
				"?size=" + imageSize + 
				"&location=" + lat + ",%20" + lng +
				"&sensor=true" +
				"&key="+ key;
		return url;
	}

	public static Image requestStreetViewImage(double lat, double lng, int heading){
		String url = streetViewImageUrl(lat, lng, heading);
		//TODO
		return null;
	}

	public static Image requestStreetViewImage(double lat, double lng){
		String url = streetViewImageUrl(lat, lng);
		//TODO
		return null;
	}
	
}
