package l2_lg;

import l3_da.*;
import l4_dm.DmAufgabe;
import l4_dm.DmAufgabeStatus;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

import java.util.ArrayList;
import java.util.List;

import static multex.MultexUtil.create;

/**
 * Created by Stephan D on 19.06.2016.
 */
public class LgSessionImpl<A extends DmAufgabe> implements LgSession {

    private final DaFactory daFactory;
    private final DaAufgabe daAufgabe;

    public LgSessionImpl(final DaFactory daFactory) {
        this.daFactory = daFactory;
        this.daAufgabe = daFactory.getAufgabeDA();
    }

    @Override
    public <A extends DmAufgabe> A speichern(final A aufgabe) throws TitelExc, RestStundenExc, IstStundenExc, EndTerminExc, VorhabenRekursionExc {
        // Titel muss zwischen 10 und 200 Zeichen lang sein! Länge: {0}, Titel: {1}
        final int length = aufgabe.getTitel().length();
        if (length < 10 || length > 200) {
            throw create(TitelExc.class, length, aufgabe.getTitel());
        }
        // Rest-Stundenanzahl darf nicht negativ sein! Wert: {0} Stunden
        if (aufgabe.getRestStunden() < 0) {
            throw create(RestStundenExc.class, aufgabe.getRestStunden());
        }
        // Ist-Stundenanzahl darf nicht negativ sein! Wert: {0} Stunden
        if (aufgabe.getIstStunden() < 0) {
            throw create(IstStundenExc.class, aufgabe.getIstStunden());
        }
        if (aufgabe instanceof DmVorhaben) {
            final DmVorhaben vorhaben = (DmVorhaben) aufgabe;
            final java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

            // Der angegebene End-Termin {0, date, short} liegt in der Vergangenheit!
            if (vorhaben.getEndTermin().compareTo(date) < 0)
                throw create(EndTerminExc.class, vorhaben.getEndTermin());
            pruefenVorhabenRekursion(vorhaben, vorhaben.getGanzes());
        }
        new Template() {

            @Override
            public void primitiveOperation() {
                daAufgabe.save(aufgabe);
            }
        }.templateMethod();

        return aufgabe;
    }

    @Override
    public void loeschen(final Long aufgabenId) throws DaGeneric.IdNotFoundExc, LoeschenTeileExc {
        final DmAufgabe entity = daAufgabe.find(aufgabenId);
        // Das Vorhaben mit ID {0} und Titel "{1}"
        // enthält noch {2} Teil(e) und kann daher nicht gelöscht werden!

        //TODO alle Aufgaben in einer Schleife durchlaufen und jede Aufgabe überprüfen
        final List<DmAufgabe> alleAufgaben = daAufgabe.findAll();
        for (final DmAufgabe dmAufgabe : alleAufgaben) {
            final DmVorhaben ganzes = dmAufgabe.getGanzes();
            if (ganzes == entity) {
                throw create(LoeschenTeileExc.class, entity, entity.getTitel(), entity.getAnzahlTeile());
            }
        }
        new Template() {
            @Override
            public void primitiveOperation() {
                daAufgabe.delete(entity);
            }
        }.templateMethod();
    }

    @Override
    public DmSchritt erledigen(final DmSchritt schritt) throws TitelExc, IstStundenExc {
        // Titel muss zwischen 10 und 200 Zeichen lang sein! Länge: {0}, Titel: {1}
        final int length = schritt.getTitel().length();
        if (length < 10 || length > 200) {
            throw create(TitelExc.class, schritt.getTitel());
        }
        // Ist-Stundenanzahl darf nicht negativ sein! Wert: {0} Stunden
        if (schritt.getIstStunden() < 0) {
            throw create(IstStundenExc.class, schritt.getIstStunden());
        }
        schritt.setRestStunden(0);
        schritt.setErledigtZeitpunkt(new java.sql.Date(System.currentTimeMillis()));
        // speichern aufrufen
        // Transaktion
        speichern(schritt);
        return schritt;
    }

    @Override
    public List<DmAufgabe> alleOberstenAufgabenLiefern() {
        final List<DmAufgabe> allerAufgaben = daAufgabe.findAll();
        final List<DmAufgabe> result = new ArrayList<>();

        new Template() {
            @Override
            public void primitiveOperation() {
                for (final DmAufgabe dmAufgabe : allerAufgaben) {
                    final DmVorhaben ganzes = dmAufgabe.getGanzes();
                    if (ganzes == null) {
                        result.add(dmAufgabe);
                    } else {
                        ganzes.getTeile().add(dmAufgabe);
                    }
                    transienteDatenRekursivBerechnen(dmAufgabe);
                }
            }
        }.templateMethod();
        return result;
    }

    @Override
    public List<DmVorhaben> alleVorhabenLiefern() {
        final List<DmAufgabe> allerAufgaben = daAufgabe.findAll();
        final List<DmVorhaben> result = new ArrayList<>();

        new Template() {
            @Override
            public void primitiveOperation() {
                for (final DmAufgabe dmAufgabe : allerAufgaben) {
                    if (dmAufgabe instanceof DmVorhaben) {
                        final DmVorhaben vorhaben = (DmVorhaben) dmAufgabe;
                        result.add(vorhaben);
                    }
                }
            }
        }.templateMethod();
        return result;
    }

    // Das in der Aufgabe angegebene Ganzes-Vorhaben mit ID {0} und Titel "{1}"
    // ist seinerseits direkt oder indirekt Teil dieser Aufgabe mit ID {2}.
    // Solche Rekursion ist verboten!
    private void pruefenVorhabenRekursion(final DmVorhaben verboten, final DmVorhaben aktuell) {
        if (aktuell == verboten)
            throw create(VorhabenRekursionExc.class, verboten.getId(), verboten.getTitel());
        if (aktuell == null) return;
        pruefenVorhabenRekursion(verboten, aktuell.getGanzes());
    }

    // Ist-Stunden aller Unterknoten rekursiv aufsummieren
    private int istStundenSumme(final DmVorhaben dmVorhaben) {
        int result = 0;
        for (final DmAufgabe dmAufgabe : dmVorhaben.getTeile()) {
            if (dmAufgabe instanceof DmSchritt) {
                result += dmAufgabe.getIstStunden();
            } else {
                result += istStundenSumme((DmVorhaben) dmAufgabe);
            }
        }
        return result;
    }

    // Rest-Stunden aller Unterknoten rekursiv aufsummieren
    private int restStundenSumme(final DmVorhaben dmVorhaben) {
        int result = 0;
        for (final DmAufgabe dmAufgabe : dmVorhaben.getTeile()) {
            if (dmAufgabe instanceof DmSchritt) {
                result += dmAufgabe.getRestStunden();
            } else {
                result += istStundenSumme((DmVorhaben) dmAufgabe);
            }
        }
        return result;
    }

    private void transienteDatenRekursivBerechnen(final DmAufgabe dmAufgabe) {
        if (dmAufgabe instanceof DmSchritt) return;
        final DmVorhaben dmVorhaben = (DmVorhaben) dmAufgabe;
        istStundenSumme(dmVorhaben);
        restStundenSumme(dmVorhaben);
        status(dmVorhaben);
    }

    private DmAufgabeStatus status(final DmVorhaben vorhaben) {
        final List<DmAufgabe> teile = vorhaben.getTeile();
        DmAufgabeStatus result = vorhaben.getStatus();
        for (DmAufgabe teil : teile) {
            if (teil instanceof DmVorhaben) {
                if (teil.getTeile() == null) {
                    ((DmVorhaben) teil).setStatus(DmAufgabeStatus.neu);
                    result = teil.getStatus();
                } else if (teil.getStatus() == DmAufgabeStatus.neu) {
                    ((DmVorhaben) teil).setStatus(DmAufgabeStatus.neu);
                    result = status((DmVorhaben) teil);
                } else {
                    ((DmVorhaben) teil).setStatus(DmAufgabeStatus.inBearbeitung);
                    result = teil.getStatus();
                }
            } else {
                result = teil.getStatus();
            }
        }
        return result;
    }

    private abstract class Template {
        public void templateMethod() {
            daFactory.beginTransaction();
            boolean ok = false;
            try {
                primitiveOperation();
                ok = true;
            } finally {
                daFactory.endTransaction(ok);
            }
        }

        public abstract void primitiveOperation();
    }
}