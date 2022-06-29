package edu.kit.exp.server.jpa.dao;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.IEntry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DefaultDAO<T extends IEntry> {

	private String entryClassName;

	protected String getEntryClassName() {
		return entryClassName;
	}

	public DefaultDAO(Class<?> entryClass) {
		// does not work unless DefaultDAO is abstract.... Java can't get the
		// type of a reflection parameter on runtime. For backwards
		// compatibility reasons.
		/*
		 * try { entryClassName = ((ParameterizedType)
		 * getClass().getGenericSuperclass()).getActualTypeArguments()[0].
		 * getTypeName(); entryClassName =
		 * Class.forName(entryClassName).getSimpleName(); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); }
		 */
		entryClassName = entryClass.getSimpleName();
	}

	public T findById(Object id) throws DataManagementException {
		// eg. "Trial.findByIdTrial"
		// eg. "idTrial"
		// then trial value
		return this.findSingleBy(getEntryClassName() + ".findById" + getEntryClassName(), "id" + getEntryClassName(), id);
	}

	public List<T> findAll() throws DataManagementException {
		// eg. "Trial.findAll"
		return this.findAll(getEntryClassName() + ".findAll");
	}

	protected T findSingleBy(String queryName, String idName, Object id) throws DataManagementException {
		EntityManager em = PersistenceUtil.createEntityManager();
		T result = null;

		try {

			@SuppressWarnings("unchecked")
			TypedQuery<T> query = (TypedQuery<T>) em.createNamedQuery(queryName);
			query.setParameter(idName, id);
			result = query.getSingleResult();

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}

		return result;
	}

	protected List<T> findAll(String queryName) throws DataManagementException {
		return findAllBy(queryName, null, null);
	}

	public List<T> findAllBy(String queryName, String valueName, Object value) throws DataManagementException {
		EntityManager em = PersistenceUtil.createEntityManager();
		List<T> results = null;

		try {

			@SuppressWarnings("unchecked")
			TypedQuery<T> query = (TypedQuery<T>) em.createNamedQuery(queryName);
			if (valueName != null) {
				query.setParameter(valueName, value);
			}
			results = query.getResultList();

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}

		return results;
	}

	public T create(T entry) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		T result;

		try {

			em.getTransaction().begin();
			em.persist(entry);
			em.getTransaction().commit();
			em.refresh(entry);

			result = this.findById(entry.getId());

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}
		return result;
	}

	public T update(T entry) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		T result;

		try {
			em.getTransaction().begin();
			em.find(entry.getClass(), entry.getId());
			result = (T) em.merge(entry);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
		return result;
	}

	public void delete(T entry) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {
			em.getTransaction().begin();
			em.remove(em.find(entry.getClass(), entry.getId()));
			em.getTransaction().commit();

			PersistenceUtil.getEntitymanagerFactory().getCache().evictAll();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
	}
}
