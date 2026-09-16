package br.ufg.inf.barbearia.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class BarbeariaJPAUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BarbeariaJPAUtil.class);
    private static final String PERSISTENCE_UNIT_NAME = "barbearia-jpa";
    private static EntityManagerFactory entityManagerFactory;

    private BarbeariaJPAUtil() {
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            LOGGER.info("Inicializando EntityManagerFactory");
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return entityManagerFactory;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void closeEntityManager(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    public static synchronized void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static void executeInTransaction(TransactionOperation operation) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            operation.execute(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception exception) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erro na transacao", exception);
        } finally {
            closeEntityManager(entityManager);
        }
    }

    @FunctionalInterface
    public interface TransactionOperation {
        void execute(EntityManager entityManager) throws Exception;
    }
}
