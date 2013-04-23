package uk.frequency.glance.server.business.logic;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static uk.frequency.glance.server.business.logic.geometry.LatLngGeometryUtil.*;
import static uk.frequency.glance.server.business.logic.TimeUtil.*;
import uk.frequency.glance.server.business.logic.event.EventGenerationLogic;
import uk.frequency.glance.server.business.remote.GoogleStaticMaps;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.MoveEvent;

public class PresentationUtil {

	public static final Color LIGHT_BLUE = new Color(71, 255, 249);
	public static final Color MIDDLE_BLUE = new Color(2, 172, 179);
	public static final Color DARK_BLUE = new Color(3, 107, 134);
	public static final Color[] WAVELINE_BLUE_SHADES = { DARK_BLUE, MIDDLE_BLUE, LIGHT_BLUE };

	static Locale locale = Locale.getDefault();

	static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	public static String timePastText(Date pastTime) {
		Date now = new Date();

		int diff = (int) ((now.getTime() - pastTime.getTime()));

		int secs = diff / 1000;
		int mins = secs / 60;
		int hours = mins / 60;
		int days = hours / 24;

		String text = "";
		if (days > 1) {
			text = days + " days ago";
		} else if (days == 1) {
			text = "yesterday";
		} else if (hours == 1) {
			text = hours + " hour ago";
		} else if (hours > 1) {
			text = hours + " hours ago";
		} else if (mins == 1) {
			text = mins + " minute ago";
		} else if (mins >= 1) {
			text = mins + " minutes ago";
		} else {
			text = "a while ago";
			// TODO return "at the moment" just for the most recent event
		}

		return text;
	}

	public static String timePastTextDayPrecision(Date pastTime) {
		Date now = new Date();

		int diff = (int) ((now.getTime() - pastTime.getTime()));
		int days = diff / (1000 * 60 * 60 * 24);

		String text = "";
		if (days > 1) {
			text = days + " days ago";
		} else {
			if (isBeforeToday(pastTime)) {
				text = "yesterday";
			} else {
				text = "today";
			}
		}

		return text;
	}

	public static String toUpperCase(String str) {
		if (str == null)
			return null;
		return str.toUpperCase(locale);
	}

	public static String timeText(Date time) {
		return timeFormat.format(time);
	}
	
	public static String dateText(Date time){
//		String str = new SimpleDateFormat("MMMM d, yyyy").format(time);
		String str = new SimpleDateFormat("d").format(time);
		str += dayOfMonthSuffix(Integer.valueOf(str));
		str += " of ";
		str += new SimpleDateFormat("MMMM, yyyy").format(time);
		return str;
	}
	
	public static String dayOfMonthSuffix(final int day) {
	    if(day < 1 || day > 31){
	    	return "";
	    }
	    if (day >= 11 && day <= 13) {
	        return "th";
	    }
	    switch (day % 10) {
	        case 1:  return "st";
	        case 2:  return "nd";
	        case 3:  return "rd";
	        default: return "th";
	    }
	}

	public static List<Position> cleanTrail(List<Position> trail) {
		final double STEP = EventGenerationLogic.BIG_RADIUS; // min distance between each step in the trail
		List<Position> cleanTrail = new ArrayList<Position>();

		cleanTrail.add(trail.get(0));
		for (Position p : trail) {
			Position last = cleanTrail.get(cleanTrail.size() - 1);
			if (distance(p, last) >= STEP) {
				cleanTrail.add(p);
			}
		}
		
		//add last position
		Position lastOriginal = trail.get(trail.size()-1);
		Position lastClean = cleanTrail.get(cleanTrail.size()-1);
		if(!lastClean.equals(lastOriginal)){
			cleanTrail.add(lastOriginal);
			if(distance(lastClean, lastOriginal) < STEP && cleanTrail.size() > 2){
				cleanTrail.remove(cleanTrail.size()-2);
			}
		}
		
		return cleanTrail;
	}
	
	public static String moveEventMapImageUrl(MoveEvent move){
		List<Position> markers = new ArrayList<Position>();
		markers.add(move.getStartLocation().getPosition());
		if(move.getEndLocation() != null){
			markers.add(move.getEndLocation().getPosition());
		}
		List<Position> cleanedTrail = cleanTrail(move.getTrail());
		return new GoogleStaticMaps(200, 200).getImageUrl(markers, cleanedTrail);
	}

}
