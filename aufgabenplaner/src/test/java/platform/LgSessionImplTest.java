package platform;

import l2_lg.LgSession;
import l2_lg.LgSessionImpl;
import l3_da.*;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.Date;

/**
 * Created by Stephan D on 06.06.2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LgSessionImplTest extends Assert {

    //private static final DaFactory daFactory = new DaFactoryForJPA();

    @Test
    public void t010_SessionSpeichernTitleLengthBelow10() throws Exception {

        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("123456789");

        try {
            lgSession.speichern(schritt);
            fail("LgSession.TitelExc expected");
        } catch (LgSession.TitelExc expected) {
        }
    }

    @Test
    public void t020_SessionSpeichernTitleLengthAbove200() throws Exception {

        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel
                ("01234567890123456789012345678901234567890123456789" +
                        "01234567890123456789012345678901234567890123456789" +
                        "01234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890");

        try {
            lgSession.speichern(schritt);
            fail("LgSession.TitelExc expected");
        } catch (LgSession.TitelExc expected) {
        }
    }

    @Test
    public void t030_SessionSpeichernRestStundenNotNegative() throws Exception {

        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("01234567890");
        schritt.setRestStunden(-1);

        try {
            lgSession.speichern(schritt);
            fail("LgSession.RestStundenExc expected");
        } catch (LgSession.RestStundenExc expected) {
        }
    }

    @Test
    public void t040_SessionSpeichernIstStundenNotNegative() throws Exception {

        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("01234567890");
        schritt.setIstStunden(-1);

        try {
            lgSession.speichern(schritt);
            fail("LgSession.IstStundenExc expected");
        } catch (LgSession.IstStundenExc expected) {
        }
    }

    @Test
    public void t050_SessionSpeichernVorhabenRekursionExc() throws Exception {

        // Session speichern
        // Das in der Aufgabe angegebene Ganzes-Vorhaben mit ID {0} und Titel "{1}"
        // ist seinerseits direkt oder indirekt Teil dieser Aufgabe mit ID {2}.
        // Solche Rekursion ist verboten!
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmVorhaben vorhaben = new DmVorhaben();
        vorhaben.setTitel("0123456789");
        vorhaben.setIstStunden(0);
        vorhaben.setRestStunden(0);
        vorhaben.setEndTermin(Date.valueOf("2020-08-01"));
        vorhaben.setGanzes(vorhaben);

        try {
            lgSession.speichern(vorhaben);
            fail("LgSession.VorhabenRekursionExc expected");
        } catch (LgSession.VorhabenRekursionExc expected) {}
    }

    @Test
    public void t060_SessionSpeichernEndTerminInPast() throws Exception {

        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmVorhaben vorhaben = new DmVorhaben();

        vorhaben.setTitel("0123456789");
        vorhaben.setRestStunden(0);
        vorhaben.setIstStunden(0);
        vorhaben.setEndTermin(Date.valueOf("2015-01-01"));

        try {
            lgSession.speichern(vorhaben);
            fail("LgSession.EndTerminExc expected");
        } catch (LgSession.EndTerminExc expected) {}
    }

    @Test
    public void t070_SessionIdNotFoundExc() throws Exception {

        // Session loeschen
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);

        try {
            lgSession.loeschen(99999L);
            fail("DaGeneric.IdNotFoundExc expected");
        } catch (DaGeneric.IdNotFoundExc expected) {}
    }

    @Test
    public void t080_SessionLoeschenTeileExc() throws Exception {

        // Session loeschen
        // Das Vorhaben mit ID {0} und Titel "{1}"
        // enthält noch {2} Teil(e) und kann daher nicht gelöscht werden!
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmVorhaben vorhaben = new DmVorhaben();
        vorhaben.setTitel("0123456789");
        vorhaben.setIstStunden(0);
        vorhaben.setRestStunden(0);
        vorhaben.setEndTermin(Date.valueOf("2018-01-01"));
        lgSession.speichern(vorhaben);

        final DmSchritt schritt = new DmSchritt();
        schritt.setGanzes(vorhaben);
        schritt.setTitel("012345678901");
        vorhaben.setIstStunden(0);
        vorhaben.setRestStunden(0);
        lgSession.speichern(schritt);

        final Long savedId = vorhaben.getId();
        assertEquals(new Long (1L), savedId);
        try {
            lgSession.loeschen(savedId);
            fail("LgSession.LoeschenTeileExc expected");
        } catch (LgSession.LoeschenTeileExc expected) {}
    }

    @Test
    public void t090_SessionSchrittErledigenTitleLengthBelow10() throws Exception {

        // Session Schritt erledigen
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("123456789");

        try {
            lgSession.erledigen(schritt);
            fail("LgSession.TitelExc expected");
        } catch (LgSession.TitelExc expected) {
        }
    }

    @Test
    public void t100_SessionSchrittErledigenTitleLengthAbove200() throws Exception {

        // Session Schritt erledigen
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel
                ("01234567890123456789012345678901234567890123456789" +
                        "01234567890123456789012345678901234567890123456789" +
                        "01234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890");

        try {
            lgSession.erledigen(schritt);
            fail("LgSession.TitelExc expected");
        } catch (LgSession.TitelExc expected) {
        }
    }

    @Test
    public void t110_SessionSchrittErledigenIstStundenNotNegative() throws Exception {

        // Session Schritt erledigen
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("0123456789");
        schritt.setIstStunden(-1);

        try {
            lgSession.erledigen(schritt);
            fail("LgSession.IstStundenExc expected");
        } catch (LgSession.IstStundenExc expected) {
        }
    }

    @Test
    public void t120_AlleOberstenAufgabenLiefern() throws Exception {

        // Alle obersten Aufgaben liefern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("Oberste Aufgabe");
        schritt.setBeschreibung("0123456789");
        schritt.setIstStunden(0);

        lgSession.speichern(schritt);
        final Long savedId = schritt.getId();

        lgSession.alleOberstenAufgabenLiefern();

        assertEquals(new Long(3L), schritt.getId());
        assertEquals("Oberste Aufgabe", schritt.getTitel());
        assertEquals("0123456789", schritt.getBeschreibung());
        assertEquals(0, schritt.getIstStunden());
    }

    @Test
    public void t130_AlleVorhabenLiefern() throws Exception {

        // Alle Vorhaben liefern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmVorhaben vorhaben = new DmVorhaben();
        vorhaben.setTitel("Mein Vorhaben");
        vorhaben.setBeschreibung("0123456789");
        vorhaben.setIstStunden(0);
        vorhaben.setRestStunden(0);
        vorhaben.setEndTermin(Date.valueOf("2020-08-01"));

        lgSession.speichern(vorhaben);
        final Long savedId = vorhaben.getId();
        assertEquals(new Long (4L), savedId);

        lgSession.alleVorhabenLiefern();

        assertEquals(new Long(4L), vorhaben.getId());
        assertEquals("Mein Vorhaben", vorhaben.getTitel());
        assertEquals("0123456789", vorhaben.getBeschreibung());
        assertEquals(0, vorhaben.getIstStunden());
        assertEquals(0, vorhaben.getRestStunden());
        assertEquals(Date.valueOf("2020-08-01"), vorhaben.getEndTermin());
    }

    @Test
    public void t140_SpeichernTest() throws Exception {
        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("MeinSpeichernTest");

        lgSession.speichern(schritt);

        final Long savedId = schritt.getId();
        assertEquals(new Long(5L), savedId);
        assertEquals("MeinSpeichernTest", schritt.getTitel());
    }

    @Test
    public void t150_ErledigenTest() throws Exception {
        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("MeinErledigenTest");

        lgSession.erledigen(schritt);

        final Long savedId = schritt.getId();
        assertEquals(new Long(6L), savedId);
        assertEquals("MeinErledigenTest", schritt.getTitel());
    }

    @Test
    public void t160_LoeschenTest() throws Exception {
        // Session speichern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmSchritt schritt = new DmSchritt();
        schritt.setTitel("MeinLoeschenTest");
        lgSession.speichern(schritt);
        final Long savedId = schritt.getId();
        assertEquals("MeinLoeschenTest", schritt.getTitel());
        assertEquals(new Long(7L), savedId);

        lgSession.loeschen(savedId);

        assertEquals(new Long (7L), schritt.getId());
    }
}