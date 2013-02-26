package uk.frequency.glance.server.business;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import uk.frequency.glance.server.data_access.GenericDAL;
import uk.frequency.glance.server.model.GenericEntity;

/**
 * @author Victor Basso
 * TODO inject the DALs through spring
 */
public abstract class GenericBL<T extends GenericEntity> {

	Class<T> entityClass;
	GenericDAL<T> dal;

	@SuppressWarnings("unchecked")
	public GenericBL(GenericDAL<T> dao) {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.dal = dao;
	}

	public T findById(long id) throws ObjectNotFoundException {
		T entity = dal.findById(id);
		dal.flush();
		return entity; 
	}

	public List<T> findAll() {
		return dal.findAll();
	}

	public T create(T entity) throws ConstraintViolationException, TransientObjectException{
		dal.makePersistent(entity);
		dal.flush();
		return entity;
	}
	
}