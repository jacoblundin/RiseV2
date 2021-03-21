package ManageEventTests;

import Controller.Controller;
import Controller.ManageEvents;
import Model.Tiles.Tavern;
import Model.Tiles.TileCollection;
import Model.player.Player;
import Model.player.PlayerList;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;
import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TavernEventTests {

    private Board board;
    private PlayerList playerList;
    private WestSidePanel westSidePanel;
    private GameFlowPanel gameFlowPanel;
    private EastSidePanel eastSidePanel;
    private Controller controller;

    private ManageEvents manageEvents;

    @BeforeEach
    void setup() {

        //Initialize dependants
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
     * Test that a player can purchase an unowned tavern when they have enough gold to do so
     */
    @Test
    void buyTavernEnoughMoney()
    {
        Player player = new Player("Jane", null, Color.GREEN, 0);
        Tavern tavern = new Tavern("Tavern", 150);

        int playerOwnedTaverns = player.getAmountOfTaverns();
        int playerBalance = 200;

        player.setBalance(playerBalance);

        manageEvents.tavernEvent(tavern, player);

        //Checks that the players amount of taverns has increased by one
        assertEquals(playerOwnedTaverns+1, player.getAmountOfTaverns());

        //Checks that the players balance has decreased by 150
        assertEquals(playerBalance-150, player.getBalance());
    }

    /**
     * Test that a player can't purchase an unowned tavern when they don't have enough gold to do so
     */
    @Test
    void buyTavernNotEnoughMoney()
    {
        Player player = new Player("Jane", null, Color.GREEN, 0);
        Tavern tavern = new Tavern("Tvern", 150);

        int playerOwnedTaverns = player.getAmountOfTaverns();
        int playerBalance = 100;

        player.setBalance(playerBalance);

        manageEvents.tavernEvent(tavern, player);

        //Checks that the players amount of taverns has not increased/decreased
        assertEquals(playerOwnedTaverns, player.getAmountOfTaverns());

        //Checks that the players balance has not decreased/increased
        assertEquals(playerBalance, player.getBalance());
    }

    /**
     * Test that a player has to pay the expected amount of rent when other player owns one tavern
     */
    @Test
    void payRentOneOwnedTavernCheckBalance()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavern = new Tavern("Tavern", 150);

        int playerBalance = 200;
        int expectedRent = 5*10;

        playerPayer.setBalance(playerBalance);
        manageEvents.setRoll(5);
        tavern.setOwner(playerOwner);
        playerOwner.addNewTavern(tavern);

        manageEvents.tavernEvent(tavern, playerPayer);

        //Checks that the players balance has decreased by the expected rent
        assertEquals(playerBalance-expectedRent, playerPayer.getBalance());

    }

    /**
     * Test that a players net worth decreases when paying rent
     */
    @Test
    void payRentOneOwnedTavernCheckNetWorth()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavern = new Tavern("Tavern", 150);

        int playerBalance = 200;
        int expectedRent = 5*10;

        playerPayer.setBalance(playerBalance);
        playerPayer.setNetWorth(playerBalance);
        manageEvents.setRoll(5);
        tavern.setOwner(playerOwner);
        playerOwner.addNewTavern(tavern);

        manageEvents.tavernEvent(tavern, playerPayer);

        //Checks that the players net worth has decreased by the expected rent
        assertEquals(playerBalance-expectedRent, playerPayer.getNetWorth());

    }

    /**
     * Test that a player has to pay the expected amount of rent when other player owns two taverns
     */
    @Test
    void payRentTwoOwnedTavernsCheckBalance()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavernOne = new Tavern("Tavern1", 150);
        Tavern tavernTwo = new Tavern("Tavern2", 150);


        int playerBalance = 200;
        int expectedRent = 5*20;

        playerPayer.setBalance(playerBalance);
        manageEvents.setRoll(5);
        tavernOne.setOwner(playerOwner);
        tavernTwo.setOwner(playerOwner);
        playerOwner.addNewTavern(tavernOne);
        playerOwner.addNewTavern(tavernTwo);

        manageEvents.tavernEvent(tavernOne, playerPayer);

        //Checks that the players balance has decreased by the expected rent
        assertEquals(playerBalance-expectedRent, playerPayer.getBalance());
    }

    /**
     * Test that a players net worth decreases when paying rent
     */
    @Test
    void payRentTwoOwnedTavernsCheckNetWorth()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavernOne = new Tavern("Tavern1", 150);
        Tavern tavernTwo = new Tavern("Tavern2", 150);


        int playerBalance = 200;
        int expectedRent = 5*20;

        playerPayer.setBalance(playerBalance);
        playerPayer.setNetWorth(playerBalance);
        manageEvents.setRoll(5);
        tavernOne.setOwner(playerOwner);
        tavernTwo.setOwner(playerOwner);
        playerOwner.addNewTavern(tavernOne);
        playerOwner.addNewTavern(tavernTwo);

        manageEvents.tavernEvent(tavernOne, playerPayer);

        //Checks that the players balance has decreased by the expected rent
        assertEquals(playerBalance-expectedRent, playerPayer.getNetWorth());
    }

    /**
     * Tests that a tavern owners balance increases from rent when owning one tavern
     */
    @Test
    void getRentOneOwnedTavernCheckBalance()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavern = new Tavern("Tavern", 150);

        int playerBalance = 200;
        int expectedRent = 5*10;

        playerPayer.setBalance(playerBalance);
        playerOwner.setBalance(playerBalance);
        manageEvents.setRoll(5);
        tavern.setOwner(playerOwner);
        playerOwner.addNewTavern(tavern);

        manageEvents.tavernEvent(tavern, playerPayer);

        //Checks that the owners balance has increased by the expected rent
        assertEquals(playerBalance+expectedRent, playerOwner.getBalance());
    }

    /**
     * Tests that a tavern owners net worth increases from rent when owning one tavern
     */
    @Test
    void getRentOneOwnedTavernCheckNetWorth()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavern = new Tavern("Tavern", 150);

        int playerBalance = 200;
        int expectedRent = 5*10;

        playerPayer.setBalance(playerBalance);
        playerPayer.setNetWorth(playerBalance);
        playerOwner.setBalance(playerBalance);
        playerOwner.setNetWorth(playerBalance);
        manageEvents.setRoll(5);
        tavern.setOwner(playerOwner);
        playerOwner.addNewTavern(tavern);

        manageEvents.tavernEvent(tavern, playerPayer);

        assertEquals(playerBalance+expectedRent, playerOwner.getNetWorth());
    }

    /**
     * Tests that a tavern owners balance increases from rent when owning two taverns
     */
    @Test
    void getRentTwoOwnedTavernsCheckBalance()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavernOne = new Tavern("Tavern", 150);
        Tavern tavernTwo = new Tavern("Tavern", 150);

        int playerBalance = 200;
        int expectedRent = 5*20;

        playerPayer.setBalance(playerBalance);
        playerOwner.setBalance(playerBalance);
        manageEvents.setRoll(5);
        tavernOne.setOwner(playerOwner);
        tavernTwo.setOwner(playerOwner);
        playerOwner.addNewTavern(tavernOne);
        playerOwner.addNewTavern(tavernTwo);

        manageEvents.tavernEvent(tavernOne, playerPayer);

        assertEquals(playerBalance+expectedRent, playerOwner.getBalance());
    }

    /**
     * Tests that a tavern owners net worth increases from rent when owning two taverns
     */
    @Test
    void getRentTwoOwnedTavernsCheckNetWorth()
    {
        Player playerPayer = new Player("Jane", null, Color.RED, 0);
        Player playerOwner = new Player("Joe", null, Color.GREEN, 0);
        Tavern tavernOne = new Tavern("Tavern", 150);
        Tavern tavernTwo = new Tavern("Tavern", 150);

        int playerBalance = 200;
        int expectedRent = 5*20;

        playerPayer.setBalance(playerBalance);
        playerPayer.setNetWorth(playerBalance);
        playerOwner.setBalance(playerBalance);
        playerOwner.setNetWorth(playerBalance);
        manageEvents.setRoll(5);
        tavernOne.setOwner(playerOwner);
        tavernTwo.setOwner(playerOwner);
        playerOwner.addNewTavern(tavernOne);
        playerOwner.addNewTavern(tavernTwo);

        manageEvents.tavernEvent(tavernOne, playerPayer);

        assertEquals(playerBalance+expectedRent, playerOwner.getNetWorth());
    }


    /**
     * Test that a player doesn't have to pay rent when landing on its own tavern
     */
    @Test
    void noRentOnOwnTavern()
    {
        Player player = new Player("Jane", null, Color.RED, 0);
        Tavern tavern = new Tavern("Tavern", 150);

        int playerBalance = 200;

        player.setBalance(playerBalance);
        manageEvents.setRoll(5);
        tavern.setOwner(player);
        player.addNewTavern(tavern);

        manageEvents.tavernEvent(tavern, player);

        //Checks that the players balance hasn't decreased/increased
        assertEquals(playerBalance, player.getBalance());

    }


}
