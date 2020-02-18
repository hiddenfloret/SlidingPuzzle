import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

public class Bilderpuzzle extends Schiebepuzzle
{
    private Image[] loesung, aBilderFeld;
    private Random random;

    public Bilderpuzzle(int pGroesse, Image[] pLoesung)
    {
        // Ausf�hrung des Konstruktors von der Mutterklasse
        super(pGroesse);
        random = new Random();

        // L�sung wird gespeichert, da das Array noch nicht gemischt wurde
        loesung = pLoesung;
        erzeugeBilderFeld();
    }

    private void erzeugeBilderFeld()
    {
        int groesse;

        groesse = gibGroesse();

        // Bilderfeld erstellen und mischen
        aBilderFeld = mischeBilderFeld(groesse);
    }

    // Das Bilderfeld wird gemischt
    private Image[] mischeBilderFeld(int pGroesse)
    {
        // Liste wird erstellt
        aBilderFeld = new Image[pGroesse * pGroesse];

        // Bevor das Puzzle gemischt wird, wird die L�sung kopiert. Diese wird
        // sp�ter f�r die �berpr�fung ob das Puzzle gel�st wurde, verwendet.
        for (int j = 0; j < aBilderFeld.length; j++)
        {
            aBilderFeld[j] = loesung[j];
        }

        // Wie oft die Bilderteile miteinander getauscht werden
        int anzahlTauschAktionen = 1000;
        boolean geloest;

        // Puzzle wird 1000 gemischt
        mische(pGroesse, anzahlTauschAktionen);

        // Es kann sein (besonders beim 2x2 Puzzle), dass nach den 1000
        // Tauschaktionen das Puzzle nicht gemischt wurde, sondern zur�ck auf
        // den Startzustand kommt. Deshalb wird es �berpr�ft ob das Puzzle nach
        // dem Mischen "gel�st" wurde. Wenn ja dann wird das Puzzle solange
        // gemischt bis das Puzzle richtig gemischt wurde.
        geloest = puzzleGeloest();
        while (geloest)
        {
            mische(pGroesse, anzahlTauschAktionen);
            geloest = puzzleGeloest();
        }

        // Gemischtes Bilderfeld zur�ckgeben
        return aBilderFeld;
    }

    // Puzzle wird n mal gemischt
    private void mische(int pGroesse, int pAnzahlTauschAktionen)
    {
        int x, y;

        // Das Zahlenfeld mischen; Dabei werden die Zahlen von der
        // obenerstellten Liste zuf�llig miteinander getauscht, damit immer ein
        // l�sbares Puzzle generiert werden kann
        for (int i = 0; i < pAnzahlTauschAktionen; i++)
        {
            // Index der Leertaste holen
            int idxLeerTaste = gibLeerTasteIdx();

            // Index der Leertaste wird mithilfe von x und y in einem
            // zweidimensionalen Array dargestellt
            x = idxLeerTaste % pGroesse;
            y = idxLeerTaste / pGroesse;

            // Index eines zuf�lligen Nachbar holen und mit diesen den Platz
            // tauschen
            int idxNachbar = gibZufaelligerNachbar(x, y);
            tauscheTastenwerte(idxNachbar, idxLeerTaste);
        }
    }

    // Schaue ob Puzzle gel�st wurde
    @Override
    public boolean puzzleGeloest()
    {
        // Schleife durch die Liste
        for (int i = 0; i < aBilderFeld.length; i++)
        {
            // Vergleiche jedes Element des Bilderfeldes mit dem aus der L�sung
            if (aBilderFeld[i] != loesung[i])
            {
                // Nicht gel�st
                return false;
            }
        }
        // gel�st
        return true;
    }

    // Index der Leertaste zur�ckgeben
    public int gibLeerTasteIdx()
    {
        // Schleife durch die Liste
        for (int i = 0; i < aBilderFeld.length; i++)
        {
            // Leertaste wird als null dargestellt
            if (aBilderFeld[i] == null)
            {
                // Gebe sein Index zur�ck
                return i;
            }
        }
        return -1; // Error, da immer eine Leertaste geben muss
    }

    // Zuf�lliger und g�ltiger Nachbar zur�ckgeben
    public void tauscheTastenwerte(int pIdxTaste, int pIdxLeertaste)
    {
        int x1, y1, x2, y2, groesse;
        Image zwischenspeicher;
        boolean benachbart = false;

        groesse = gibGroesse();

        // F�r beide Tasten den Index aus dem eindimensionalen Array in x und y
        // Komponenten zerlegen. (siehe Abiturpr�fung Aufgabe 4.5.2)
        x1 = pIdxTaste % groesse;
        x2 = pIdxLeertaste % groesse;

        y1 = pIdxTaste / groesse;
        y2 = pIdxLeertaste / groesse;

        // �berpr�fung der Nachbarschaft (siehe Abiturpr�fung Aufgabe 4.5.2.2)
        if (x1 == x2)
        {
            if (Math.abs(y1 - y2) == 1)
            {
                benachbart = true;
            }
        }
        else
        if (y1 == y2)
        {
            if (Math.abs(x1 - x2) == 1)
            {
                benachbart = true;
            }
        }

        // Wenn die Tasten benachbart sind
        if (benachbart)
        {
            // Pl�tze tauschen
            zwischenspeicher = aBilderFeld[pIdxTaste];
            aBilderFeld[pIdxTaste] = aBilderFeld[pIdxLeertaste];
            aBilderFeld[pIdxLeertaste] = zwischenspeicher;
        }
    }

    // Zuf�lliger und g�ltiger Nachbar zur�ckgeben
    private int gibZufaelligerNachbar(int pX, int pY)
    {
        int xNachbar, yNachbar;
        int idxNachbar;
        int factor = gibGroesse();
        ArrayList<Integer> nachbaren = new ArrayList<Integer>();

        // Nachbar von links
        xNachbar = pX - 1;
        yNachbar = pY;

        // Formel um auf den Index des Nachbars zu kommen
        idxNachbar = factor * yNachbar + xNachbar;

        // Ist der Index des Nachbars g�ltig? Eine Taste kann maximal vier
        // g�ltige Nachbaren haben. Eine Taste die sich in einer Ecke befindet
        // hat z. B. nur zwei g�ltige Nachbaren. Deshalb muss es �berpr�ft
        // werden, ob der Nachbar g�ltig ist.
        if (idxNachbar >= 0 && idxNachbar < aBilderFeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Nachbar von rechts
        xNachbar = pX + 1;
        yNachbar = pY;
        idxNachbar = factor * yNachbar + xNachbar;

        if (idxNachbar >= 0 && idxNachbar < aBilderFeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Nachbar von oben
        xNachbar = pX;
        yNachbar = pY - 1;
        idxNachbar = factor * yNachbar + xNachbar;

        if (idxNachbar >= 0 && idxNachbar < aBilderFeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Nachbar von unten
        xNachbar = pX;
        yNachbar = pY + 1;
        idxNachbar = factor * yNachbar + xNachbar;

        if (idxNachbar >= 0 && idxNachbar < aBilderFeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Ein zuf�lliger Nachbar zur�ckgeben
        return nachbaren.get(random.nextInt(nachbaren.size()));
    }

    // Getter
    public Image[] gibBilderFeld()
    {
        return aBilderFeld;
    }
}
