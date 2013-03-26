package uk.frequency.glance.server.business.logic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Waveline {

	public static void main(String[] args) {
		Waveline w = new Waveline();
		Image image = w.render();
		show(image);
	}
	
	static void show(final Image image) {
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
	
	int width, height;
	
	void draw(Graphics2D g){
		
	}
	
	Image render(){
		BufferedImage bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		draw((Graphics2D)bi.getGraphics());
		return bi;
	}
	
}
