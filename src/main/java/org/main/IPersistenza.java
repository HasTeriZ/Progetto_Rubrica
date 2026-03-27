package org.main;

import java.util.Vector;


public interface IPersistenza {

    void salvaPersone(Vector<Persona> persone);

    Vector<Persona> caricaPersone();
}
