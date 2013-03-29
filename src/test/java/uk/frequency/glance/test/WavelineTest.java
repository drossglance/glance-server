package uk.frequency.glance.test;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.frequency.glance.server.business.logic.PresentationUtil;
import uk.frequency.glance.server.business.logic.waveline.BasicColorPicker;
import uk.frequency.glance.server.business.logic.waveline.Waveline;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.BelievableDataSource;
import uk.frequency.glance.server.business.logic.waveline.streamgraph.ColorPicker;

public class WavelineTest {

	public static void main(String[] args) {
		Image bg = null;
		try {
			File bgFile = new File("./src/test/java/waveline_background.png");
			bg = ImageIO.read(bgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		List<Event> events = DBDownloader.fromDTO(DBDownloader.downloadEvents(1));
//		Layer[] layers = new EventDataWavelineAdapter().buildLayers(events);
		ColorPicker coloring = new BasicColorPicker(PresentationUtil.WAVELINE_BLUE_SHADES);
//		Waveline w = new Waveline(layers, coloring);
		
		Waveline w = new Waveline(new BelievableDataSource().make(3, 20), coloring);
		Image image = w.render(720, 243);

		Graphics g = bg.getGraphics();
		g.drawImage(image, 0, 0, null);

		showOnFrame(bg);
	}
	
	@SuppressWarnings("serial")
	static void showOnFrame(final Image image) {
		final int padding = 40;
		
		JFrame frame = new JFrame();
		JPanel pane = new JPanel(){
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(image, padding, padding, null); 
			}
		};
		pane.setPreferredSize(new Dimension(image.getWidth(null) + 2*padding, image.getHeight(null) + 2*padding));
		frame.add(pane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
