package Model.player;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import View.WestGUI.WestSidePanel;
import Model.Tiles.Property;
import Model.Tiles.Tavern;
import Model.Tiles.Tile;
import gamehistorylog.GameHistoryLog;
import soundservice.SoundFx;
import soundservice.SoundService;

/**
 * Player class deals with everything that has to do with a player.
 *
 * @author AevanDino, Seth �berg, Muhammad Hasan, Sebastian Viro
 */
public class Player {
    private String name;
    private Boolean isAlive;
    private ImageIcon playerIcon;
    private int counter;
    private int playerIndex;
    private int originalPlayerIndex;
    private int playerJailCounter = 0;
    private boolean playerIsInJail = false;
    private Color playerColor;
    private PlayerRanks playerRank;
    private int balance;
    private int netWorth;
    private boolean playerPassedgo = false;
    private ArrayList<Property> propertiesOwned;
    private ArrayList<Tile> tilesOwned;
    private ArrayList<Tavern> tavernsOwned;
    private WestSidePanel westSidePanel;

    /**
     * Constructor for adding a new Model.player, new players are created by the
     * playerList class and are automatically set at index 0 on the board with the
     * counter variable set to 0
     *
     * @param inPlayerName chosen Name
     * @param playerIcon   imageIcon from ColorIconMap
     * @param playerIndex  index of player (for example if second player the playerIndex is 1)
     */
    public Player(String inPlayerName, ImageIcon playerIcon, Color playerColor, int playerIndex) {
        this.playerColor = playerColor;
        this.name = inPlayerName;

        this.isAlive = true;
        this.playerIndex = playerIndex;
        this.originalPlayerIndex = playerIndex;

        setBalance(1500);
        setNetWorth(1500);

        this.playerRank = PlayerRanks.PEASANT;
        this.playerIndex = playerIndex;
        this.tavernsOwned = new ArrayList<>();
        this.propertiesOwned = new ArrayList<>();

        counter = 0;

        //Generate the boardPiece
        this.playerIcon = BoardPiece.newPiece(this.playerRank, this.playerColor);
    }

    /**
     * Keep track of how many turns a user has been in jail, if 3 the Model.player gets
     * out of jail if less than 3 the "roll dice" button is to be inactivated and
     * the end turn activated
     *
     * @return playerJailCounter
     */
    public int getJailCounter() {
        return playerJailCounter;
    }

    /**
     * method used for increasing or resetting the jailCounter of a Model.player
     *
     * @param amount
     */
    public void setJailCounter(int amount) {
        this.playerJailCounter = amount;
    }

    /**
     * Increase number of turns spent in jail by one
     */
    public void increaseJailCounter() {
        this.playerJailCounter++;
    }

    /**
     * @param isInJail if Model.player is sent to jail send true, if Model.player is not in jail
     *                 anymore set to false
     */
    public void setPlayerIsInJail(boolean isInJail) {
        this.playerIsInJail = isInJail;
    }

    /**
     * @return Return either true or false if Model.player in in jail or not
     */
    public Boolean isPlayerInJail() {
        return this.playerIsInJail;
    }

    /**
     * @return name, the players name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param playerName, the Model.player name to set
     */
    public void setName(String playerName) {
        this.name = playerName;
    }

    /**
     * Set the playerIndex of the Model.player (the index the user has in the playerList
     * array)
     *
     * @param index
     */
    public void setPlayerIndex(int index) {
        this.playerIndex = index;
    }

    /**
     * @return the playerIndex of a Model.player
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * Get the position a Model.player has on the View.GUI.board from 0-39
     *
     * @return counter
     */
    public int getPosition() {
        return counter;
    }

    /**
     * Move Model.player to a specific index on the View.GUI.board
     *
     * @param newPosition
     */
    public void setPositionInSpecificIndex(int newPosition) {
        this.counter = newPosition;
    }

    /**
     * method used to move the Model.player by either one or many steps
     *
     * @param amountOfStepsToMove
     */
    public void setPosition(int amountOfStepsToMove) {
        for (int i = 0; i < amountOfStepsToMove; i++) {
            if (counter < 39) {
                counter++;
            } else {
                counter = 0;
                playerPassedgo = true;
            }
        }
    }

    /**
     * @return playerPassedgo, boolean to keep track if user has passed go
     */
    public boolean passedGo() {
        return playerPassedgo;
    }

    /**
     * reset has passedGo variable to false
     */
    public void resetPassedGo() {
        playerPassedgo = false;
    }

    /**
     * @return balance, the Model.player balance
     */
    public int getBalance() {
        return this.balance;
    }

    /**
     * @return playerIcon, the image of a Model.player
     */
    public ImageIcon getImage() {
        return playerIcon;
    }

    /**
     * @param playerBalance the playerBalance to set
     */
    public void setBalance(int playerBalance) {
        this.balance = playerBalance;
    }

    /**
     * @param decrease amount to decrease players balance by
     */
    public void decreaseBalance(int decrease) {
        this.balance -= decrease;
        SoundService.instance().playSoundFx(SoundFx.SOUND_MONEY);
        checkPlayerRank();
    }

    /**
     * @param income increase players balance by an amount
     */
    public void increaseBalance(int income) {
        this.balance += income;
        SoundService.instance().playSoundFx(SoundFx.SOUND_MONEY);
        checkPlayerRank();
    }

    /**
     * @return the playerIsAlive
     */
    public Boolean isAlive() {
        return isAlive;
    }

    /**
     * @param playerIsAlive the playerIsAlive to set
     */
    public void setIsAlive(Boolean playerIsAlive) {
        this.isAlive = playerIsAlive;
    }

    /**
     * @return playerRank the rank of the Model.player
     */
    public PlayerRanks getPlayerRank() {
        return this.playerRank;
    }

    /**
     * @param playerRank set the rank of this Model.player
     */
    public void setPlayerRank(PlayerRanks playerRank) {
        switch (this.playerRank.compareTo(playerRank)) {
            case -1:
                //Rank up
                this.playerRank = playerRank;
                this.playerIcon = BoardPiece.newPiece(playerRank, this.playerColor);
                SoundService.instance().playSoundFx(SoundFx.SOUND_RANK);
                JOptionPane.showMessageDialog(null, name + ", you ranked up! You may now upgrade your buildings\n" +
                        "to a higher level and you earn more from your work.");
                GameHistoryLog.instance().logPlayerRankUpEvent(this);
                break;
            case 0:
                //Equal rank set: do nothing
                break;
            case 1:
                //Rank down
                this.playerRank = playerRank;
                this.playerIcon = BoardPiece.newPiece(playerRank, this.playerColor);
                SoundService.instance().playSoundFx(SoundFx.SOUND_RANK);
                JOptionPane.showMessageDialog(null, name + ", you fell in rank."
                );
                GameHistoryLog.instance().logPlayerRankDownEvent(this);
                break;
        }
    }

    public int setLeftToNextRank(PlayerList playerList, int playerNbr) {
        PlayerRanks currentRank = playerList.getPlayerFromIndex(playerNbr).getPlayerRank();
        int nextRank = 0;
        if (currentRank == PlayerRanks.PEASANT) {
            nextRank = 2000;
        }
        if (currentRank == PlayerRanks.KNIGHT) {
            nextRank = 4000;
        }
        if (currentRank == PlayerRanks.LORD) {
            nextRank = 7500;
        }
        return nextRank;
    }

    /**
     * @return the netWorth
     */
    public int getNetWorth() {
        return this.netWorth;
    }

    /**
     * @param netWorth the netWorth to set
     */
    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    /**
     * @param decrease
     */
    public void decreaseNetWorth(int decrease) {
        this.netWorth -= decrease;
        checkPlayerRank();
    }

    /**
     * @param income
     */
    public void increaseNetWorth(int income) {
        this.netWorth += income;
        checkPlayerRank();
    }

    /**
     * Adds newly purchased property to ownedProperties array
     *
     * @param newProperty, the newly bought property.
     */
    public void addNewProperty(Property newProperty) {
        this.propertiesOwned.add(newProperty);
    }

    public void removeProperty(Property property) {
        this.propertiesOwned.remove(property);
        property.setOwner(null);
    }

    public void sellProperty(Property property) {
        int total = (property.getPrice() + (property.getLevel() * property.getLevelPrice()));
        int res = JOptionPane.showConfirmDialog(null,
                "Do you really want to sell " + property.getName() + " for: " + total);
        if (res == 0) {
            GameHistoryLog.instance().logPropertySellEvent(this, property);
            increaseBalance(total);
            this.propertiesOwned.remove(property);
            property.setOwner(null);
        }
    }

    /**
     * @param newTavern add a new Tavern to a user
     */
    public void addNewTavern(Tavern newTavern) {
        this.tavernsOwned.add(newTavern);
    }

    /**
     * If user has two taverns the event will differ
     *
     * @return amount of taverns
     */
    public int getAmountOfTaverns() {
        return tavernsOwned.size();
    }

    /**
     * If user is eliminated reset all users properties and taverns by setting the
     * amount of houses to 0 and remove the owner
     */
    public void clearPlayer() {
        for (int i = 0; i < propertiesOwned.size(); i++) {
            propertiesOwned.get(i).clearProperty();
        }
        for (int i = 0; i < tavernsOwned.size(); i++) {
            tavernsOwned.get(i).clearTavern();
        }
    }

    public Property getPropertyAt(int pos) {
        return this.propertiesOwned.get(pos);
    }

    /**
     * Gets property at specified position
     *
     * @param pos
     * @return
     */
    public Tile getProperty(int pos) {
        return this.propertiesOwned.get(pos);
    }

    public void checkPlayerRank() {
        if (this.netWorth <= 2000) {
            setPlayerRank(PlayerRanks.PEASANT);
        } else if (this.netWorth < 4000) {
            setPlayerRank(PlayerRanks.KNIGHT);
        } else if (this.netWorth < 7500) {
            setPlayerRank(PlayerRanks.LORD);
        } else if (this.netWorth >= 7500) {
            setPlayerRank(PlayerRanks.KINGS);
        }
    }

    /**
     * @return propertiesOwned, returns entire ArrayList of properties owned.
     */
    public ArrayList<Property> getProperties() {
        return this.propertiesOwned;
    }

    /**
     * @return all taverns owned by Model.player
     */
    public ArrayList<Tavern> getTaverns() {
        return this.tavernsOwned;
    }

    /**
     * Returns the players color
     *
     * @return playerColor
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    public int getOriginalPlayerIndex() {
        return originalPlayerIndex;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}