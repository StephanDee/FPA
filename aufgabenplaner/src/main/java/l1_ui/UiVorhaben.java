package l1_ui;

/**
 * Created by Stephan D on 20.04.2016.
 */

import l4_dm.DmAufgabeStatus;
import l4_dm.DmVorhaben;

import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class UiVorhaben extends UiAufgabe {

    protected final JLabel edate = new JLabel("End Termin:");
    protected final JTextField edatefield = new JTextField("");

    public UiVorhaben(final DmVorhaben vorhaben, final List<DmVorhaben> list) {
        super(vorhaben, list);

        this.setTitle("Vorhaben speichern/löschen");

        idfield.setText(String.valueOf(vorhaben.getId()));
        titlefield.setText(vorhaben.getTitel());
        descrarea.setText(vorhaben.getBeschreibung());
        resthfield.setText(String.valueOf(vorhaben.getRestStunden()));
        resthfield.setEditable(false);
        ishfield.setText(String.valueOf(vorhaben.getIstStunden()));
        statusfield.setText(String.valueOf(vorhaben.getStatus()));
        ishfield.setEditable(false);
        statusfield.setEditable(false);
        fieldpanel.add(edate);
        edate.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(edatefield);
        edatefield.setAlignmentX(Component.LEFT_ALIGNMENT);
        edatefield.setText(String.valueOf(vorhaben.getEndTermin()));
    }

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final DmVorhaben vorhaben = new DmVorhaben(){
                    @Override
                    public Long getId() {
                        return 11L;
                    }
                };
                final List<DmVorhaben> list = Arrays.asList(new DmVorhaben() {
                    @Override
                    public Long getId() {
                        return 11L;
                    }

                    @Override
                    public final String getTitel() {
                        return "Persistenzaufgabe lösen";
                    }
                });
                vorhaben.setTitel("Meine große Aufgabe");
                vorhaben.setBeschreibung("Diese Aufgabe ist groß." + '\n' + "Sie muss dennoch erledigt werden.");
                vorhaben.setRestStunden(7);
                vorhaben.setIstStunden(0);
                vorhaben.setStatus(DmAufgabeStatus.neu);
                vorhaben.setEndTermin(Date.valueOf("2016-08-01"));
                new UiVorhaben(vorhaben, list);
            }
        });
    }

}
