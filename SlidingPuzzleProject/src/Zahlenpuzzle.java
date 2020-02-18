import java.util.ArrayList;
import java.util.Random;

public class Zahlenpuzzle extends Schiebepuzzle
{
    private int[] aZahlenfeld;
    private Random random;

    public Zahlenpuzzle(int pGroesse)
    {
        // Ausf�hrung des Konstruktors von der Mutterklasse
        super(pGroesse);
        random = new Random();
        erzeugePuzzleFeld();
    }

    // Schaue ob Puzzle gel�st wurde
    @Override
    public boolean puzzleGeloest()
    {
        // Schleife durch die Liste
        for (int i = 0; i < aZahlenfeld.length; i++)
        {
            // Damit das Puzzle als gel�st gilt, m�ssen sich die Zahlen in
            // aufsteigender Form befinden. Z. B. Gr��e = 4; [1,2,3,-1].
            // Dabei wird geschaut ob das nicht der Fall ist.
            if (aZahlenfeld[i] != i + 1 && aZahlenfeld[i] != -1)
            {
                // Nicht gel�st
                return false;
            }
        }
        // gel�st
        return true;
    }

    // Erzeugung des Zahlenfeldes
    private void erzeugePuzzleFeld()
    {
        int groesse;

        groesse = gibGroesse();

        // Zahlenfeld erstellen und mischen
        aZahlenfeld = zieheZufallszahlen(groesse);
    }

    // Index der Leertaste zur�ckgeben
    public int gibLeerTasteIdx()
    {
        // Schleife durch die Liste
        for (int i = 0; i < aZahlenfeld.length; i++)
        {
            // Leertaste wird als -1 dargestellt
            if (aZahlenfeld[i] == -1)
            {
                // Gebe sein Index zur�ck
                return i;
            }
        }
        return -1; // Error, da immer eine Leertaste geben muss
    }

    // Leertaste wird mit einer Nachbartaste getauscht; Es wird auch �berpr�ft
    // ob die zwei Tasten Nachbaren sind
    public void tauscheTastenwerte(int pIdxTaste, int pIdxLeertaste)
    {
        int x1, y1, x2, y2, groesse, zwischenspeicher;
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
            zwischenspeicher = aZahlenfeld[pIdxTaste];
            aZahlenfeld[pIdxTaste] = aZahlenfeld[pIdxLeertaste];
            aZahlenfeld[pIdxLeertaste] = zwischenspeicher;
        }
    }

    // Das Zahlenfeld wird gemischt
    private int[] zieheZufallszahlen(int pGroesse)
    {
        // Liste mit Zahlen wird erstellt
        int[] zF = new int[pGroesse * pGroesse];

        // Schleife durch die Liste und nummeriere sie durch (aufsteigend). Zum
        // Beispiel: Gr��e = 4; zF = [1,2,3,-1]
        for (int j = 0; j < zF.length; j++)
        {
            // Wenn es sich nicht um das letzte Element handelt
            if (j != zF.length - 1)
            {
                zF[j] = j + 1;
            }
            else
            {
                // Die Leertaste wird als -1 dargestellt
                zF[j] = -1;
            }
        }

        aZahlenfeld = zF;

        // Wie oft die Zahlen miteinander getauscht werden
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

        // Gemischtes Zahlenfeld zur�ckgeben
        return aZahlenfeld;
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
        if (idxNachbar >= 0 && idxNachbar < aZahlenfeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Nachbar von rechts
        xNachbar = pX + 1;
        yNachbar = pY;
        idxNachbar = factor * yNachbar + xNachbar;

        if (idxNachbar >= 0 && idxNachbar < aZahlenfeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Nachbar von oben
        xNachbar = pX;
        yNachbar = pY - 1;
        idxNachbar = factor * yNachbar + xNachbar;

        if (idxNachbar >= 0 && idxNachbar < aZahlenfeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Nachbar von unten
        xNachbar = pX;
        yNachbar = pY + 1;
        idxNachbar = factor * yNachbar + xNachbar;

        if (idxNachbar >= 0 && idxNachbar < aZahlenfeld.length)
        {
            nachbaren.add(idxNachbar);
        }

        // Ein zuf�lliger Nachbar zur�ckgeben
        return nachbaren.get(random.nextInt(nachbaren.size()));
    }

    // Getter
    public int[] gibZahlenFeld()
    {
        return aZahlenfeld;
    }
}
