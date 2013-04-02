package uk.frequency.glance.server.business.remote;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.frequency.glance.server.business.logic.geometry.BoundingBox;
import uk.frequency.glance.server.business.logic.geometry.EuclideanGeometryUtil;
import uk.frequency.glance.server.model.component.Position;

public class GoogleStaticMaps {

	static final String rootUrl = "http://maps.googleapis.com/maps/api/staticmap";
	static final String key = EventDataFinder.googleAPIsKey;
	
	int width, height;
	
	public GoogleStaticMaps(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public String getImageUrl(double lat, double lng, int zoom){
		String url = rootUrl + 
				"?size=" + width + "x" + height +  
				"&center=" + lat + "," + lng +
				"&zoom=" + zoom + 
				"&sensor=false" +
				"&key="+ key;
		return url;
	}
	
	public String getImageUrl(Position pos, int zoom){
		return getImageUrl(pos.getLat(), pos.getLng(), zoom);
	}
	
	public String getImageUrl(List<Position> path){
		String url = rootUrl + 
				"?size=" + width + "x" + height + 
				"&path=" + locationListString(path) + 
				"&sensor=false" +
				"&key="+ key;
		return url;
	}
	
	public String getImageUrl(List<Position> markers, List<Position> path){
		String url = rootUrl + 
				"?size=" + width + "x" + height +
				"&markers=" + locationListString(markers) + 
				"&path=" + locationListString(path) + 
				"&sensor=false" +
				"&key="+ key;
		return url;
	}
	
	public String getImageUrl(BoundingBox bounds){
		String url = rootUrl + 
				"?size=" + width + "x" + height + 
				"&visible=" + locationListString(bounds) + 
				"&sensor=f8alse" +
				"&key="+ key;
		return url;
	}
	
	private static String locationListString(List<Position> path){
		StringBuilder s = new StringBuilder();
		for (Position position : path) {
			String posStr = String.format(Locale.US, "%f,%f|", position.getLat(), position.getLng());
			s.append(posStr);
		}
		s.deleteCharAt(s.length()-1);
		return NetUtil.urlEncode(s.toString());
	}
	
	private static String locationListString(BoundingBox bounds){
		List<Position> pos = new ArrayList<Position>();
		pos.add(bounds.getNECorner());
		pos.add(bounds.getSWCorner());
		return locationListString(pos);
	}
	
	public int findZoomToContain(BoundingBox bounds){
		Position center = bounds.findCenter();
		for(int zoom=20; zoom>=0; zoom--){
			Rectangle pixelBounds = findRectangle(center, zoom);
			BoundingBox newBounds = fromPixel(pixelBounds, zoom);
			if(newBounds.contains(bounds)){
				return zoom;
			}
		}
		return -1;
	}
	
	public Rectangle findRectangle(Position center, int zoom){
		Point c = toPixel(center, zoom);
		return EuclideanGeometryUtil.findRectangle(c, width, height);
	}
	
	public static int findZoomToContain(BoundingBox bounds, int width, int height){
		Position center = bounds.findCenter();
		for(int zoom=20; zoom>=0; zoom--){
			Point c = toPixel(center, zoom);
			Rectangle pixelBounds = EuclideanGeometryUtil.findRectangle(c, width, height);
			BoundingBox newBounds = fromPixel(pixelBounds, zoom);
			if(newBounds.contains(bounds)){
				return zoom;
			}
		}
		return -1;
	}
	
	public static Rectangle toPixel(BoundingBox bounds, int zoom){
		Position nw = bounds.getNWCorner();
		Position se = bounds.getSECorner();
		Point nwPixel = toPixel(nw, zoom);
		Point sePixel = toPixel(se, zoom);
		int width = sePixel.x - nwPixel.x;
		int height = sePixel.y - nwPixel.y;
		return new Rectangle(nwPixel.x, nwPixel.y, width, height);
	}
	
	public static BoundingBox fromPixel(Rectangle bounds, int zoom){
		Point nwPixel = new Point(bounds.x, bounds.y);
		Point sePixel = new Point(bounds.x+bounds.width, bounds.y+bounds.height);
		Position nw = fromPixel(nwPixel, zoom);
		Position se = fromPixel(sePixel, zoom);
		return new BoundingBox(nw, se);
	}
	
	public static List<Point> toPixel(List<Position> pos, int zoom){
		List<Point> pixel = new ArrayList<Point>();
		for (Position p : pos) {
			pixel.add(toPixel(p, zoom));
		}
		return pixel;
	}
	
	public static Point toPixel(Position pos, int zoom){
		double offset = 256 << (zoom-1);
		double lat = pos.getLat();
		double lng = pos.getLng();
	    int x = (int)Math.round(offset + offset*lng/180);
	    int y = (int)Math.round(offset - offset/Math.PI * Math.log((1 + Math.sin(lat * Math.PI / 180)) / (1 - Math.sin(lat * Math.PI / 180))) / 2);
		return new Point(x,y);
	}
	
	public static Position fromPixel(Point pixel, int zoom){
		double offset = 256 << (zoom-1);
		double lng = (pixel.x/offset-1)*180;
		double lat = (Math.PI/2-2*Math.atan(Math.exp((pixel.y-offset)/(offset/Math.PI))))*180/Math.PI;
		Position pos = new Position();
		pos.setLat(lat);
		pos.setLng(lng);
		return pos;
	}
	
}
