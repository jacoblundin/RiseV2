package ModelTests;

import Model.player.Player;
import Model.player.PlayerRanks;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;

public class PlayerTests {

	private Player player;

	@BeforeEach
	void setup() {
		player = new Player("Adam", null, Color.BLUE, 0);
	}

	//Rank up, Peasant - Knight
	@Test
	void testRankUpKnight() {
		assertEquals(PlayerRanks.PEASANT, player.getPlayerRank());

		player.increaseBalance(500);
		player.increaseNetWorth(500);

		player.checkPlayerRank();

		assertEquals(PlayerRanks.KNIGHT, player.getPlayerRank());

	}

	//Rank up, Knight - Lord
	@Test
	void testRankUpLord() {
		player.setBalance(2000);
		player.setNetWorth(2000);
		player.checkPlayerRank();
		assertEquals(PlayerRanks.KNIGHT, player.getPlayerRank());

		player.increaseBalance(2000);
		player.increaseNetWorth(2000);

		player.checkPlayerRank();

		assertEquals(PlayerRanks.LORD, player.getPlayerRank());

	}

	//Rank up, Lord - King
	@Test
	void testRankUpKing() {
		player.setBalance(4000);
		player.setNetWorth(4000);
		player.checkPlayerRank();
		assertEquals(PlayerRanks.LORD, player.getPlayerRank());

		player.increaseBalance(3500);
		player.increaseNetWorth(3500);

		player.checkPlayerRank();

		assertEquals(PlayerRanks.KINGS, player.getPlayerRank());

	}

	//Rank down, Peasant - Knight
	@Test
	void testRankDownPeasant() {
		player.setBalance(2199);
		player.setNetWorth(2199);
		player.checkPlayerRank();
		assertEquals(PlayerRanks.KNIGHT, player.getPlayerRank());

		player.decreaseBalance(200);
		player.decreaseNetWorth(200);

		player.checkPlayerRank();

		assertEquals(PlayerRanks.PEASANT, player.getPlayerRank());

	}

	//Rank down, Knight - Lord
	@Test
	void testRankDownKnight() {
		player.setBalance(4000);
		player.setNetWorth(4000);
		player.checkPlayerRank();
		assertEquals(PlayerRanks.LORD, player.getPlayerRank());

		player.decreaseBalance(100);
		player.decreaseNetWorth(100);

		player.checkPlayerRank();

		assertEquals(PlayerRanks.KNIGHT, player.getPlayerRank());

	}

	/**
	 * Tests that passing go awards gold coins
	 */
	@Test
	void testPlayerPassGo()
	{
		fail("Test not implemented"); //Fail the test
	}

}
