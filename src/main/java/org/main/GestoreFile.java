package org.main;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class GestoreFile implements IPersistenza {

    private static final String CARTELLA = "informazioni";

    public GestoreFile() {
        // Crea la cartella se non esiste
        File dir = new File(CARTELLA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void salvaPersone(Vector<Persona> persone) {
        File dir = new File(CARTELLA);

        // Elimina tutti i file .txt presenti nella cartella
        File[] vecchi = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (vecchi != null) {
            for (File f : vecchi) {
                f.delete();
            }
        }

        // Salva ogni persona in un file dedicato, gestendo i duplicati
        Set<String> nomiUsati = new HashSet<>();

        for (Persona p : persone) {
            String nomeFile = generaNomeFile(p, nomiUsati);
            nomiUsati.add(nomeFile);

            File file = new File(CARTELLA + File.separator + nomeFile);
            try (PrintStream ps = new PrintStream(file, "UTF-8")) {
                ps.println(p.toString());
            } catch (Exception e) {
                System.err.println("Errore salvataggio " + nomeFile + ": " + e.getMessage());
            }
        }
    }

    @Override
    public Vector<Persona> caricaPersone() {
        Vector<Persona> persone = new Vector<>();
        File dir = new File(CARTELLA);

        if (!dir.exists() || !dir.isDirectory()) {
            return persone;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null) return persone;

        // Ordina i file per nome per avere un ordine stabile
        java.util.Arrays.sort(files);

        for (File file : files) {
            try (Scanner scanner = new Scanner(file, "UTF-8")) {
                if (scanner.hasNextLine()) {
                    String riga = scanner.nextLine().trim();
                    Persona p = Persona.daStringa(riga);
                    if (p != null) {
                        persone.add(p);
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore lettura " + file.getName() + ": " + e.getMessage());
            }
        }

        return persone;
    }

    /**
     * Genera un nome file univoco della forma "NOME-COGNOME.txt".
     * Se già usato, aggiunge _2, _3, ... fino a trovare uno disponibile.
     */
    private String generaNomeFile(Persona p, Set<String> nomiUsati) {
        // Pulizia dei caratteri non validi per i nomi file
        String nome = p.getNome().replaceAll("[^a-zA-Z0-9àèéìòùÀÈÉÌÒÙ]", "_");
        String cognome = p.getCognome().replaceAll("[^a-zA-Z0-9àèéìòùÀÈÉÌÒÙ]", "_");
        String base = nome + "-" + cognome;

        String candidato = base + ".txt";
        int contatore = 2;
        while (nomiUsati.contains(candidato)) {
            candidato = base + "_" + contatore + ".txt";
            contatore++;
        }
        return candidato;
    }
}
