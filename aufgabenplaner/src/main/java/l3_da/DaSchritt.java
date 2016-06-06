package l3_da;

import l4_dm.DmSchritt;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public interface DaSchritt extends DaGeneric<DmSchritt> {

    public final String persistenceUnitName = "aufgabenplaner"; //as specified in src/main/resources/META-INF/persistence.xml
    //createEntityManagerFactory ist eine sehr aufwändige Operation! Die EntityManagerFactory muss am Ende manuell geschlossen werden!
    public final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

    public static void execute() throws Exception {

        {
            // Entity persistieren
            final DaFactoryForJPA transaction = new DaFactoryForJPA();
            transaction.beginTransaction();
            final DmSchritt schritt = new DmSchritt();
            schritt.getId();
            schritt.getTitel();
            schritt.getBeschreibung();
            schritt.getTeile();
            schritt.getIstStunden();
            schritt.getRestStunden();
            schritt.getStatus();
            schritt.getErledigtZeitpunkt();

            //Hierdurch wird schritt zu einer "managed entity":
            transaction.em.persist(schritt);

            //Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
            transaction.endTransaction(true);

        }

    }

}
