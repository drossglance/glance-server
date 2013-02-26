package uk.frequency.glance.server.model.event;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ListenEvent extends Event {

	Date startTime;
	
	Date endTime;
	
	String songMetadata;
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSongMetadata() {
		return songMetadata;
	}

	public void setSongMetadata(String songMetadata) {
		this.songMetadata = songMetadata;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + startTime
				+ " | " + songMetadata;
	}

}