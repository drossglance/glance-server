package uk.frequency.glance.server.business.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public class EuclideanGeometryUtil {

	public static Rectangle findRectangle(Point center, int width, int height){
		return new Rectangle(center.x-width/2, center.y-height/2, width, height);
	}
	
	public static Rectangle findRectangle(List<Point> points){
		int minY = Integer.MAX_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxX = Integer.MIN_VALUE;
		for (Point p : points) {
			minY = Math.min(minY, p.y);
			maxY = Math.max(maxY, p.y);
			minX = Math.min(minX, p.x);
			maxX = Math.max(maxX, p.x);
		}
		Rectangle rect = new Rectangle(minX, minY, maxX-minX, maxY-minY);
		return rect;
	}
}
