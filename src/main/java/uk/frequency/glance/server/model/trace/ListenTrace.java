package uk.frequency.glance.server.model.trace;

import javax.persistence.Entity;

@Entity
public class ListenTrace extends Trace {

	String songMetadata;

	public String getSongMetadata() {
		return songMetadata;
	}

	public void setSongMetadata(String songMetadata) {
		this.songMetadata = songMetadata;
	}
	
}
