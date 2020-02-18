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
    private JPanel panelPuzzle; // JPanel wo das Puzzle gezeichnet wird

    // JFrame f�r das Referenzbild
    private JFrame frameReferenz;

    // Radiobuttons
    private JRadioButton rdbtnZP;
    private JRadioButton rdbtnBP;
    private JRadioButton rdbtn2;
    private JRadioButton rdbtnGrn;
    private JRadioButton rdbtnBlau;
    private JRadioButton rdbtnRot;
    private JRadioButton rdbtn3;
    private JRadioButton rdbtn4;
    private JRadioButton rdbtnBild3;
    private JRadioButton rdbtnBild2;
    private JRadioButton rdbtnBild1;

    // Buttongroups
    private ButtonGroup groupFarben;
    private ButtonGroup groupPuzzleArt;
    private ButtonGroup groupGroesse;
    private ButtonGroup groupBilder;

    // JButtons
    private JButton aTastenFeld[]; // Puzzle besteht aus einer Liste von
    // JButtons
    private JButton btnNeuesSpiel;
    private JButton btnSpielBeenden;

    // Labels
    private JLabel lblMeldungsText;
    private JLabel lblRestKlicks;

    // Eigenschaften des Puzzles
    private char aTyp;
    private char aFarbe;
    private int aGroesse;
    private int groessePanelPuzzle;

    private Steuerung dieSteuerung;

    // Teilung des Bildes
    private Image geaendertesBild;
    private ImageIcon anfangsBild;
    private Image[] bildTeile;
    private String dateiPfad;

    private boolean bildAusgewaehlt;

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
        setTitle("Schiebepuzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 816, 725);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnDatei = new JMenu("Datei");
        menuBar.add(mnDatei);

        JMenuItem mntmNewMenuItem = new JMenuItem("Bild laden");
        mntmNewMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clickBildOeffnen();
            }
        });
        mnDatei.add(mntmNewMenuItem);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        panelPuzzle = new JPanel();
        panelPuzzle.setBackground(Color.white);
        panelPuzzle.setBounds(10, 10, 600, 600);
        contentPane.add(panelPuzzle);
        panelPuzzle.setLayout(null);

        // Gr��e von panelPuzzle wird gespeichert
        groessePanelPuzzle = panelPuzzle.getWidth();

        lblMeldungsText = new JLabel(
                "Art, Farbe, Gr\u00F6\u00DFe w\u00E4hlen; dann Spiel starten");
        lblMeldungsText.setHorizontalAlignment(SwingConstants.CENTER);
        lblMeldungsText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblMeldungsText.setBounds(65, 625, 500, 33);
        contentPane.add(lblMeldungsText);

        JPanel panelPuzzleArt = new JPanel();
        panelPuzzleArt.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Puzzle-Art",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelPuzzleArt.setBounds(620, 11, 171, 121);
        contentPane.add(panelPuzzleArt);
        panelPuzzleArt.setLayout(null);

        JPanel panelBilder = new JPanel();
        panelBilder.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Bilder",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelBilder.setBounds(620, 351, 171, 145);
        contentPane.add(panelBilder);
        panelBilder.setLayout(null);

        rdbtnBild1 = new JRadioButton("Bild 1");
        rdbtnBild1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                bildAuswaehlen(0);
                aktiviereNeuesSpiel();
            }
        });
        rdbtnBild1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnBild1.setBounds(6, 31, 159, 23);
        panelBilder.add(rdbtnBild1);

        rdbtnBild2 = new JRadioButton("Bild 2");
        rdbtnBild2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                bildAuswaehlen(1);
                aktiviereNeuesSpiel();
            }
        });
        rdbtnBild2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnBild2.setBounds(6, 68, 159, 23);
        panelBilder.add(rdbtnBild2);

        rdbtnBild3 = new JRadioButton("Bild 3");
        rdbtnBild3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                bildAuswaehlen(2);
                aktiviereNeuesSpiel();
            }
        });
        rdbtnBild3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnBild3.setBounds(6, 104, 159, 23);
        panelBilder.add(rdbtnBild3);

        rdbtnZP = new JRadioButton("Zahlenpuzzle");
        rdbtnZP.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aTyp = 'z';
                farbenAuswahlAktivieren();
                bilderAuswahlDeaktivieren();
                aktiviereNeuesSpiel();
            }
        });
        rdbtnZP.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnZP.setBounds(6, 33, 159, 23);
        panelPuzzleArt.add(rdbtnZP);

        rdbtnBP = new JRadioButton("Bilderpuzzle");
        rdbtnBP.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aTyp = 'b';
                farbenAuswahlDeaktivieren();
                bilderAuswahlAktivieren();
                aktiviereNeuesSpiel();
            }
        });
        rdbtnBP.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnBP.setBounds(6, 75, 159, 23);
        panelPuzzleArt.add(rdbtnBP);

        JPanel panelFarbe = new JPanel();
        panelFarbe.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Farbe",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelFarbe.setBounds(620, 143, 82, 203);
        contentPane.add(panelFarbe);
        panelFarbe.setLayout(null);

        rdbtnRot = new JRadioButton("rot");
        rdbtnRot.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aFarbe = 'r';
                aktiviereNeuesSpiel();
            }
        });
        rdbtnRot.setForeground(Color.RED);
        rdbtnRot.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnRot.setBounds(6, 41, 70, 23);
        panelFarbe.add(rdbtnRot);

        rdbtnGrn = new JRadioButton("gr\u00FCn");
        rdbtnGrn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aFarbe = 'g';
                aktiviereNeuesSpiel();
            }
        });
        rdbtnGrn.setForeground(Color.GREEN);
        rdbtnGrn.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnGrn.setBounds(6, 95, 70, 23);
        panelFarbe.add(rdbtnGrn);

        rdbtnBlau = new JRadioButton("blau");
        rdbtnBlau.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aFarbe = 'b';
                aktiviereNeuesSpiel();
            }
        });
        rdbtnBlau.setForeground(Color.BLUE);
        rdbtnBlau.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtnBlau.setBounds(6, 154, 70, 23);
        panelFarbe.add(rdbtnBlau);

        JPanel panelGroesse = new JPanel();
        panelGroesse.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Gr\u00F6\u00DFe",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.PLAIN, 16), null));
        panelGroesse.setBounds(709, 143, 82, 203);
        contentPane.add(panelGroesse);
        panelGroesse.setLayout(null);

        rdbtn2 = new JRadioButton("2X2");
        rdbtn2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aGroesse = 2;
                aktiviereNeuesSpiel();
            }
        });
        rdbtn2.setForeground(Color.BLACK);
        rdbtn2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtn2.setBounds(6, 41, 70, 23);
        panelGroesse.add(rdbtn2);

        rdbtn3 = new JRadioButton("3X3");
        rdbtn3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aGroesse = 3;
                aktiviereNeuesSpiel();
            }
        });
        rdbtn3.setForeground(Color.BLACK);
        rdbtn3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtn3.setBounds(6, 95, 70, 23);
        panelGroesse.add(rdbtn3);

        rdbtn4 = new JRadioButton("4X4");
        rdbtn4.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                aGroesse = 4;
                aktiviereNeuesSpiel();
            }
        });
        rdbtn4.setForeground(Color.BLACK);
        rdbtn4.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtn4.setBounds(6, 154, 70, 23);
        panelGroesse.add(rdbtn4);

        btnNeuesSpiel = new JButton("Neues Spiel");
        btnNeuesSpiel.setEnabled(false);
        btnNeuesSpiel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clickNeuesSpiel();
            }
        });
        btnNeuesSpiel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnNeuesSpiel.setBounds(620, 507, 171, 45);
        contentPane.add(btnNeuesSpiel);

        btnSpielBeenden = new JButton("Spiel beenden");
        btnSpielBeenden.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clickEnde();
            }
        });
        btnSpielBeenden.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSpielBeenden.setBounds(620, 563, 171, 45);
        contentPane.add(btnSpielBeenden);

        JLabel lblTxtRest = new JLabel("Rest-Klicks:");
        lblTxtRest.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTxtRest.setBounds(630, 625, 96, 33);
        contentPane.add(lblTxtRest);

        lblRestKlicks = new JLabel("");
        lblRestKlicks.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblRestKlicks.setBounds(716, 625, 75, 33);
        contentPane.add(lblRestKlicks);

        // Radiobuttons werden zu Buttongroups hinzugef�gt
        groupPuzzleArt = new ButtonGroup();
        groupPuzzleArt.add(rdbtnZP);
        groupPuzzleArt.add(rdbtnBP);

        groupFarben = new ButtonGroup();
        groupFarben.add(rdbtnRot);
        groupFarben.add(rdbtnGrn);
        groupFarben.add(rdbtnBlau);

        groupGroesse = new ButtonGroup();
        groupGroesse.add(rdbtn2);
        groupGroesse.add(rdbtn3);
        groupGroesse.add(rdbtn4);

        groupBilder = new ButtonGroup();
        groupBilder.add(rdbtnBild1);
        groupBilder.add(rdbtnBild2);
        groupBilder.add(rdbtnBild3);

        // Standardwerte
        aTyp = 'x';
        aFarbe = 'x';
        aGroesse = -1;
        bildAusgewaehlt = false;

        // Erstellung der Assoziation zur Steuerung
        dieSteuerung = new Steuerung(this);
    }

    // Auswahl der Bilder wird aktiviert
    private void bilderAuswahlAktivieren()
    {
        rdbtnBild1.setEnabled(true);
        rdbtnBild2.setEnabled(true);
        rdbtnBild3.setEnabled(true);
    }

    // Auswahl der Bilder wird deaktiviert
    private void bilderAuswahlDeaktivieren()
    {
        // Zur�cksetzung von bildAusgewaehlt
        bildAusgewaehlt = false;
        groupBilder.clearSelection();

        rdbtnBild1.setEnabled(false);
        rdbtnBild2.setEnabled(false);
        rdbtnBild3.setEnabled(false);
    }

    // Das ausgew�hlte Bild von der GUI wird entsprechend dimensioniert
    private void bildAuswaehlen(int pIndex)
    {
        // bildAusgewaehlt wird f�r die Aktivierung des JButtons "Neues Spiel"
        // verwendet
        bildAusgewaehlt = true;

        // Ein ImageIcon mit dem ausgew�hlten Bild wird initialisiert
        // Die Bilder befinden sich im res-Ordner
        switch (pIndex)
        {
            case 0:
                anfangsBild = new ImageIcon(this.getClass().getResource(
                        "/Images/Bild1.jpg"));
                break;
            case 1:
                anfangsBild = new ImageIcon(this.getClass().getResource(
                        "/Images/Bild2.jpg"));
                break;
            case 2:
                anfangsBild = new ImageIcon(this.getClass().getResource(
                        "/Images/Bild3.jpg"));
                break;
            default:
                break;
        }

        // Die Gr��e des Bildes wird angepasst
        geaendertesBild = anfangsBild.getImage().getScaledInstance(
                panelPuzzle.getSize().width, panelPuzzle.getSize().height,
                Image.SCALE_DEFAULT);
        aktiviereNeuesSpiel();
    }

    // JButton "Neues Spiel" wird nur dann aktiviert, wenn der Spieler alles
    // richtig ausw�hlt
    private void aktiviereNeuesSpiel()
    {
        // Spieler hat ein Typ und eine Gr��e ausgew�hlt
        if (aTyp != 'x' && aGroesse != -1)
        {
            // Spieler hat Zahlenpuzzle und eine Farbe ausgew�hlt
            if (aTyp == 'z' && aFarbe != 'x')
            {
                // Spiel kann starten
                btnNeuesSpiel.setEnabled(true);
            }
            else
                // Spieler hat Bilderpuzzle und ein Bild ausgew�hlt (von der GUI
                // oder vom Computer)
                if (aTyp == 'b' && bildAusgewaehlt == true)
                {
                    // Spiel kann starten
                    btnNeuesSpiel.setEnabled(true);
                }
                else
                {
                    // Spiel kann nicht starten
                    btnNeuesSpiel.setEnabled(false);
                }
        }
        else
        {
            // Spiel kann nicht starten
            btnNeuesSpiel.setEnabled(false);
        }
    }

    // Spieler sucht sich ein Bild vom Computer aus und nicht von der GUI
    private void clickBildOeffnen()
    {
        JFileChooser dateiSuchen;
        File datei = null;

        int wert;

        dateiSuchen = new JFileChooser();
        wert = dateiSuchen.showOpenDialog(null);

        if (wert != 1)
        {
            // Spieler hat ein Bild ausgew�hlt
            if (wert == JFileChooser.APPROVE_OPTION)
            {
                bildAusgewaehlt = true;

                datei = dateiSuchen.getSelectedFile();
                // Der Pfad zum Bild wird ermittelt
                dateiPfad = datei.getAbsolutePath();

                // Bild wird als ImageIcon gespeichert
                anfangsBild = new ImageIcon(dateiPfad);

                // Die Gr��e des Bildes wird angepasst
                geaendertesBild = anfangsBild.getImage().getScaledInstance(
                        panelPuzzle.getSize().width,
                        panelPuzzle.getSize().height, Image.SCALE_DEFAULT);

                aktiviereNeuesSpiel();
            }
        }
    }

    // Fenster mit dem Referenzbild wird ge�ffnet
    private void oeffneBild(String pDatei)
    {
        // Erstellung des JFrames
        frameReferenz = new JFrame();
        frameReferenz.setVisible(true);
        frameReferenz.setTitle("Referenzbild");
        frameReferenz.setResizable(false);
        frameReferenz.setBounds(0, 0, panelPuzzle.getWidth(),
                panelPuzzle.getHeight());
        frameReferenz.setLocationRelativeTo(null);

        // Erstellung des JPanels
        JPanel bildPanel = new JPanel();
        bildPanel.setBounds(0, 0, panelPuzzle.getWidth(),
                panelPuzzle.getHeight());
        frameReferenz.getContentPane().add(bildPanel);

        // Bild wird als ein ImageIcon von einem Label gezeichnet
        JLabel bildLabel = new JLabel(new ImageIcon(geaendertesBild));

        // Aktualisierung des JPanels
        bildPanel.removeAll();
        bildPanel.add(bildLabel);
        bildPanel.revalidate();
        bildPanel.repaint();
    }

    // Spieler dr�ckt aus JButton "Spiel beenden"
    private void clickEnde()
    {
        // Spieler hat gerade ein Spiel gewonnen oder verloren
        if (dieSteuerung.getZustand() == "lost"
                || dieSteuerung.getZustand() == "won")
        {
            // Spielzustand wird zur�ckgesetzt
            dieSteuerung.setZustand("init");

            // Alle JButtons werden vom panelPuzzle gel�scht
            panelPuzzle.removeAll();

            // Wenn Spieler gerade ein Bilderpuzzle gespielt hat
            if (aTyp == 'b')
            {
                // Fenster mit Referenzbild wird geschlossen
                frameReferenz.dispatchEvent(new WindowEvent(frameReferenz,
                        WindowEvent.WINDOW_CLOSING));
            }

            // Anzahl der Klicks wird auf "" zur�ckgesetzt
            schreibeRestKlicks(-1);

            // Attribute werden auf ihre Standardwerte zur�ckgesetzt
            aTyp = 'x';
            aFarbe = 'x';
            aGroesse = -1;
            bildAusgewaehlt = false;

            // JButton "Neues Spiel" wird deaktiviert -> Spieler muss erneut
            // alles richtig ausw�hlen
            btnNeuesSpiel.setEnabled(false);

            // Alles was vorher ausgew�hlt wurde, wird gel�scht
            groupPuzzleArt.clearSelection();
            groupFarben.clearSelection();
            groupGroesse.clearSelection();
            groupBilder.clearSelection();

            // panelPuzzle wird aktualisiert
            panelPuzzle.revalidate();
            panelPuzzle.repaint();

            // Alle Radiobuttons von den 4 Buttongroups werden aktiviert
            Enumeration<AbstractButton> enumeration = groupFarben.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }

            enumeration = groupGroesse.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }

            enumeration = groupPuzzleArt.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }

            enumeration = groupBilder.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(true);
            }
        }
    }

    // Spieler dr�ckt auf JButton "Neues Spiel"
    private void clickNeuesSpiel()
    {
        // Spieler hat gerade ein neues Spiel angefangen
        if (dieSteuerung.getZustand() == "init")
        {
            // Spieler hat Zahlenpuzzle ausgew�hlt
            if (aTyp == 'z')
            {
                panelPuzzle.removeAll();

                // Das Puzzlefeld wird mit der ausgew�hlten Gr��e erstellt
                initPuzzleFeld(aGroesse);

                // Die ausgew�hlte Farbe wird eingestellt
                setzeTastenFarbe(aFarbe);

                // Spiel f�ngt an
                dieSteuerung.neuesSpiel(aTyp, aGroesse);

                // panelPuzzle wird aktualisiert
                panelPuzzle.revalidate();
                panelPuzzle.repaint();
            }

            else
                // Spieler hat Bilderpuzzle ausgew�hlt
                if (aTyp == 'b')
                {
                    // Schrittweite wird berechnet
                    int schritt = panelPuzzle.getWidth() / aGroesse;

                    // Z�hler wird deklariert
                    int count = 0;

                    // Liste mit den Bilderteile wird erstellt
                    bildTeile = new Image[aGroesse * aGroesse];

                    // Bild wird in aGroesse * aGroesse St�cke geteilt
                    for (int i = 0; i < panelPuzzle.getWidth(); i += schritt)
                    {
                        for (int j = 0; j < panelPuzzle.getHeight(); j += schritt)
                        {
                            // Wenn das letzte Teil erreicht wird
                            if (j == panelPuzzle.getHeight() - schritt
                                    && i == panelPuzzle.getWidth() - schritt)
                            {
                                // Das letzte Bildteil wird wegen der
                                // Leertaste nicht gespeichert
                                bildTeile[count] = null;
                            }
                            // Sonst wird das Bildteil gespeichert
                            else
                            {
                                // Bild wird mithilfe von j und i geteilt
                                // schritt ist die L�nge und Breite des Teils
                                bildTeile[count] = createImage(new FilteredImageSource(
                                        geaendertesBild.getSource(),
                                        new CropImageFilter(j, i, schritt,
                                                schritt)));
                            }
                            // Z�hler wird inkrementiert
                            count++;
                        }
                    }

                    panelPuzzle.removeAll();

                    // Das Puzzlefeld wird mit der ausgew�hlten Gr��e erstellt
                    initPuzzleFeld(aGroesse);

                    // Spiel f�ngt an
                    dieSteuerung.neuesSpiel(aTyp, aGroesse);

                    // panelPuzzle wird aktualisiert
                    panelPuzzle.revalidate();
                    panelPuzzle.repaint();

                    // Fenster mit Referenzbild wird ge�ffnet
                    oeffneBild(dateiPfad);
                }

            // Alle Radionbuttons von den 4 Buttongroups werden deaktiviert
            Enumeration<AbstractButton> enumeration = groupFarben.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }

            enumeration = groupGroesse.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }

            enumeration = groupPuzzleArt.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }

            enumeration = groupBilder.getElements();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement().setEnabled(false);
            }
        }
        else
            // Spieler hat gerade ein Spiel verloren oder gewonnen und dr�ckt
            // auf "Neues Spiel" -> Neues Spiel startet mit den Einstellungen
            // vom letzten Spiel
            if (dieSteuerung.getZustand() == "won"
                    || dieSteuerung.getZustand() == "lost")
            {
                // Neues Spiel startet ohne die vorherigen Einstellungen zu
                // �ndern
                dieSteuerung.neuesSpiel(aTyp, aGroesse);

                // panelPuzzle wird aktualisiert
                panelPuzzle.revalidate();
                panelPuzzle.repaint();
            }
    }

    // Der gedr�ckte JButton wird an die Steuerung weitergegeben
    private void clickTaste(JButton pTaste)
    {
        for (int i = 0; i < aTastenFeld.length; i++)
        {
            // JButton wird in der Liste aTastenFeld gesucht
            if (aTastenFeld[i] == pTaste)
            {
                // Sein Index wird an die Steuerung weitergegeben
                dieSteuerung.tastenKlick(i);
            }
        }
    }

    // Aktualisierung der restlichen Klicks auf der GUI
    public void schreibeRestKlicks(int pRestKlicks)
    {
        if (pRestKlicks == -1) // Wenn sich das Spiel im Zustand "init" befindet
            lblRestKlicks.setText("");
        else
            lblRestKlicks.setText(Integer.toString(pRestKlicks));
    }

    // Spielzustand wird auf die GUI angezeigt
    public void melde(int pTextNr)
    {
        switch (pTextNr)
        {
            case 0:
                lblMeldungsText
                        .setText("Art, Farbe, Gr��e w�hlen; dann Spiel starten");
                break;

            case 1:
                lblMeldungsText
                        .setText("Taste durch Klicken mit Leertaste tauschen");
                break;

            case 2:
                lblMeldungsText.setText("Verloren - Puzzle wurde nicht gel�st");
                break;

            case 3:
                lblMeldungsText.setText("Gewonnen - Puzzle wurde gel�st");
                break;
        }
    }

    // JButtons werden durchnummeriert
    public void setzeTastenZahlen(int[] zahlenFeld)
    {
        for (int i = 0; i < zahlenFeld.length; i++)
        {
            // Nicht Leertaste
            if (zahlenFeld[i] != -1)
            {
                // JButton bekommt eine Zahl
                aTastenFeld[i].setText(Integer.toString(zahlenFeld[i]));
            }
            // Bei Leertaste
            else
            {
                // JButton bekommt keine Zahl
                aTastenFeld[i].setText("");
            }
        }
    }

    // Zahlen der JButtons bekommen eine Farbe
    private void setzeTastenFarbe(char pFarbe)
    {
        for (int i = 0; i < aTastenFeld.length; i++)
        {
            if (pFarbe == 'r') // rot
            {
                aTastenFeld[i].setForeground(Color.red);
            }
            else
            if (pFarbe == 'g') // gr�n
            {
                aTastenFeld[i].setForeground(Color.green);
            }
            else
            if (pFarbe == 'b') // blau
            {
                aTastenFeld[i].setForeground(Color.blue);
            }
        }
    }

    // Initialisierung des Puzzlefeldes
    private void initPuzzleFeld(int pGroesse)
    {
        // L�nge der Liste aTastenFeld wird berechnet
        int anzahlTasten = pGroesse * pGroesse;

        // Berechnung der L�nge und Breite eines JButtons
        int kantenLaenge = groessePanelPuzzle / pGroesse;

        // Erstellung der Liste mit den JButtons
        aTastenFeld = new JButton[anzahlTasten];

        // Schleife durch die Liste aTastenFeld
        for (int i = 0; i < anzahlTasten; i++)
        {
            // index ist final, damit es im ActionListener von unten sichtbar
            // ist
            final int index = i;

            // Berechnung der x und y Koordinaten des JButtons
            int x = i % pGroesse;
            int y = i / pGroesse;

            aTastenFeld[i] = new JButton("");
            // Jeder JButton bekommt einen ActionListener, der beim Dr�cken
            // ausgef�hrt wird
            aTastenFeld[i].addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    // Zahlenpuzzle
                    if (aTyp == 'z')
                    {
                        // Wenn JButton nicht die Leertaste ist
                        if (aTastenFeld[index].getText() != "")
                        {
                            // Dann wird der Klick erkannt; Beim Dr�cken der
                            // Leertaste sollte nichts passieren
                            clickTaste(aTastenFeld[index]);
                        }
                    }
                    else
                        // Bilderpuzzle
                        if (aTyp == 'b')
                        {
                            // Wenn JButton nicht die Leertaste ist
                            if (aTastenFeld[index].getIcon().getIconWidth() != -1)
                            {
                                // Dann wird der Klick erkannt; Beim Dr�cken der
                                // Leertaste sollte nichts passieren
                                clickTaste(aTastenFeld[index]);
                            }
                        }
                }
            });
            aTastenFeld[i].setFont(new Font("Tahoma", Font.PLAIN, 100));

            // Mithilfe von den obenberechneten x und y, wird die Position des
            // JButtons auf dem panelPuzzle berechnet
            aTastenFeld[i].setBounds(x * kantenLaenge, y * kantenLaenge,
                    kantenLaenge, kantenLaenge);

            // JButton wird auf den panelPuzzle gezeichnet
            panelPuzzle.add(aTastenFeld[i]);
        }
    }

    // Radiobuttons der Farbenauswahl werden aktiviert
    private void farbenAuswahlAktivieren()
    {
        rdbtnRot.setEnabled(true);
        rdbtnGrn.setEnabled(true);
        rdbtnBlau.setEnabled(true);
    }

    // Radiobuttons der Farbenauswahl werden deaktiviert
    private void farbenAuswahlDeaktivieren()
    {
        aFarbe = 'x'; // Zur�cksetzung von aFarbe
        groupFarben.clearSelection();

        rdbtnRot.setEnabled(false);
        rdbtnGrn.setEnabled(false);
        rdbtnBlau.setEnabled(false);
    }

    // Bilderteile werden auf den JButtons gezeichnet
    public void setzeTastenBilder(Image[] pBilderFeld)
    {
        for (int i = 0; i < pBilderFeld.length; i++)
        {
            // Wenn Bildteil nicht der Leertaste entspricht
            if (pBilderFeld[i] != null)
            {
                // Dann zeichne das Bildteil auf den JButton
                aTastenFeld[i].setIcon(new ImageIcon(pBilderFeld[i]));
            }
            // Wenn Bild der Leertaste entspricht, dann bekommt der entprechende
            // JButton kein Bild
            else
            {
                aTastenFeld[i].setIcon(new ImageIcon());
            }
        }
    }

    // Getter
    public Image[] getBildTeile()
    {
        return bildTeile;
    }
}
