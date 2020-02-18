public class TimerPuzzle
{
    private int aIntervall;

    private Steuerung dieSteuerung;
    private Thread thread;

    public TimerPuzzle(Steuerung pSteuerung, int pIntervall)
    {
        // Erstellung der Assoziation zur Steuerung
        dieSteuerung = pSteuerung;

        // Intervall einstellen
        aIntervall = pIntervall;

        // Timer starten
        restart();
    }

    // Timer stoppen
    public void stop()
    {
        thread.stop();
    }

    // Beim Ablauf des Intervalls wird diese Methode ausgef�hrt.
    public void zeitEreignis()
    {
        dieSteuerung.timerEreignis();
    }

    // Timer starten bzw. neustarten
    public void restart()
    {
        // Thread erstellen
        thread = new Thread()
        {
            public void run()
            {
                try
                {
                    // Thread f�r aIntervall (Millisekunden) schlafen legen
                    Thread.sleep(aIntervall);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                // Wenn Spieler nicht auf eine Taste w�hrend dem "Schlaf"
                // gedr�ckt hat, dann entsteht ein Zeitereignis
                zeitEreignis();
            }
        };
        // Thread starten
        thread.start();
    }
}
