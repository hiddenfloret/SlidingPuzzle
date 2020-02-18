import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

public class ImagePuzzle extends SlidingPuzzle
{
    private Image[] solution, imageField;
    private Random random;

    public ImagePuzzle(int pSize, Image[] pSolution)
    {
        super(pSize);
        random = new Random();
        solution = pSolution;
        initImageField();
    }

    private void initImageField()
    {
        int size;

        size = getSize();

        // Create and shuffle image field
        imageField = shuffleImageField(size);
    }

    // Shuffle image field
    private Image[] shuffleImageField(int pSize)
    {
        imageField = new Image[pSize * pSize];

        // The starting point before the shuffle is the solution.
        // This will later be used to check if the puzzle is solved
        for (int j = 0; j < imageField.length; j++)
        {
            imageField[j] = solution[j];
        }

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
        return imageField;
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

    // Looks if puzzle got solved
    @Override
    public boolean puzzleSolved()
    {
        for (int i = 0; i < imageField.length; i++)
        {
            // Compare every element of the image field with the solution
            if (imageField[i] != solution[i])
            {
                // Not solved
                return false;
            }
        }
        // Solved
        return true;
    }

    // Returns the index of the spacebar
    @Override
    public int getIndexOfSpacebar()
    {
        for (int i = 0; i < imageField.length; i++)
        {
            // The spacebar is depicted as null
            if (imageField[i] == null)
            {
                // Return the index
                return i;
            }
        }
        return -1; // Error, because there always should be a spacebar
    }

    // Swaps spacebar with a neighbor field. These two must be neighbors
    public void swapFieldValues(int pFieldIndex, int pSpacebarIndex)
    {
        int x1, y1, x2, y2, size;
        Image temp;
        boolean isNeighbor = false;

        size = getSize();

        // Convert index of field to x and y components
        x1 = pFieldIndex % size;
        x2 = pSpacebarIndex % size;

        y1 = pFieldIndex / size;
        y2 = pSpacebarIndex / size;

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
            temp = imageField[pFieldIndex];
            imageField[pFieldIndex] = imageField[pSpacebarIndex];
            imageField[pSpacebarIndex] = temp;
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
        if (indexOfNeighbor >= 0 && indexOfNeighbor < imageField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Right neighbor
        xNeighbor = pX + 1;
        yNeighbor = pY;
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        if (indexOfNeighbor >= 0 && indexOfNeighbor < imageField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Upper neighbor
        xNeighbor = pX;
        yNeighbor = pY - 1;
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        if (indexOfNeighbor >= 0 && indexOfNeighbor < imageField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Bottom neighbor
        xNeighbor = pX;
        yNeighbor = pY + 1;
        indexOfNeighbor = factor * yNeighbor + xNeighbor;

        if (indexOfNeighbor >= 0 && indexOfNeighbor < imageField.length)
        {
            neighbors.add(indexOfNeighbor);
        }

        // Return a random neighbor
        return neighbors.get(random.nextInt(neighbors.size()));
    }

    // Getter
    public Image[] getImageField()
    {
        return imageField;
    }
}
