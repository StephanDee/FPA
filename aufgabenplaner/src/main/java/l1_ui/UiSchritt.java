package l1_ui;

/**
 * Created by Stephan D on 20.04.2016.
 */

import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

// Path: View/Tool Windows/project or "alt" + "1"
// Auskommentieren: "strg" + "÷"
// Einrücken: "strg" + "alt" + "L"

public class UiSchritt extends UiAufgabe {

    private final JButton todobtn = new JButton("Erledigen");

    private final JLabel done = new JLabel("Erledigt Zeitpunkt:");
    private final JTextField donefield = new JTextField("");

    protected UiSchritt(final DmSchritt schritt, final List<DmVorhaben> list) {
        super(schritt, list);

        this.setTitle("Schritt erfassen/ändern");


        idfield.setText(String.valueOf(schritt.getId()));
        titlefield.setText(schritt.getTitel());
        descrarea.setText(schritt.getBeschreibung());
        String[] auswahl = schritt.getTeil();
        for (int i = 0; i < auswahl.length; ++i) {
            planbox.addItem(auswahl[i]);
        }

        resthfield.setText(String.valueOf(schritt.getRestStunden()));
        ishfield.setText(String.valueOf(schritt.getIstStunden()));
        statusfield.setText(String.valueOf(schritt.getStatus()));
        statusfield.setEditable(false);
        fieldpanel.add(done);
        done.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(donefield);
        donefield.setAlignmentX(Component.LEFT_ALIGNMENT);
        donefield.setText(String.valueOf(schritt.getErledigtZeitpunkt()));
        donefield.setEditable(false);
        btnpnl.add(todobtn);

        /**
         * add ActionListener
         */
//        final ActionListener al = new AL();
//        changebtn.addActionListener(al);

    }

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final DmSchritt schritt = new DmSchritt();
                final List<DmVorhaben> list = null;
                schritt.setTitel("Meine große Aufgabe");
                schritt.setBeschreibung("Diese Aufgabe ist groß." + '\n' + "Sie muss dennoch erledigt werden.");
                schritt.setTeil(new String[]{"11 Persistenzaufgabe lösen", "12 Exceptionaufgabe lösen", "13 Datenbankaufgabe lösen"});
                schritt.setRestStunden(7);
                schritt.setIstStunden(0);
                schritt.setErledigtZeitpunkt(Date.valueOf("2014-08-01"));
                new UiSchritt(schritt, list);
            }
        });
    }

}
