package br.com.app.dao;

import java.util.List;

import javax.persistence.EntityManager;

public class GenericDAO<T> {

	private EntityManager em = EntityManagerUtil.getEntityManager();

	protected Class<T> entityClass;

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T save(T toPersist) {
		try {
			em.getTransaction().begin();

			T result = em.merge(toPersist);

			em.getTransaction().commit();

			return result;
		} catch (Exception e) {
			em.getTransaction().rollback();

			throw e;
		}
	}

	public List<T> list() {
		return em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
	}

	public void remove(T toRemove) {
		try {
			em.getTransaction().begin();
			em.remove(toRemove);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();

			throw e;
		}
	}
}
