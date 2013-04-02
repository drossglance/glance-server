package uk.frequency.glance.server;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DebugUtil {

	static final String EMAIL = "victor.basso@glance.cc";

	public static void sendEmail(String txt) {
//		// Recipient's email ID needs to be mentioned.
//		String to = "abcd@gmail.com";
//
//		// Sender's email ID needs to be mentioned
//		String from = "web@gmail.com";
//
//		// Assuming you are sending email from localhost
//		String host = "localhost";
//
//		// Get system properties
//		Properties properties = System.getProperties();
//
//		// Setup mail server
//		properties.setProperty("mail.smtp.host", host);
//
//		// Get the default Session object.
//		Session session = Session.getDefaultInstance(properties);
//
//		try {
//			// Create a default MimeMessage object.
//			MimeMessage message = new MimeMessage(session);
//
//			// Set From: header field of the header.
//			message.setFrom(new InternetAddress(from));
//
//			// Set To: header field of the header.
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//			// Set Subject: header field
//			message.setSubject("This is the Subject Line!");
//
//			// Now set the actual message
//			message.setText("This is actual message");
//
//			// Send message
//			Transport.send(message);
//			System.out.println("Sent message successfully....");
//		} catch (MessagingException mex) {
//			mex.printStackTrace();
//		}
	}

	public static void sendEmail(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String str = e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + sw.toString();
		sendEmail(str);
	}
	
	public static String timeStr(Date time){
		return DateFormat.getTimeInstance(DateFormat.MEDIUM).format(time);
	}
	
	public static String dateStr(Date time){
		return DateFormat.getDateInstance(DateFormat.MEDIUM).format(time);
	}

	public static void showOnFrame(final Image image){
		showOnFrame(image, null);
	}
	
	@SuppressWarnings("serial")
	public static void showOnFrame(final Image image, String title) {
		final int padding = 40;
		
		JFrame frame = new JFrame(title);
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
