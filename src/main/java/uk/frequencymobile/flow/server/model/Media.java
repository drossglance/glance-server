package uk.frequencymobile.flow.server.model;

import javax.persistence.Embeddable;

@Embeddable
public class Media{

	public enum Type { IMAGE, DRAWING, SOUND }
	
	String url;
	
	Type type;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return super.toString()
				+ " | " + url
				+ " | " + type.name();
	}
	
}
