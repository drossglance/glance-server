package uk.frequency.glance.server.data_access.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;

/**
 * @author Victor Basso
 * For development only.
 * Opens the first Hibernate session to trigger table creation before a request have been made.
 */
public class HibernateAutoInit implements ServletContextListener{

	public void contextInitialized(ServletContextEvent ev) {
		Session ses = HibernateConfig.getSessionFactory().openSession();
		ses.close();
	}
	
	public void contextDestroyed(ServletContextEvent ev) {

	}

}
