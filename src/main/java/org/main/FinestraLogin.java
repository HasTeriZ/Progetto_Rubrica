package org.main;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class FinestraLogin extends JFrame {

    private final IAutenticazione autenticazione;
    private final IPersistenza persistenza;
    private JTextField campoUsername;
    private JPasswordField campoPassword;

    public FinestraLogin(IAutenticazione autenticazione, IPersistenza persistenza) {
        this.autenticazione = autenticazione;
        this.persistenza = persistenza;
        setTitle("Rubrica — Login");
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        costruisciUI();
        setVisible(true);
    }

    private void costruisciUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Cerchio decorativo in alto
                g2.setColor(Tema.ACCENT.brighter());
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.06f);
                g2.setComposite(ac);
                g2.fillOval(-60, -60, 260, 260);
                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // titolo
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel titolo = new JLabel("Rubrica Telefonica");
        titolo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titolo.setForeground(Tema.TEXT);
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sottotitolo = new JLabel("Accedi per continuare");
        sottotitolo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sottotitolo.setForeground(Tema.TEXT_DIM);
        sottotitolo.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(titolo);
        topPanel.add(Box.createVerticalStrut(4));
        topPanel.add(sottotitolo);

        // Card form
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.PANEL);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        card.add(creaLabelCampo("USERNAME"));
        card.add(Box.createVerticalStrut(6));
        campoUsername = creaTextField();
        card.add(campoUsername);
        card.add(Box.createVerticalStrut(16));
        card.add(creaLabelCampo("PASSWORD"));
        card.add(Box.createVerticalStrut(6));
        campoPassword = creaPasswordField();
        card.add(campoPassword);
        card.add(Box.createVerticalStrut(24));

        JButton btnLogin = creaBtnLogin();
        card.add(btnLogin);

        // Assembla
        root.add(topPanel, BorderLayout.NORTH);
        root.add(Box.createVerticalStrut(24), BorderLayout.CENTER);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        centerWrap.add(card, BorderLayout.CENTER);
        root.add(centerWrap, BorderLayout.CENTER);

        // Azioni
        ActionListener az = e -> tentaLogin();
        btnLogin.addActionListener(az);
        campoPassword.addActionListener(az);
        campoUsername.addActionListener(e -> campoPassword.requestFocus());

        setContentPane(root);
    }

    // Helpers UI

    private JLabel creaLabelCampo(String testo) {
        JLabel l = new JLabel(testo);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(Tema.TEXT_DIM);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField creaTextField() {
        JTextField f = new JTextField();
        stilizzaCampo(f);
        return f;
    }

    private JPasswordField creaPasswordField() {
        JPasswordField f = new JPasswordField();
        stilizzaCampo(f);
        return f;
    }

    private void stilizzaCampo(JTextField f) {
        f.setBackground(Tema.FIELD_BG);
        f.setForeground(Tema.TEXT);
        f.setCaretColor(Tema.ACCENT);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(new RoundedBorder(8, Tema.FIELD_BORD));
        f.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                f.setBorder(new RoundedBorder(8, Tema.ACCENT));
            }

            @Override
            public void focusLost(FocusEvent e) {
                f.setBorder(new RoundedBorder(8, Tema.FIELD_BORD));
            }
        });
    }

    private JButton creaBtnLogin() {
        JButton btn = new JButton("Accedi") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? Tema.ACCENT_HOV : Tema.ACCENT;
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    // Login logic

    private void tentaLogin() {
        String username = campoUsername.getText().trim();
        String password = new String(campoPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostraErrore("Inserire username e password.");
            return;
        }
        if (autenticazione.verificaLogin(username, password)) {
            dispose();
            SwingUtilities.invokeLater(() ->
                    new FinestraPrincipale(new GestoreRubrica(persistenza
                    )));
        } else {
            mostraErrore("Username o password errati.");
            campoPassword.setText("");
            campoPassword.requestFocus();
        }
    }

    private void mostraErrore(String msg) {
        Popup.errore(this, msg, "Errore");}

        // Border arrotondato

        static class RoundedBorder extends AbstractBorder {
            private final int radius;
            private final Color color;

            RoundedBorder(int radius, Color color) {
                this.radius = radius;
                this.color = color;
            }

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 2, h - 2, radius, radius));
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(6, 10, 6, 10);
            }
        }
    }