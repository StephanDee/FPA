package platform;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import l4_dm.DmSchritt;

/**
 * Erstellen Sie einen JUnit-Testtreiber, der die Operationen des JPA-EntityManager testet.
 * Überprüfen Sie in Zusicherungen mindestens die Daten auf erwartete Werte,
 * die in dieser Beispielanwendung durch die logger-Aufrufe ausgegeben werden.
 */
public class JpaAufgabe {

    /**
     * Probiert einige Operationen von JPA aus.
     */
    public static void main(String[] args) throws Exception {
        new JpaAufgabe().execute();
    }

    private final String persistenceUnitName = "aufgabenplaner"; //as specified in src/main/resources/META-INF/persistence.xml
    //createEntityManagerFactory ist eine sehr aufwändige Operation! Die EntityManagerFactory muss am Ende manuell geschlossen werden!
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

    private final Logger logger = Logger.getLogger(getClass().getName());

    private void execute() throws Exception {
        { //1. Entity persistieren in manueller Transaktion mit "transaction-scoped persistence context" ohne Rollback:
            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            final EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            final DmSchritt schritt = new DmSchritt();
            schritt.setTitel("Post abholen");
            logger.info("Schritt-Id vor persist: " + schritt.getId());
            logger.info("contains vor persist: " + entityManager.contains(schritt));
            //Hierdurch wird schritt zu einer "managed entity":
            entityManager.persist(schritt);
            logger.info("Schritt-Id nach persist: " + schritt.getId());
            logger.info("contains nach persist: " + entityManager.contains(schritt));
            //Hierdurch werden alle "managed entities" in die Datenbank geschrieben:
            transaction.commit();
            //Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
            entityManager.close();
        }

        { //2. Wirkung des rollback ausprobieren:
            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            final EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            final DmSchritt schritt = new DmSchritt();
            schritt.setTitel("Ein problematischer Schritt");
            entityManager.persist(schritt);
            final Long idOfFailed = schritt.getId();
            logger.info("Schritt nach persist: ID=" + idOfFailed + ", titel=" + schritt.getTitel());
            //Hierdurch werden alle Veränderungen an "managed entities" seit dem letzten commit in der Datenbank rückabgewickelt.
            //Im Arbeitsspeicher aber bleiben sie bestehen. Die Transaktion wird beendet.
            transaction.rollback();
            logger.info("Schritt nach rollback: ID=" + schritt.getId() + ", titel=" + schritt.getTitel());
            //Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
            entityManager.close();

            //Ab jetzt alle Transaktionen redundanzfrei mit untenstehender Hilfsklasse EMTransaction:
            final EMTransaction tx = new EMTransaction();
            final DmSchritt schritt2 = tx.em.find(DmSchritt.class, idOfFailed);
            logger.info("Schritt nach rollback und Holen: " + schritt2);
            tx.close(true);
        }

        { //3. Entity mit id holen und ändern:
            final EMTransaction tx = new EMTransaction();
            final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
            logger.info("Schritt in neuer TX nach find: ID=" + schritt.getId() + ", titel=" + schritt.getTitel() + ", istStunden=" + schritt.getIstStunden());
            schritt.setRestStunden(0);
            schritt.setIstStunden(2);
            //Die Änderungen sollen durch das commit gespeichert werden:
            tx.close(true);
            //Alle Entities sind jetzt DETACHED: Weitere Änderungen werden nicht mehr automatisch gespeichert:
            logger.info("Schritt-Id nach close(true): " + schritt.getId());
            //Diese Änderungen werden nicht mehr gespeichert:
            schritt.setIstStunden(1000);
        }

        { //4. detach und merge prüfen:
            final DmSchritt schritt;
            {
                final EMTransaction tx = new EMTransaction();
                schritt = tx.em.find(DmSchritt.class, 1L);
                logger.info("Schritt nach close(true) und Änderung in neuer TX: ID="
                        + schritt.getId() + ", titel=" + schritt.getTitel()
                        + ", istStunden=" + schritt.getIstStunden());
                tx.close(true);
                //Alle Entities sind jetzt DETACHED: Weitere Änderungen werden nicht mehr automatisch gespeichert:
            }
            //Diese Änderungen werden normalerweise nicht mehr gespeichert:
            schritt.setIstStunden(3);
            {
                final EMTransaction tx = new EMTransaction();
                //Die Entity schritt wieder MANAGED machen. Aktueller Zustand von ihr wird bei Transaktionsende gespeichert:
                tx.em.merge(schritt);
                logger.info("Schritt-IstStunden nach detach, Änderung und merge in neuer TX: " + schritt.getIstStunden());
                tx.close(true);
            }
        }

        { //5. Entity wieder mit id holen und prüfen:
            final EMTransaction tx = new EMTransaction();
            final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
            logger.info("Schritt in neuer TX nach close,Ändern,merge,close,find: ID=" + schritt.getId() + ", titel=" + schritt.getTitel() + ", istStunden=" + schritt.getIstStunden());
            tx.close(true);
        }

        { //6. Alle Schritte mit Java Persistence Query Language (JPQL) holen:
            final EMTransaction tx = new EMTransaction();
            //TypedQuery<T> wurde in JPA 2 eingeführt, um den Cast nach T zu vermeiden.
            final TypedQuery<DmSchritt> q = tx.em.createQuery("SELECT o FROM " + DmSchritt.class.getName() + " o ORDER BY id DESC", DmSchritt.class);
            final List<DmSchritt> results = q.getResultList();
            tx.close(true);
            final StringBuilder out = new StringBuilder();
            for (final DmSchritt s : results) {
                out.append("Schritt: id=").append(s.getId()).append(", titel=").append(s.getTitel()).append("\n");
            }
            logger.info("Alle Schritte: " + out);
        }

        { //7. Entity wieder mit id holen und dann löschen:
            final EMTransaction tx = new EMTransaction();
            final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
            logger.info("Schritt in neuer TX vor Löschen: ID=" + schritt.getId() + ", titel=" + schritt.getTitel());
            tx.em.remove(schritt);
            logger.info("Schritt in neuer TX nach Löschen: ID=" + schritt.getId() + ", titel=" + schritt.getTitel());
            tx.close(true);
        }

        { //8. Entity nach Löschen holen:
            final EMTransaction tx = new EMTransaction();
            final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
            logger.info("Schritt nach Löschen und Holen: schritt=" + schritt);
            tx.close(true);
        }

        { //Aufräumen:
            entityManagerFactory.close();
        }

    }

    /**
     * Kapselt einen {@link EntityManager} mit einem "transaction-scoped persistence context" und einer {@link EntityTransaction} und startet diese Transaktion.
     * Zum Vorgehen bei manueller Transaktionsverwaltung siehe https://docs.jboss.org/hibernate/entitymanager/3.5/reference/en/html/transactions.html#transactions-demarcation-nonmanaged
     */
    private class EMTransaction {
        /**
         * Der {@link EntityManager} mit Transaction Scope für die weiteren Datenzugriffsoperationen.
         */
        public final EntityManager em = entityManagerFactory.createEntityManager();
        private final EntityTransaction transaction = em.getTransaction();

        {
            transaction.begin();
        }

        /**
         * Schließt die {@link EMTransaction} mit entweder commit oder rollback.
         *
         * @param ok die Transaktion war bis zum Ende fehlerfrei => commit, andernfalls rollback.
         */
        public void close(final boolean ok) {
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
        }
    }

}

