package uk.frequency.glance.server.model.trace;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Proxy;

import uk.frequency.glance.server.model.UserExpression;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Proxy(lazy=false)
@Table(uniqueConstraints={
	@UniqueConstraint(columnNames={"user_id", "time"})
})
public abstract class Trace extends UserExpression {

	Date time;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return super.toString()
				+ " | " + time;
	}
	
}
