package uk.frequency.glance.server.business.remote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NetUtil {

	public static String urlEncode(String str){
		try {
			return URLEncoder.encode(str.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
