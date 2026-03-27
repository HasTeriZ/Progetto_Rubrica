package org.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class EditorPersona extends JDialog {

    private JTextField campoNome, campoCognome, campoIndirizzo, campoTelefono, campoEta;
    private boolean salvato = false;
    private Persona personaRisultato = null;

    public EditorPersona(JFrame parent, Persona p) {
        super(parent,p == null ? "Nuovo contatto" : "Modifica contatto", true);
        setSize(480, 380);
        setLocationRelativeTo(parent);
        setResizable(false);
        costruisciUI(p);
    }

    private void costruisciUI(Persona p) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Tema.BG);

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Tema.PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        JLabel titolo = new JLabel(p == null ? "➕  Nuovo contatto" : "✎  Modifica contatto");
        titolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titolo.setForeground(Tema.TEXT);
        header.add(titolo, BorderLayout.WEST);

        // ── Form con GridBagLayout ──
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Tema.BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 16, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Riga 0 — label NOME | label COGNOME
        gbc.gridy = 0; gbc.gridx = 0; gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, 4, 8);
        form.add(label("NOME"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 4, 0);
        form.add(label("COGNOME"), gbc);

        // Riga 1 — campo NOME | campo COGNOME
        campoNome = campo(p != null ? p.getNome()    : "");
        campoCognome = campo(p != null ? p.getCognome() : "");
        gbc.gridy = 1; gbc.gridx = 0; gbc.insets = new Insets(0, 0, 14, 8);
        form.add(campoNome, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 14, 0);
        form.add(campoCognome, gbc);

        // Riga 2 — label INDIRIZZO
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 4, 0);
        form.add(label("INDIRIZZO"), gbc);

        // Riga 3 — campo INDIRIZZO
        campoIndirizzo = campo(p != null ? p.getIndirizzo() : "");
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 14, 0);
        form.add(campoIndirizzo, gbc);

        // Riga 4 — label TELEFONO | label ETÀ
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, 4, 8);
        form.add(label("TELEFONO"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 4, 0);
        form.add(label("ETÀ"), gbc);

        // Riga 5 — campo TELEFONO | campo ETÀ
        campoTelefono = campo(p != null ? p.getTelefono()            : "");
        campoEta = campo(p != null ? String.valueOf(p.getEta()) : "");
        gbc.gridy = 5; gbc.gridx = 0; gbc.insets = new Insets(0, 0, 0, 8);
        form.add(campoTelefono, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(campoEta, gbc);

        // ── Footer ──
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(Tema.PANEL);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER));

        JButton btnAnnulla = creaBtnGhost("Annulla");
        JButton btnSalva = creaBtnAccent("Salva");
        btnAnnulla.addActionListener(e -> dispose());
        btnSalva.addActionListener(e -> salva());
        footer.add(btnAnnulla);
        footer.add(btnSalva);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    // ── Helpers UI ───────────────────────────────────────────

    private JLabel label(String testo) {
        JLabel l = new JLabel(testo);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(Tema.TEXT_DIM);
        return l;
    }

    private JTextField campo(String valore) {
        JTextField f = new JTextField(valore);
        f.setBackground(Tema.FIELD_BG);
        f.setForeground(Tema.TEXT);
        f.setCaretColor(Tema.ACCENT);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(new FinestraLogin.RoundedBorder(7, Tema.FIELD_BORD));
        f.setPreferredSize(new Dimension(0, 36));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { f.setBorder(new FinestraLogin.RoundedBorder(7, Tema.ACCENT)); }
            @Override public void focusLost(FocusEvent e)   { f.setBorder(new FinestraLogin.RoundedBorder(7, Tema.FIELD_BORD)); }
        });
        return f;
    }

    private JButton creaBtnAccent(String testo) {
        JButton btn = new JButton(testo) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Tema.ACCENT_HOV : Tema.ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 34));
        return btn;
    }

    private JButton creaBtnGhost(String testo) {
        JButton btn = new JButton(testo) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Tema.PANEL_HOV : Tema.PANEL);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(Tema.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Tema.TEXT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 34));
        return btn;
    }

    // ── Salvataggio ──────────────────────────────────────────

    private void salva() {
        String nome = campoNome.getText().trim();
        String cognome = campoCognome.getText().trim();
        String indirizzo = campoIndirizzo.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String etaStr = campoEta.getText().trim();

        if (nome.isEmpty() || cognome.isEmpty()) {
            Popup.errore(this, "Nome e Cognome sono obbligatori.", "Errore di validazione");
            return;
        }

        if (telefono.isEmpty()) {
            Popup.errore(this, "Il telefono è obbligatorio.", "Errore di validazione");
            return;
        }
        if (!telefono.matches("[+]?[0-9 ]+")) {
            Popup.errore(this, "Il telefono può contenere solo cifre, spazi e il prefisso +.", "Errore di validazione");
            return;
        }

        int eta;

        if (etaStr.isEmpty()) {
            Popup.errore(this, "Il campo età è obbligatorio.", "Errore di validazione");
            return;
        }

        try {
            eta = Integer.parseInt(etaStr);
            if (eta < 1 || eta > 150) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            Popup.errore(this, "Inserire un'età valida (1–150).", "Errore di validazione");
            return;
        }
        personaRisultato = new Persona(nome, cognome, indirizzo, telefono, eta);
        salvato = true;
        dispose();
    }

    public boolean isSalvato() { return salvato; }
    public Persona getPersonaRisultato() { return personaRisultato; }
}