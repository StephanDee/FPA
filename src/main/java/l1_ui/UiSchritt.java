package l1_ui;

/**
 * Created by Stephan D on 20.04.2016.
 */

import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;
import multex.Exc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

// Path: View/Tool Windows/project or "alt" + "1"
// Auskommentieren: "strg" + "÷"
// Einrücken: "strg" + "alt" + "L"

public class UiSchritt extends UiAufgabe {

    private final JLabel done = new JLabel("Erledigt Zeitpunkt:");
    private final JTextField donefield = new JTextField("");

    private UiSchritt(final DmSchritt schritt, final List<DmVorhaben> list) {
        super(schritt, list);

        this.setTitle("Schritt ändern");

        idfield.setText(String.valueOf(schritt.getId()));
        titlefield.setText(schritt.getTitel());
        descrarea.setText(schritt.getBeschreibung());
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
        btnpnl.add(new JButton(this.todoAction));

    }

            final Action todoAction = new ExceptionReportingSwingAction("Erledigen"){
            @Override
            public void actionPerformedWithThrows(final ActionEvent evt) throws Exc {
                System.out.println("Erledigen des Schritts " + titlefield.getText());
                throw new multex.Exc("Die Aufgabe {0} ist zu schwer.", titlefield.getText());
            }
        };

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final DmSchritt schritt = new DmSchritt(){
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
                    public String getTitel() {
                        return "Persistenzaufgabe lösen";
                    }
                });
                schritt.setTitel("Meine große Aufgabe");
                schritt.setBeschreibung("Diese Aufgabe ist groß." + '\n' + "Sie muss dennoch erledigt werden.");
                schritt.setRestStunden(7);
                schritt.setIstStunden(0);
                schritt.setErledigtZeitpunkt(Date.valueOf("2014-08-01"));
                new UiSchritt(schritt, list);
            }
        });
    }

}
