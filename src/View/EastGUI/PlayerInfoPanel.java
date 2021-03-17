package View.EastGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;

import Model.player.PlayerList;

/**
 * This class is used to show information about the players
 * and the current properties in tabs from property window
 *
 * @author Abdulkhuder Muhammad, Sebastian Viro.
 */
public class PlayerInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel lblName = new JLabel("");
    private JLabel lblRank = new JLabel("");
    private JLabel lblGold = new JLabel("");
    private JLabel lblNetworth = new JLabel("");
    private JLabel lblNextRank = new JLabel("");
    private JLabel lblLeftToNextRank = new JLabel("");
    private JPanel p1 = new JPanel();
    private JPanel p2 = new JPanel();
    private JPanel p3 = new JPanel();
    private JPanel p4 = new JPanel();
    private JPanel p5 = new JPanel();
    private PropertyWindow propertyWindow;
    private Font font = new Font("ALGERIAN", Font.PLAIN, 18);
    private EastSidePanel eastSidePanel;

    /**
     * @param playerList
     * @param playerNbr  Model.player list is used to get the players to display correct information
     *                   playernbr is to specify what Model.player
     */
    public PlayerInfoPanel(PlayerList playerList, int playerNbr, EastSidePanel eastSidePanel) {
        this.eastSidePanel = eastSidePanel;
        setPreferredSize(new Dimension(345, 860));
        p1.setBounds(10, 5, 330, 35);
        setBackground(Color.DARK_GRAY);
        p1.setBackground(playerList.getPlayerFromIndex(playerNbr).getPlayerColor());
        p1.setBorder(BorderFactory.createLineBorder(Color.black));
        p2.setBounds(10, 35, 330, 35);
        p2.setBorder(BorderFactory.createLineBorder(Color.black));
        p3.setBounds(10, 70, 330, 35);
        p3.setBorder(BorderFactory.createLineBorder(Color.black));
        p4.setBounds(10, 105, 330, 35);
        p4.setBorder(BorderFactory.createLineBorder(Color.black));
        p5.setBounds(10, 140, 330, 60);
        p5.setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(null);

        lblName.setText(playerList.getPlayerFromIndex(playerNbr).getName().toUpperCase());
        lblName.setFont(font);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setOpaque(false);
        lblName.setForeground(Color.white);
        lblName.setBackground(playerList.getPlayerFromIndex(playerNbr).getPlayerColor());
        p1.add(lblName);
        add(p1);

        lblGold.setText("Amount of gold: " + playerList.getPlayerFromIndex(playerNbr).getBalance());
        lblGold.setFont(font);
        lblGold.setHorizontalAlignment(SwingConstants.CENTER);
        lblGold.setForeground(Color.black);
        p2.add(lblGold);
        add(p2);

        lblNetworth.setText("Total wealth: " + playerList.getPlayerFromIndex(playerNbr).getNetWorth());
        lblNetworth.setFont(font);
        lblNetworth.setHorizontalAlignment(SwingConstants.CENTER);
        p3.add(lblNetworth);
        add(p3);

        lblRank.setText("Player Rank: " + playerList.getPlayerFromIndex(playerNbr).getPlayerRank());
        lblRank.setFont(font);
        lblRank.setHorizontalAlignment(SwingConstants.CENTER);
        p4.add(lblRank);
        add(p4);

        lblNextRank.setText("Wealth required for higher rank:");
        lblNextRank.setFont(font);
        lblNextRank.setHorizontalAlignment(SwingConstants.CENTER);
        lblLeftToNextRank.setText(playerList.getPlayerFromIndex(playerNbr).getNetWorth() + "/" + playerList.getActivePlayer().setLeftToNextRank(playerList, playerNbr));
        lblLeftToNextRank.setFont(font);
        lblLeftToNextRank.setHorizontalAlignment(SwingConstants.CENTER);
        p5.add(lblNextRank);
        p5.add(lblLeftToNextRank);
        add(p5);

        propertyWindow = new PropertyWindow(playerList, playerNbr, eastSidePanel);
        propertyWindow.setBounds(10, 210, 335, 626);
        propertyWindow.addTabs();
        add(propertyWindow);
    }
}