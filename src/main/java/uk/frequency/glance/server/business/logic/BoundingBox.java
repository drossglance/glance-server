package uk.frequency.glance.server.business.logic;

import java.util.List;

import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.trace.PositionTrace;

public class BoundingBox {

	double minLat, maxLat, minLng, maxLng;
	
	public static BoundingBox from(List<PositionTrace> traces){
		double minLat = Double.POSITIVE_INFINITY;
		double minLng = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY;
		double maxLng = Double.NEGATIVE_INFINITY;
		for (PositionTrace trace : traces) {
			minLat = Math.min(minLat, trace.getPosition().getLat());
			maxLat = Math.max(maxLat, trace.getPosition().getLat());
			minLng = Math.min(minLng, trace.getPosition().getLng());
			maxLng = Math.max(maxLng, trace.getPosition().getLng());
		}
		BoundingBox box = new BoundingBox();
		box.minLat = minLat;
		box.minLng = minLng;
		box.maxLat = maxLat;
		box.maxLng = maxLng;
		return box;
	}
	
	public boolean canContainCircle(double radius){
		return maxLat - minLat < 2 * radius && maxLng - minLng < 2 * radius;
	}
	
	public Position findCenter(){
		double avgLat = (minLat + maxLat) / 2;
		double avgLng = (minLng + maxLng) / 2;
		Position pos = new Position();
		pos.setLat(avgLat);
		pos.setLng(avgLng);
		return pos;
	}
}
