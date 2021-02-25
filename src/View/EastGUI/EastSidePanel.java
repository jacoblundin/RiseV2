package View.EastGUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import Controller.Controller;
import Controller.ManageEvents;
import Model.Tiles.Property;
import Model.player.PlayerList;

/**
 * this class add tabs that displays informations about the players
 * in tabs
 *
 * @author Abdulkhuder Muhammad, Sebastian Viro.
 */
public class EastSidePanel extends JPanel {
    private static final long serialVersionUID = 3397908521882247649L;
    private PlayerList playerList;
    private JTabbedPane tab;
    private int currentPlayer = 0;
    private Controller controller;

    /**
     * Draws the View.GUI
     */
    public EastSidePanel(PlayerList playerList) {
        this.playerList = playerList;

        setPreferredSize(new Dimension(345, 860));
        setOpaque(false);
        setLayout(null);
        UIManager.put("TabbedPane.contentOpaque", false);
        UIManager.put("TabbedPane.selected", Color.cyan);

        tab = new JTabbedPane();
        tab.setBounds(0, 0, 355, 860);
        tab.setBackground(new Color(0, 0, 0));
        add(tab);

        addTabs();
    }

    /**
     * this method adds tabs according to the amount of players
     */
    public void addTabs() {
        SwingUtilities.invokeLater(() -> tab.removeAll());

        for (int i = 0; i < playerList.getLength(); i++) {
            var index = i;
            SwingUtilities.invokeLater(() -> {
                PlayerInfoPanel playerInfoPnl = new PlayerInfoPanel(playerList, index, this);
                playerInfoPnl.setOpaque(false);
                tab.addTab("Player " + (index + 1), playerInfoPnl);
                tab.setOpaque(false);
            });
        }

        SwingUtilities.invokeLater(() -> {
            tab.setSelectedIndex(currentPlayer);
            tab.setForeground(Color.white);
            tab.setBackground(new Color(157, 0, 0));
            tab.setBackgroundAt(currentPlayer, new Color(0, 157, 0));
        });
    }

    /**
     * this method is used to display the correct color
     * the active players turn should be green and the others should be red.
     */
    public void setTab() {
        tab.setBackgroundAt(currentPlayer, null);

        currentPlayer++;
        if (currentPlayer > playerList.getLength() - 1) {
            currentPlayer = 0;
            tab.setSelectedIndex(currentPlayer);
            tab.setForeground(Color.white);
            tab.setBackground(new Color(157, 0, 0));
            tab.setBackgroundAt(currentPlayer, new Color(0, 157, 0));

        } else {
            tab.setSelectedIndex(currentPlayer);
            tab.setForeground(Color.white);
            tab.setBackground(new Color(157, 0, 0));
            tab.setBackgroundAt(currentPlayer, new Color(0, 157, 0));
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void upgradeProperty(Property property) {
        controller.upgradeProperty(property);
    }

    public void sellProperty(Property property) {
        controller.sellProperty(property);
    }

    public void downgradeProperty(Property property) {
        controller.downgradeProperty(property);
    }
}
