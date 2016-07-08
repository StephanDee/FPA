package platform;

import l2_lg.LgSession;
import l2_lg.LgSessionImpl;
import l3_da.DaFactory;
import l3_da.DaFactoryForJPA;
import l3_da.DaGeneric;
import l3_da.DaSchritt;
import l4_dm.DmAufgabe;
import l4_dm.DmAufgabeStatus;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

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
        final DmVorhaben vorhaben = new DmVorhaben() {
            @Override
            public Long getId() {
                return new Long(0L);
            }
        };
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
        final DmVorhaben vorhaben = new DmVorhaben() {
            @Override
            public Long getId() {
                return new Long(0L);
            }
        };
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
            lgSession.loeschen(11L);
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
        final DmVorhaben vorhaben = new DmVorhaben() {
            @Override
            public Long getId() {
                return new Long(2L);
            }

            @Override
            public int getAnzahlTeile() {
                return 2;
            }
        };
        assertEquals(new Long(2L), vorhaben.getId());
        vorhaben.setTitel("0123456789");
        vorhaben.setIstStunden(0);
        vorhaben.setRestStunden(0);
        vorhaben.setEndTermin(Date.valueOf("2018-01-01"));
        assertEquals(new Long(2L), vorhaben.getId());
        assertEquals(2, vorhaben.getAnzahlTeile());

        try {
            lgSession.loeschen(vorhaben.getId());
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
        final DmSchritt schritt = new DmSchritt() {
            @Override
            public Long getId() {
                return new Long(9L);
            }
        };
        schritt.setTitel("Oberste Aufgabe");
        schritt.setTitel("0123456789");
        schritt.setIstStunden(0);

        final DmAufgabe aufgabe = new DmAufgabe() {
            @Override
            public int getRestStunden() {
                return 0;
            }

            @Override
            public int getIstStunden() {
                return 0;
            }

            @Override
            public int getAnzahlTeile() {
                return 0;
            }

            @Override
            public List<DmAufgabe> getTeile() {
                return null;
            }

            @Override
            public DmAufgabeStatus getStatus() {
                return null;
            }
        };

        lgSession.alleOberstenAufgabenLiefern();

        // Entity Schritt im Round-Trip-Verfahren lesen und prüfen
//        final DaFactory daFactory2 = new DaFactoryForJPA();
//        final DaSchritt daSchritt2 = daFactory2.getSchrittDA();
//        final DmSchritt schritt2 = daSchritt2.find(9L);
//
//        daFactory2.beginTransaction();
//
//        assertEquals(new Long(1L), schritt2.getId());
//        assertEquals("Oberste Aufgabe", schritt2.getTitel());
//        assertEquals("0123456789", schritt2.getBeschreibung());
//        assertEquals(0, schritt2.getIstStunden());
//        // assertEquals(DmAufgabeStatus.valueOf("inBearbeitung"), schritt2.getStatus());
//
//        daFactory2.endTransaction(true);
    }

    @Test
    public void t130_AlleVorhabenLiefern() throws Exception {

        // Alle Vorhaben liefern
        final DaFactory daFactory = new DaFactoryForJPA();
        final LgSession lgSession = new LgSessionImpl(daFactory);
        final DmVorhaben vorhaben = new DmVorhaben();
        vorhaben.setTitel("Oberste Aufgabe");
        vorhaben.setTitel("0123456789");
        vorhaben.setIstStunden(0);

        lgSession.alleVorhabenLiefern();
    }
}