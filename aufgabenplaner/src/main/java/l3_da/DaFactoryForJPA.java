package l3_da;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static l3_da.DaSchritt.entityManagerFactory;

/**
 * Created by Stephan D on 27.05.2016.
 */
public class DaFactoryForJPA {

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
