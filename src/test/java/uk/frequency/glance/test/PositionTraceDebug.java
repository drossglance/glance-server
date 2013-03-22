package uk.frequency.glance.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.frequency.glance.server.business.logic.BoundingBox;
import uk.frequency.glance.server.business.logic.LatLngGeometryUtil;
import uk.frequency.glance.server.business.remote.GoogleStaticMaps;
import uk.frequency.glance.server.business.remote.RemoteAPIClient;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.transfer.trace.PositionTraceDTO;

@SuppressWarnings("serial")
public class PositionTraceDebug {

	static int width = 1024;
	static int height = 1024;
	static GoogleStaticMaps maps = new GoogleStaticMaps(width, height);
	
	public static void main(String[] args) {
		List<PositionTraceDTO> traces = DBDownloader.downloadPositionTraces(2);
		
//		int pace = 100;
//		for (int i = 0; i < traces.size(); i+=pace) {
//			int end = Math.min(i+pace, traces.size());
//			List<PositionTraceDTO> part = traces.subList(i, end);
//			showOnMap(part);
//		}
		
		showOnMap(traces);
	}
	
	static void showOnMap(List<PositionTraceDTO> traces){
		List<Position> trail = LatLngGeometryUtil.tracesToPositions(traces);
		BoundingBox box = BoundingBox.from(trail);
		Position center = box.findCenter();
		int zoom = maps.findZoomToContain(box);
		Rectangle rect = maps.findRectangle(center, zoom);
		String url = maps.getStreetViewImageUrl(center, zoom);
		RemoteAPIClient client = new RemoteAPIClient("");
		Image map = client.getImage(url);
		showOnFrame(map, trail, zoom, rect);
	}
	
	static void showOnFrame(JPanel canvas){
		JFrame frame = new JFrame();
		frame.add(canvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	static void showOnFrame(Image image, List<Position> trail, int zoom, Rectangle rect){
		JPanel canvas = new Canvas(image, trail, zoom, rect);
		canvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
		showOnFrame(canvas);
	}
	
	static void showOnFrame(Image image){
		JPanel canvas = new Canvas(image);
		canvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
		showOnFrame(canvas);
	}
	
	static class Canvas extends JPanel{
		
		Image image;
		List<Point> trail;
		int zoom;
		Rectangle rect;
		
		public Canvas(Image image, List<Position> trail, int zoom, Rectangle rect) {
			this.image = image;
			this.trail = GoogleStaticMaps.toPixel(trail, zoom);
			this.zoom = zoom;
			this.rect = rect;
//			this.rect = EuclidianGeometryUtil.findRectangle(this.trail);
		}
		
		public Canvas(Image image){
			this.image = image;
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(image, 0, 0, null);
			
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.RED);
			
			if(trail != null)
			for (int i = 0; i < trail.size()-1; i++) {
				Point p1 = trail.get(i);
				Point p2 = trail.get(i+1);
				g2.drawLine(p1.x-rect.x, p1.y-rect.y, p2.x-rect.x, p2.y-rect.y);
			}
			
		}
	}

}
