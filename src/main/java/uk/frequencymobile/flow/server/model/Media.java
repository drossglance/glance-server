package uk.frequencymobile.flow.server.model;

import javax.persistence.Embeddable;

@Embeddable
public class Media{

	public enum MediaType { IMAGE, DRAWING, SOUND }
	
	String url;
	
	MediaType type;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + url
				+ " | " + type.name();
	}
	
}
