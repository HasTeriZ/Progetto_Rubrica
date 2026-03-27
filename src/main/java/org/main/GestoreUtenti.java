package org.main;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class GestoreUtenti implements IAutenticazione{

    private static final String FILE_UTENTI = "utenti.txt";
    private Vector<Utente> utenti;

    public GestoreUtenti() {
        utenti = new Vector<>();
        caricaUtenti();
    }

    private void caricaUtenti() {
        File file = new File(FILE_UTENTI);

        if (!file.exists()) {
            // Crea utente di default se non esiste ancora il file
            utenti.add(new Utente("admin", "admin"));
            salvaUtenti();
            return;
        }

        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextLine()) {
                Utente u = Utente.daStringa(scanner.nextLine());
                if (u != null) utenti.add(u);
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento utenti: " + e.getMessage());
        }
    }

    private void salvaUtenti() {
        try (PrintStream ps = new PrintStream(new File(FILE_UTENTI), "UTF-8")) {
            for (Utente u : utenti) {
                ps.println(u.toString());
            }
        } catch (Exception e) {
            System.err.println("Errore salvataggio utenti: " + e.getMessage());
        }
    }

    public boolean verificaLogin(String username, String password) {
        for (Utente u : utenti) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Aggiunge un nuovo utente, non implementato
     */
    public void aggiungiUtente(String username, String password) {
        utenti.add(new Utente(username, password));
        salvaUtenti();
    }

    public Vector<Utente> getUtenti() {
        return utenti;
    }
}
