package l3_da;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * Created by Stephan D on 27.05.2016.
 */
public class DaFactoryForJPA {


    private EntityManager entityManager;

    DaFactoryForJPA(){
        final EntityManagerFactory entityManagerFactory;
    }

    public void beginTransaction() {
        final EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
    }

    public void endTransaction(final boolean ok) {
        final EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive()) return;
            if (ok) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } finally { //wird auch bei Erfolg oder return ausgeführt!
            entityManager.close();
        }
        entityManager.clear();
    }
}
