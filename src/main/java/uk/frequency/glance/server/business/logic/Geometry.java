package uk.frequency.glance.server.business.logic;

import java.awt.Point;

import uk.frequency.glance.server.model.component.Position;

public class Geometry {

	final static double ROUGH_KM_DEGREE_RATIO = 111.111;
	//http://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of-meters
	
	public static double distance(Position p1, Position p2){
		return Point.distance(p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
		//FIXME get a proper geometry library to handle earth`s variable radius and points across the 180deg meridian
	}
	
	public static double degreesToKm(double degrees){
		return degrees*ROUGH_KM_DEGREE_RATIO;
	}
	
	public static double kmToDegrees(double km){
		return km/ROUGH_KM_DEGREE_RATIO;
	}
	
}
