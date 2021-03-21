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

public class TaxEventTests {

	private Board board;
	private PlayerList playerList;
	private WestSidePanel westSidePanel;
	private GameFlowPanel gameFlowPanel;
	private EastSidePanel eastSidePanel;
	private Controller controller;

	private ManageEvents manageEvents;

	private SundayChurch churchTile;
	private Tax taxTile;

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
	 * Test that the tax event draws the correct amount from a player
	 */
	@Test
	void testTaxAmount() {

		Player player = new Player("JohnDoe", null, Color.BLUE, 0);

		int playerBalance = 500;
		int taxAmount = 200; //FIXME: Tax amount is currently hardcoded as 200, the requirements do not specify an amount

		player.setBalance(playerBalance);
		player.setNetWorth(playerBalance);

		manageEvents.newEvent(taxTile, player);

		int newPlayerBalance = playerBalance - taxAmount;

		assertEquals(newPlayerBalance, player.getBalance());
		assertEquals(newPlayerBalance, player.getNetWorth());

	}

	/**
	 * Test that a player can be eliminated if they are taxed
	 */
	@Test
	void testPlayerEliminated() {

		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");
		Player player = playerList.getPlayerFromIndex(1);

		player.setPosition(10); //To avoid triggering a duel

		int playerBalance = 100;

		player.setBalance(playerBalance);
		player.setNetWorth(playerBalance);

		manageEvents.newEvent(taxTile, player);

		assertEquals(false, player.isAlive());
		assertEquals(1, playerList.getActivePlayers().size());

	}

	/**
	 * Test that a player can rank down if they get taxed
	 */
	@Test
	void testTaxPlayerRankDown() throws InterruptedException {

		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");
		Player player = playerList.getPlayerFromIndex(1);

		player.setPosition(10); //To avoid triggering a duel

		int playerBalance = 2100;

		player.setBalance(playerBalance);
		player.setNetWorth(playerBalance);

		player.checkPlayerRank();

		manageEvents.newEvent(taxTile, player);

		Thread.sleep(1000); //This is needed since the rank check is performed by the Swing EDT
		assertEquals(PlayerRanks.PEASANT, player.getPlayerRank()); //Race condition here, the EDT might not have had time to update the ranks

	}

}
