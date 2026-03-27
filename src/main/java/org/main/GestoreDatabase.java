package org.main;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

public class GestoreDatabase implements IPersistenza {

    private static final String FILE_PROPRIETA = "credenziali_database.properties";

    private String url;
    private String utente;
    private String password;
    private boolean configurato = false;

    public GestoreDatabase() {
        caricaCredenziali();
    }

    // ── Caricamento credenziali ──────────────────────────────

    private void caricaCredenziali() {
        // Prima prova come file esterno, poi nel classpath (Gradle)
        InputStream in = null;
        try {
            File fileEsterno = new File(FILE_PROPRIETA);
            if (fileEsterno.exists()) {
                in = new FileInputStream(fileEsterno);
            } else {
                in = getClass().getClassLoader().getResourceAsStream(FILE_PROPRIETA);
            }

            if (in == null) {
                mostraErroreDB(
                        "File credenziali non trovato:\n" + FILE_PROPRIETA +
                                "\n\nVerifica che il file esista in src/main/resources/",
                        "Configurazione mancante");
                return;
            }

            Properties props = new Properties();
            props.load(in);

            String host = props.getProperty("db.host", "localhost");
            String porta = props.getProperty("db.porta", "3306");
            String database = props.getProperty("db.nome", "rubrica");
            utente = props.getProperty("db.utente", "root");
            password = props.getProperty("db.password", "");

            url = "jdbc:mysql://" + host + ":" + porta + "/" + database
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Test connessione immediato per dare feedback visivo all'avvio
            try (Connection test = DriverManager.getConnection(url, utente, password)) {
                configurato = true;
                System.out.println("[DB] Connesso a " + url);
            }

        } catch (ClassNotFoundException e) {
            mostraErroreDB(
                    "Driver MySQL non trovato nel classpath.\n\n" +
                            "Aggiungi mysql-connector-j al build.gradle.kts:\n" +
                            "implementation(\"com.mysql:mysql-connector-j:9.6.0\")",
                    "Driver mancante");

        } catch (SQLException e) {
            mostraErroreDB(
                    "Connessione al database fallita.\n\n" +
                            "Errore: " + e.getMessage() + "\n\n" +
                            "Verifica:\n" +
                            "• MySQL è in esecuzione\n" +
                            "• Le credenziali in " + FILE_PROPRIETA + " sono corrette\n" +
                            "• Il database 'rubrica' esiste (esegui schema_database.sql)",
                    "Errore di connessione");

        } catch (Exception e) {
            mostraErroreDB(
                    "Errore durante il caricamento delle credenziali:\n" + e.getMessage(),
                    "Errore configurazione");
        } finally {
            if (in != null) try {
                in.close();
            } catch (Exception ignored) {
            }
        }
    }

    // ── Connessione ──────────────────────────────────────────

    private Connection getConnessione() throws SQLException {
        if (!configurato) {
            throw new SQLException(
                    "Database non configurato. Verificare " + FILE_PROPRIETA);
        }
        return DriverManager.getConnection(url, utente, password);
    }

    // ── IPersistenza ─────────────────────────────────────────

    @Override
    public void salvaPersone(Vector<Persona> persone) {
        String sqlDelete = "DELETE FROM persone";
        String sqlInsert = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnessione();
             Statement stmtDel = conn.createStatement();
             PreparedStatement stmtIns = conn.prepareStatement(sqlInsert)) {

            stmtDel.executeUpdate(sqlDelete);
            for (Persona p : persone) {
                stmtIns.setString(1, p.getNome());
                stmtIns.setString(2, p.getCognome());
                stmtIns.setString(3, p.getIndirizzo());
                stmtIns.setString(4, p.getTelefono());
                stmtIns.setInt(5, p.getEta());
                stmtIns.addBatch();
            }
            stmtIns.executeBatch();

        } catch (SQLException e) {
            mostraErroreDB(
                    "Salvataggio fallito.\n\nErrore: " + e.getMessage(),
                    "Errore salvataggio");
        }
    }

    @Override
    public Vector<Persona> caricaPersone() {
        Vector<Persona> persone = new Vector<>();
        String sql = "SELECT nome, cognome, indirizzo, telefono, eta " +
                "FROM persone ORDER BY cognome, nome";

        try (Connection conn = getConnessione();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                persone.add(new Persona(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        rs.getInt("eta")
                ));
            }
        } catch (SQLException e) {
            mostraErroreDB(
                    "Caricamento contatti fallito.\n\nErrore: " + e.getMessage(),
                    "Errore lettura");
        }
        return persone;
    }

    // ── Utenti ───────────────────────────────────────────────

    public boolean verificaUtente(String username, String passwordInput) {
        String sql = "SELECT password FROM utenti WHERE username = ?";
        try (Connection conn = getConnessione();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("password").equals(passwordInput);
            }
        } catch (SQLException e) {
            mostraErroreDB(
                    "Verifica utente fallita.\n\nErrore: " + e.getMessage(),
                    "Errore autenticazione");
        }
        return false;
    }

    public boolean isConfigurato() {
        return configurato;
    }

    // ── Helper popup ─────────────────────────────────────────

    /**
     * Mostra un Popup.errore thread-safe: se siamo sull'EDT lo chiama
     * direttamente, altrimenti usa invokeAndWait per bloccare il thread
     * chiamante finché l'utente non chiude il dialogo.
     */
    private void mostraErroreDB(String messaggio, String titolo) {
        System.err.println("[DB] " + titolo + ": " + messaggio);
        Runnable mostra = () -> Popup.errore(null, messaggio, titolo);
        if (SwingUtilities.isEventDispatchThread()) {
            mostra.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(mostra);
            } catch (Exception ignored) {
            }
        }
    }
}