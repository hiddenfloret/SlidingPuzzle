public abstract class Schiebepuzzle
{
    private int aGroesse;
    private int aAnzahlKlicks;

    public Schiebepuzzle(int pGroesse)
    {
        aGroesse = pGroesse;
        aAnzahlKlicks = 0;
    }

    // aAnzahlKlicks inkrementieren und zurückgeben
    public int inkrAnzahlKlicks()
    {
        aAnzahlKlicks++;
        return aAnzahlKlicks;
    }

    // abstrakte Funktion
    public abstract boolean puzzleGeloest();

    // Getters
    protected int gibGroesse()
    {
        return aGroesse;
    }

    public int gibAnzahlKlicks()
    {
        return aAnzahlKlicks;
    }
}