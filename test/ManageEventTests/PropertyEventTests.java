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
import java.util.ArrayList;

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
        controller = new Controller(null, null, null, null, null,null);

        manageEvents = new ManageEvents(board, playerList, westSidePanel, gameFlowPanel, eastSidePanel, controller);
    }

    /**
     * Test that a player can not purchase an unowned property when they do not have enough gold
     */
    @Test
    void testPropertyPurchaseNotEnoughGold() {
        Player playerBuyer = new Player("", null, null, 0);
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
     */
    @Test
    void testPropertyPurchaseEnoughGold() {
        Player playerBuyer = new Player("", null, Color.BLUE, 0);
        Property property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("tilePics/Wood.png"));

        int playerBalance = 61;

        playerBuyer.setBalance(playerBalance);
        playerBuyer.setNetWorth(playerBalance);

        manageEvents.propertyEvent(property, playerBuyer);

        assertEquals(property, playerBuyer.getProperties().get(0));
        assertEquals(playerBuyer.getBalance(), playerBalance);
        assertEquals(playerBuyer.getNetWorth(), playerBalance);
    }

    /**
     * Test that a player can purchase an unowned property when the player only has the exact amount of gold required
     *
     */
    @Test
    void testPropertyPurchaseJustEnoughGold() {
        Player playerBuyer = new Player("", null, null, 0);
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
     * Test that the an owned property is not able to be purchased by another player
     */
    @Test
    void testPropertyPurchaseNotAvailable() {

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
     * Tests if the owner of the property ranks up when being payed rent.
     */
    @Test
    void testPropertyOwnerRankUp()
    {

    }

    /**
     * Tests if the player ranks down when paying rent.
     */
    @Test
    void testPropertyPlayerRankDown()
    {

    }

    /**
     * Tests if the player gets eliminated when paying rent.
     */
    @Test
    void testPropertyPlayerElimination()
    {

    }
}
