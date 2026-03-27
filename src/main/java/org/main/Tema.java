package org.main;

import java.awt.Color;

/**
 * Palette centralizzata dell'applicazione.
 */
public final class Tema {

    private Tema() {
    }

    // Sfondi
    public static final Color BG = new Color(0x1A1F2E); // sfondo principale
    public static final Color PANEL = new Color(0x252B3B); // header, footer, card
    public static final Color FIELD_BG = new Color(0x141824); // sfondo campi input

    // Testo
    public static final Color TEXT = new Color(0xE8EAF0); // testo principale
    public static final Color TEXT_DIM = new Color(0x8892A4); // testo secondario/label

    // Bordi
    public static final Color BORDER = new Color(0x2E3447); // bordi pannelli
    public static final Color FIELD_BORD = new Color(0x3A4256); // bordi campi input

    // Accento
    public static final Color ACCENT = new Color(0x4D7CFE); // bottone principale, focus
    public static final Color ACCENT_HOV = new Color(0x6B93FF); // hover accento

    // Pericolo (rosso)
    public static final Color DANGER = new Color(0xFE4D6B); // bottone elimina
    public static final Color DANGER_HOV = new Color(0xFF6B84); // hover pericolo

    // Righe tabella
    public static final Color ROW_ALT = new Color(0x1E2438); // riga alternata
    public static final Color ROW_SEL = new Color(0x2D3D6B); // riga selezionata

    // Hover pannello (ghost button)
    public static final Color PANEL_HOV = new Color(0x2E3447); // hover bottone ghost

    // Warning (giallo)
    public static final Color WARNING = new Color(0xF5A623); // avviso
}

