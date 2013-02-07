package uk.frequencymobile.flow.server.data;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import uk.frequencymobile.flow.server.model.GenericEntity;
import uk.frequencymobile.flow.server.util.HibernateUtil;

/**
 * @author Victor Basso
 * Provides common DAO methods and initializes the session.
 * https://community.jboss.org/wiki/GenericDataAccessObjects
 * http://www.ibm.com/developerworks/java/library/j-genericdao/index.html
 */
@SuppressWarnings("unchecked")
public class GenericDAO<T extends GenericEntity> {

	Class<T> entityClass;
    Session session;
 
    public GenericDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
     }
 
    public T findById(long id) {
        T entity = (T) session.load(entityClass, id);
        return entity;
    }
 
    public List<T> findAll() {
        return findByCriteria();
    }
 
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = session.createCriteria(entityClass);
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
 
    public T makePersistent(T entity) {
    	session.saveOrUpdate(entity);
        return entity;
    }
 
    public void makeTransient(T entity) {
    	session.delete(entity);
    }
 
    /**
     * Use this inside subclasses as a convenience method.
     */
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = session.createCriteria(entityClass);
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
   }
	
}
