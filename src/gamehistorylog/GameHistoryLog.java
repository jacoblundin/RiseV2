package gamehistorylog;

import Model.Tiles.Property;
import Model.Tiles.Tile;
import Model.player.Player;
import utils.Utils;

/**
 * GameHistoryLog is a class for logging game events and player actions to the game history log.
 *
 * GameHistoryLog has a GameHistoryLogPanel which is a JPanel, this panel is where the logged events / actions are
 * displayed. Calling getGameHistoryPanel will return this panel.
 *
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

	/**
	 * Get the panel which displays all the logged events. Use this to get a reference of the panel so it can be added
	 * to other panels and frames.
	 *
	 * @return the GameHistoryPanel of this Log
	 */
	public GameHistoryPanel getGameHistoryPanel() {
		return this.gameHistoryPanel;
	}

	//Log methods
	/*
	All of these methods are very "clunky" and this should probably be better modeled / structured / implemented.

	Here are all the methods for logging events / actions. There is a separate method for each type of event.
	 */

	public void logDiceRollEvent(Player player, Tile landingTile, int sumRoll) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String landingTileColorHex = Utils.colorToHexString(landingTile.getColor());
		String landingTileName = landingTile.getName();

		gameHistoryPanel.append(String.format("<font color=%s><b>%s</b></font> rolled a combined <b>%d</b>, landing on <font color=%s><b>%s</b></font>.", playerColorHex, playerName, sumRoll, landingTileColorHex, landingTileName));
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
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
		String propertyTileName = propertyTile.getName();
		int propertyTilePrice = propertyTile.getPrice();
		gameHistoryPanel.append(String.format("%s purchased %s for %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(propertyTilePrice)));
	}

	public void logPropertySellEvent(Player player, Property propertyTile) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
		String propertyTileName = propertyTile.getName();
		int propertyTilePrice = propertyTile.getPrice();
		gameHistoryPanel.append(String.format("%s sold %s for %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(propertyTilePrice)));
	}

	public void logPropertyRentEvent(Player player, Property propertyTile) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
		String propertyTileName = propertyTile.getName();
		String propertyOwnerName = propertyTile.getOwner().getName();
		String propertyOwnerColorHex = Utils.colorToHexString(propertyTile.getOwner().getPlayerColor());
		int propertyTileRent = propertyTile.getTotalRent();

		gameHistoryPanel.append(String.format("%s visited %s's %s and had to pay %s in rent.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatPlayerName(propertyOwnerName, propertyOwnerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), htmlFormatGoldCoins(propertyTileRent)));
	}

	public void logPropertyUpgradeEvent(Player player, Property propertyTile) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
		String propertyTileName = propertyTile.getName();
		int propertyTileLevel = propertyTile.getLevel();
		int propertyTileUpgradePrice = propertyTile.getLevelPrice(); //TODO: WARNING:This might cause the wrong information to be displayed, if this value changes after an upgrade the new upgrade price will be returned here and not the old
		gameHistoryPanel.append(String.format("%s upgraded %s to level %s, for %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), propertyTileLevel, htmlFormatGoldCoins(propertyTileUpgradePrice)));
	}

	public void logPropertyDownGradeEvent(Player player, Property propertyTile) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String propertyTileColorHex = Utils.colorToHexString(propertyTile.getColor());
		String propertyTileName = propertyTile.getName();
		int propertyTileLevel = propertyTile.getLevel();
		int propertyTileUpgradePrice = propertyTile.getLevelPrice(); //TODO: WARNING:This might cause the wrong information to be displayed, if this value changes after an upgrade the new upgrade price will be returned here and not the old
		gameHistoryPanel.append(String.format("%s downgraded %s to level %s, recouping %s.", htmlFormatPlayerName(playerName, playerColorHex), htmlFormatTileName(propertyTileName, propertyTileColorHex), propertyTileLevel, htmlFormatGoldCoins(propertyTileUpgradePrice)));
	}

	public void logPlayerRankUpEvent(Player player) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String playerRank = player.getPlayerRank().name();
		gameHistoryPanel.append(String.format("%s ranked up! Reaching the rank %s!", htmlFormatPlayerName(playerName, playerColorHex), playerRank));
	}

	public void logPlayerRankDownEvent(Player player) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		String playerRank = player.getPlayerRank().name();
		gameHistoryPanel.append(String.format("%1$s ranked down! %1$s is now a %2$s.", htmlFormatPlayerName(playerName, playerColorHex), playerRank));
	}

	public void logPlayerEliminatedEvent(Player player) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		gameHistoryPanel.append(String.format("%s has been eliminated!", htmlFormatPlayerName(playerName, playerColorHex)));
	}

	public void logJailEnterEvent(Player player) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		gameHistoryPanel.append(String.format("%s has been jailed for their crimes!", htmlFormatPlayerName(playerName, playerColorHex)));
	}

	public void logJailExitEvent(Player player) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		gameHistoryPanel.append(String.format("%s has been released from jail.", htmlFormatPlayerName(playerName, playerColorHex)));
	}

	public void logJailStayEvent(Player player) {
		String playerColorHex =  Utils.colorToHexString(player.getPlayerColor());
		String playerName = player.getName();
		gameHistoryPanel.append(String.format("%s stayed the round in jail.", htmlFormatPlayerName(playerName, playerColorHex)));
	}


	//HTML format helpers

	private String htmlFormatGoldCoins(int goldCoinsAmount) {
		String goldCoinImgPath = getClass().getResource("assets/coin.jpg").toString();
		String ret = "<b>" + goldCoinsAmount + "</b>"+ "<img src=" + goldCoinImgPath + " height=11 width=11>" + " gold coins";

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
