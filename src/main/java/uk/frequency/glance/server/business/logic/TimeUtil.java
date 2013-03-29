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
	
	public static double getDurationInDays(Date begin, Date end){
		long milis = getDuration(begin, end);
		return (double)milis/(1000*60*60*24);
	}
	
	public static double getDurationInHours(Date begin, Date end){
		long milis = getDuration(begin, end);
		return (double)milis/(1000*60*60);
	}
	
	public static long getDuration(Date begin, Date end){
		return end.getTime() - begin.getTime();
	}
	
	public static double getHoursInTheDay(Date time){
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int sec = cal.get(Calendar.SECOND);
		return (double)sec/(60*60);
	}

	public static boolean isBeforeToday(Date time){
		Calendar then = Calendar.getInstance();
		then.setTime(time);
		Calendar today = Calendar.getInstance();
		today.setTime(beginOfToday());
		return then.before(today);
	}
	
	public static Date beginOfToday(){
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		return begin.getTime();
	}
	
}
