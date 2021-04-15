package View.EastGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import Model.player.Player;
import Model.player.PlayerList;
import Model.Tiles.Property;
import gamehistorylog.GameHistoryLog;

/**
 * @author Muhammad Abdulkhuder Aevan Dino sebastian Viro.
 */
public class PlayerProperties extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JLabel lblName = new JLabel("Name");
    private JLabel lblPicture = new JLabel("");
    private JLabel lblRent = new JLabel("Rent");
    private JLabel lblRentPerLevel = new JLabel("Rent Per Level");
    private JTextArea taLevel = new JTextArea("");
    private JButton btnUpgrade = new JButton("Upgrade");
    private JButton btnDowngrade = new JButton("Downgrade");
    private JButton btnTrade = new JButton("Trade");
    private JButton btnSell = new JButton("Sell");

    // Lagt till font -------------
    private File algerianTtfFile = new File("fonts/Algerian.ttf");
    private Font fontAlgerian;
    {
        try {
            fontAlgerian = Font.createFont(Font.TRUETYPE_FONT, algerianTtfFile);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Font font = fontAlgerian.deriveFont(Font.BOLD, 22);
    //new Font("ALGERIAN", Font.BOLD, 22);
    private Font fontLevel = fontAlgerian.deriveFont(Font.BOLD, 50);
    //new Font("ALGERIAN", Font.BOLD, 50);
    //-----------------------

    private String plus = "+";
    private PlayerList playerList;
    //private int playerAtI, propertyAtI;
    private EastSidePanel eastSidePanel;
    private Property property;
    private Player player;

    public PlayerProperties(Player player, Property property, PlayerList playerList) {
        this.eastSidePanel = eastSidePanel;

        buildPanel(player, property, playerList, false);

    }
    /**
     * @param playerList
     * @param playerAtI
     * @param propertyAtI
     */
    public PlayerProperties(PlayerList playerList, int playerAtI, int propertyAtI, EastSidePanel eastSidePanel) {
        this.eastSidePanel = eastSidePanel;
        Player player = playerList.getPlayerFromIndex(playerAtI);
        Property property = player.getProperties().get(propertyAtI);

        buildPanel(player, property, playerList, true);

    }

    private void buildPanel(Player player, Property property, PlayerList playerList, boolean showActionButtons) {

        this.player = player;
        this.property = property;
        this.playerList = playerList;

        setBorder(null);
        setOpaque(false);
        setBackground(Color.DARK_GRAY);
        int preferredHeight = showActionButtons ? 647 : 547;
        setPreferredSize(new Dimension(330, preferredHeight));
        setLayout(null);

        lblRent.setForeground(Color.white);
        lblRentPerLevel.setForeground(Color.white);
        lblRent.setText(
                "The Rent is: " + property.getTotalRent());
        lblRentPerLevel.setText("The rent per level : "
                + property.getRentPerLevel());
        lblRent.setFont(font);
        lblRentPerLevel.setFont(font);

        lblRent.setBounds(0, 92, 330, 64);
        add(lblRent);
        lblRentPerLevel.setBounds(0, 140, 330, 64);
        add(lblRentPerLevel);

        taLevel.setEditable(false);
        taLevel.setBounds(46, 38, 263, 53);
        taLevel.setOpaque(false);
        taLevel.setFont(fontLevel);
        taLevel.setForeground(Color.white);
        updateLevels(property);
        add(taLevel);

        lblName.setForeground(Color.white);
        lblName.setOpaque(false);
        lblName.setText(property.getName());

        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setBounds(0, 11, 330, 46);
        add(lblName);
        lblName.setFont(font);
        lblPicture.setForeground(Color.WHITE);

        lblPicture.setBorder(null);
        lblPicture.setBounds(0, 0, 330, 480);
        add(lblPicture);

        lblPicture.setFont(font);
        lblPicture.setOpaque(true);
        btnDowngrade.setFont(font);
        btnUpgrade.setFont(font);
        btnTrade.setFont(font);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(
                    property.getPicture().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image resizedImg = img.getScaledInstance(lblPicture.getWidth(), lblPicture.getHeight(), Image.SCALE_SMOOTH);

        lblPicture.setIcon(new ImageIcon(resizedImg));

        if (showActionButtons) {
            btnDowngrade.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
            btnDowngrade.setBounds(163, 481, 167, 53);
            add(btnDowngrade);

            btnUpgrade.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
            btnUpgrade.setBounds(0, 481, 167, 53);
            add(btnUpgrade);

            btnTrade.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
            btnTrade.setBounds(163, 532, 167, 46);
            add(btnTrade);

            btnSell.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
            btnSell.setBounds(0, 532, 167, 46);
            btnSell.setForeground(Color.red);
            add(btnSell);
            btnSell.setFont(font);

            btnUpgrade.addActionListener(this);
            btnDowngrade.addActionListener(this);
            btnSell.addActionListener(this);
            btnTrade.addActionListener(this);
        } else {
            //TODO This is a temporary solution
            JLabel lblPurchase = new JLabel("Do you want to purchase " + property.getName() + "?");
            lblPurchase.setLocation(0, 481);
            lblPurchase.setBounds(0, 481, 330, 50);
            add(lblPurchase);
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSell) {
            eastSidePanel.sellProperty(this.property);
        } else if (e.getSource() == btnUpgrade) {
            eastSidePanel.upgradeProperty(this.property);
        } else if (e.getSource() == btnDowngrade) {
            eastSidePanel.downgradeProperty(this.property);
        } else if (e.getSource() == btnTrade) {
            int otherPlayerInt = 0;
            int whichPropertyToGive = 0;
            int whichPropertyYouWant = 0;
            int offer = 0;
            int type = 0;
            int confirm;

            do {
                otherPlayerInt = (Integer
                        .parseInt(JOptionPane.showInputDialog(null,
                                "Which player do you want to trade with?\n 1 for player 1 \n 2 for player 2 and so on..."))
                        - 1);
            } while (otherPlayerInt == playerList.getActivePlayer().getPlayerIndex() || otherPlayerInt > playerList.getLength() - 1);

            Player activePlayer = playerList.getActivePlayer();
            Player otherPlayer = playerList.getPlayerFromIndex(otherPlayerInt);

            if (otherPlayer.getProperties().size() > 0) {
                do {
                    type = (Integer.parseInt(JOptionPane.showInputDialog(null,
                            "Pick a trade type\n 1 = Property for property \n 2 = Money for property\n 3 = Both")));
                } while (type < 0 || type > 3);

                if (type == 1 || type == 3) {

                    do {
                        whichPropertyToGive = (Integer.parseInt(JOptionPane.showInputDialog(null,
                                "Which property do you want to give away \n 1 for property 1 \n 2 for property 2 and so on..."))
                                - 1);
                    } while (whichPropertyToGive > activePlayer.getProperties().size());
                }

                if (type == 2 || type == 3) {
                    do {
                        offer = (Integer.parseInt(JOptionPane.showInputDialog(null,
                                "How much do you offer " + otherPlayer.getName() + "?")));
                    } while (offer > activePlayer.getBalance());
                }

                do {
                    whichPropertyYouWant = (Integer.parseInt(JOptionPane.showInputDialog(null,
                            "Which property do you want \n 1 for property 1 \n 2 for property 2 and so on...")) - 1);
                } while (whichPropertyYouWant > otherPlayer.getProperties().size());


                Property activePlayerProperty = playerList.getActivePlayer().getPropertyAt(whichPropertyToGive);
                Property otherPlayersProperty = playerList.getPlayerFromIndex(otherPlayerInt)
                        .getPropertyAt(whichPropertyYouWant);

                if (type == 1) {
                    confirm = JOptionPane.showConfirmDialog(null,
                            otherPlayer.getName() + " Are you okay with this trade?" + "\n You are getting "
                                    + activePlayerProperty.getName() + "\n and are trading away " + otherPlayersProperty.getName());

                    if (confirm == 0) {

                        activePlayer.removeProperty(activePlayerProperty);
                        otherPlayer.removeProperty(otherPlayersProperty);

                        activePlayerProperty.setOwner(otherPlayer);
                        activePlayer.addNewProperty(otherPlayersProperty);

                        otherPlayersProperty.setOwner(activePlayer);
                        otherPlayer.addNewProperty(activePlayerProperty);

                        JOptionPane.showMessageDialog(null, "Trade Complete! Omedato gosaimasu!!!");
                        GameHistoryLog.instance().logTradeEventProperty(activePlayer, otherPlayer, activePlayerProperty, otherPlayersProperty);
                    }
                }

                if (type == 2) {
                    confirm = JOptionPane.showConfirmDialog(null, otherPlayer.getName() + " Are you okay with this trade?"
                            + "\n You are getting " + offer + "Gold coins for " + otherPlayersProperty.getName());

                    if (confirm == 0) {

                        otherPlayer.removeProperty(otherPlayersProperty);
                        activePlayerProperty.setOwner(otherPlayer);
                        activePlayer.addNewProperty(otherPlayersProperty);

                        activePlayer.decreaseBalance(offer);
                        activePlayer.decreaseNetWorth(offer);

                        otherPlayer.increaseBalance(offer);
                        otherPlayer.increaseNetWorth(offer);

                        JOptionPane.showMessageDialog(null, "Trade Complete! Omedato gosaimasu!!!");
                        GameHistoryLog.instance().logTradeEventGold(activePlayer, otherPlayer, otherPlayersProperty, offer);

                    }
                }
                if (type == 3) {
                    confirm = JOptionPane.showConfirmDialog(null,
                            otherPlayer.getName() + " Are you okay with this trade?" + "\n You are getting " + offer
                                    + " Gold coins" + "\n and are trading away " + otherPlayersProperty.getName() + "\n for "
                                    + activePlayerProperty.getName());

                    if (confirm == 0) {

                        activePlayer.removeProperty(activePlayerProperty);
                        otherPlayer.removeProperty(otherPlayersProperty);

                        activePlayer.decreaseBalance(offer);
                        activePlayer.decreaseNetWorth(offer);

                        otherPlayer.increaseBalance(offer);
                        otherPlayer.increaseNetWorth(offer);

                        activePlayerProperty.setOwner(otherPlayer);
                        activePlayer.addNewProperty(otherPlayersProperty);

                        otherPlayersProperty.setOwner(activePlayer);
                        otherPlayer.addNewProperty(activePlayerProperty);

                        JOptionPane.showMessageDialog(null, "Trade Complete! Omedato gosaimasu!!!");
                        GameHistoryLog.instance().logTradeEventGoldAndProperty(activePlayer, otherPlayer, activePlayerProperty, otherPlayersProperty, offer);
                    }
                }
                    eastSidePanel.tradeProperty();
                } else {
                    JOptionPane.showMessageDialog(null, "Trade can not be done! The player you picked does not own any properties!");
                }
            }
        }

        /**
         * @param playerList
         * @param playerIndex
         * @param propertyIndex updates levels shown adds a plus to the picture
         */
        public void updateLevels(PlayerList playerList, int playerIndex, int propertyIndex) {
            Property property = playerList.getPlayerFromIndex(playerIndex).getPropertyAt(propertyIndex);
            updateLevels(property);
        }

        public void updateLevels(Property property) {
            int lvl = property.getLevel();
            for (int i = 0; i < lvl; i++) {
                taLevel.append(plus);
            }
        }
    }