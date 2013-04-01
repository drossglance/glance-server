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
import uk.frequency.glance.server.model.component.Position;

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

	public static List<Position> cleanTrail(List<Position> trail) {
		final double STEP = metersToDegrees(100); // min distance between each step in the trail
		List<Position> cleanTrail = new ArrayList<Position>();
		cleanTrail.add(trail.get(0));
		for (Position p : trail) {
			Position last = cleanTrail.get(cleanTrail.size() - 1);
			if (distance(p, last) >= STEP) {
				cleanTrail.add(p);
			}
		}
		if(!cleanTrail.get(cleanTrail.size()-1).equals(trail.get(trail.size()-1))){
			cleanTrail.add(trail.get(trail.size()-1));
		}
		return cleanTrail;
	}

}
