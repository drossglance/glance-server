package uk.frequency.glance.server.business.logic.waveline;

import java.util.List;

import uk.frequency.glance.server.business.logic.waveline.streamgraph.Layer;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;

public class WavelineDataAdapter {

	private static final int SERIES_LENGTH = 20; // length of the time series (x axis). (it's an abstract unity, later translated to time)
	private int[] index; //used by EventViewActivity to associate X positions in the waveline to events
	
	/**
	 * @param events ordered by start time
	 */
	public Layer[] buildLayers(List<Event> events) {

		// initialize
		float[] series1 = new float[SERIES_LENGTH]; // stay
		float[] series2 = new float[SERIES_LENGTH]; // move
		float[] series3 = new float[SERIES_LENGTH]; // sleep
		index = new int[SERIES_LENGTH];

		if (events.size() > 1) {
			long overallBegin = findEarliestTime(events);
			long overallEnd = findLatestTime(events);
		
			//load the time series and index
			for (int i = 0; i < events.size(); i++) {
				Event event = events.get(i);
				Long start = event.getStartTime().getTime();
				Long end = event.getEndTime() == null ? start : event.getEndTime().getTime();
				if (end == null) {
					end = overallEnd;
				}
				int startPos = (int) ((start - overallBegin) * (SERIES_LENGTH - 1) / (overallEnd - overallBegin)); //casting is safe because 0 <= result <= seriesLenght
				int endPos = (int) ((end - overallBegin) * (SERIES_LENGTH - 1) / (overallEnd - overallBegin));
				
				//value attribution based on event score
				int score = (int) Math.round(event.getScore().getRelevance());
				if (event instanceof StayEvent) {
					if (event.getType() == EventType.SLEEP) {
						for (int j = startPos; j < endPos; j++) {
							series3[j] += score;
						}
					} else if (event.getType() == EventType.JOIN) {
						series3[startPos] += score;
					} else {
						for (int j = startPos; j < endPos; j++) {
							series1[j] += score;
						}
					}
				} else if (event instanceof MoveEvent) {
					for (int j = startPos; j < endPos; j++) {
						series2[j] += score;
					}
				}
				
				index[startPos] = i;
			}
			
			//fill empty indexes with next value in the left
			int lastIndex = 0;
			for(int i=0; i<SERIES_LENGTH; i++){
				if(index[i] == 0){
					index[i] = lastIndex;
				}else{
					lastIndex = index[i];
				}
			}
		
		}

		//prepare to return
		Layer[] layers = new Layer[3];
		layers[0] = new Layer("stay", series1);
		layers[1] = new Layer("move", series2);
		layers[2] = new Layer("sleep", series3);
		return layers;
	}
	
	public long findEarliestTime(List<Event> events){
		long min = Long.MAX_VALUE;
		for (int i = 0; i < events.size(); i++) {
			long time = events.get(i).getStartTime().getTime();
			if(time < min){
				min = time;
			}
		}
		return min;
	}
	
	public long findLatestTime(List<Event> events){
		long max = Long.MIN_VALUE;
		for (int i = 0; i < events.size(); i++) {
			Event event = events.get(i);
			long time = event.getEndTime() != null ? event.getEndTime().getTime() : event.getStartTime().getTime();
			if(time > max){
				max = time;
			}
		}
		return max;
	}

	public int[] getIndex() {
		return index;
	}

	public int getSeriesLenght() {
		return SERIES_LENGTH;
	}
	
}
