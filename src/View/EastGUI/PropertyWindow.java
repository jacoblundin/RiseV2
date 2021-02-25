package View.EastGUI;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import Controller.ManageEvents;
import Model.player.PlayerList;

/**
 * @author Muhammad Abdulkhuder, Aevan Dino.
 */
public class PropertyWindow extends JPanel {
    private static final long serialVersionUID = 1L;
    private PlayerList playerList;
    private JTabbedPane tab;
    private int playerAt;
    private EastSidePanel eastSidePanel;

    public PropertyWindow(PlayerList playerList, int playerAt, EastSidePanel eastSidePanel) {
        this.playerList = playerList;
        this.playerAt = playerAt;
        this.eastSidePanel = eastSidePanel;

        setPreferredSize(new Dimension(330, 600));
        setOpaque(false);
        setLayout(null);
        UIManager.put("TabbedPane.contentOpaque", false);
        UIManager.put("TabbedPane.selected", Color.cyan);

        tab = new JTabbedPane();
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tab.setBorder(null);
        tab.setBounds(0, 0, 330, 600);
        add(tab);
    }

    /**
     * this method loops the amount of players and adds tabs according to the number of
     * properties
     */
    public void addTabs() {
        tab.removeAll();
        tab.setForeground(Color.white);
        int size = playerList.getPlayerFromIndex(playerAt).getProperties().size();

        for (int i = 0; i < size; i++) {
            PlayerProperties playerProperties = new PlayerProperties(playerList, playerAt, i, eastSidePanel);
            tab.addTab("Property" + (i + 1), playerProperties);
            tab.setBackgroundAt(i, playerList.getPlayerFromIndex(playerAt).getProperty(i).getColor());
        }
    }
}
