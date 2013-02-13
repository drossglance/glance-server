package uk.frequency.glance.server.transfer;

import java.util.List;

import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.Media.MediaType;

@SuppressWarnings("serial")
public class EventDTO extends GenericDTO{

	long id;
	
	long authorId;
	
	String text;
	
	Location location;
	
	MediaType mediaType;

	List<Media> media;
	
	List<Long> commentIds;
	
	boolean autoGenerated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
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

	public List<Long> getCommentIds() {
		return commentIds;
	}

	public void setCommentIds(List<Long> commentIds) {
		this.commentIds = commentIds;
	}

	public boolean isAutoGenerated() {
		return autoGenerated;
	}

	public void setAutoGenerated(boolean autoGenerated) {
		this.autoGenerated = autoGenerated;
	}
	
}