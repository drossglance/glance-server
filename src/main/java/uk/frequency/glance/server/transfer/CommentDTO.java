package uk.frequency.glance.server.transfer;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Media.MediaType;

@XmlRootElement
@SuppressWarnings("serial")
public class CommentDTO extends GenericDTO{

	public long userId;
	
	public long subjectId;
	
	public String text;
	
	public Location location;

	public MediaType mediaType;
	
	public List<Media> media;
	
}
