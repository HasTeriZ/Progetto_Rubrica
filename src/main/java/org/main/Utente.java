package org.main;

/**
 * Rappresenta un utente che può accedere alla rubrica.
 * Contiene username e password
 */
public class Utente {
    private String username;
    private String password;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Serializza nel formato: username;password
     */
    @Override
    public String toString() {
        return username + ";" + password;
    }

    /**
     * Deserializza da "username;password". Restituisce null se non valido.
     */
    public static Utente daStringa(String riga) {
        if (riga == null || riga.trim().isEmpty()) return null;
        String[] parti = riga.trim().split(";", 2);
        if (parti.length != 2) return null;
        return new Utente(parti[0], parti[1]);
    }
}
