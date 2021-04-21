package ManageEventTests;

import Controller.Controller;
import Controller.ManageEvents;
import Model.Tiles.TileCollection;
import Model.player.PlayerList;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;

import Model.player.Player;
import Model.Tiles.Property;

import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


public class PropertyEventTests {

    private Board board;
    private PlayerList playerList;
    private WestSidePanel westSidePanel;
    private GameFlowPanel gameFlowPanel;
    private EastSidePanel eastSidePanel;
    private Controller controller;

    private ManageEvents manageEvents;

    @BeforeEach
    void setup() {

        //Initiallize dependants
        TileCollection tileCollection = new TileCollection();
        westSidePanel = new WestSidePanel();
        playerList = new PlayerList();
        board = new Board(playerList, westSidePanel, tileCollection);
        gameFlowPanel = null;
        eastSidePanel = null;
        controller = new Controller(board, null, null, null, null,null);

        manageEvents = new ManageEvents(board, playerList, westSidePanel, gameFlowPanel, eastSidePanel, controller);
    }

    /**
     * Test that a player can not purchase an unowned property when they do not have enough gold
     */
    @Test
    void testPropertyPurchaseNotEnoughGold() {
        Player playerBuyer = new Player("JohnDoe", null, null, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("tilePics/Wood.png"));

        int buyerOwnedProperties = playerBuyer.getProperties().size();
        int playerBalance = 59;

        playerBuyer.setBalance(playerBalance);
        playerBuyer.setNetWorth(playerBalance);

        manageEvents.propertyEvent(property, playerBuyer);

        assertEquals(buyerOwnedProperties, playerBuyer.getProperties().size());
        assertEquals(playerBuyer.getBalance(), playerBalance);
        assertEquals(playerBuyer.getNetWorth(), playerBalance);

    }

    /**
     * Test that a player can purchase an unowned property when they have enough gold to do so
     *
     * Expected result:
     */
    @Test
    void testPropertyPurchaseEnoughGold() {
        Player playerBuyer = new Player("JohnDoe", null, Color.BLUE, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("images/tilePics/Wood.png"));

        int initBalance = 61;
        int finalBalance = 1;
        int finalNetWorth = initBalance; //Initial balance is the same as initial net worth. Net worth should not change when buying a property

        playerBuyer.setBalance(initBalance);
        playerBuyer.setNetWorth(initBalance);

        manageEvents.propertyEvent(property, playerBuyer);

        assertEquals(property, playerBuyer.getProperties().get(0)); //Assert that the player now owns the property
        assertEquals(finalBalance, playerBuyer.getBalance()); //Assert that the players gold coins are updated
        assertEquals(finalNetWorth, playerBuyer.getNetWorth()); //Assert that the players net worth is updated
    }

    /**
     * Test that a player can purchase an unowned property when the player only has the exact amount of gold required
     *
     *
     */
    @Test
    void testPropertyPurchaseJustEnoughGold() {
        Player playerBuyer = new Player("JohnDoe", null, Color.BLUE, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("images/tilePics/Wood.png"));

        int initBalance = 60;
        int finalBalance = 0;
        int finalNetWorth = initBalance; //Initial balance is the same as initial net worth.

        playerBuyer.setBalance(initBalance);
        playerBuyer.setNetWorth(initBalance);

        manageEvents.propertyEvent(property, playerBuyer);

        assertEquals(property, playerBuyer.getProperties().get(0)); //Assert that the player now owns the property
        assertEquals(finalBalance, playerBuyer.getBalance()); //Assert that the players gold coins are updated
        assertEquals(finalNetWorth, playerBuyer.getNetWorth()); //Assert that the players net worth is updated
    }

    /**
     * Test that the an owned property is not able to be purchased by another player
     */
    @Test
    void testPropertyPurchaseNotAvailable() {
        Player playerBuyer = new Player("Buy", null, Color.GREEN, 0);
        Player playerOwner = new Player("Own", null, Color.RED, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("tilePics/Wood.png"));

        property.setOwner(playerOwner);

        int nrOwnedPropertiesExpected = playerBuyer.getProperties().size();

        manageEvents.propertyEvent(property, playerBuyer);

        assertEquals(nrOwnedPropertiesExpected, playerBuyer.getProperties().size());
        assertEquals(playerOwner, property.getOwner());

    }

    /**
     * Tests that the player is losing the correct amount of gold that is to be payed as rent.
      */
    @Test
    void testPropertyRentGoldPayed()
    {
        Player playerPayRent = new Player("", null, Color.GREEN, 0);
        Player playerOwner = new Player("", null, Color.RED, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("tilePics/Wood.png"));

        property.setOwner(playerOwner);

        int playerBalance = 60;
        playerPayRent.setBalance(playerBalance);
        playerPayRent.setNetWorth(playerBalance);

        manageEvents.propertyEvent(property, playerPayRent);

        assertEquals(playerBalance-2, playerPayRent.getBalance());
        assertEquals(playerBalance-2, playerPayRent.getNetWorth());
    }

    /**
     * Tests that the owner of the property earns the correct amount of gold when rent is payed.
     */
    @Test
    void testPropertyRentGoldEarned()
    {
        Player playerPayRent = new Player("", null, Color.BLUE, 0);
        Player playerOwner = new Player("", null, Color.RED, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("tilePics/Wood.png"));

        property.setOwner(playerOwner);

        int playerBalance = 60;
        playerOwner.setBalance(playerBalance);
        playerOwner.setNetWorth(playerBalance);

        manageEvents.propertyEvent(property, playerPayRent);

        assertEquals(playerBalance+2, playerOwner.getBalance());
        assertEquals(playerBalance+2, playerOwner.getNetWorth());

    }

    /**
     * Tests that the owner of the property doesn't pay rent on their own properties.
     */
    @Test
    void testPropertyRentOwner()
    {
        fail("Test not implemented"); //Fail the test
    }

    /**
     * Tests if the owner of the property ranks up when being payed rent.
     */
    @Test
    void testPropertyOwnerRankUp()
    {
        fail("Test not implemented"); //Fail the test
    }


    /**
     * Tests if the player ranks down when paying rent.
     */
    @Test
    void testPropertyPlayerRankDown()
    {
        fail("Test not implemented"); //Fail the test
    }

    /**
     * Tests that an owner can win the game through receiving rent
     */
    @Test
    void testPropertyOwnerWin()
    {
        fail("Test not implemented"); //Fail the test
    }

    /**
     * Tests if the player gets eliminated when paying rent.
     */
    @Test
    void testPropertyPlayerElimination()
    {
        fail("Test not implemented"); //Fail the test
    }
}
