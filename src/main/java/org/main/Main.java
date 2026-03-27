package org.main;

import org.main.FinestraLogin;
import org.main.GestoreUtenti;

import javax.swing.SwingUtilities;

/**
 * Punto di ingresso dell'applicazione.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestoreUtenti gestoreUtenti = new GestoreUtenti();
            new FinestraLogin(gestoreUtenti);
        });
    }
}
