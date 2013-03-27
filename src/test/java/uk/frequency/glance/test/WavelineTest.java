package uk.frequency.glance.test;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import streamgraph.Layer;
import uk.frequency.glance.server.business.logic.waveline.EventDataAdapter;
import uk.frequency.glance.server.business.logic.waveline.Waveline;
import uk.frequency.glance.server.model.event.Event;

public class WavelineTest {

	public static void main(String[] args) {
//		Waveline w = new Waveline(new LateOnsetDataSource().make(20, 100));
		
		List<Event> events = DBDownloader.fromDTO(DBDownloader.downloadEvents(1));
		Layer[] layers = new EventDataAdapter().buildLayers(events);
		Waveline w = new Waveline(layers);
		Image image = w.render(1000,400);
		showOnFrame(image);
	}
	
	@SuppressWarnings("serial")
	static void showOnFrame(final Image image) {
		JFrame frame = new JFrame();
		JPanel pane = new JPanel(){
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(image, 0, 0, null); 
			}
		};
		pane.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
		frame.add(pane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
