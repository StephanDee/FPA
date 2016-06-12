package platform;

import l3_da.DaFactoryForJPA;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;
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

    @Test
    public void t020_VorhabenEntityPersist() throws Exception {

        // Entity persistieren
        final DaFactoryForJPA transaction = new DaFactoryForJPA();
        final DmVorhaben vorhaben = new DmVorhaben();
        vorhaben.getId();
        vorhaben.getTitel();
        vorhaben.getBeschreibung();
        vorhaben.getTeile();
        vorhaben.getIstStunden();
        vorhaben.getRestStunden();
        vorhaben.getStatus();
        vorhaben.getEndTermin();

        //Hierdurch wird schritt zu einer "managed entity":
        assertEquals(null, vorhaben.getId());
        assertEquals(null, vorhaben.getTitel());
        assertEquals(null, vorhaben.getBeschreibung());
//        assertEquals([], vorhaben.getTeile());
        assertEquals(-999999, vorhaben.getIstStunden());
        assertEquals(-999999, vorhaben.getRestStunden());
//        assertEquals(dmaufgabestatus<neu>"neu", vorhaben.getStatus());
        assertEquals(null, vorhaben.getEndTermin());
        assertEquals(transaction.em.contains(vorhaben), false);

        transaction.em.persist(vorhaben);

        // Hierdurch werden alle "managed entities" in die Datenbank geschrieben:
        assertEquals(new Long(2L), vorhaben.getId());
        assertEquals(null, vorhaben.getTitel());
        assertEquals(null, vorhaben.getBeschreibung());
//        assertEquals([], vorhaben.getTeile());
        assertEquals(-999999, vorhaben.getIstStunden());
        assertEquals(-999999, vorhaben.getRestStunden());
//        assertEquals(dmaufgabestatus<neu>"neu", schritt.getStatus());
        assertEquals(null, vorhaben.getEndTermin());
        assertEquals(transaction.em.contains(vorhaben), true);

        transaction.endTransaction(true);
    }

}