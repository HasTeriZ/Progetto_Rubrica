package org.main;

import javax.swing.SwingUtilities;

/**
 * Punto di ingresso dell'applicazione.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            IAutenticazione autenticazione = new GestoreUtenti();
//            IPersistenza persistenza = new GestoreFile();
            GestoreDatabase db = new GestoreDatabase();
            new FinestraLogin(db, db);
        });
    }
}
