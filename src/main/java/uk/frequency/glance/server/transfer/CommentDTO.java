package uk.frequency.glance.server.transfer;

import java.util.List;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.Media.MediaType;

@SuppressWarnings("serial")
public class CommentDTO extends GenericDTO{

	long id;
	
	long authorId;
	
	long subjectId;
	
	String text;
	
	Location location;

	MediaType mediaType;
	
	List<Media> media;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long userId) {
		this.authorId = userId;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long eventId) {
		this.subjectId = eventId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}

	
}
