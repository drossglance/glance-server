package uk.frequency.glance.server.business.logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static final DateFormat TIMESTAMP = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
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
		int min = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour + (double)min/60 + (double)sec/(60*60);
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
	
	public static Date getBeginOfDay(Date time){
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static boolean isInSameDay(Date time1, Date time2){
		return getBeginOfDay(time1).getTime() == getBeginOfDay(time2).getTime();
	}
	
	public static Date parse(String str){
		try {
			return TIMESTAMP.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static int hoursToMiliseconds(int hours){
		return hours*60*60*1000;
	}
}
