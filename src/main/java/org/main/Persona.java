package org.main;

public class Persona {
    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;

    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.eta = eta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    @Override
    public String toString() {
        return nome + ";" + cognome + ";" + indirizzo + ";" + telefono + ";" + eta;
    }

    /**
     * Deserializza una persona dalla stringa "nome;cognome;indirizzo;telefono;eta".
     * Restituisce null se la stringa non è valida.
     */
    public static Persona daStringa(String riga) {
        if (riga == null || riga.trim().isEmpty()) return null;
        String[] parti = riga.trim().split(";", -1);
        if (parti.length != 5) return null;
        try {
            return new Persona(parti[0], parti[1], parti[2], parti[3],
                    Integer.parseInt(parti[4].trim()));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
