package ModelTests;

import Model.Tiles.Property;
import Model.player.Player;

import Model.player.PlayerRanks;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

public class PropertyTests {

	private Player playerOwner;
	private Property property;

	@BeforeEach
	void setup() {
		playerOwner = new Player("JohnDoe", null, Color.BLUE, 0);
		property = new Property("Wood Cutter Camp", 60, 2, 30, new Color(58,20,56,255), 50,new ImageIcon("images/tilePics/Wood.png"));
	}


	//Test upgrade
	@Test
	void testUpgradeProperty() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);

		int propertyLevelBefore = property.getLevel();
		int propertyLevelAfter = propertyLevelBefore + 1;
		int ownerGoldBefore = playerOwner.getBalance();
		int ownerGoldAfter = ownerGoldBefore - property.getLevelPrice();

		property.increaseLevel(property);

		assertEquals(propertyLevelAfter, property.getLevel());
		assertEquals(ownerGoldAfter, playerOwner.getBalance());

	}

	//Test downgrade
	@Test
	void testDowngradeProperty() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);
		property.increaseLevel(property);

		int propertyLevelBefore = property.getLevel();
		int propertyLevelAfter = propertyLevelBefore - 1;

		property.decreaseLevel(property);

		assertEquals(propertyLevelAfter, property.getLevel());

	}

	/**
	 * Test that a player receives the intended gold coin refund amount when downgrading a property
	 * F 5.10
	 */
	@Test
	void testDowngradeRefund() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);
		property.increaseLevel(property);

		int initialGoldCoins = playerOwner.getBalance();
		int initialNetWorth = playerOwner.getNetWorth();
		int expectedGoldCoins = initialGoldCoins + (property.getLevelPrice() / 2);

		//The owners net worth is negatively impacted by downgrading a property.
		//This is because the full cost of an upgrade is counted towards net worth, and only half of that is refunded
		int expectedNetWorth = initialNetWorth - (property.getLevelPrice() / 2);

		property.decreaseLevel(property);

		assertEquals(expectedGoldCoins, playerOwner.getBalance());
		assertEquals(expectedNetWorth, playerOwner.getNetWorth());

	}

	//Test downgrade when grade 0
	@Test
	void testDowngradePropertyWhenGrade0() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);

		int propertyLevelAfter = property.getLevel();
		int ownerGoldAfter = playerOwner.getBalance();
		int ownerNetWorthAfter = playerOwner.getNetWorth();

		property.decreaseLevel(property);

		assertEquals(propertyLevelAfter, property.getLevel());
		assertEquals(ownerGoldAfter, playerOwner.getBalance());
		assertEquals(ownerNetWorthAfter, playerOwner.getNetWorth());

	}

	//Test upgrade when not enough gold
	@Test
	void testUpgradePropertyNotEnoughGold() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);

		playerOwner.setBalance(10);
		playerOwner.setNetWorth(playerOwner.getBalance() + property.getPrice());

		int ownerBalanceExpected = playerOwner.getBalance();
		int ownerNetWorthExpected = playerOwner.getNetWorth();
		int propertyLevelExpected = property.getLevel();

		property.increaseLevel(property);

		assertEquals(ownerBalanceExpected, playerOwner.getBalance());
		assertEquals(ownerNetWorthExpected, playerOwner.getNetWorth());
		assertEquals(propertyLevelExpected, property.getLevel());

	}

	//Test rent increase when upgraded

	//Test max upgrade when peasant
	@Test
	void testMaxUpgradeLevelPeasant() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);

		playerOwner.setBalance(1500);
		playerOwner.setNetWorth(1500);
		playerOwner.checkPlayerRank();

		assertEquals(PlayerRanks.PEASANT, playerOwner.getPlayerRank()); // Make sure the owner is the expected rank

		int maxUpgradeLevel = playerOwner.getPlayerRank().nbrOfLevels();

		//Attempt to upgrade the property one more time than max upgrade
		for (int i = 0; i <= maxUpgradeLevel; i++) {
			property.increaseLevel(property);
		}

		assertEquals(maxUpgradeLevel, property.getLevel()); // Assert that the property did not upgrade past max level
	}

	//Test max upgrade when knight
	@Test
	void testMaxUpgradeLevelKnight() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);

		playerOwner.setBalance(2500);
		playerOwner.setNetWorth(2500);
		playerOwner.checkPlayerRank();

		assertEquals(PlayerRanks.KNIGHT, playerOwner.getPlayerRank()); // Make sure the owner is the expected rank

		int maxUpgradeLevel = playerOwner.getPlayerRank().nbrOfLevels();

		//Attempt to upgrade the property one more time than max upgrade
		for (int i = 0; i <= maxUpgradeLevel; i++) {
			property.increaseLevel(property);
		}

		assertEquals(maxUpgradeLevel, property.getLevel()); // Assert that the property did not upgrade past max level
	}

	//Test max upgrade when lord
	@Test
	void testMaxUpgradeLevelLord() {
		property.setOwner(playerOwner);
		playerOwner.addNewProperty(property);

		playerOwner.setBalance(5000);
		playerOwner.setNetWorth(5000);
		playerOwner.checkPlayerRank();

		assertEquals(PlayerRanks.LORD, playerOwner.getPlayerRank()); // Make sure the owner is the expected rank

		int maxUpgradeLevel = playerOwner.getPlayerRank().nbrOfLevels();

		//Attempt to upgrade the property one more time than max upgrade
		for (int i = 0; i <= maxUpgradeLevel; i++) {
			property.increaseLevel(property);
		}

		assertEquals(maxUpgradeLevel, property.getLevel()); // Assert that the property did not upgrade past max level
	}


}
