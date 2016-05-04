package l1_ui;

import l4_dm.DmAufgabe;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Stephan D on 01.05.2016.
 */
public class UiAufgabenTabelle extends JDialog{

    private final Container container = this.getContentPane();
    private final JPanel tablepanel = new JPanel();
    private final JPanel btnpnl = new JPanel(new FlowLayout());

    private UiAufgabenTabelle(final DmAufgabe aufgabe, final List<DmVorhaben> list) {
        this.setTitle("Meine Aufgaben");

        // Create columns names
        String columnNames[] = { "ID", "Titel", "#Teile", "Status"};

        // Create some data
        String dataValues[][] =
                {
                        { "1", "Schritt1: Vorbereiten", "0", "erledigt" },
                        { "2", "Vorhaben: Bearbeiten", "2", "inBearbeitung" },
                        { "5", "Schritt 3: Beenden", "0", "inBearbeitung" }
                };

        btnpnl.add(new JButton("Schritt erfassen"));
        btnpnl.add(new JButton("Vorhaben erfassen"));
        btnpnl.add(new JButton("Ändern"));
        btnpnl.add(new JButton("Oberste Aufgaben"));

        final JTable table = new JTable(dataValues, columnNames);
        final JScrollPane scrollpane = new JScrollPane(table);

        container.add(scrollpane, BorderLayout.CENTER);
        container.add(btnpnl, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent i_) {
                System.exit(0);
            }
        });

        this.pack();
        this.setSize(525, 200);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final DmSchritt schritt = new DmSchritt(){
                    @Override
                    public Long getId(){return 99L;}
                };
                final List<DmVorhaben> list = null;
                new UiAufgabenTabelle(schritt, list);
            }
        });
    }

}
