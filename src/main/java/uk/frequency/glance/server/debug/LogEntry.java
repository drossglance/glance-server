package uk.frequency.glance.server.debug;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LogEntry {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE) //TODO create different generators for each hierarchy
	public Long id;
	
	public Date creationTime;
	
	public long userId;

	public Date time;

	public String text;

}
