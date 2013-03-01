package uk.frequency.glance.server.model.event;

import javax.persistence.Entity;

@Entity
public class ListenEvent extends Event {

	String songMetadata;
	
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