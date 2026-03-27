package org.main;

import javax.swing.SwingUtilities;

/**
 * Punto di ingresso dell'applicazione.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            /**
             * Queste due righe servono per la persistenza in file
             */
//            IAutenticazione autenticazione = new GestoreUtenti();
//            IPersistenza persistenza = new GestoreFile();
            GestoreDatabase db = new GestoreDatabase();
            new FinestraLogin(db, db);
        });
    }
}
