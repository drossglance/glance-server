package uk.frequency.glance.server.data_access;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Victor
 * TODO static content should be hosted in a dedicated server
 */
public class StaticResourcesLoader {

	static final String LOCAL_ROOT = "src/main/java/static_resources";
	static final String REMOTE_ROOT = "http://glance-server.herokuapp.com/services/static"; //FIXME get this dynamically from proper place
	
	public static byte[] loadImage(String path) throws IOException{
		File file = new File(LOCAL_ROOT, path);
		System.out.println(file.getAbsolutePath());
		RenderedImage img = ImageIO.read(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "png", out);
		out.flush();
		return out.toByteArray();
	}
	
	public  static String getImageUrl(String path, int index) {
		File dir = new File(LOCAL_ROOT, "images/" + path);
		File[] files = dir.listFiles(); 
		File file = files[index%files.length];
		return REMOTE_ROOT + "/images/" + path + "/" + file.getName();
	}
	
}
