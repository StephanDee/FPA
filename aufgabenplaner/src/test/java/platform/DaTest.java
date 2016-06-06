package platform;

import l3_da.DaFactoryForJPA;
import l4_dm.DmSchritt;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.persistence.*;

/**
 * Created by Stephan D on 06.06.2016.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaTest extends Assert {

    private static final String persistenceUnitName = "aufgabenplaner"; //as specified in src/main/resources/META-INF/persistence.xml
    //createEntityManagerFactory ist eine sehr aufwändige Operation! Die EntityManagerFactory muss am Ende manuell geschlossen werden!
    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    @Test
    public void t010_SchrittEntityPersist() throws Exception {

        // Entity persistieren
        final DaFactoryForJPA transaction = new DaFactoryForJPA();
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
        assertEquals(null, schritt.getId());
        assertEquals(null, schritt.getTitel());
        assertEquals(null, schritt.getBeschreibung());
//        assertEquals([], schritt.getTeile());
        assertEquals(0, schritt.getIstStunden());
        assertEquals(0, schritt.getRestStunden());
//        assertEquals(dmaufgabestatus<neu>"neu", schritt.getStatus());
        assertEquals(null, schritt.getErledigtZeitpunkt());
        assertEquals(transaction.em.contains(schritt), false);

        transaction.em.persist(schritt);

        // Hierdurch werden alle "managed entities" in die Datenbank geschrieben:
        assertEquals(new Long(1L), schritt.getId());
        assertEquals(null, schritt.getTitel());
        assertEquals(null, schritt.getBeschreibung());
//        assertEquals([], schritt.getTeile());
        assertEquals(0, schritt.getIstStunden());
        assertEquals(0, schritt.getRestStunden());
//        assertEquals(dmaufgabestatus<neu>"neu", schritt.getStatus());
        assertEquals(null, schritt.getErledigtZeitpunkt());
        assertEquals(transaction.em.contains(schritt), true);

        transaction.endTransaction(true);
    }

}