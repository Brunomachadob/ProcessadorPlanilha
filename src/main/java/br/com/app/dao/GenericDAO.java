package br.com.app.dao;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
public class GenericDAO<T> {

	@Inject
	EntityManager em;

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
	
	public List<T> list(String query, Class<T> clazz) {
		return em.createQuery(query, clazz).getResultList();
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
