package uk.frequency.glance.server.business.logic;

import java.awt.Point;
import java.util.List;

import uk.frequency.glance.server.model.component.Position;

public class GeometryUtil {

	final static double ROUGH_KM_DEGREE_RATIO = 111.111;
	//http://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of-meters
	
	public static double distance(Position p1, Position p2){
		return Point.distance(p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
	}
	
	public static double degreesToKm(double degrees){
		//FIXME get a proper geometry library to handle earth's variable radius and points across the 180deg meridian
		return degrees*ROUGH_KM_DEGREE_RATIO;
	}
	
	public static double kmToDegrees(double km){
		//FIXME get a proper geometry library to handle earth's variable radius and points across the 180deg meridian
		return km/ROUGH_KM_DEGREE_RATIO;
	}
	
	public static int degreesToMeters(double degrees){
		return (int)(degreesToKm(degrees)*1000);
	}
	
	public static double metersToDegrees(int meters){
		return kmToDegrees((double)meters/1000);
	}
	
	public static int findCloser(Position pos, List<Position> list){
		int closer = -1;
		double minDist = Double.POSITIVE_INFINITY;
		for(int i=0; i<list.size(); i++){
			Position p = list.get(i);
			double dist = GeometryUtil.distance(pos, p);
			if(dist < minDist){
				minDist = dist;
				closer = i;
			}
		}
		return closer;
	}
	
}
