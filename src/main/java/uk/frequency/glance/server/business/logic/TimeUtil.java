package uk.frequency.glance.server.business.logic;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static Date add(Date time, int miliseconds){
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MILLISECOND, miliseconds);
		return cal.getTime();
	}
	
}
