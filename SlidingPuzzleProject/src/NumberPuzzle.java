import java.util.ArrayList;
import java.util.Random;

public class NumberPuzzle extends SlidingPuzzle
{
    private int[] numberField;
    private Random random;

    public NumberPuzzle(int pGroesse)
    {
        super(pGroesse);
        random = new Random();
        initPuzzleField();
    }

    // Looks if puzzle got solved
    @Override
    public boolean puzzleSolved()
    {
        for (int i = 0; i < numberField.length; i++)
        {
            // The puzzle counts as solved, when the numbers are in a
            // ascending order. For example if the size of the puzzle
            // were to be 4, then the puzzle should look like this
            // if it is solved: [1,2,3,-1]
            if (numberField[i] != i + 1 && numberField[i] != -1)
            {
                // Not solved
                return false;
            }
        }
        // Solved
        return true;
    }

    // Initializes the number field
    private void initPuzzleField()
    {
        int size;

        size = getSize();

        // Create number field and shuffle it
        numberField = shuffleNumberField(size);
    }

    // Returns the index of the spacebar
    @Override
    public int getIndexOfSpacebar()
    {
        for (int i = 0; i < numberField.length; i++)
        {
            // The spacebar is depicted as a -1
            if (numberField[i] == -1)
            {
                // Return the index
                return i;
            }
        }
        return -1; // Error, because there always should be a spacebar
    }

    // Swaps spacebar with a neighbor field. These two must be neighbors
    public void swapFieldValues(int pSpacebarIndex, int pFieldIndex)
    {
        int x1, y1, x2, y2, size, temp;
        boolean isNeighbor = false;

        size = getSize();

        // Convert index of field to x and y components
        x1 = pSpacebarIndex % size;
        x2 = pFieldIndex % size;

        y1 = pSpacebarIndex / size;
        y2 = pFieldIndex / size;

        // Checks if they are neigbors
        if (x1 == x2)
        {
            if (Math.abs(y1 - y2) == 1)
            {
                isNeighbor = true;
            }
        }
        else
        if (y1 == y2)
        {
            if (Math.abs(x1 - x2) == 1)
            {
                isNeighbor = true;
            }
        }

        if (isNeighbor)
        {
            // Swap places
            temp = numberField[pSpacebarIndex];
            numberField[pSpacebarIndex] = numberField[pFieldIndex];
            numberField[pFieldIndex] = temp;
        }
    }

    // Shuffles the number field
    private int[] shuffleNumberField(int pSize)
    {
        // Create number field
        int[] tempNumberField = new int[pSize * pSize];

        // Numbering the field in ascending order
        for (int j = 0; j < tempNumberField.length; j++)
        {
            // If index is not last
            if (j != tempNumberField.length - 1)
            {
                tempNumberField[j] = j + 1;
            }
            else
            {
                // The last index is the spacebar
                tempNumberField[j] = -1;
            }
        }

        numberField = tempNumberField;

        // Number of shuffles
        int shuffles = 1000;
        boolean isSolved;

        // Shuffling puzzle 1000 times
        shuffle(pSize, shuffles);

        // It is possible that after the shuffle, the puzzle isn't still mixed.
        // That's why it checks if the puzzle is solved and shuffles it again
        // till it is mixed indeed
        isSolved = puzzleSolved();
        while (isSolved)
        {
            shuffle(pSize, shuffles);
            isSolved = puzzleSolved();
        }

        // Return the shuffled field
        return numberField;
    }

    // Field gets n times shuffled, this way the created puzzle
    // will always have a solution
    private void shuffle(int pSize, int pShuffles)
    {
        int x, y;

        for (int i = 0; i < pShuffles; i++)
        {
            // Get index of spacebar
            int indexOfSpacebar = getIndexOfSpacebar();

            // Index of the spacebar is depicted as x and y coordinates in
            // a two dimensional array
            x = indexOfSpacebar % pSize;
            y = indexOfSpacebar / pSize;

            // Get index of a random neighbor and swap places with him
            int indexOfNeighbor = getRandomNeighbor(x, y);
            swapFieldValues(indexOfNeighbor, indexOfSpacebar);
        }
    }

    // Returns random and valid neighbor of a field
    private int getRandomNeighbor(int pX, int pY)
    {
        int xNeighbor, yNeighbor;
        int indexOfNeighbor;
        int factor = getSize();
        ArrayList<Integer> neighbors = new ArrayList<Integer>();

        // Left neighbor
        xNeighbor = pX - 1;
        yNeighbor = pY;

        // Formula to get index of neighbor
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        // Checks if the index of neighbor is valid, because some fields,
        // especially those in corners, have four neighbors but only two
        // are valid
        if (indexOfNeighbor >= 0 && indexOfNeighbor < numberField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Right neighbor
        xNeighbor = pX + 1;
        yNeighbor = pY;
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        if (indexOfNeighbor >= 0 && indexOfNeighbor < numberField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Upper neighbor
        xNeighbor = pX;
        yNeighbor = pY - 1;
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        if (indexOfNeighbor >= 0 && indexOfNeighbor < numberField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Bottom neighbor
        xNeighbor = pX;
        yNeighbor = pY + 1;
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        if (indexOfNeighbor >= 0 && indexOfNeighbor < numberField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Return a random neighbor
        return neighbors.get(random.nextInt(neighbors.size()));
    }

    // Getter
    public int[] getNumberField()
    {
        return numberField;
    }
}
