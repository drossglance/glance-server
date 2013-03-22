package uk.frequency.glance.server.business.logic;

import java.util.List;

import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.trace.PositionTrace;

public class BoundingBox {

	double minLat, maxLat, minLng, maxLng;
	
	private BoundingBox(){
		
	}
	
	public BoundingBox(Position nw, Position se){
		minLat = se.getLat();
		maxLat = nw.getLat();
		minLng = nw.getLng();
		maxLng = se.getLng();
	}
	
	public static BoundingBox fromTraces(List<PositionTrace> traces){
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
	
	public static BoundingBox from(List<Position> positions){
		double minLat = Double.POSITIVE_INFINITY;
		double minLng = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY;
		double maxLng = Double.NEGATIVE_INFINITY;
		for (Position trace : positions) {
			minLat = Math.min(minLat, trace.getLat());
			maxLat = Math.max(maxLat, trace.getLat());
			minLng = Math.min(minLng, trace.getLng());
			maxLng = Math.max(maxLng, trace.getLng());
		}
		BoundingBox box = new BoundingBox();
		box.minLat = minLat;
		box.minLng = minLng;
		box.maxLat = maxLat;
		box.maxLng = maxLng;
		return box;
	}
	
	public boolean canContainCircle(double radius){
		return maxLat - minLat >= 2 * radius && maxLng - minLng >= 2 * radius;
	}
	
	public Position findCenter(){
		double avgLat = (minLat + maxLat) / 2;
		double avgLng = (minLng + maxLng) / 2;
		Position pos = new Position();
		pos.setLat(avgLat);
		pos.setLng(avgLng);
		return pos;
	}
	
	public boolean contains(BoundingBox box){
		return minLat < box.minLat && minLng < box.minLng && maxLat > box.maxLat && maxLng > box.maxLng;
	}
	
	public Position getNECorner(){
		Position p = new Position();
		p.setLat(maxLat);
		p.setLng(maxLng);
		return p;
	}
	
	public Position getSWCorner(){
		Position p = new Position();
		p.setLat(minLat);
		p.setLng(minLng);
		return p;
	}
	
	public Position getNWCorner(){
		Position p = new Position();
		p.setLat(maxLat);
		p.setLng(minLng);
		return p;
	}
	
	public Position getSECorner(){
		Position p = new Position();
		p.setLat(minLat);
		p.setLng(maxLng);
		return p;
	}
}
