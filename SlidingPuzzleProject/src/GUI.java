import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.util.Enumeration;

public class GUI extends JFrame
{
    // JPanels
    private JPanel contentPane;
    private JPanel panelPuzzle; // where the puzzle will be drawn

    // JFrame for the reference image
    private JFrame frameReference;

    // Radio buttons
    private JRadioButton rdbtnNumberPuzzle;
    private JRadioButton rdbtnImagePuzzle;
    private JRadioButton rdbtnSizeTwo;
    private JRadioButton rdbtnGreen;
    private JRadioButton rdbtnBlue;
    private JRadioButton rdbtnRed;
    private JRadioButton rdbtnSizeThree;
    private JRadioButton rdbtnSizeFour;
    private JRadioButton rdbtnImage3;
    private JRadioButton rdbtnImage2;
    private JRadioButton rdbtnImage1;

    // Button groups
    private ButtonGroup groupColors;
    private ButtonGroup groupPuzzleType;
    private ButtonGroup groupSize;
    private ButtonGroup groupImages;

    // JButtons
    private JButton listOfFields[]; // Puzzle is composed of a list of JButtons
    private JButton btnNewGame;
    private JButton btnCloseGame;

    // Labels
    private JLabel lblMessageText;
    private JLabel lblNumberOfClicks;

    // Properties of the puzzle
    private char type;
    private char color;
    private int size;
    private int sizeOfPanelPuzzle;

    private Controller controller;

    // Image splitting
    private Image changedImage;
    private ImageIcon startingImage;
    private Image[] imageParts;
    private String filePath;

    private boolean isImageSelected;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    GUI frame = new GUI();
                    frame.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public GUI()
    {
        setResizable(false);
        setTitle("Sliding puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 816, 725);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmLoadImage = new JMenuItem("Load image");
        mntmLoadImage.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clickOpenImage();
            }
        });
        mnFile.add(mntmLoadImage);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        panelPuzzle = new JPanel();
        panelPuzzle.setBackground(Color.white);
        panelPuzzle.setBounds(10, 10, 600, 600);
        contentPane.add(panelPuzzle);
        panelPuzzle.setLayout(null);

        // The size of the panelPuzzle is saved
        sizeOfPanelPuzzle = panelPuzzle.getWidth();

        lblMessageText = new JLabel(
                "Choose type, color and size of the puzzle then start the game");
        lblMessageText.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessageText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMessageText.setBounds(65, 625, 500, 33);
        contentPane.add(lblMessageText);

        JPanel panelPuzzleType = new JPanel();
        panelPuzzleType.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Type of Puzzle",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelPuzzleType.setBounds(620, 11, 171, 121);
        contentPane.add(panelPuzzleType);
        panelPuzzleType.setLayout(null);

        JPanel panelImages = new JPanel();
        panelImages.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Images",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelImages.setBounds(620, 351, 171, 145);
        contentPane.add(panelImages);
        panelImages.setLayout(null);

        rdbtnImage1 = new JRadioButton("Image 1");
        rdbtnImage1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                selectImage(0);
                activateNewGame();
            }
        });
        rdbtnImage1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnImage1.setBounds(6, 31, 159, 23);
        panelImages.add(rdbtnImage1);

        rdbtnImage2 = new JRadioButton("Image 2");
        rdbtnImage2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                selectImage(1);
                activateNewGame();
            }
        });
        rdbtnImage2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnImage2.setBounds(6, 68, 159, 23);
        panelImages.add(rdbtnImage2);

        rdbtnImage3 = new JRadioButton("Image 3");
        rdbtnImage3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                selectImage(2);
                activateNewGame();
            }
        });
        rdbtnImage3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnImage3.setBounds(6, 104, 159, 23);
        panelImages.add(rdbtnImage3);

        rdbtnNumberPuzzle = new JRadioButton("Number puzzle");
        rdbtnNumberPuzzle.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                type = 'z';
                activateColorSelection();
                deactivateImageSelection();
                activateNewGame();
            }
        });
        rdbtnNumberPuzzle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnNumberPuzzle.setBounds(6, 33, 159, 23);
        panelPuzzleType.add(rdbtnNumberPuzzle);

        rdbtnImagePuzzle = new JRadioButton("Image puzzle");
        rdbtnImagePuzzle.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                type = 'b';
                deactivateColorSelection();
                activateImageSelection();
                activateNewGame();
            }
        });
        rdbtnImagePuzzle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnImagePuzzle.setBounds(6, 75, 159, 23);
        panelPuzzleType.add(rdbtnImagePuzzle);

        JPanel panelColor = new JPanel();
        panelColor.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Color",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelColor.setBounds(620, 143, 82, 203);
        contentPane.add(panelColor);
        panelColor.setLayout(null);

        rdbtnRed = new JRadioButton("red");
        rdbtnRed.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                color = 'r';
                activateNewGame();
            }
        });
        rdbtnRed.setForeground(Color.RED);
        rdbtnRed.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnRed.setBounds(6, 41, 70, 23);
        panelColor.add(rdbtnRed);

        rdbtnGreen = new JRadioButton("green");
        rdbtnGreen.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                color = 'g';
                activateNewGame();
            }
        });
        rdbtnGreen.setForeground(Color.GREEN);
        rdbtnGreen.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnGreen.setBounds(6, 95, 70, 23);
        panelColor.add(rdbtnGreen);

        rdbtnBlue = new JRadioButton("blue");
        rdbtnBlue.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                color = 'b';
                activateNewGame();
            }
        });
        rdbtnBlue.setForeground(Color.BLUE);
        rdbtnBlue.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnBlue.setBounds(6, 154, 70, 23);
        panelColor.add(rdbtnBlue);

        JPanel panelSize = new JPanel();
        panelSize.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Size",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelSize.setBounds(709, 143, 82, 203);
        contentPane.add(panelSize);
        panelSize.setLayout(null);

        rdbtnSizeTwo = new JRadioButton("2X2");
        rdbtnSizeTwo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                size = 2;
                activateNewGame();
            }
        });
        rdbtnSizeTwo.setForeground(Color.BLACK);
        rdbtnSizeTwo.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnSizeTwo.setBounds(6, 41, 70, 23);
        panelSize.add(rdbtnSizeTwo);

        rdbtnSizeThree = new JRadioButton("3X3");
        rdbtnSizeThree.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                size = 3;
                activateNewGame();
            }
        });
        rdbtnSizeThree.setForeground(Color.BLACK);
        rdbtnSizeThree.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnSizeThree.setBounds(6, 95, 70, 23);
        panelSize.add(rdbtnSizeThree);

        rdbtnSizeFour = new JRadioButton("4X4");
        rdbtnSizeFour.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                size = 4;
                activateNewGame();
            }
        });
        rdbtnSizeFour.setForeground(Color.BLACK);
        rdbtnSizeFour.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnSizeFour.setBounds(6, 154, 70, 23);
        panelSize.add(rdbtnSizeFour);

        btnNewGame = new JButton("New game");
        btnNewGame.setEnabled(false);
        btnNewGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clickNewGame();
            }
        });
        btnNewGame.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnNewGame.setBounds(620, 507, 171, 45);
        contentPane.add(btnNewGame);

        btnCloseGame = new JButton("Close game");
        btnCloseGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clickClose();
            }
        });
        btnCloseGame.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCloseGame.setBounds(620, 563, 171, 45);
        contentPane.add(btnCloseGame);

        JLabel lblNumberOfClicks = new JLabel("Clicks:");
        lblNumberOfClicks.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNumberOfClicks.setBounds(630, 625, 96, 33);
        contentPane.add(lblNumberOfClicks);

        this.lblNumberOfClicks = new JLabel("");
        this.lblNumberOfClicks.setFont(new Font("Tahoma", Font.PLAIN, 16));
        this.lblNumberOfClicks.setBounds(716, 625, 75, 33);
        contentPane.add(this.lblNumberOfClicks);

        // Radio buttons are added to button groups
        groupPuzzleType = new ButtonGroup();
        groupPuzzleType.add(rdbtnNumberPuzzle);
        groupPuzzleType.add(rdbtnImagePuzzle);

        groupColors = new ButtonGroup();
        groupColors.add(rdbtnRed);
        groupColors.add(rdbtnGreen);
        groupColors.add(rdbtnBlue);

        groupSize = new ButtonGroup();
        groupSize.add(rdbtnSizeTwo);
        groupSize.add(rdbtnSizeThree);
        groupSize.add(rdbtnSizeFour);

        groupImages = new ButtonGroup();
        groupImages.add(rdbtnImage1);
        groupImages.add(rdbtnImage2);
        groupImages.add(rdbtnImage3);

        // Standard values
        type = 'x';
        color = 'x';
        size = -1;
        isImageSelected = false;

        controller = new Controller(this);
    }

    // Image selection gets activated
    private void activateImageSelection()
    {
        rdbtnImage1.setEnabled(true);
        rdbtnImage2.setEnabled(true);
        rdbtnImage3.setEnabled(true);
    }

    // Image selection gets deactivated
    private void deactivateImageSelection()
    {
        // Reset
        isImageSelected = false;
        groupImages.clearSelection();

        rdbtnImage1.setEnabled(false);
        rdbtnImage2.setEnabled(false);
        rdbtnImage3.setEnabled(false);
    }

    // Scale the selected image
    private void selectImage(int pIndex)
    {
        isImageSelected = true;

        // The predefined images are in the Images directory
        switch (pIndex)
        {
            case 0:
                startingImage = new ImageIcon(this.getClass().getResource(
                        "/Images/Image1.jpg"));
                break;
            case 1:
                startingImage = new ImageIcon(this.getClass().getResource(
                        "/Images/Image2.jpg"));
                break;
            case 2:
                startingImage = new ImageIcon(this.getClass().getResource(
                        "/Images/Image3.jpg"));
                break;
            default:
                break;
        }

        // Adjust the size of the image
        changedImage = startingImage.getImage().getScaledInstance(
                panelPuzzle.getSize().width, panelPuzzle.getSize().height,
                Image.SCALE_DEFAULT);
        activateNewGame();
    }

    // JButton "New game" only gets activated, when the player makes the right selection
    private void activateNewGame()
    {
        // Player has selected a type and a size
        if (type != 'x' && size != -1)
        {
            // Player has selected number puzzle and a color
            if (type == 'z' && color != 'x')
            {
                // Game can start
                btnNewGame.setEnabled(true);
            }
            else
                // Player has selected image puzzle and an image (from the user interface or
                // their own image)
                if (type == 'b' && isImageSelected == true)
                {
                    // Game can start
                    btnNewGame.setEnabled(true);
                }
                else
                {
                    // Game can't start
                    btnNewGame.setEnabled(false);
                }
        }
        else
        {
            // Game can't start
            btnNewGame.setEnabled(false);
        }
    }

    // Player chooses their own image
    private void clickOpenImage()
    {
        JFileChooser searchFile;
        File file = null;

        int value;

        searchFile = new JFileChooser();
        value = searchFile.showOpenDialog(null);

        if (value != 1)
        {
            // Player has selected an image
            if (value == JFileChooser.APPROVE_OPTION)
            {
                isImageSelected = true;

                file = searchFile.getSelectedFile();
                // Get path to file
                filePath = file.getAbsolutePath();

                // Image is wrapped as an ImageIcon object
                startingImage = new ImageIcon(filePath);

                // Adjust the size of the image
                changedImage = startingImage.getImage().getScaledInstance(
                        panelPuzzle.getSize().width,
                        panelPuzzle.getSize().height, Image.SCALE_DEFAULT);

                activateNewGame();
            }
        }
    }

    // Open new window with a reference image
    private void openImage(String pFile)
    {
        // Create the JFrame
        frameReference = new JFrame();
        frameReference.setVisible(true);
        frameReference.setTitle("Reference image");
        frameReference.setResizable(false);
        frameReference.setBounds(0, 0, panelPuzzle.getWidth(),
                panelPuzzle.getHeight());
        frameReference.setLocationRelativeTo(null);

        // Create the JPanel
        JPanel imagePanel = new JPanel();
        imagePanel.setBounds(0, 0, panelPuzzle.getWidth(),
                panelPuzzle.getHeight());
        frameReference.getContentPane().add(imagePanel);

        // Image gets drawn as an ImageIcon of a label
        JLabel imageLabel = new JLabel(new ImageIcon(changedImage));

        // Update the JPanel
        imagePanel.removeAll();
        imagePanel.add(imageLabel);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    // Player clicks the "Close game" button
    private void clickClose()
    {
        // Player won or lost a game
        if (controller.getState() == "lost"
                || controller.getState() == "won")
        {
            // Reset the game state
            controller.setState("init");

            // Remove all JButtons
            panelPuzzle.removeAll();

            // Player just played an image puzzle
            if (type == 'b')
            {
                // Close the window with the reference image
                frameReference.dispatchEvent(new WindowEvent(frameReference,
                        WindowEvent.WINDOW_CLOSING));
            }

            // Reset the number of clicks
            updateRemainedClicks(-1);

            // Standard values are set again
            type = 'x';
            color = 'x';
            size = -1;
            isImageSelected = false;

            // The "New game" JButton gets deactivated
            btnNewGame.setEnabled(false);

            // Release all selection
            groupPuzzleType.clearSelection();
            groupColors.clearSelection();
            groupSize.clearSelection();
            groupImages.clearSelection();

            // Update the panelPuzzle
            panelPuzzle.revalidate();
            panelPuzzle.repaint();

            // Activate the radio buttons
            Enumeration<AbstractButton> enumeration = groupColors.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }

            enumeration = groupSize.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }

            enumeration = groupPuzzleType.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }

            enumeration = groupImages.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }
        }
    }

    // Player clicks the "New game" JButton
    private void clickNewGame()
    {
        if (controller.getState() == "init")
        {
            // Number puzzle
            if (type == 'z')
            {
                panelPuzzle.removeAll();
                initPuzzleField(size);
                setColorOfFields(color);
                controller.startNewGame(type, size);

                // Update panelPuzzle
                panelPuzzle.revalidate();
                panelPuzzle.repaint();
            }

            else
                // Image puzzle
                if (type == 'b')
                {
                    // Calculate step size of an image part
                    int stepSize = panelPuzzle.getWidth() / size;

                    int counter = 0;

                    imageParts = new Image[size * size];

                    // Image gets divided in sizexsize parts
                    for (int i = 0; i < panelPuzzle.getWidth(); i += stepSize)
                    {
                        for (int j = 0; j < panelPuzzle.getHeight(); j += stepSize)
                        {
                            // Last part
                            if (j == panelPuzzle.getHeight() - stepSize
                                    && i == panelPuzzle.getWidth() - stepSize)
                            {
                                // The last part must be the spacebar
                                imageParts[counter] = null;
                            }
                            // If not the last image part
                            else
                            {
                                // Size of an image part
                                imageParts[counter] = createImage(new FilteredImageSource(
                                        changedImage.getSource(),
                                        new CropImageFilter(j, i, stepSize,
                                                stepSize)));
                            }
                            counter++;
                        }
                    }

                    panelPuzzle.removeAll();
                    initPuzzleField(size);

                    controller.startNewGame(type, size);

                    panelPuzzle.revalidate();
                    panelPuzzle.repaint();

                    // Open window with the reference image
                    openImage(filePath);
                }

            // Deactivate the radio buttons
            Enumeration<AbstractButton> enumeration = groupColors.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }

            enumeration = groupSize.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }

            enumeration = groupPuzzleType.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }

            enumeration = groupImages.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }
        }
        else
            // If player clicks again on the "New game" Button, a game with the
            // same properties will be generated
            if (controller.getState() == "won"
                    || controller.getState() == "lost")
            {
                controller.startNewGame(type, size);

                panelPuzzle.revalidate();
                panelPuzzle.repaint();
            }
    }

    // The clicked JButton is sent to the controller
    private void clickField(JButton pField)
    {
        for (int i = 0; i < listOfFields.length; i++)
        {
            // Search for the field
            if (listOfFields[i] == pField)
            {
                // Index of the field
                controller.onClickedField(i);
            }
        }
    }

    // Update the number of clicks
    public void updateRemainedClicks(int pRemainedClicks)
    {
        if (pRemainedClicks == -1) // When the state is "init"
            lblNumberOfClicks.setText("");
        else
            lblNumberOfClicks.setText(Integer.toString(pRemainedClicks));
    }

    // Show game state on the interface
    public void message(int pTextNumber)
    {
        switch (pTextNumber)
        {
            case 0:
                lblMessageText
                        .setText("Choose type, color and size of the puzzle then start the game");
                break;

            case 1:
                lblMessageText
                        .setText("Click a field to swap it with the spacebar");
                break;

            case 2:
                lblMessageText.setText("You lost!");
                break;

            case 3:
                lblMessageText.setText("You won, congratz!");
                break;
        }
    }

    // Number the JButtons
    public void setNumberOfFields(int[] numberField)
    {
        for (int i = 0; i < numberField.length; i++)
        {
            // Not the spacebar
            if (numberField[i] != -1)
            {
                // JButton gets a number
                listOfFields[i].setText(Integer.toString(numberField[i]));
            }
            // Spacebar
            else
            {
                // JButton gets no number
                listOfFields[i].setText("");
            }
        }
    }

    // Numbers of the JButtons get a color
    private void setColorOfFields(char pColor)
    {
        for (int i = 0; i < listOfFields.length; i++)
        {
            if (pColor == 'r') // red
            {
                listOfFields[i].setForeground(Color.red);
            }
            else
            if (pColor == 'g') // green
            {
                listOfFields[i].setForeground(Color.green);
            }
            else
            if (pColor == 'b') // blue
            {
                listOfFields[i].setForeground(Color.blue);
            }
        }
    }

    // Init of the puzzle field
    private void initPuzzleField(int pSize)
    {
        int numberOfFields = pSize * pSize;

        // Calculation of the length and width of a JButton
        int length = sizeOfPanelPuzzle / pSize;

        listOfFields = new JButton[numberOfFields];

        for (int i = 0; i < numberOfFields; i++)
        {
            final int index = i;

            // Calculate x and y coordinates of the JButton
            int x = i % pSize;
            int y = i / pSize;

            listOfFields[i] = new JButton("");
            // Every JButton gets an ActionListener that gets triggered
            // when clicked on
            listOfFields[i].addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    // Number puzzle
                    if (type == 'z')
                    {
                        // Not spacebar
                        if (listOfFields[index].getText() != "")
                        {
                            clickField(listOfFields[index]);
                        }
                    }
                    else
                        // Image puzzle
                        if (type == 'b')
                        {
                            // Not spacebar
                            if (listOfFields[index].getIcon().getIconWidth() != -1)
                            {
                                clickField(listOfFields[index]);
                            }
                        }
                }
            });
            listOfFields[i].setFont(new Font("Tahoma", Font.PLAIN, 100));

            // Position of the JButton
            listOfFields[i].setBounds(x * length, y * length,
                    length, length);

            // Draw JButton
            panelPuzzle.add(listOfFields[i]);
        }
    }

    // Activate radio buttons for color selection
    private void activateColorSelection()
    {
        rdbtnRed.setEnabled(true);
        rdbtnGreen.setEnabled(true);
        rdbtnBlue.setEnabled(true);
    }

    // Deactivate radio buttons for color selection
    private void deactivateColorSelection()
    {
        color = 'x'; // Reset
        groupColors.clearSelection();

        rdbtnRed.setEnabled(false);
        rdbtnGreen.setEnabled(false);
        rdbtnBlue.setEnabled(false);
    }

    // Draw image parts on the JButtons
    public void setImageOfFields(Image[] pImageField)
    {
        for (int i = 0; i < pImageField.length; i++)
        {
            // Not spacebar
            if (pImageField[i] != null)
            {
                // Draw the image part on the JButton
                listOfFields[i].setIcon(new ImageIcon(pImageField[i]));
            }
            // If spacebar then no image
            else
            {
                listOfFields[i].setIcon(new ImageIcon());
            }
        }
    }

    // Getter
    public Image[] getImageParts()
    {
        return imageParts;
    }
}
