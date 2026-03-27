package org.main;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.RowSorter;

public class FinestraPrincipale extends JFrame {

    private final GestoreRubrica gestore;
    private JTable tabella;
    private DefaultTableModel modelloTabella;
    private JLabel labelContatore;

    public FinestraPrincipale(GestoreRubrica gestore) {
        this.gestore = gestore;
        setTitle("Rubrica Telefonica");
        setSize(720, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        costruisciUI();
        aggiornaTabella();
        setVisible(true);
    }

    private void costruisciUI() {
        // Root panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Tema.BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Tema.PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        JLabel titolo = new JLabel("Rubrica Telefonica");
        titolo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titolo.setForeground(Tema.TEXT);

        labelContatore = new JLabel("0 contatti");
        labelContatore.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelContatore.setForeground(Tema.TEXT_DIM);

        header.add(titolo, BorderLayout.WEST);
        header.add(labelContatore, BorderLayout.EAST);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Tema.BG);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER));

        JButton btnNuovo = creaBtnAccent("＋  Nuovo", false);
        JButton btnModifica = creaBtnGhost("✎  Modifica");
        JButton btnElimina = creaBtnDanger("✕  Elimina");

        toolbar.add(btnNuovo);
        toolbar.add(btnModifica);
        toolbar.add(btnElimina);

        // Tabella
        String[] colonne = {"Nome", "Cognome", "Telefono"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabella = new JTable(modelloTabella) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(Tema.ROW_SEL);
                    c.setForeground(Tema.TEXT);
                } else {
                    c.setBackground(row % 2 == 0 ? Tema.BG : Tema.ROW_ALT);
                    c.setForeground(Tema.TEXT);
                }
                if (c instanceof JComponent) ((JComponent) c).setBorder(
                        BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelloTabella);
        tabella.setRowSorter(sorter);

        // Ordina per la colonna 1 (Cognome) in ordine crescente
        sorter.setSortKeys(java.util.List.of(
                new RowSorter.SortKey(1, SortOrder.ASCENDING)
        ));

        // Header tabella
        JTableHeader th = tabella.getTableHeader();
        th.setBackground(Tema.PANEL);
        th.setForeground(Tema.TEXT_DIM);
        th.setFont(new Font("SansSerif", Font.BOLD, 11));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER));
        th.setPreferredSize(new Dimension(th.getPreferredSize().width, 36));
        ((DefaultTableCellRenderer) th.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        tabella.setBackground(Tema.BG);
        tabella.setForeground(Tema.TEXT);
        tabella.setSelectionBackground(Tema.ROW_SEL);
        tabella.setSelectionForeground(Tema.TEXT);
        tabella.setGridColor(Tema.BORDER);
        tabella.setShowHorizontalLines(true);
        tabella.setShowVerticalLines(false);
        tabella.setRowHeight(36);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabella.getTableHeader().setReorderingAllowed(false);
        tabella.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabella.setIntercellSpacing(new Dimension(0, 0));
        tabella.setFocusable(false);

        // Larghezze colonne
        tabella.getColumnModel().getColumn(0).setPreferredWidth(180);
        tabella.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabella.getColumnModel().getColumn(2).setPreferredWidth(140);

        tabella.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) apriModifica();
            }
        });

        JScrollPane scroll = new JScrollPane(tabella);
        scroll.setBackground(Tema.BG);
        scroll.getViewport().setBackground(Tema.BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Tema.PANEL);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        JLabel hint = new JLabel("Doppio click su una riga per modificare");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(Tema.TEXT_DIM);
        statusBar.add(hint, BorderLayout.WEST);

        // Azioni
        btnNuovo.addActionListener(e -> apriNuova());
        btnModifica.addActionListener(e -> apriModifica());
        btnElimina.addActionListener(e -> eliminaPersona());

        // Assembla
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Tema.BG);
        center.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        center.add(scroll, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(Tema.BG);
        mainArea.add(toolbar, BorderLayout.NORTH);
        mainArea.add(center, BorderLayout.CENTER);
        mainArea.add(statusBar, BorderLayout.SOUTH);

        root.add(mainArea, BorderLayout.CENTER);
        setContentPane(root);
    }

    // Button factories

    private JButton creaBtnAccent(String testo, boolean small) {
        JButton btn = new JButton(testo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Tema.ACCENT_HOV : Tema.ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        stilizzaBtn(btn, Color.WHITE);
        return btn;
    }

    private JButton creaBtnGhost(String testo) {
        JButton btn = new JButton(testo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? Tema.PANEL_HOV : Tema.PANEL;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(Tema.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        stilizzaBtn(btn, Tema.TEXT);
        return btn;
    }

    private JButton creaBtnDanger(String testo) {
        JButton btn = new JButton(testo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Tema.DANGER_HOV : Tema.DANGER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        stilizzaBtn(btn, Color.WHITE);
        return btn;
    }

    private void stilizzaBtn(JButton btn, Color fg) {
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 24, 34));
    }

    // Logica

    private void aggiornaTabella() {
        modelloTabella.setRowCount(0);
        for (Persona p : gestore.getPersone())
            modelloTabella.addRow(new Object[]{p.getNome(), p.getCognome(), p.getTelefono()});
        int n = gestore.getPersone().size();
        labelContatore.setText(n + (n == 1 ? " contatto" : " contatti"));
    }

    private void apriNuova() {
        EditorPersona editor = new EditorPersona(this, null);
        editor.setVisible(true);
        if (editor.isSalvato()) {
            gestore.aggiungiPersona(editor.getPersonaRisultato());
            aggiornaTabella();
        }
    }

    private void apriModifica() {
        int rigaVista = tabella.getSelectedRow();
        if (rigaVista == -1) {
            Popup.avviso(this, "Seleziona prima una persona da modificare.", "Attenzione");
            return;
        }
        int rigaModello = tabella.convertRowIndexToModel(rigaVista);
        EditorPersona editor = new EditorPersona(this, gestore.getPersone().get(rigaModello));
        editor.setVisible(true);
        if (editor.isSalvato()) {
            gestore.modificaPersona(rigaModello, editor.getPersonaRisultato());
            aggiornaTabella();
        }
    }

    private void eliminaPersona() {
        int rigaVista = tabella.getSelectedRow();
        if (rigaVista == -1) {
            Popup.avviso(this, "Seleziona prima una persona da eliminare.", "Attenzione");
            return;
        }
        int rigaModello = tabella.convertRowIndexToModel(rigaVista);
        Persona p = gestore.getPersone().get(rigaModello);
        int ok = Popup.conferma(this,
                "Eliminare " + p.getNome() + " " + p.getCognome() + "?",
                "Conferma eliminazione");
        if (ok == Popup.YES) {
            gestore.eliminaPersona(rigaModello);
            aggiornaTabella();
        }
    }
}