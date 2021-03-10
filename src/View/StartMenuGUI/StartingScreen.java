package View.StartMenuGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.CompletableFuture;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import View.BoardGUI.GamePanels;
import Model.player.PlayerList;
import soundservice.SoundService;

/**
 * First screen which Model.player sees, here he is able to choose the amount of players and
 * what names and colors the players will have during the game.
 *
 * @author Aevan Dino
 */
public class StartingScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private PlayerList playerList = new PlayerList();
    GamePanels mainWindow;
    private JButton btnConfirm = new JButton("Confirm");
    private JButton btnStartGame = new JButton("Start Game");
    private JButton btnReset = new JButton("Reset");

    private ImageIcon imgBackground = new ImageIcon(
            new ImageIcon("images/fancyRoll.jpg").getImage().getScaledInstance(900, 860, Image.SCALE_SMOOTH));

    private Font fontRadioButtons = new Font("Gabriola", Font.PLAIN, 24);
    private Font fontHeader = new Font("Gabriola", Font.BOLD, 92);
    private Font fontLabel = new Font("Gabriola", Font.BOLD, 42);
    private Font fontLabelPlayer = new Font("Gabriola", Font.BOLD, 30);

    private JPanel pnlPlayerInfo = new JPanel();

    private JRadioButton[] radioButtons = new JRadioButton[4];
    private ButtonGroup btnGroup = new ButtonGroup();

    private JLabel lblPlayer = new JLabel("How many players?");
    private JPanel pnlMain = new JPanel();
    private JLabel lblBackground = new JLabel("", imgBackground, JLabel.CENTER);
    private JLabel lblRise = new JLabel("RISE");

    private JLabel lblPlayerIndex1 = new JLabel("Player 1:");
    private JLabel lblPlayerIndex2 = new JLabel("Player 2:");
    private JLabel lblPlayerIndex3 = new JLabel("Player 3:");
    private JLabel lblPlayerIndex4 = new JLabel("Player 4:");
    private JLabel[] playerLabels = new JLabel[]{lblPlayerIndex1, lblPlayerIndex2, lblPlayerIndex3, lblPlayerIndex4};

    private JTextField tfPlayer1 = new JTextField("Name1...");
    private JTextField tfPlayer2 = new JTextField("Name2...");
    private JTextField tfPlayer3 = new JTextField("Name3...");
    private JTextField tfPlayer4 = new JTextField("Name4...");
    private JTextField[] playerTf = new JTextField[]{tfPlayer1, tfPlayer2, tfPlayer3, tfPlayer4};

    private String[] colors = new String[]{"RED", "GREEN", "ORANGE", "YELLOW", "CYAN", "MAGENTA"};
    private String[] colors1 = new String[]{"GREEN", "ORANGE", "YELLOW", "CYAN", "MAGENTA", "RED"};
    private String[] colors2 = new String[]{"ORANGE", "YELLOW", "CYAN", "MAGENTA", "RED", "GREEN"};
    private String[] colors3 = new String[]{"YELLOW", "CYAN", "MAGENTA", "RED", "GREEN", "ORANGE"};

    private JComboBox<String> playerColors1 = new JComboBox<String>(colors);
    private JComboBox<String> playerColors2 = new JComboBox<String>(colors1);
    private JComboBox<String> playerColors3 = new JComboBox<String>(colors2);
    private JComboBox<String> playerColors4 = new JComboBox<String>(colors3);
    private JComboBox[] playerColors = new JComboBox[]{playerColors1, playerColors2, playerColors3, playerColors4};

    private CompletableFuture<PlayerList> retrievePlayerList;

    /**
     * Mute button
     */
    private JCheckBox mute = new JCheckBox("Music On");

    /**
     * Integers
     */
    private int amountOfPlayers;

    public StartingScreen(CompletableFuture<PlayerList> retrievePlayerList) {
        this.retrievePlayerList = retrievePlayerList;
    }

    public StartingScreen() {
    }

    /**
     * Method to initilize the View.GUI.
     */
    public void initializeGUI() {

        createFrame();

        /**
         * Label used to create a background
         */
        pnlMain.setBounds(0, 0, 900, 830);
        pnlMain.setLayout(null);

        /**
         * JPanel for information about players
         */
        pnlPlayerInfo.setBounds(0, 0, 900, 830);
        pnlPlayerInfo.setOpaque(false);
        pnlPlayerInfo.setLayout(null);

        /**
         * Label used to create a background
         */
        lblBackground.setBounds(0, 0, 900, 830);
        lblBackground.setIcon(imgBackground);
        lblBackground.setLayout(null);

        /**
         *  Header reading "RISE"
         */
        lblRise.setFont(fontHeader);
        lblRise.setBounds(375, 125, 175, 200);
        pnlMain.add(lblRise);

        /**
         * JLabel reading "How many players?"
         */
        lblPlayer.setFont(fontLabel);
        lblPlayer.setBounds(315, 175, 300, 200);

        /**
         * Create three JRadioButtons
         */
        createRadioButtons();

        /**
         * Confirm button
         */
        btnConfirm.setBounds(375, 315, 150, 30);
        btnConfirm.addActionListener(new ButtonListener());

        /**
         * Create players
         */
        CreatePlayers();

        /**
         * Start game button
         */
        btnStartGame.setBounds(350, 530, 200, 40);
        btnStartGame.setVisible(false);
        btnStartGame.addActionListener(new ButtonListener());

        /**
         * Rest button
         */
        btnReset.setBounds(375, 575, 150, 30);
        btnReset.setVisible(false);
        btnReset.addActionListener(new ButtonListener());

        /**
         * Mute button
         */
        mute.setBounds(2, 740, 150, 35);
        mute.setForeground(Color.white);
        mute.setFont(fontRadioButtons);
        mute.setOpaque(false);
        mute.addActionListener(new ButtonListener());

        /**
         * Adding stuff to background label
         */
        pnlMain.add(lblRise);
        pnlMain.add(lblPlayer);
        pnlMain.add(btnConfirm);
        pnlMain.add(pnlPlayerInfo);
        pnlMain.add(btnReset);
        pnlMain.add(btnStartGame);
        pnlMain.add(mute);
        pnlMain.add(lblBackground);
        add(pnlMain);

        pnlMain.validate();
        pnlMain.repaint();
        this.validate();
        this.repaint();
    }

    public void createFrame() {
        setSize(900, 830);
        setTitle("Choose Player!");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Methods create radio buttons using for-loop
     */
    public void createRadioButtons() {
        for (int i = 0; i < 3; i++) {
            JRadioButton btnRadio = new JRadioButton((i + 2) + "");
            btnRadio.setBounds(375 + i * 65, 275, 50, 50);
            btnRadio.setFont(fontRadioButtons);
            btnRadio.setOpaque(false);
            btnGroup.add(btnRadio);
            radioButtons[i] = btnRadio;
            add(btnRadio);
        }
    }

    /**
     * Creates all players, textfields, labels and color choice boxes
     */
    public void CreatePlayers() {
        for (int i = 0; i < 4; i++) {
            playerLabels[i].setBounds(280, 360 + i * 40, 150, 50);
            playerLabels[i].setFont(fontLabelPlayer);
            playerLabels[i].setVisible(false);

            playerTf[i].setBounds(375, 360 + i * 40, 150, 30);
            playerTf[i].setVisible(false);
            playerTf[i].addMouseListener(new MouseAction());

            playerColors[i].setBounds(530, 360 + i * 40, 100, 30);
            playerColors[i].setVisible(false);

            pnlPlayerInfo.add(playerLabels[i]);
            pnlPlayerInfo.add(playerTf[i]);
            pnlPlayerInfo.add(playerColors[i]);
        }
    }

    /**
     * Buttonlistener class, listens for clicks.
     *
     * @author Aevan Dino
     */
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnReset) {
                btnPressed(false);
            }

            if (e.getSource() == mute) {
                if (mute.getText().contains("n")) {
                    mute.setText("Music Off");
                    SoundService.instance().pauseBgMusic();
                } else {
                    mute.setText("Music On");
                    SoundService.instance().playBgMusic();
                }
            }

            if (e.getSource() == btnConfirm) {

                if (radioButtons[0].isSelected()) {
                    amountOfPlayers = 2;
                    btnPressed(true);
                } else if (radioButtons[1].isSelected()) {
                    amountOfPlayers = 3;
                    btnPressed(true);

                } else if (radioButtons[2].isSelected()) {
                    amountOfPlayers = 4;
                    btnPressed(true);
                }
            }

            if (e.getSource() == btnStartGame) {

                if (tfPlayer1.getText().length() == 0 || tfPlayer2.getText().length() == 0 || tfPlayer3.getText().length() == 0 || tfPlayer4.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "All players must have a name");
                } else {

                    switch (amountOfPlayers) {
                        case 2:
                            if (playerColors[0].getSelectedItem().equals(playerColors[1].getSelectedItem())) {
                                JOptionPane.showMessageDialog(null, "Two players are not allowed to have the same color");
                            } else {
                                startUpGame();
                            }
                            break;

                        case 3:
                            if (playerColors[0].getSelectedItem().equals(playerColors[1].getSelectedItem())
                                    || playerColors[2].getSelectedItem().equals(playerColors[0].getSelectedItem())) {
                                JOptionPane.showMessageDialog(null, "Two or more players are not allowed to have the same color");
                            } else {
                                startUpGame();
                            }
                            break;

                        case 4:
                            if (playerColors[0].getSelectedItem().equals(playerColors[1].getSelectedItem())
                                    || playerColors[2].getSelectedItem().equals(playerColors[3].getSelectedItem())
                                    || playerColors[0].getSelectedItem().equals(playerColors[3].getSelectedItem())) {
                                JOptionPane.showMessageDialog(null, "Two or more players are not allowed to have the same color");
                            } else {
                                startUpGame();
                            }
                            break;
                    }
                }
            }
        }

        /**
         * Method called when Model.player clicks start game
         */
        public void startUpGame() {
            createNewUsers();
            dispose();
            if (retrievePlayerList != null) {
                retrievePlayerList.complete(playerList);
            }
        }

        /**
         * Creates the right amount of players.
         */
        private void createNewUsers() {

            for (int i = 0; i < amountOfPlayers; i++) {
                if (playerTf[i].getText().length() >= 10) {
                    playerTf[i].setText(playerTf[i].getText().substring(0, 10));
                }
                playerList.addNewPlayer(playerTf[i].getText(), (String) playerColors[i].getSelectedItem());
            }
        }

        /**
         * Whenever Model.player chooses to reset the start screen
         *
         * @param bool, boolean indicating whether or not components should be visible.
         */
        public void btnPressed(boolean bool) {
            for (int i = 0; i < amountOfPlayers; i++) {
                playerLabels[i].setVisible(bool);
                playerTf[i].setVisible(bool);
                playerColors[i].setVisible(bool);
            }
            btnStartGame.setVisible(bool);
            btnReset.setVisible(bool);
            btnConfirm.setEnabled(!bool);

        }
    }

    /**
     * MouseClickedListener for the name inserting so the text disappear when the Model.player clicks.
     */
    private class MouseAction implements MouseListener {
        int counter1 = 0, counter2 = 0, counter3 = 0, counter4 = 0;

        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == tfPlayer1) {
                if (counter1 < 1) {
                    counter1++;
                    tfPlayer1.setText(null);
                }
            }
            if (e.getSource() == tfPlayer2) {
                if (counter2 < 1) {
                    counter2++;
                    tfPlayer2.setText(null);
                }
            }
            if (e.getSource() == tfPlayer3) {
                if (counter3 < 1) {
                    counter3++;
                    tfPlayer3.setText(null);
                }
            }
            if (e.getSource() == tfPlayer4) {
                if (counter4 < 1) {
                    counter4++;
                    tfPlayer4.setText(null);
                }
            }
        }

        /**
         * Nothing will happen with the other implemented methods. Methods must be implemented by MouseListener.
         */
        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}