import java.awt.Image;

public class Steuerung
{
    private GUI dieGUI;

    private String aZustand;
    private int aIntervall;
    private int aMaxKlicks;
    private char aTyp;

    private Schiebepuzzle einPuzzle;
    private TimerPuzzle derTimer;

    public Steuerung(GUI pDieGui)
    {
        // Erstellung der Assoziation zur GUI
        dieGUI = pDieGui;

        // Startzustand
        aZustand = "init";

        // Intervall f�r den Timer in Millisekunden
        aIntervall = 2000;
    }

    // Ein neues Spiel wird gestartet
    public void neuesSpiel(char pTyp, int pGroesse)
    {
        // Zustandswechsel
        aZustand = "running";
        aTyp = pTyp;

        // Je nach Gr��e des Puzzles wird die Anzahl der Klicks ausgew�hlt
        switch (pGroesse)
        {
            case 2:
                aMaxKlicks = 10;
                break;
            case 3:
                aMaxKlicks = 200;
                break;
            case 4:
                aMaxKlicks = 400;
                break;
        }

        // Erstellung der Assoziation zum Timer; dabei startet auch der Timer
        derTimer = new TimerPuzzle(this, aIntervall);

        // Zahlenpuzzle
        if (pTyp == 'z')
        {
            // Neues Puzzle generieren
            einPuzzle = new Zahlenpuzzle(pGroesse);

            // Das gemischte Puzzle wird an die GUI weitergegeben
            dieGUI.setzeTastenZahlen(((Zahlenpuzzle) einPuzzle).gibZahlenFeld());

            // GUI aktualisieren
            dieGUI.melde(1);
            dieGUI.schreibeRestKlicks(aMaxKlicks);
        }
        else
            // Bilderpuzzle
            if (pTyp == 'b')
            {
                // L�sung des Puzzles wird von der GUI genommen
                Image[] loesung = dieGUI.getBildTeile();

                // Neues Puzzle generieren
                einPuzzle = new Bilderpuzzle(pGroesse, loesung);

                // Das gemischte Puzzle wird an die GUI weitergegeben
                dieGUI.setzeTastenBilder(((Bilderpuzzle) einPuzzle)
                        .gibBilderFeld());

                // GUI aktualisieren
                dieGUI.melde(1);
                dieGUI.schreibeRestKlicks(aMaxKlicks);
            }
    }

    // Spieler dr�ckt auf eine Taste
    public void tastenKlick(int pIdxTaste)
    {
        // Taste darf nur im Zustand "running" gedr�ckt werden
        if (aZustand == "running")
        {
            // Timer wird gestoppt, da der Spieler vor dem Intervall eine Taste
            // gedr�ckt hat
            derTimer.stop();

            // Zahlenpuzzle
            if (aTyp == 'z')
            {
                int idxLeerTaste;
                int anzahlKlicks;
                int[] dasZahlenFeld;
                boolean geloest;

                // Leertaste wird lokalisiert
                idxLeerTaste = ((Zahlenpuzzle) einPuzzle).gibLeerTasteIdx();

                // Gedr�ckte Taste mit der Leertaste tauschen
                ((Zahlenpuzzle) einPuzzle).tauscheTastenwerte(pIdxTaste,
                        idxLeerTaste);

                // Das neue Zahlenfeld holen und die GUI aktualisieren
                dasZahlenFeld = ((Zahlenpuzzle) einPuzzle).gibZahlenFeld();
                dieGUI.setzeTastenZahlen(dasZahlenFeld);

                // anzahlKlicks inkrementieren und die GUI aktualisieren
                anzahlKlicks = ((Zahlenpuzzle) einPuzzle).inkrAnzahlKlicks();
                dieGUI.schreibeRestKlicks(aMaxKlicks - anzahlKlicks);

                // schaue ob Puzzle gel�st wurde
                geloest = ((Zahlenpuzzle) einPuzzle).puzzleGeloest();
                if (geloest) // ja
                {
                    // Zustandswechsel und GUI aktualisieren
                    aZustand = "won";
                    dieGUI.melde(3);
                }
                else
                // nein
                {
                    // Wenn Puzzle nicht gel�st wurde und Spieler keine Klicks
                    // mehr hat -> verloren
                    if (anzahlKlicks == aMaxKlicks)
                    {
                        // Zustandswechsel und GUI aktualisieren
                        aZustand = "lost";
                        dieGUI.melde(2);
                    }
                    // Spieler hat noch Klicks
                    else
                    {
                        // Timer zur�cksetzen
                        derTimer.restart();
                    }
                }
            }
            else
                // Bilderpuzzle
                if (aTyp == 'b')
                {
                    int idxLeerTaste;
                    int anzahlKlicks;
                    Image[] dasBilderFeld;
                    boolean geloest;

                    // Leertaste wird lokalisiert
                    idxLeerTaste = ((Bilderpuzzle) einPuzzle).gibLeerTasteIdx();

                    // Gedr�ckte Taste mit der Leertaste tauschen
                    ((Bilderpuzzle) einPuzzle).tauscheTastenwerte(pIdxTaste,
                            idxLeerTaste);

                    // Das neue Bilderfeld holen und die GUI aktualisieren
                    dasBilderFeld = ((Bilderpuzzle) einPuzzle).gibBilderFeld();
                    dieGUI.setzeTastenBilder(dasBilderFeld);

                    // anzahlKlicks inkrementieren und die GUI aktualisieren
                    anzahlKlicks = ((Bilderpuzzle) einPuzzle)
                            .inkrAnzahlKlicks();
                    dieGUI.schreibeRestKlicks(aMaxKlicks - anzahlKlicks);

                    // schaue ob Puzzle gel�st wurde
                    geloest = ((Bilderpuzzle) einPuzzle).puzzleGeloest();
                    if (geloest) // ja
                    {
                        // Zustandswechsel und GUI aktualisieren
                        aZustand = "won";
                        dieGUI.melde(3);
                    }
                    else
                    // nein
                    {
                        // Hat der Spieler noch Klicks?
                        if (anzahlKlicks == aMaxKlicks)
                        {
                            // Zustandswechsel und GUI aktualisieren
                            aZustand = "lost";
                            dieGUI.melde(2);
                        }
                        else
                        {
                            // Timer zur�cksetzen
                            derTimer.restart();
                        }
                    }
                }
        }
    }

    // Beim Ablauf des Intervalls, bevor der Spieler eine Taste dr�ckt
    public void timerEreignis()
    {
        int anzahlKlicks = 0;

        // Zahlenpuzzle
        if (aTyp == 'z')
        {
            // anzahlKlicks inkrementieren und die GUI aktualisieren
            anzahlKlicks = ((Zahlenpuzzle) einPuzzle).inkrAnzahlKlicks();
            dieGUI.schreibeRestKlicks(aMaxKlicks - anzahlKlicks);
        }
        else
            // Bilderpuzzle
            if (aTyp == 'b')
            {
                // anzahlKlicks inkrementieren und die GUI aktualisieren
                anzahlKlicks = ((Bilderpuzzle) einPuzzle).inkrAnzahlKlicks();
                dieGUI.schreibeRestKlicks(aMaxKlicks - anzahlKlicks);
            }

        // Wenn Spieler keine Klicks mehr hat -> verloren
        if (anzahlKlicks == aMaxKlicks)
        {
            // Zustandswechsel und GUI aktualisieren
            aZustand = "lost";
            dieGUI.melde(2);
        }
        else
        {
            // Sonst wird der Timer zur�ckgesetzt
            derTimer.restart();
        }
    }

    // Getter und Setter
    public String getZustand()
    {
        return aZustand;
    }

    public void setZustand(String pZustand)
    {
        aZustand = pZustand;
    }
}
