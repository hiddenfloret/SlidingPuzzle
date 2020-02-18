import java.awt.Image;

public class Controller
{
    private GUI gui;

    private String state;
    private int interval;
    private int numberOfClicks;
    private char puzzleType;

    private SlidingPuzzle puzzle;
    private Timer timer;

    public Controller(GUI pGUI)
    {
        gui = pGUI;

        // Starting state
        state = "init";

        // Interval of timer in milliseconds
        interval = 2000;
    }

    // Start new game
    public void startNewGame(char pType, int pSize)
    {
        // State change
        state = "running";
        puzzleType = pType;

        // In proportion with the size of the puzzle, the number of
        // clicks is chosen
        switch (pSize)
        {
            case 2:
                numberOfClicks = 10;
                break;
            case 3:
                numberOfClicks = 200;
                break;
            case 4:
                numberOfClicks = 400;
                break;
        }

        // Start timer
        timer = new Timer(this, interval);

        // Number field
        if (pType == 'z')
        {
            // Generate new puzzle
            puzzle = new NumberPuzzle(pSize);

            // The shuffled puzzle is sent to the user interface
            gui.setNumberOfFields(((NumberPuzzle) puzzle).getNumberField());

            // Update the user interface
            gui.message(1);
            gui.updateRemainedClicks(numberOfClicks);
        }
        else
            // Image puzzle
            if (pType == 'b')
            {
                // The user interface sends the solution
                Image[] solution = gui.getImageParts();

                // Generate new puzzle
                puzzle = new ImagePuzzle(pSize, solution);

                // The shuffled puzzle is sent to the user interface
                gui.setImageOfFields(((ImagePuzzle) puzzle)
                        .getImageField());

                // Update the user interface
                gui.message(1);
                gui.updateRemainedClicks(numberOfClicks);
            }
    }

    // Player clicks on a field
    public void onClickedField(int pFieldIndex)
    {
        // Fields are only allowed to be clicked, when the state
        // corresponds to 'running'
        if (state.equals("running"))
        {
            // Stop timer, because the player reacted before the time event got triggered
            timer.stop();

            // Number puzzle
            if (puzzleType == 'z')
            {
                int indexOfSpacebar;
                int numberOfClicks;
                int[] numberField;
                boolean isSolved;

                indexOfSpacebar = ((NumberPuzzle) puzzle).getIndexOfSpacebar();

                // Swap clicked field with the spacebar
                ((NumberPuzzle) puzzle).swapFieldValues(pFieldIndex,
                        indexOfSpacebar);

                // Update the user interface with the new field
                numberField = ((NumberPuzzle) puzzle).getNumberField();
                gui.setNumberOfFields(numberField);

                // Increment number of clicks and update the user interface
                numberOfClicks = ((NumberPuzzle) puzzle).incrementNumOfClicks();
                gui.updateRemainedClicks(this.numberOfClicks - numberOfClicks);
                isSolved = ((NumberPuzzle) puzzle).puzzleSolved();
                if (isSolved)
                {
                    // Change of state and update of user interface
                    state = "won";
                    gui.message(3);
                }
                else
                {
                    // If puzzle is unsolved and the player has no remaining clicks
                    if (numberOfClicks == this.numberOfClicks)
                    {
                        // Change of state and update of user interface
                        state = "lost";
                        gui.message(2);
                    }
                    // Player still has clicks
                    else
                    {
                        // Reset the timer
                        timer.restart();
                    }
                }
            }
            else
                // Image field
                if (puzzleType == 'b')
                {
                    int indexOfSpacebar;
                    int numberOfClicks;
                    Image[] imageField;
                    boolean isSolved;

                    indexOfSpacebar = ((ImagePuzzle) puzzle).getIndexOfSpacebar();

                    ((ImagePuzzle) puzzle).swapFieldValues(pFieldIndex,
                            indexOfSpacebar);

                    imageField = ((ImagePuzzle) puzzle).getImageField();
                    gui.setImageOfFields(imageField);

                    numberOfClicks = ((ImagePuzzle) puzzle)
                            .incrementNumOfClicks();
                    gui.updateRemainedClicks(this.numberOfClicks - numberOfClicks);

                    isSolved = ((ImagePuzzle) puzzle).puzzleSolved();
                    if (isSolved)
                    {
                        state = "won";
                        gui.message(3);
                    }
                    else
                    {
                        if (numberOfClicks == this.numberOfClicks)
                        {
                            state = "lost";
                            gui.message(2);
                        }
                        else
                        {
                            timer.restart();
                        }
                    }
                }
        }
    }

    // If player doesn't click a field in time
    public void timerEvent()
    {
        int numberOfClicks = 0;

        // Number field
        if (puzzleType == 'z')
        {
            // Increment number of clicks and update the user interface
            numberOfClicks = ((NumberPuzzle) puzzle).incrementNumOfClicks();
            gui.updateRemainedClicks(this.numberOfClicks - numberOfClicks);
        }
        else
            // Image field
            if (puzzleType == 'b')
            {
                numberOfClicks = ((ImagePuzzle) puzzle).incrementNumOfClicks();
                gui.updateRemainedClicks(this.numberOfClicks - numberOfClicks);
            }

        // If the player has no clicks
        if (numberOfClicks == this.numberOfClicks)
        {
            // State change and update the user interface
            state = "lost";
            gui.message(2);
        }
        else
        {
            // Reset the timer
            timer.restart();
        }
    }

    // Getter und Setter
    public String getState()
    {
        return state;
    }

    public void setState(String pState)
    {
        state = pState;
    }
}
