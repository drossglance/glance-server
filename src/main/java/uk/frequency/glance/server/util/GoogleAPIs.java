package uk.frequency.glance.server.util;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

public class GoogleAPIs {

	static String streetViewUrl = "http://maps.googleapis.com/maps/api/streetview";
	static String geocodingUrl = "http://maps.googleapis.com/maps/api/geocode/json";
	static String placesUrl = "https://maps.googleapis.com/maps/api/place/";
	
	static String key = "AIzaSyByhzaP3j5iMrMSw_hnMQUTugiVH0cTldc"; //TODO get from properties
	
	static String imageSize = "200x200"; 
	
	public static String getStreetViewImageUrl(double lat, double lng, int heading){
		String url = streetViewUrl + 
				"?size=" + imageSize + 
				"&location=" + lat + ",%20" + lng +
				"&heading=" + heading + 
				"&sensor=true" +
				"&key="+ key;
		return url;
	}
	
	public static String getStreetViewImageUrl(double lat, double lng){
		String url = streetViewUrl + 
				"?size=" + imageSize + 
				"&location=" + lat + ",%20" + lng +
				"&sensor=true" +
				"&key="+ key;
		return url;
	}
	
//	public static String inverseGeocode(double lat, double lng){
//		String url = geocodingUrl + 
//				"?size=" + imageSize + 
//				"&latlng=" + lat + ",%20" + lng +
//				"&sensor=true" +
//				"&key="+ key;
//		return url;
//	}
	
	//TODO get from google places
	public static String getAddress(double lat, double lng){
		Geocoder geo = new Geocoder(); //TODO create clientId? https://developers.google.com/maps/documentation/business/guide
		LatLng latlng = new LatLng(""+lat, ""+lng);
		GeocoderRequest req = new GeocoderRequestBuilder().setLocation(latlng).getGeocoderRequest();
		GeocodeResponse res = geo.geocode(req);
		
		GeocoderStatus status = res.getStatus();
		if(!status.equals(GeocoderStatus.OK))
			throw new RuntimeException("geocoding api returned status: " + status);
		
//		for(GeocoderResult result : res.getResults()){
//			for(GeocoderAddressComponent comp : result.getAddressComponents()){
//				System.out.println(comp.getTypes().toString() + ": " + comp.getShortName());
//			}
//			/*TEST*/System.out.println();
//		}
		
		String address = res.getResults().get(0).getFormattedAddress();
		return address;
	}

}
