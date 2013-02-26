package uk.frequency.glance.server.model.component;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embeddable;

@Embeddable
@DiscriminatorColumn(name="discr", discriminatorType=DiscriminatorType.STRING)
public class Media{

	public enum MediaType { IMAGE, DRAWING, SOUND, VIDEO }
	
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
