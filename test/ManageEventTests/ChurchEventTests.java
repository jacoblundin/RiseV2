package ManageEventTests;

import Controller.Controller;
import Controller.ManageEvents;
import Model.Tiles.SundayChurch;
import Model.Tiles.Tax;
import Model.Tiles.TileCollection;
import Model.player.PlayerList;
import Model.player.PlayerRanks;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;

import Model.player.Player;

import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChurchEventTests {

	private Board board;
	private PlayerList playerList;
	private WestSidePanel westSidePanel;
	private GameFlowPanel gameFlowPanel;
	private EastSidePanel eastSidePanel;
	private Controller controller;

	private ManageEvents manageEvents;

	private SundayChurch churchTile;
	private Tax taxTile;

	private int TAX_AMOUNT = 200;

	@BeforeEach
	void setup() {

		playerList = new PlayerList();

		TileCollection tileCollection = new TileCollection();

		westSidePanel = new WestSidePanel();
		eastSidePanel = new EastSidePanel(playerList);

		board = new Board(playerList, westSidePanel, tileCollection);
		gameFlowPanel = null;
		controller = new Controller(board, playerList, westSidePanel, null, eastSidePanel,null);

		manageEvents = new ManageEvents(board, playerList, westSidePanel, gameFlowPanel, eastSidePanel, controller);

		churchTile = new SundayChurch();
		taxTile = new Tax(churchTile);
	}

	/**
	 * Test that no gold is payed out if there is no tax collected
	 */
	@Test
	void testTaxPayout0() {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");

		Player playerReceiver = playerList.getPlayerFromIndex(1);

		int initBalance = playerReceiver.getBalance();
		int initNetWorth = playerReceiver.getNetWorth();

		manageEvents.newEvent(churchTile, playerReceiver);

		assertEquals(initBalance, initBalance); //No tax has been payed, so no tax will be payed out.
		assertEquals(initNetWorth, initNetWorth);

	}

	/**
	 * Test that collected tax is payed out
	 */
	@Test
	void testTaxPayout1() {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");
		Player playerPayer = playerList.getPlayerFromIndex(0);
		Player playerReceiver = playerList.getPlayerFromIndex(1);

		int newBalance = playerReceiver.getBalance() + TAX_AMOUNT;
		int newNetWorth = playerReceiver.getBalance() + TAX_AMOUNT;

		manageEvents.newEvent(taxTile, playerPayer);

		manageEvents.newEvent(churchTile, playerReceiver);

		assertEquals(newBalance, playerReceiver.getBalance());
		assertEquals(newNetWorth, playerReceiver.getNetWorth());

	}

	/**
	 * Test that collected tax is payed out
	 */
	@Test
	void testTaxPayoutMultiple() {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");
		Player playerPayer = playerList.getPlayerFromIndex(0);
		Player playerReceiver = playerList.getPlayerFromIndex(1);

		int newBalance = playerReceiver.getBalance() + (TAX_AMOUNT * 3);
		int newNetWorth = playerReceiver.getBalance() + (TAX_AMOUNT * 3);

		manageEvents.newEvent(taxTile, playerPayer);
		manageEvents.newEvent(taxTile, playerPayer);
		manageEvents.newEvent(taxTile, playerPayer);

		manageEvents.newEvent(churchTile, playerReceiver);

		assertEquals(newBalance, playerReceiver.getBalance());
		assertEquals(newNetWorth, playerReceiver.getNetWorth());

	}

	/**
	 * Test that a player can rank up from a church event
	 */
	@Test
	void testTaxPayoutRankUp() {

		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");
		Player playerPayer = playerList.getPlayerFromIndex(0);
		Player playerReceiver = playerList.getPlayerFromIndex(1);

		int newBalance = playerReceiver.getBalance() + (TAX_AMOUNT * 3);
		int newNetWorth = playerReceiver.getBalance() + (TAX_AMOUNT * 3);

		manageEvents.newEvent(taxTile, playerPayer);
		manageEvents.newEvent(taxTile, playerPayer);
		manageEvents.newEvent(taxTile, playerPayer);

		manageEvents.newEvent(churchTile, playerReceiver);

		assertEquals(newBalance, playerReceiver.getBalance());
		assertEquals(newNetWorth, playerReceiver.getNetWorth());
		assertEquals(PlayerRanks.KNIGHT, playerReceiver.getPlayerRank()); //FIXME: PlayerRank is not checked in the correct order, and may lead to failure here.

	}

	/**
	 * Test that a player can win from a church event
	 */
	@Test
	void testTaxPayoutWin() {

		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");
		Player playerPayer = playerList.getPlayerFromIndex(0);
		Player playerReceiver = playerList.getPlayerFromIndex(1);

		playerReceiver.setBalance(7400);
		playerReceiver.setNetWorth(7400);
		int newBalance = playerReceiver.getBalance() + (TAX_AMOUNT);
		int newNetWorth = playerReceiver.getBalance() + (TAX_AMOUNT);

		manageEvents.newEvent(taxTile, playerPayer);

		manageEvents.newEvent(churchTile, playerReceiver);

		assertEquals(newBalance, playerReceiver.getBalance());
		assertEquals(newNetWorth, playerReceiver.getNetWorth());

	}

}
