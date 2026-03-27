package org.main;

import java.util.Vector;

/**
 * Logica centrale della rubrica. Usa l'interfaccia IPersistenza
 * per essere indipendente dal tipo di storage (file o database).
 */
public class GestoreRubrica {

    private Vector<Persona> persone;
    private final IPersistenza persistenza;

    /**
     * Crea il gestore con il sistema di persistenza specificato.
     * @param persistenza implementazione concreta (GestoreFile o GestoreDatabase)
     */
    public GestoreRubrica(IPersistenza persistenza) {
        this.persistenza = persistenza;
        this.persone = persistenza.caricaPersone();
    }

    public Vector<Persona> getPersone() {
        return persone;
    }

    public void aggiungiPersona(Persona p) {
        persone.add(p);
        persistenza.salvaPersone(persone);
    }

    public void modificaPersona(int indice, Persona p) {
        persone.set(indice, p);
        persistenza.salvaPersone(persone);
    }

    public void eliminaPersona(int indice) {
        persone.remove(indice);
        persistenza.salvaPersone(persone);
    }
}
