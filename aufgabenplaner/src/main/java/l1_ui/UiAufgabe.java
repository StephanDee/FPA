package l1_ui;

/**
 * Created by Stephan D on 24.04.2016.
 */

import l4_dm.DmAufgabe;
import l4_dm.DmVorhaben;
import multex.Exc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.util.List;

public class UiAufgabe extends JDialog {

    protected final JLabel id = new JLabel("ID:");
    protected final JTextField idfield = new JTextField("");

    protected final JLabel title = new JLabel("Titel:");
    protected final JTextField titlefield = new JTextField("");

    protected final JLabel descr = new JLabel("Beschreibung:");
    protected final JTextArea descrarea = new JTextArea("");

    protected final JLabel plan = new JLabel("Teil von Vorhaben:");
    protected final JComboBox<DmVorhaben> planbox;

    protected final JLabel resth = new JLabel("Rest Stunden zu arbeiten:");
    protected final JTextField resthfield = new JTextField("");

    protected final JLabel ish = new JLabel("Ist Stunden gearbeitet:");
    protected final JTextField ishfield = new JTextField("");

    protected final JLabel status = new JLabel("Status:");
    protected final JTextField statusfield = new JTextField("");

    protected final Container container = this.getContentPane();
    protected final JPanel fieldpanel = new JPanel();
    protected final JPanel btnpnl = new JPanel(new FlowLayout());

    protected UiAufgabe(final DmAufgabe aufgabe, final List<DmVorhaben> list) {

        fieldpanel.setLayout(new BoxLayout(fieldpanel, BoxLayout.Y_AXIS));
        fieldpanel.add(id);
        id.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(idfield);
        idfield.setAlignmentX(Component.LEFT_ALIGNMENT);
        idfield.setEditable(false);
        fieldpanel.add(title);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(titlefield);
        titlefield.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(descr);
        descr.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(descrarea);
        descrarea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descrarea.setRows(5);
        fieldpanel.add(plan);
        plan.setAlignmentX(Component.LEFT_ALIGNMENT);
        planbox = new JComboBox(list.toArray(new DmVorhaben[list.size()]));
        fieldpanel.add(planbox);
        planbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        planbox.setEditable(false);
        fieldpanel.add(resth);
        resth.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(resthfield);
        resthfield.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(ish);
        ish.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(ishfield);
        ishfield.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(status);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldpanel.add(statusfield);
        statusfield.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusfield.setEditable(false);

        btnpnl.add(new JButton(this.saveAction));
        btnpnl.add(new JButton(this.deleteAction));

        container.add(fieldpanel, BorderLayout.CENTER);
        container.add(btnpnl, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent i_) {
                System.exit(0);
            }
        });

        this.pack();
        this.setSize(550, 450);
        this.setResizable(false);
        this.setVisible(true);
    }

    final Action saveAction = new ExceptionReportingSwingAction("Speichern"){
        @Override
        public void actionPerformedWithThrows(final ActionEvent evt) throws Exc {
            System.out.println("Erfassen Schritt " + titlefield.getText());
            throw new multex.Exc("Die Aufgabe {0} ist nicht erfasst.", titlefield.getText());
        }
    };

    final Action deleteAction = new ExceptionReportingSwingAction("Löschen"){
        @Override
        public void actionPerformedWithThrows(final ActionEvent evt) throws Exc {
            System.out.println("Löschen Schritt " + titlefield.getText());
            throw new multex.Exc("Die Aufgabe {0} ist nicht gelöscht.", titlefield.getText());
        }
    };

}
