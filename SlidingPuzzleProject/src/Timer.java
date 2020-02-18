public class Timer
{
    private int interval;

    private Controller controller;
    private Thread thread;

    public Timer(Controller pController, int pInterval)
    {
        controller = pController;
        interval = pInterval;

        // Start timer
        restart();
    }

    // Stop timer
    public void stop()
    {
        thread.stop();
    }

    // This method gets triggered after the interval
    public void timeEvent()
    {
        controller.timerEvent();
    }

    // Start or restart timer
    public void restart()
    {
        thread = new Thread()
        {
            public void run()
            {
                try
                {
                    // Thread sleeps for 'interval' miliseconds
                    Thread.sleep(interval);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                // If the user didn't click a field during the sleep of the thread,
                // a time event gets triggered
                timeEvent();
            }
        };
        thread.start();
    }
}
