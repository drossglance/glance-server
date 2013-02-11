package uk.frequency.glance.server.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserSettings {

	@Column(columnDefinition="boolean default true")
	boolean notificationsEnabled;
	
	@Column(columnDefinition="int default 1")
	int autoLocationInterval;
	
}
