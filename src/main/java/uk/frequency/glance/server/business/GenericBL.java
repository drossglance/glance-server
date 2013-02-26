package uk.frequency.glance.server.business;

import java.lang.reflect.ParameterizedType;
import java.util.List;

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

	public T findById(long id) {
		return dal.findById(id);
	}

	public List<T> findAll() {
		return dal.findAll();
	}

	public T create(T entity) {
		return dal.makePersistent(entity);
	}
	
}