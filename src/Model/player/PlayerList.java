package Model.player;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import Model.colorsAndIcons.ColorIconMap;
import Model.colorsAndIcons.StringColorMap;

/**
 * A class that holds all active players
 *
 * @author Seth Oberg
 */

public class PlayerList {
    private List<Player> activePlayers = new ArrayList<>();
    private ColorIconMap colorIcons = new ColorIconMap();
    private StringColorMap colorMap = new StringColorMap();
    private ImageIcon playerIcon = new ImageIcon();
    private int currentPlayer = 0;
    private int playerListLength = 0;

    public PlayerList() {
        currentPlayer = 0;
    }

    /**
     * Adds new Model.player with the use of the ColorIconMap
     *
     * @param name chosen name
     * @param icon string containing a color used to get a color from the ColorIconMap
     */
    public void addNewPlayer(String name, String icon) {
        playerIcon = colorIcons.getColorFromMap(icon);
        activePlayers.add(new Player(name, playerIcon, colorMap.getColorFromMap(icon), playerListLength));
        playerListLength++;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    /**
     * @param index get specific Model.player
     * @return Model.player at chosen index
     */
    public Player getPlayerFromIndex(int index) {
        return activePlayers.get(index);
    }

    /**
     * @return the current Model.player
     */
    public Player getActivePlayer() {
        return activePlayers.get(currentPlayer);
    }

    /**
     * @return amount of players
     */
    public int getLength() {
        return activePlayers.size();
    }

    /**
     * @param player to remove from list
     */
    public void eliminatePlayer(Player player) {
        player.clearPlayer();
        activePlayers.remove(player.getPlayerIndex());
    }

    /**
     * Update amount of players after a Model.player has been removed
     */
    public void updatePlayerList() {
        for (int i = 0; i < activePlayers.size(); i++) {
            activePlayers.get(i).setPlayerIndex(i);
        }
    }

    /**
     * Used to switch to the current Model.player to the next one
     */
    public void switchToNextPlayer() {
        if (currentPlayer < (activePlayers.size() - 1)) {
            currentPlayer = currentPlayer + 1;
        } else {
            currentPlayer = 0;
        }
    }

    @Override
    public String toString() {
        return "PlayerList{" +
                "activePlayers=" + activePlayers +
                '}';
    }
}
