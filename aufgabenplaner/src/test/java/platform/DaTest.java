package platform;

import l3_da.*;
import l4_dm.DmAufgabeStatus;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collections;

/**
 * Created by Stephan D on 06.06.2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaTest extends Assert {

    private static final DaFactory daFactory = new DaFactoryForJPA();

    @Test
    public void t001_findEntityTest() throws Exception {

        // Entity Schritt speichern
        final DaSchritt daSchritt = daFactory.getSchrittDA();
        try {
            daSchritt.find(999999L);
            fail("DaGeneric.IdNotFoundExc expected");
        } catch (DaGeneric.IdNotFoundExc expected) {}
    }

    @Test
    public void t010_SaveEntitySchritt() throws Exception {

        // Entity Schritt speichern
        final DaSchritt daSchritt = daFactory.getSchrittDA();
        final DmSchritt schritt = new DmSchritt();

        schritt.setTitel("Mein Aufgabenplaner");
        schritt.setBeschreibung("Meine Aufgabe");
        schritt.getTeile();
        schritt.setIstStunden(0);
        schritt.setRestStunden(2);
        schritt.getStatus();
        schritt.getErledigtZeitpunkt();

        daFactory.beginTransaction();
        daSchritt.save(schritt);
        daFactory.endTransaction(true);

        final Long savedId = schritt.getId();

        // Entity Schritt im Round-Trip-Verfahren lesen und prüfen
        final DaFactory daFactory2 = new DaFactoryForJPA();
        final DaSchritt daSchritt2 = daFactory2.getSchrittDA();
        final DmSchritt schritt2 = daSchritt2.find(savedId);

        daFactory2.beginTransaction();

        assertEquals(new Long(1L), schritt2.getId());
        assertEquals("Mein Aufgabenplaner", schritt2.getTitel());
        assertEquals("Meine Aufgabe", schritt2.getBeschreibung());
        assertEquals(Collections.emptyList(), schritt2.getTeile());
        assertEquals(0, schritt2.getIstStunden());
        assertEquals(2, schritt2.getRestStunden());
        assertEquals(DmAufgabeStatus.valueOf("inBearbeitung"), schritt2.getStatus());
        assertEquals(null, schritt2.getErledigtZeitpunkt());

        daFactory2.endTransaction(true);

    }

    @Test
    public void t020_MergeEntitySchritt() throws Exception {

        // Entity Schritt mergen
        final DaFactory daFactory = new DaFactoryForJPA();
        final DaSchritt daSchritt = daFactory.getSchrittDA();
        final DmSchritt schritt = daSchritt.find(1L);

        schritt.setTitel("Mein erster Aufgabenplaner");
        schritt.setBeschreibung("Meine erste Aufgabe");
        schritt.getTeile();
        schritt.setIstStunden(1);
        schritt.setRestStunden(4);
        schritt.getStatus();
        schritt.getErledigtZeitpunkt();

        daFactory.beginTransaction();
        daSchritt.save(schritt);
        daFactory.endTransaction(true);

        final Long savedId = schritt.getId();

        // Entity Schritt im Round-Trip-Verfahren lesen und prüfen
        final DaFactory daFactory2 = new DaFactoryForJPA();
        final DaSchritt daSchritt2 = daFactory2.getSchrittDA();
        final DmSchritt schritt2 = daSchritt2.find(savedId);

        daFactory2.beginTransaction();

        assertEquals(new Long(1L), schritt2.getId());
        assertEquals("Mein erster Aufgabenplaner", schritt2.getTitel());
        assertEquals("Meine erste Aufgabe", schritt2.getBeschreibung());
        assertEquals(Collections.emptyList(), schritt2.getTeile());
        assertEquals(1, schritt2.getIstStunden());
        assertEquals(4, schritt2.getRestStunden());
        assertEquals(DmAufgabeStatus.valueOf("inBearbeitung"), schritt2.getStatus());
        assertEquals(null, schritt2.getErledigtZeitpunkt());

        daFactory2.endTransaction(true);
    }

    @Test
    public void t030_DeleteEntitySchritt() throws Exception {

        // Entity Schritt löschen
        final DaFactory daFactory = new DaFactoryForJPA();
        final DaSchritt daSchritt = daFactory.getSchrittDA();
        final DmSchritt schritt = daSchritt.find(1L);

        daFactory.beginTransaction();
        daSchritt.delete(schritt);
        daFactory.endTransaction(true);

        final Long savedId = schritt.getId();

        // Entity Schritt im Round-Trip-Verfahren lesen und prüfen
        final DaFactory daFactory2 = new DaFactoryForJPA();
        final DaSchritt daSchritt2 = daFactory2.getSchrittDA();
        final DmSchritt schritt2 = daSchritt2.find(savedId);

        daFactory2.beginTransaction();
        assertEquals(null, schritt2);
        daFactory2.endTransaction(true);

    }

    @Test
    public void t040_SaveEntityVorhaben() throws Exception {

        // Entity Vorhaben speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final DaVorhaben daVorhaben = daFactory.getVorhabenDA();
        final DmVorhaben vorhaben = new DmVorhaben();
        vorhaben.setTitel("Mein zweiter Aufgabenplaner");
        vorhaben.setBeschreibung("Meine zweite Aufgabe");
        vorhaben.getTeile();
        vorhaben.setIstStunden(-999999);
        vorhaben.setRestStunden(-999999);
        vorhaben.getEndTermin();

        daFactory.beginTransaction();
        daVorhaben.save(vorhaben);
        daFactory.endTransaction(true);

        final Long savedId = vorhaben.getId();

        // Entity Vorhaben im Round-Trip-Verfahren lesen und prüfen
        final DaFactory daFactory2 = new DaFactoryForJPA();
        final DaVorhaben daVorhaben2 = daFactory2.getVorhabenDA();
        final DmVorhaben vorhaben2 = daVorhaben2.find(savedId);

        daFactory2.beginTransaction();

        assertEquals(savedId, vorhaben2.getId());
        assertEquals("Mein zweiter Aufgabenplaner", vorhaben2.getTitel());
        assertEquals("Meine zweite Aufgabe", vorhaben2.getBeschreibung());
        assertEquals(Collections.emptyList(), vorhaben2.getTeile());
        assertEquals(-999999, vorhaben2.getIstStunden());
        assertEquals(-999999, vorhaben2.getRestStunden());
        assertEquals(null, vorhaben2.getEndTermin());

        daFactory2.endTransaction(true);
    }

    @Test
    public void t050_DeleteEntityVorhaben() throws Exception {

        // Entity Vorhaben löschen
        final DaFactory daFactory = new DaFactoryForJPA();
        final DaVorhaben daVorhaben = daFactory.getVorhabenDA();
        final DmVorhaben vorhaben = daVorhaben.find(2L);


        daFactory.beginTransaction();
        daVorhaben.delete(vorhaben);

        assertEquals(new Long(2L), vorhaben.getId());
        assertEquals("Mein zweiter Aufgabenplaner", vorhaben.getTitel());
        assertEquals("Meine zweite Aufgabe", vorhaben.getBeschreibung());
        assertEquals(Collections.emptyList(), vorhaben.getTeile());
        assertEquals(-999999, vorhaben.getIstStunden());
        assertEquals(-999999, vorhaben.getRestStunden());
        assertEquals(null, vorhaben.getEndTermin());

        daFactory.endTransaction(true);

        final Long savedId = vorhaben.getId();

        // Entity Vorhaben im Round-Trip-Verfahren lesen und prüfen
        final DaFactory daFactory2 = new DaFactoryForJPA();
        final DaVorhaben daVorhaben2 = daFactory2.getVorhabenDA();
        final DmVorhaben vorhaben2 = daVorhaben2.find(savedId);

        daFactory2.beginTransaction();
        assertEquals(null, vorhaben2);
        daFactory2.endTransaction(true);
    }

}