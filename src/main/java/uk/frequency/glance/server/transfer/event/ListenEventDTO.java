package uk.frequency.glance.server.transfer.event;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@SuppressWarnings("serial")
public class ListenEventDTO extends EventDTO{

	public String songMetadata;

}
