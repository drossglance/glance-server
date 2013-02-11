package uk.frequency.glance.server.business;

import java.util.List;

import uk.frequency.glance.server.data_access.GenericDAL;
import uk.frequency.glance.server.model.GenericEntity;

/**
 * @author Victor Basso TODO inject the DAOs through spring
 */
public abstract class GenericBL<T extends GenericEntity> {

	GenericDAL<T> dal;

	public GenericBL(GenericDAL<T> dao) {
		this.dal = dao;
	}

	public T findById(long id) {
		return dal.findById(id);
	}

	public List<T> findAll() {
		return dal.findAll();
	}

	public T makePersistent(T entity) {
		return dal.makePersistent(entity);
	}

}