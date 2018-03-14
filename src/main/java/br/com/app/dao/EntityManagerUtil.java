package br.com.app.dao;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class EntityManagerUtil {
	private static final EntityManagerFactory FACTORY;
	
	static {
		try {
			FACTORY = Persistence.createEntityManagerFactory("PPUnit");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	private EntityManagerUtil() {
	}

	@Produces
	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}
}