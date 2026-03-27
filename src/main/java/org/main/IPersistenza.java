package org.main;

import java.util.Vector;

/**
 * Interfaccia di persistenza: definisce il contratto per il salvataggio
 * e il caricamento dei dati. Può essere implementata sia con file che con DB.
 */
public interface IPersistenza {
    /**
     * Salva l'intera lista di persone nel sistema di persistenza.
     *
     * @param persone la lista completa da salvare
     */
    void salvaPersone(Vector<Persona> persone);

    /**
     * Carica tutte le persone dal sistema di persistenza.
     *
     * @return un Vector con tutte le persone caricate
     */
    Vector<Persona> caricaPersone();
}
