package ManageEventTests;

import Controller.Controller;
import Controller.ManageEvents;
import Model.Tiles.SundayChurch;
import Model.Tiles.Tax;
import Model.Tiles.TileCollection;
import Model.Tiles.Work;
import Model.player.PlayerList;
import Model.player.PlayerRanks;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;

import Model.player.Player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;


public class WorkEventTests {

	private Board board;
	private PlayerList playerList;
	private WestSidePanel westSidePanel;
	private GameFlowPanel gameFlowPanel;
	private EastSidePanel eastSidePanel;
	private Controller controller;

	private ManageEvents manageEvents;
	private Work workTile;

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
		workTile = new Work();

	}

	/**
	 * Test that players ranked peasant receive the correct salary
	 */
	@Test
	void testPeasantSalary() {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");

		Player peasant = playerList.getPlayerFromIndex(0);
		peasant.setPosition(10); //To avoid triggering a duel

		int rollValue = 10;
		int rankMultiplier = 20;

		int initBalance = 1500;
		peasant.setBalance(initBalance);
		int expectedBalance = initBalance + (rankMultiplier * rollValue);

		int initNetWorth = 1500;
		peasant.setNetWorth(initNetWorth);
		int expectedNetWorth = initNetWorth + (rankMultiplier * rollValue);

		peasant.checkPlayerRank();
		assertEquals(PlayerRanks.PEASANT, peasant.getPlayerRank()); //Make sure that the player is the expected rank

		manageEvents.setRoll(rollValue);
		manageEvents.newEvent(workTile, peasant);

		assertEquals(expectedBalance, peasant.getBalance());
		assertEquals(expectedNetWorth, peasant.getNetWorth());

	}

	/**
	 * Test that players ranked lord receive the correct salary
	 */
	@Test
	void testKnightSalary() {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");

		Player knight = playerList.getPlayerFromIndex(0);
		knight.setPosition(10); //To avoid triggering a duel

		int rollValue = 15;
		int rankMultiplier = 25;

		int initBalance = 2500;
		knight.setBalance(initBalance);
		int expectedBalance = initBalance + (rankMultiplier * rollValue);

		int initNetWorth = 2500;
		knight.setNetWorth(initNetWorth);
		int expectedNetWorth = initNetWorth + (rankMultiplier * rollValue);

		knight.checkPlayerRank();
		assertEquals(PlayerRanks.KNIGHT, knight.getPlayerRank()); //Make sure that the player is the expected rank

		manageEvents.setRoll(rollValue);
		manageEvents.newEvent(workTile, knight);

		assertEquals(expectedBalance, knight.getBalance());
		assertEquals(expectedNetWorth, knight.getNetWorth());

	}

	/**
	 * Test that players ranked lord receive the correct salary
	 */
	@Test
	void testLordSalary() {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");

		Player lord = playerList.getPlayerFromIndex(0);
		lord.setPosition(10); //To avoid triggering a duel

		int rollValue = 4;
		int rankMultiplier = 30;

		int initBalance = 4500;
		lord.setBalance(initBalance);
		int expectedBalance = initBalance + (rankMultiplier * rollValue);

		int initNetWorth = 4500;
		lord.setNetWorth(initNetWorth);
		int expectedNetWorth = initNetWorth + (rankMultiplier * rollValue);

		lord.checkPlayerRank();
		assertEquals(PlayerRanks.LORD, lord.getPlayerRank()); //Make sure that the player is the expected rank

		manageEvents.setRoll(rollValue);
		manageEvents.newEvent(workTile, lord);

		assertEquals(expectedBalance, lord.getBalance());
		assertEquals(expectedNetWorth, lord.getNetWorth());

	}

	/**
	 * Test that players can rank up from a work event, and that the game
	 * properly notifies players of the rank up
	 */
	@Test
	void testSalaryRankUp() throws InterruptedException {
		playerList.addNewPlayer("JohnDoe", "RED");
		playerList.addNewPlayer("JaneDoe", "GREEN");

		Player peasant = playerList.getPlayerFromIndex(0);
		peasant.setPosition(10); //To avoid triggering a duel

		int rollValue = 10;
		int rankMultiplier = 20;

		int initBalance = 1900;
		peasant.setBalance(initBalance);
		int expectedBalance = initBalance + (rankMultiplier * rollValue);

		int initNetWorth = 1900;
		peasant.setNetWorth(initNetWorth);
		int expectedNetWorth = initNetWorth + (rankMultiplier * rollValue);

		peasant.checkPlayerRank();
		assertEquals(PlayerRanks.PEASANT, peasant.getPlayerRank()); //Make sure that the player is the expected rank

		manageEvents.setRoll(rollValue);
		manageEvents.newEvent(workTile, peasant);

		Thread.sleep(1000); //This is needed since the rank check is performed by the Swing EDT
		assertEquals(PlayerRanks.KNIGHT, peasant.getPlayerRank()); //Race condition here, the EDT might not have had time to update the ranks
	}

	/**
	 * Test that players can win from a work event
	 */
	@Test
	void testSalaryWin() {

		//Can't currently test if a win has occurred

		fail("Test not implemented"); //Fail the test
	}

}
