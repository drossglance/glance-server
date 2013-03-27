package uk.frequency.glance.server.business.logic.waveline;

import java.util.List;

import streamgraph.Layer;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventType;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;

public class EventDataAdapter {

	private static final int SERIES_LENGTH = 20; // length of the time series (x axis). (it's an abstract unity, later translated to time)
	private int[] index; //used by EventViewActivity to associate X positions in the waveline to events
	
	/**
	 * @param events ordered by start time
	 */
	public Layer[] buildLayers(List<Event> events) {

		// deal with trivial cases
		if (events.size() <= 1) {
			return new Layer[3];
		}

		// initialize
		float[] series1 = new float[SERIES_LENGTH]; // stay
		float[] series2 = new float[SERIES_LENGTH]; // move
		float[] series3 = new float[SERIES_LENGTH]; // sleep
//		Arrays.fill(series1, .1f);
//		Arrays.fill(series2, .1f);
//		Arrays.fill(series3, .1f);
		long begin = events.get(0).getStartTime().getTime();
		long end = events.get(events.size() - 1).getStartTime().getTime();
		index = new int[SERIES_LENGTH];
		
		//load the time series and index
		for (int i = 0; i < events.size(); i++) {
			Event event = events.get(i);
			Long startTime = event.getStartTime().getTime();
			Long endTime = event.getEndTime() == null ? startTime : event.getEndTime().getTime();
			if (endTime == null) {
				endTime = end;
			}
			int startPos = (int) ((startTime - begin) * (SERIES_LENGTH - 1) / (end - begin)); //casting is safe because 0 <= result <= seriesLenght
			int endPos = (int) ((endTime - begin) * (SERIES_LENGTH - 1) / (end - begin));
			
			//value attribution based on event score
			int score = (int) Math.round(event.getScore().getRelevance());
			if (event instanceof StayEvent) {
				if (event.getType() == EventType.SLEEP) {
					for (int j = startPos; j < endPos; j++) {
						series3[j] += score;
					}
				} else if (event.getType() == EventType.WAKE) {
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

		//prepare to return
		Layer[] layers = new Layer[3];
		layers[0] = new Layer("stay", series1);
		layers[1] = new Layer("move", series2);
		layers[2] = new Layer("sleep", series3);
		return layers;
	}
	

	public int[] getIndex() {
		return index;
	}

	public int getSeriesLenght() {
		return SERIES_LENGTH;
	}
	
}
