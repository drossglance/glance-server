package uk.frequency.glance.server.util;


import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import uk.frequency.glance.server.model.Comment;
import uk.frequency.glance.server.model.Feeling;
import uk.frequency.glance.server.model.GenericEntity;
import uk.frequency.glance.server.model.Location;
import uk.frequency.glance.server.model.Media;
import uk.frequency.glance.server.model.Position;
import uk.frequency.glance.server.model.User;
import uk.frequency.glance.server.model.UserExpression;
import uk.frequency.glance.server.model.UserProfile;
import uk.frequency.glance.server.model.UserSettings;
import uk.frequency.glance.server.model.event.Action;
import uk.frequency.glance.server.model.event.Event;
import uk.frequency.glance.server.model.event.EventScore;
import uk.frequency.glance.server.model.event.ListenAction;
import uk.frequency.glance.server.model.event.StayEvent;
import uk.frequency.glance.server.model.event.TellEvent;
import uk.frequency.glance.server.model.event.MoveEvent;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration config = new Configuration()
				.setNamingStrategy(new ImprovedNamingStrategy())
				.addPackage("uk.frequencymobile.flow.server.model")
				.addAnnotatedClass(GenericEntity.class)
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(UserProfile.class)
				.addAnnotatedClass(UserSettings.class)
				.addAnnotatedClass(Location.class)
				.addAnnotatedClass(Position.class)
				.addAnnotatedClass(Event.class)
				.addAnnotatedClass(StayEvent.class)
				.addAnnotatedClass(MoveEvent.class)
				.addAnnotatedClass(TellEvent.class)
				.addAnnotatedClass(Action.class)
				.addAnnotatedClass(ListenAction.class)
				.addAnnotatedClass(EventScore.class)
				.addAnnotatedClass(UserExpression.class)
				.addAnnotatedClass(Comment.class)
				.addAnnotatedClass(Feeling.class)
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