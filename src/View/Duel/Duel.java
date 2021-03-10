package View.Duel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import Controller.Controller;
import Model.player.Player;
import Model.player.PlayerList;
import soundservice.SoundFx;
import soundservice.SoundService;

public class Duel extends JFrame {
    private enum Weapon {
        SWORD, SHIELD, BOW;
    }

    private JPanel panel1;
    private JLabel playerOneImage;
    private JLabel playerTwoImage;
    private JLabel winnerLabel;
    private final ImageIcon shieldIcon;
    private final ImageIcon swordIcon;
    private final ImageIcon bowIcon;
    private final ImageIcon backgroundIcon;
    private final ImageIcon helmetIcon;
    private boolean playerOnePressed;
    private boolean playerTwoPressed;
    private Weapon playerOneSelection;
    private Weapon playerTwoSelection;
    private static final char P1_SHIELD = 'a';
    private static final char P1_SWORD = 's';
    private static final char P1_BOW = 'd';
    private static final char P2_SHIELD = 'j';
    private static final char P2_SWORD = 'k';
    private static final char P2_BOW = 'l';
    private final Map<Weapon, ImageIcon> weaponToImage;
    private final Map<Character, Weapon> selectedWeapon;
    private final Player playerOne;
    private final Player playerTwo;
    private final Controller controller;

    public Duel(Player playerOne, Player playerTwo, Controller controller) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.controller = controller;
        this.helmetIcon = openAndScaleImage("images/duel/helmet.jpg", 200, 200);
        this.backgroundIcon = openAndScaleImage("images/duel/background.jpg", 800, 600);
        this.shieldIcon = openAndScaleImage("images/duel/shield.jpg", 200, 200);
        this.swordIcon = openAndScaleImage("images/duel/sword.jpg", 200, 200);
        this.bowIcon = openAndScaleImage("images/duel/bow.jpg", 200, 200);

        weaponToImage = Map.of(
                Weapon.SWORD, this.swordIcon,
                Weapon.SHIELD, this.shieldIcon,
                Weapon.BOW, this.bowIcon
        );
        selectedWeapon = Map.of(
                P1_SHIELD, Weapon.SHIELD,
                P2_SHIELD, Weapon.SHIELD,
                P1_SWORD, Weapon.SWORD,
                P2_SWORD, Weapon.SWORD,
                P1_BOW, Weapon.BOW,
                P2_BOW, Weapon.BOW
        );

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                var pressedChar = e.getKeyChar();

                if (!playerOnePressed && (pressedChar == P1_SHIELD || pressedChar == P1_SWORD || pressedChar == P1_BOW)) {
                    playerOnePressed = true;
                    playerOneSelection = selectedWeapon.get(pressedChar);
                    playerOneImage.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
                } else if (!playerTwoPressed && (pressedChar == P2_SHIELD || pressedChar == P2_SWORD || pressedChar == P2_BOW)) {
                    playerTwoPressed = true;
                    playerTwoSelection = selectedWeapon.get(pressedChar);
                    playerTwoImage.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
                }

                if (playerOnePressed && playerTwoPressed) {
                    checkWinner();
                }
            }
        });

        initializeComponents();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();
        this.setVisible(true);

        SoundService.instance().playSoundFx(SoundFx.SOUND_DEFEND);

    }

    private void initializeComponents() {
        panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setPreferredSize(new Dimension(800, 600));

        var background = new JLabel(backgroundIcon);
        background.setBounds(0, 0, 800, 600);
        background.setLayout(null);
        panel1.add(background);

        playerOneImage = new JLabel(helmetIcon);
        playerOneImage.setBounds(80, 300, 200, 200);
        background.add(playerOneImage);

        playerTwoImage = new JLabel(helmetIcon);
        playerTwoImage.setBounds(520, 300, 200, 200);
        background.add(playerTwoImage);

        var playerOneInfo = new JLabel();
        playerOneInfo.setText(playerOne.getName());
        playerOneInfo.setBackground(Color.WHITE);
        playerOneInfo.setOpaque(true);
        playerOneInfo.setBounds(80, 250, 200, 40);
        playerOneInfo.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        background.add(playerOneInfo);

        var playerTwoInfo = new JLabel();
        playerTwoInfo.setText(playerTwo.getName());
        playerTwoInfo.setBackground(Color.WHITE);
        playerTwoInfo.setOpaque(true);
        playerTwoInfo.setBounds(520, 250, 200, 40);
        playerTwoInfo.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        background.add(playerTwoInfo);

        var playerOneKeyboardInfo = infoPanel(P1_SHIELD, P1_SWORD, P1_BOW);
        playerOneKeyboardInfo.setBounds(0, 350, 70, 150);
        background.add(playerOneKeyboardInfo);

        var playerTwoKeyboardInfo = infoPanel(P2_SHIELD, P2_SWORD, P2_BOW);
        playerTwoKeyboardInfo.setBounds(730, 350, 70, 150);
        background.add(playerTwoKeyboardInfo);

        winnerLabel = new JLabel();
        winnerLabel.setBackground(Color.WHITE);
        winnerLabel.setBounds(300, 300, 200, 200);
        winnerLabel.setOpaque(true);
        winnerLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        winnerLabel.setVisible(false);
        background.add(winnerLabel);
    }

    private JPanel infoPanel(char firstLetter, char secondLetter, char thirdLetter) {
        var panel = new JPanel();
        panel.setLayout(null);
        var label1 = letterAndImage(firstLetter);
        label1.setBounds(0, 0, 70, 40);
        panel.add(label1);
        var label2 = letterAndImage(secondLetter);
        label2.setBounds(0, 50, 70, 40);
        panel.add(label2);
        var label3 = letterAndImage(thirdLetter);
        label3.setBounds(0, 100, 70, 40);
        panel.add(label3);
        panel.setBackground(Color.white);
        return panel;
    }

    private JLabel letterAndImage(char letter) {
        var label = new JLabel();
        label.setLayout(null);
        var letterLabel = letterLabel(letter);
        var imageLabel = imageLabel(letter);
        label.add(letterLabel);
        label.add(imageLabel);
        letterLabel.setBounds(0, 0, 30, 40);
        imageLabel.setBounds(30, 0, 40, 40);
        return label;
    }

    private JLabel letterLabel(char letter) {
        var label = new JLabel(String.valueOf(letter));
        label.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        return label;
    }

    private JLabel imageLabel(char letter) {
        var weapon = selectedWeapon.get(letter);
        var image = weaponToImage.get(weapon);
        var scaledImage = scaleImage(image, 40, 40);
        return new JLabel(scaledImage);
    }

    private void checkWinner() {
        playerOneImage.setIcon(weaponToImage.get(playerOneSelection));
        playerTwoImage.setIcon(weaponToImage.get(playerTwoSelection));

        if (playerOneSelection == playerTwoSelection) {
            draw();
        } else if (playerOneSelection == Weapon.SWORD && playerTwoSelection == Weapon.SHIELD) {
            winner(playerOne, playerTwo);
        } else if (playerOneSelection == Weapon.SWORD && playerTwoSelection == Weapon.BOW) {
            winner(playerTwo, playerOne);
        } else if (playerOneSelection == Weapon.SHIELD && playerTwoSelection == Weapon.SWORD) {
            winner(playerTwo, playerOne);
        } else if (playerOneSelection == Weapon.SHIELD && playerTwoSelection == Weapon.BOW) {
            winner(playerOne, playerTwo);
        } else if (playerOneSelection == Weapon.BOW && playerTwoSelection == Weapon.SWORD) {
            winner(playerOne, playerTwo);
        } else if (playerOneSelection == Weapon.BOW && playerTwoSelection == Weapon.SHIELD) {
            winner(playerTwo, playerOne);
        } else {
            throw new RuntimeException("I forgot something in checkWinner..." + playerOneSelection + " " + playerTwoSelection);
        }
    }

    private void winner(Player winner, Player loser) {
        winnerLabel.setVisible(true);
        winnerLabel.setText(winner.getName() + "\n" + " won!");

        SwingUtilities.invokeLater(() -> {
            try {
                SoundService.instance().playSoundFx(SoundFx.SOUND_WAR_NOT_OVER);
                Thread.sleep(3000);
                this.dispose();
                controller.duelWinner(winner, loser);
            } catch (InterruptedException ignore) {
            }
        });
    }

    private void draw() {
        winnerLabel.setText("Draw!");
        winnerLabel.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            try {
                SoundService.instance().playSoundFx(SoundFx.SOUND_AWKWARD);
                Thread.sleep(3000);
                restartGame();
            } catch (InterruptedException ignore) {
            }
        });
    }

    private void restartGame() {
        playerOnePressed = false;
        playerTwoPressed = false;
        playerOneImage.setBorder(null);
        playerTwoImage.setBorder(null);
        playerOneImage.setIcon(helmetIcon);
        playerTwoImage.setIcon(helmetIcon);
        winnerLabel.setVisible(false);
    }

    private ImageIcon openAndScaleImage(String filename, int x, int y) {
        var icon = new ImageIcon(filename);
        return scaleImage(icon, x, y);
    }

    private ImageIcon scaleImage(ImageIcon icon, int x, int y) {
        var image = icon.getImage();
        var scaledImage = image.getScaledInstance(x, y, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static void main(String[] args) {
        var playerList = new PlayerList();
        playerList.addNewPlayer("Johan", "RED");
        playerList.addNewPlayer("Hanna", "GREEN");
        var d = new Duel(playerList.getPlayerFromIndex(0), playerList.getPlayerFromIndex(1), null);
    }
}