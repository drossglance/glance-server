package uk.frequency.glance.server.transfer.event;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@SuppressWarnings("serial")
public class ListenEventDTO extends EventDTO{

	long startTime;
	
	long endTime;
	
	String songMetadata;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getSongMetadata() {
		return songMetadata;
	}

	public void setSongMetadata(String songMetadata) {
		this.songMetadata = songMetadata;
	}
	
}
