package uk.frequency.glance.server.transfer.event;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@SuppressWarnings("serial")
public class ListenEventDTO extends EventDTO{

	String songMetadata;

	public String getSongMetadata() {
		return songMetadata;
	}

	public void setSongMetadata(String songMetadata) {
		this.songMetadata = songMetadata;
	}
	
}
