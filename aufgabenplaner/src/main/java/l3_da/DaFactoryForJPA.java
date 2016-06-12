package l3_da;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by Stephan D on 27.05.2016.
 */
public class DaFactoryForJPA {

    public final String persistenceUnitName = "aufgabenplaner"; //as specified in src/main/resources/META-INF/persistence.xml
    //createEntityManagerFactory ist eine sehr aufwändige Operation! Die EntityManagerFactory muss am Ende manuell geschlossen werden!
    public final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

    public final EntityManager em = entityManagerFactory.createEntityManager();

    public DaFactoryForJPA() {
        final EntityManagerFactory entityManagerFactory;
    }

    public void beginTransaction() {
        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();
    }

    public void endTransaction(final boolean ok) {
        final EntityTransaction transaction = em.getTransaction();
        try {
            if (!transaction.isActive()) return;
            if (ok) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } finally { //wird auch bei Erfolg oder return ausgeführt!
            em.close();
        }
        em.clear();
    }
}
