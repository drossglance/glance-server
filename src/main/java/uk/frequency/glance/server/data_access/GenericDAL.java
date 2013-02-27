package uk.frequency.glance.server.data_access;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import uk.frequency.glance.server.data_access.util.HibernateUtil;
import uk.frequency.glance.server.model.GenericEntity;

/**
 * @author Victor Basso
 * Provides common Data Access Layer methods and initialises the session.
 * https://community.jboss.org/wiki/GenericDataAccessObjects
 * http://www.ibm.com/developerworks/java/library/j-genericdao/index.html
 */
@SuppressWarnings("unchecked")
public class GenericDAL<T extends GenericEntity> {

	Class<T> entityClass;
	Session session;
 
	public GenericDAL() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.session = HibernateUtil.getSessionFactory().getCurrentSession();
	}
 
    public T findById(long id) {
        T entity = (T) getSession().load(entityClass, id);
        return entity;
    }
 
    public List<T> findAll() {
        return findByCriteria();
    }
 
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(entityClass);
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
 
    public T makePersistent(T entity) {
    	getSession().saveOrUpdate(entity);
        return entity;
    }
 
    public void makeTransient(T entity) {
    	getSession().delete(entity);
    }
 
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(entityClass);
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}
	
	public void flush() throws RuntimeException{
		try{
			getSession().getTransaction().commit();
		} catch (RuntimeException ex) {
			if (getSession().getTransaction().isActive()) {
				getSession().getTransaction().rollback();
			}
			getSession().beginTransaction();
			throw ex;
		}
		getSession().beginTransaction();
	}
	
	protected Session getSession(){
		return HibernateUtil.getSessionFactory().getCurrentSession();
//		return session;
	}
	
}
