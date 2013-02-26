package uk.frequency.glance.server.transfer.trace;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@SuppressWarnings("serial")
public class ListenTraceDTO extends TraceDTO {

	String songMetadata;

	public String getSongMetadata() {
		return songMetadata;
	}

	public void setSongMetadata(String songMetadata) {
		this.songMetadata = songMetadata;
	}
	
}
