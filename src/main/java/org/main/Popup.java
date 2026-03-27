package org.main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sostituisce JOptionPane con dialoghi stilizzati nel tema scuro.
 */
public class Popup {


    public static final int YES = 0;
    public static final int NO = 1;

    // ── API pubblica ─────────────────────────────────────────

    /**
     * Messaggio informativo (OK).
     */
    public static void info(Component parent, String messaggio, String titolo) {
        mostra(parent, messaggio, titolo, "ℹ", Tema.ACCENT, false);
    }

    /**
     * Messaggio di avvertimento (OK).
     */
    public static void avviso(Component parent, String messaggio, String titolo) {
        mostra(parent, messaggio, titolo, "⚠", Tema.WARNING, false);
    }

    /**
     * Messaggio di errore (OK).
     */
    public static void errore(Component parent, String messaggio, String titolo) {
        mostra(parent, messaggio, titolo, "✕", Tema.DANGER, false);
    }

    /**
     * Dialogo di conferma con Sì / No.
     *
     * @return {@code Popup.YES} o {@code Popup.NO}
     */
    public static int conferma(Component parent, String messaggio, String titolo) {
        return mostraConferma(parent, messaggio, titolo);
    }


    private static void mostra(Component parent, String messaggio, String titolo,
                               String emoji, Color accentColor, boolean isConfirm) {
        JDialog dlg = creaDialog(parent, titolo);

        JPanel root = pannelloRoot();
        root.setLayout(new BorderLayout());

        // Header
        root.add(creaHeader(titolo, emoji, accentColor), BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Tema.BG);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel msg = new JLabel("<html><body style='width:260px'>" + messaggio + "</body></html>");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setForeground(Tema.TEXT);
        body.add(msg, BorderLayout.CENTER);
        root.add(body, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(Tema.PANEL);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER));
        JButton btnOk = creaBtnAccent("OK", accentColor, accentColor.brighter());
        btnOk.addActionListener(e -> dlg.dispose());
        footer.add(btnOk);
        root.add(footer, BorderLayout.SOUTH);

        dlg.setContentPane(root);
        dlg.pack();
        dlg.setMinimumSize(new Dimension(340, 160));
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true);
    }

    private static int mostraConferma(Component parent, String messaggio, String titolo) {
        AtomicInteger risultato = new AtomicInteger(NO);
        JDialog dlg = creaDialog(parent, titolo);

        JPanel root = pannelloRoot();
        root.setLayout(new BorderLayout());

        // Header
        root.add(creaHeader(titolo, "⚠", Tema.WARNING), BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Tema.BG);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel msg = new JLabel("<html><body style='width:260px'>" + messaggio + "</body></html>");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setForeground(Tema.TEXT);
        body.add(msg, BorderLayout.CENTER);
        root.add(body, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(Tema.PANEL);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER));

        JButton btnNo = creaBtnGhost("No");
        JButton btnSi = creaBtnDanger("Sì");

        btnNo.addActionListener(e -> {
            risultato.set(NO);
            dlg.dispose();
        });
        btnSi.addActionListener(e -> {
            risultato.set(YES);
            dlg.dispose();
        });

        footer.add(btnNo);
        footer.add(btnSi);
        root.add(footer, BorderLayout.SOUTH);

        dlg.setContentPane(root);
        dlg.pack();
        dlg.setMinimumSize(new Dimension(340, 160));
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true); // bloccante perché il dialog è modal

        return risultato.get();
    }

    // ── Helpers costruzione UI ───────────────────────────────

    private static JDialog creaDialog(Component parent, String titolo) {
        Window window = parent == null ? null
                : (parent instanceof Window ? (Window) parent
                   : SwingUtilities.getWindowAncestor(parent));
        JDialog dlg;
        if (window instanceof Frame) {
            dlg = new JDialog((Frame) window, titolo, true);
        } else if (window instanceof Dialog) {
            dlg = new JDialog((Dialog) window, titolo, true);
        } else {
            dlg = new JDialog((Frame) null, titolo, true);
        }
        dlg.setResizable(false);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return dlg;
    }

    private static JPanel pannelloRoot() {
        JPanel p = new JPanel();
        p.setBackground(Tema.BG);
        return p;
    }

    private static JPanel creaHeader(String titolo, String emoji, Color accentColor) {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
        h.setBackground(Tema.PANEL);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER));

        // Cerchio colorato con emoji
        JLabel badge = new JLabel(emoji, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(),
                        accentColor.getBlue(), 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("SansSerif", Font.PLAIN, 16));
        badge.setForeground(accentColor);
        badge.setPreferredSize(new Dimension(34, 34));

        JLabel lbl = new JLabel(titolo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(Tema.TEXT);

        h.add(badge);
        h.add(lbl);
        return h;
    }

    // ── Button factory ───────────────────────────────────────

    private static JButton creaBtnAccent(String testo, Color bg, Color hover) {
        JButton btn = new JButton(testo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : bg);
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
        btn.setPreferredSize(new Dimension(80, 32));
        return btn;
    }

    private static JButton creaBtnGhost(String testo) {
        JButton btn = new JButton(testo) {
            @Override
            protected void paintComponent(Graphics g) {
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
        btn.setPreferredSize(new Dimension(80, 32));
        return btn;
    }

    private static JButton creaBtnDanger(String testo) {
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
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 32));
        return btn;
    }
}