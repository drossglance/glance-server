package uk.frequency.glance.server.model.event;

import javax.persistence.Embeddable;

@Embeddable
public class ListenAction extends Action {

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
				+ " | " + songMetadata;
	}

}