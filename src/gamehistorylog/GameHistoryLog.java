package gamehistorylog;

import Model.Tiles.Property;
import Model.Tiles.Tile;
import Model.player.Player;
import utils.Utils;

/**
 * GameHistoryLog is a class for logging game events and player actions to the game history log.
 * <p>
 * GameHistoryLog has a GameHistoryLogPanel which is a JPanel, this panel is where the logged events / actions are
 * displayed. Calling getGameHistoryPanel will return this panel.
 * <p>
 * GameHistoryLog is modeled as a Singleton, this is because the current architecture
 * of the system isn't very well structured. The actions and events will most
 * likely be logged from different scopes.
 */
public class GameHistoryLog {
    GameHistoryPanel gameHistoryPanel;
    private static GameHistoryLog instance;

    public static GameHistoryLog instance() {
        if (instance == null) {
            instance = new GameHistoryLog();
        }
        return instance;
    }

    private GameHistoryLog() {
        this.gameHistoryPanel = new GameHistoryPanel();
    }

    public static void resetLog() {
        instance = new GameHistoryLog();
    }

    /**
     * Get the panel which displays all the logged events. Use this to get a reference of the panel so it can be added
     * to other panels and frames.
     *
     * @return the GameHistoryPanel of this Log
     */
    public GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    public void logDiceRollEvent(Player player, Tile landingTile, int sumRoll) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String landingTileColorHex = Utils.colorToHexString(landingTile.getColor());
        String landingTileName = landingTile.getName();
        gameHistoryPanel.append(String.format("<font color=%s><b>%s</b></font> rolled a combined <b>%d</b>, landing on <font color=%s><b>%s</b></font>.", playerColorHex, playerName, sumRoll, landingTileColorHex, landingTileName));
    }

    public void logDoubleDiceRollEvent(Player player, Tile landingTile, int doubleRoll) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String landingTileColorHex = Utils.colorToHexString(landingTile.getColor());
        String landingTileName = landingTile.getName();
        gameHistoryPanel.append(String.format("<font color=%s><b>%s</b></font> rolled a combined <b>%d</b>, landing on <font color=%s><b>%s</b></font>.", playerColorHex, playerName, doubleRoll, landingTileColorHex, landingTileName));
    }

    public void logWorkEvent(Player player, int payAmount) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s earned %s working.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(payAmount)));
    }

    public void logTaxEvent(Player player, int taxAmount) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s got taxed %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(taxAmount)));
    }

    public void logTaxPayoutEvent(Player player, int taxPayoutAmount) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s collected %s from the Church.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(taxPayoutAmount)));
    }

    public void logPassGoEvent(Player player, int payoutAmount) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s passed go and received %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(payoutAmount)));
    }

    public void logPropertyBuyEvent(Player player, Property propertyTile) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileName = propertyTile.getName();
        int propertyTilePrice = propertyTile.getPrice();
        gameHistoryPanel.append(String.format("%s purchased %s for %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(propertyTilePrice)));
    }

    public void logPropertySellEvent(Player player, Property propertyTile) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileName = propertyTile.getName();
        int propertyTilePrice = propertyTile.getPrice();
        gameHistoryPanel.append(String.format("%s sold %s for %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(propertyTilePrice)));
    }

    public void logPropertyRentEvent(Player player, Property propertyTile) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileName = propertyTile.getName();
        String propertyOwnerName = propertyTile.getOwner().getName();
        String propertyOwnerColorHex = Utils.colorToHexString(propertyTile.getOwner().getPlayerColor());
        int propertyTileRent = propertyTile.getTotalRent();
        gameHistoryPanel.append(String.format("%s visited %s's %s and had to pay %s in rent.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatPlayerName(propertyOwnerName, propertyOwnerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(propertyTileRent)));
    }

    public void logPropertyUpgradeEvent(Player player, Property propertyTile) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileName = propertyTile.getName();
        int propertyTileLevel = propertyTile.getLevel();
        int propertyTileUpgradePrice = propertyTile.getLevelPrice(); //TODO: WARNING:This might cause the wrong information to be displayed, if this value changes after an upgrade the new upgrade price will be returned here and not the old
        gameHistoryPanel.append(String.format("%s upgraded %s to level %s, for %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), propertyTileLevel, htmlFormatGoldCoins(propertyTileUpgradePrice)));
    }

    public void logPropertyDownGradeEvent(Player player, Property propertyTile) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileName = propertyTile.getName();
        int propertyTileLevel = propertyTile.getLevel();
        int propertyTileUpgradePrice = propertyTile.getLevelPrice(); //TODO: WARNING:This might cause the wrong information to be displayed, if this value changes after an upgrade the new upgrade price will be returned here and not the old
        gameHistoryPanel.append(String.format("%s downgraded %s to level %s, recouping %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), propertyTileLevel, htmlFormatGoldCoins(propertyTileUpgradePrice)));
    }

    public void logPlayerRankUpEvent(Player player) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String playerRank = player.getPlayerRank().name();
        gameHistoryPanel.append(String.format("%s ranked up! Reaching the rank %s!", htmlFormatPlayerName(playerName, playerColorHex), playerRank));
    }

    public void logPlayerRankDownEvent(Player player) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        String playerRank = player.getPlayerRank().name();
        gameHistoryPanel.append(String.format("%1$s ranked down! %1$s is now a %2$s.", htmlFormatPlayerName(playerName, playerColorHex), playerRank));
    }

    public void logPlayerEliminatedEvent(Player player) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s has been eliminated!", htmlFormatPlayerName(playerName, playerColorHex)));
    }

    public void logJailEnterEvent(Player player) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s has been jailed for their crimes!", htmlFormatPlayerName(playerName, playerColorHex)));
    }

    public void logJailExitEvent(Player player) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s has been released from jail.", htmlFormatPlayerName(playerName, playerColorHex)));
    }

    public void logJailExitCostEvent(Player player, int totalBail) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s has been released from jail after paying %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(totalBail)));
    }

    public void logJailStayEvent(Player player) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%1$s stayed the round in jail. %1$s has been in jail for %2$s rounds.", htmlFormatPlayerName(playerName, playerColorHex), player.getJailCounter()));
    }

    public void logFortuneBlessingEvent(Player player, int income) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s collected %s from fortune teller", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(income)));
    }

    public void logFortuneCurseEvent(Player player, int amount) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s lost %s from fortune teller", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(amount)));
    }

    public void logDuelWinner(Player player, int income) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s won a duel and earned %s", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(income)));
    }

    public void logDuelLoser(Player player, int amount) {
        String playerColorHex = Utils.colorToHexString(player.getPlayerColor());
        String playerName = player.getName();
        gameHistoryPanel.append(String.format("%s lost a duel and lost %s", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatGoldCoins(amount)));
    }

    public void logTradeEventGold(Player activePlayer, Player otherPlayer, Property propertyTile, int offer){
        String playerColorHex = Utils.colorToHexString(activePlayer.getPlayerColor());
        String playerColorHex2 = Utils.colorToHexString(otherPlayer.getPlayerColor());
        String playerName = activePlayer.getName();
        String playerName2 = otherPlayer.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileName = propertyTile.getName();
        gameHistoryPanel.append(String.format("%1$s completed a trade with %2$s. %1$s has purchased %3$s from %2$s for %4$s", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatPlayerName(playerName2, playerColorHex2), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(offer)));
    }

    public void logTradeEventProperty(Player activePlayer, Player otherPlayer, Property propertyTile, Property propertyTile2){
        String playerColorHex = Utils.colorToHexString(activePlayer.getPlayerColor());
        String playerColorHex2 = Utils.colorToHexString(otherPlayer.getPlayerColor());
        String playerName = activePlayer.getName();
        String playerName2 = otherPlayer.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileColorHex2 = Utils.colorToHexString(propertyTile2.getColor());
        String propertyTileName = propertyTile.getName();
        String propertyTileName2 = propertyTile2.getName();
        gameHistoryPanel.append(String.format("%1$s completed a trade with %2$s. %1$s traded away %3$s to %2$s and received %4$s in return.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatPlayerName(playerName2, playerColorHex2), htmlFormatTileName(propertyTileName, propertyTileColorHex),htmlFormatTileName(propertyTileName2, propertyTileColorHex2)));
    }

    public void logTradeEventGoldAndProperty(Player activePlayer, Player otherPlayer, Property propertyTile, Property propertyTile2, int offer) {
        String playerColorHex = Utils.colorToHexString(activePlayer.getPlayerColor());
        String playerColorHex2 = Utils.colorToHexString(otherPlayer.getPlayerColor());
        String playerName = activePlayer.getName();
        String playerName2 = otherPlayer.getName();
        String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
        String propertyTileColorHex2 = Utils.colorToHexString(propertyTile2.getColor());
        String propertyTileName = propertyTile.getName();
        String propertyTileName2 = propertyTile2.getName();
        gameHistoryPanel.append(String.format("%1$s completed a trade with %2$s. %1$s traded away %3$s and %5$s to %2$s and received %4$s in return", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatPlayerName(playerName2, playerColorHex2), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatTileName(propertyTileName2, propertyTileColorHex2), htmlFormatGoldCoins(offer)));
    }

    //HTML format helpers
    private String htmlFormatGoldCoins(int goldCoinsAmount) {
        String goldCoinImgPath = "file:images/coin.jpg";
        String ret = "<b>" + goldCoinsAmount + "</b>" + "<img src=" + goldCoinImgPath + " height=11 width=11>" + " gold coins";
        return ret;
    }

    private String htmlFormatPlayerName(String playerName, String hexColorString) {
        String ret = "<font color=" + hexColorString + "><b>" + playerName + "</b></font>";
        return ret;
    }

    private String htmlFormatTileName(String tileName) {
        //If no color is specified use black as the default color
        return htmlFormatTileName(tileName, "#000000");
    }

    private String htmlFormatTileName(String tileName, String hexColorString) {
        String ret = "<font color=" + hexColorString + "><b>" + tileName + "</b></font>";
        return ret;
    }
}