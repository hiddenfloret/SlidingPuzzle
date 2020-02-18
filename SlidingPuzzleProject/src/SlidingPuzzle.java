public abstract class SlidingPuzzle
{
    private int size;
    private int numOfClicks;

    public SlidingPuzzle(int pSize)
    {
        size = pSize;
        numOfClicks = 0;
    }

    // Increment numOfClicks and return it
    public int incrementNumOfClicks()
    {
        return ++numOfClicks;
    }

    // Abstract Functions
    public abstract boolean puzzleSolved();
    public abstract int getIndexOfSpacebar();

    // Getters
    protected int getSize()
    {
        return size;
    }

    public int getNumOfClicks()
    {
        return numOfClicks;
    }
}