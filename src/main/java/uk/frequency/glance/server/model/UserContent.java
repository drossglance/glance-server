package uk.frequency.glance.server.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import uk.frequency.glance.server.model.Media.MediaType;

@MappedSuperclass
public abstract class UserContent extends UserExpression{

	String text;
	
	@Embedded
	Location location;

	@ElementCollection
	List<Media> media;
	
	MediaType mediaType;
	
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

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}


	@Override
	public String toString() {
		return super.toString()
				+ " | " + author.id
				+ " | " + text
				+ " | " + location;
	}
	
}