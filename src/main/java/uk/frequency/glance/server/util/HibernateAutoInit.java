package uk.frequency.glance.server.util;

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
		Session ses = HibernateUtil.getSessionFactory().openSession();
		ses.close();
	}
	
	public void contextDestroyed(ServletContextEvent ev) {

	}

}
