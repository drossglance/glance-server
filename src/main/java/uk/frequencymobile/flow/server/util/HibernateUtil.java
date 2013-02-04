package uk.frequencymobile.flow.server.util;


import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import uk.frequencymobile.flow.server.model.Comment;
import uk.frequencymobile.flow.server.model.Event;
import uk.frequencymobile.flow.server.model.GenericEntity;
import uk.frequencymobile.flow.server.model.Likes;
import uk.frequencymobile.flow.server.model.Location;
import uk.frequencymobile.flow.server.model.User;
import uk.frequencymobile.flow.server.model.UserSettings;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration config = new Configuration()
				.setNamingStrategy(new ImprovedNamingStrategy())
				.addPackage("uk.frequencymobile.flow.server.model")
				.addAnnotatedClass(GenericEntity.class)
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(Location.class)
				.addAnnotatedClass(Event.class)
				.addAnnotatedClass(Comment.class)
				.addAnnotatedClass(Likes.class)
				.addAnnotatedClass(UserSettings.class);
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