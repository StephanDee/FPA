package l1_ui;

import l4_dm.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Stephan D on 01.05.2016.
 */
public class UiAufgabenTabelle extends JDialog {

    private final Container container = this.getContentPane();
    private final JPanel btnpnl = new JPanel(new FlowLayout());
    private final JTable table = new JTable();

    public UiAufgabenTabelle(final String title, final List<DmAufgabe> aufgabenliste) {
        this.setTitle(title);

        btnpnl.add(new JButton("Schritt erfassen"));
        btnpnl.add(new JButton("Vorhaben erfassen"));
        btnpnl.add(new JButton("Ändern"));
        btnpnl.add(new JButton("Oberste Aufgaben"));

        final JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(btnpnl, BorderLayout.PAGE_END);

        final TableModel model = new TableModel(aufgabenliste);
        table.setModel(model);

        this.pack();
        this.setSize(525, 200);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final DmSchritt schritt1 = new DmSchritt() {
                    @Override
                    public Long getId() {
                        return 1L;
                    }
                    public String getTitel() {
                        return "Schritt 1: Vorbereiten";
                    }
                    public int getAnzahlTeile() {
                        return 0;
                    }
                    public DmAufgabeStatus getStatus() {
                        return DmAufgabeStatus.erledigt;
                    }
                };

                final DmVorhaben vorhaben1 = new DmVorhaben() {
                    @Override
                    public Long getId() {
                        return 2L;
                    }
                    public String getTitel() {
                        return "Vorhaben: Bearbeiten";
                    }
                    public int getAnzahlTeile() {
                        return 2;
                    }
                    public DmAufgabeStatus getStatus() {
                        return DmAufgabeStatus.inBearbeitung;
                    }
                };

                final DmSchritt schritt2 = new DmSchritt() {
                    @Override
                    public Long getId() {
                        return 5L;
                    }
                    public String getTitel() {
                        return "Schritt 3: Beenden";
                    }
                    public int getAnzahlTeile() {
                        return 0;
                    }
                    public DmAufgabeStatus getStatus() {
                        return DmAufgabeStatus.inBearbeitung;
                    }
                };
                final List<DmAufgabe> aufgabenliste = Arrays.asList(schritt1, vorhaben1, schritt2);
                new UiAufgabenTabelle("Meine Aufgaben", aufgabenliste);
            }
        });
    }

    public static class TableModel extends AbstractTableModel {

        protected final Object[][] data;

        public TableModel(List<DmAufgabe> aufgabenliste) {

            final int anzahlSpalten = 4;
            data = new Object[aufgabenliste.size()][anzahlSpalten];

            for (int i = 0; i < aufgabenliste.size(); i++) {
                final Object[] temp = new Object[anzahlSpalten];
                temp[0] = aufgabenliste.get(i).getId();
                temp[1] = aufgabenliste.get(i).getTitel();
                temp[2] = aufgabenliste.get(i).getAnzahlTeile();
                temp[3] = aufgabenliste.get(i).getStatus();
                data[i] = temp;
            }
        }

        private final String[] columnNames = {"ID", "Titel", "#Teile", "Status"};


        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

    }

}