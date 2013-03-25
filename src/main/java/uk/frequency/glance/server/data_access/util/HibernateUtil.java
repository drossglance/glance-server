package uk.frequency.glance.server.data_access.util;


import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.GenericEntity;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.UserExpression;
import uk.frequency.glance.server.model.component.Media;
import uk.frequency.glance.server.model.component.Position;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.ListenEvent;
import uk.frequency.glance.server.model.event.MoveEvent;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.event.TellEvent;
import uk.frequency.glance.server.model.trace.ListenTrace;
import uk.frequency.glance.server.model.trace.PositionTrace;
import uk.frequency.glance.server.model.trace.Trace;
import uk.frequency.glance.server.model.user.EventGenerationInfo;
import uk.frequency.glance.server.model.user.Friendship;
import uk.frequency.glance.server.model.user.User;
import uk.frequency.glance.server.model.user.UserLocation;
import uk.frequency.glance.server.model.user.UserProfile;
import uk.frequency.glance.server.model.user.UserSettings;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration config = new Configuration()
				.setNamingStrategy(new ImprovedNamingStrategy())
				.addAnnotatedClass(GenericEntity.class)
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(UserProfile.class)
				.addAnnotatedClass(Friendship.class)
				.addAnnotatedClass(UserLocation.class)
				.addAnnotatedClass(UserSettings.class)
				.addAnnotatedClass(EventGenerationInfo.class)
				.addAnnotatedClass(Location.class)
				.addAnnotatedClass(Position.class)
				.addAnnotatedClass(Trace.class)
				.addAnnotatedClass(PositionTrace.class)
				.addAnnotatedClass(ListenTrace.class)
				.addAnnotatedClass(Event.class)
				.addAnnotatedClass(StayEvent.class)
				.addAnnotatedClass(MoveEvent.class)
				.addAnnotatedClass(TellEvent.class)
				.addAnnotatedClass(ListenEvent.class)
				.addAnnotatedClass(EventScore.class)
				.addAnnotatedClass(UserExpression.class)
				.addAnnotatedClass(Comment.class)
				.addAnnotatedClass(Media.class);
			
			
//			new SchemaExport(config).create(true, true);
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	/**
	 * Utility method for lazy initialization problems.
	 * http://stackoverflow.com/questions/3787716/introspection-table-name-of-an-object-managed-by-hibernate-javassistlazyiniti
	 * https://forum.hibernate.org/viewtopic.php?t=947035
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T var) {
	    if (var == null) {
	        throw new IllegalArgumentException("passed argument is null");
	    }

	    Hibernate.initialize(var);
	    if (var instanceof HibernateProxy) {
	        var = (T) ((HibernateProxy) var).getHibernateLazyInitializer().getImplementation();
	    }
	    return var;
	}
	
}