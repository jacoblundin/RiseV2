package Controller;

import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;

import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.PopUpGUI.DeathGUI;
import View.PopUpGUI.FortuneTellerGUI;
import View.PopUpGUI.SecretGui;
import View.PopUpGUI.WinGui;
import Model.player.Player;
import Model.player.PlayerList;
import Model.player.PlayerRanks;
import Model.Tiles.FortuneTeller;
import Model.Tiles.GoToJail;
import Model.Tiles.Jail;
import Model.Tiles.Property;
import Model.Tiles.SundayChurch;
import Model.Tiles.Tavern;
import Model.Tiles.Tax;
import Model.Tiles.Tile;
import Model.Tiles.Work;
import gamehistorylog.GameHistoryLog;
import soundservice.SoundFx;
import soundservice.SoundService;

/**
 * The class handles all the Controller.events that occur when a Model.player lands on a tile.
 * @author Seth Oberg, Rohan Samandari,Muhammad Abdulkhuder ,Sebastian Viro, Aevan Dino.
 */

public class ManageEvents {
	private PlayerList playerList;
	private Board board;
	private GameFlowPanel gameFlowPanel;
	private DeathGUI deathGUI;
	private FortuneTellerGUI msgGUI;
	private EastSidePanel eastPanel;
	private Random rand = new Random();
	private int roll;
	private int taxCounter = 0;
	private WestSidePanel westPanel;
	private GameHistoryLog gameHistoryLog;
	private Controller controller;

	/**
	 * Constructor initializes objects in the parameter. Creates Death -and MessageGUI.
	 * @param board
	 * @param playerList
	 * @param pnlWest
	 * @param gameFlowPanel
	 * @param eastPanel
	 */
	public ManageEvents(Board board, PlayerList playerList, WestSidePanel pnlWest, GameFlowPanel gameFlowPanel,
						EastSidePanel eastPanel, Controller controller) {
		this.gameFlowPanel = gameFlowPanel;
		this.westPanel = pnlWest;
		this.board = board;
		this.playerList = playerList;
		this.eastPanel = eastPanel;
		deathGUI = new DeathGUI();
		msgGUI = new FortuneTellerGUI();
		gameHistoryLog = GameHistoryLog.instance();
		this.controller = controller;
	}

	/**
	 * Method checks what type of tile the Model.player has landed on.
	 * @param tile the Model.player landed on.
	 * @param player, Model.player who landed on a tile.
	 */
	public void newEvent(Tile tile, Player player) {
		player.checkPlayerRank();

		if (player.getPlayerRank() == PlayerRanks.KINGS) {
			new WinGui();
		}

		if (playerList.getLength() == 1) {
			new WinGui();
		}

		if (tile instanceof Property) {
			propertyEvent(tile, player);
		}

		if (tile instanceof Tax) {
			taxEvent(tile, player);
		}

		if (tile instanceof Jail) {
			jailEvent(tile, player);
		}

		if (tile instanceof GoToJail) {
			goToJailEvent(tile, player);
		}

		if (tile instanceof Tavern) {
			tavernEvent(tile, player);
		}

		if (tile instanceof SundayChurch) {
			churchEvent(player);
		}

		if (tile instanceof Work) {
			workEvent(tile, player);
		}

		if (tile instanceof FortuneTeller) {
			fortuneTellerEvent(tile, player);
		}
		eastPanel.addTabs();
	}

	/**
	 * This method is supposed to be called from any class that requires the current
	 * player to pay any amount, if the user does not have the amount required they
	 * should be removed from the game
	 */
	public void control(Player player, int amount) {

		if (player.getBalance() < amount) {
			player.setIsAlive(false);
			playerList.switchToNextPlayer();
			playerList.eliminatePlayer(player);
			playerList.updatePlayerList();
            eastPanel.addTabs();
			board.removePlayer(player);
			deathGUI.addGui();
			controller.drawBorderColors();
		}
	}

	/**
	 * Method called when player lands on a property. Checks if it's availability and if the Model.player has to pay rent or
	 * can purchase the property.
	 * @param tile
	 * @param player
	 */
	public void propertyEvent(Tile tile, Player player) {
		Property tempProperty = (Property) tile;
		int tempInt = 0;

		if (tempProperty.getPurchasable()) {
			if (player.getBalance() < tempProperty.getPrice()) {
				JOptionPane.showMessageDialog(null, "Not enough funds to purchase this property");
			} else {
				propertyDialog(tempProperty, player);
			}
		} else {

			//TODO Is this if statement necessary? The if and else bodies seem to do the same thing
			if (tempProperty.getLevel() == 0) {
				tempInt = tempProperty.getDefaultRent(); //TODO This is the only difference, but this should be possible to change

				control(player, tempInt);
				if (player.isAlive() == true) {
					JOptionPane.showMessageDialog(null, player.getName() + " paid " + tempProperty.getTotalRent() + " GC to "
							+ tempProperty.getOwner().getName());

					//Log rent event
					gameHistoryLog.logPropertyRentEvent(player, tempProperty);

					player.decreaseBalace(tempInt);
					player.decreaseNetWorth(tempInt);
					tempProperty.getOwner().increaseBalance(tempInt);
				}
			} else {
				tempInt = tempProperty.getTotalRent();
				control(player, tempInt);
				if (player.isAlive() == true) {
					JOptionPane.showMessageDialog(null, player.getName() + " paid " + tempProperty.getTotalRent() + " GC to "
							+ tempProperty.getOwner().getName());

					//Log rent event
					gameHistoryLog.logPropertyRentEvent(player, tempProperty);

					player.decreaseBalace(tempInt);
					tempProperty.getOwner().increaseBalance(tempInt);
				}
			}
		}
	}

	/**
	 * Method called when the player lands on a work tile.
	 * @param tile
	 * @param player
	 */
	public void workEvent(Tile tile, Player player) {

		Work tempWorkObject = (Work) tile;
		tempWorkObject.setPlayer(player);
		tempWorkObject.payPlayer(getRoll());

		//Log work event
		gameHistoryLog.logWorkEvent(player,tempWorkObject.getPay());

		SoundService.instance().playSoundFx(SoundFx.SOUND_WORK);

		JOptionPane.showMessageDialog(null,
				"The roll is " + roll + "\n" + "You got: " + tempWorkObject.getPay() + " GC for your hard work");

	}

	/**
	 * Method called when the player lands on a tax tile.
	 * @param tile
	 * @param player
	 */
	public void taxEvent(Tile tile, Player player) {
		Tax tempTaxObject = (Tax) tile;
		int chargePlayer = tempTaxObject.getTax();

		control(player, chargePlayer);

		if (player.isAlive()) {

			//Log tax event
			gameHistoryLog.logTaxEvent(player, 200);

			JOptionPane.showMessageDialog(null, "You paid 200 gold in tax to the Church");
			player.decreaseBalace(chargePlayer);
			player.decreaseNetWorth(chargePlayer);
			taxCounter++;

			SoundService.instance().playSoundFx(SoundFx.SOUND_MONEY);
		}
	}

	/**
	 * Gets the total tax paid by players
	 * @return total tax
	 */
	public int getChurchTax() {
		int totalTax = taxCounter * 200;
		return totalTax;
	}

	/**
	 * Method called when players lands on a tavern tile, checks it's availability.
	 * @param tile
	 * @param player
	 */
	public void tavernEvent(Tile tile, Player player) {
		Tavern tempTavernObj = (Tavern) tile;

		if (tempTavernObj.getPurchasable()) {
			if (player.getBalance() < tempTavernObj.getPrice()) {
				JOptionPane.showMessageDialog(null, "Not enough funds to purchase this tavern");
			} else {
				tavernDialog(tempTavernObj, player);
			}
		} else {
			int randomValue = 0;

			if (tempTavernObj.getOwner().getAmountOfTaverns() == 1) {
				randomValue = (getRoll() * 10);
			} else if (tempTavernObj.getOwner().getAmountOfTaverns() == 2) {
				randomValue = (getRoll() * 20);
			}

			control(player, randomValue);
			if (player.isAlive() == true) {
				JOptionPane.showMessageDialog(null, player.getName() + " paid " + randomValue + " GC to "
						+ tempTavernObj.getOwner().getName());
				//TODO Log tavern rent
				/*westPanel.append(player.getName() + " paid " + randomValue + " GC to "
						+ tempTavernObj.getOwner().getName() + "\n");*/
				tempTavernObj.getOwner().increaseBalance(randomValue);
				tempTavernObj.getOwner().increaseNetWorth(randomValue);
				player.decreaseBalace(randomValue);
			}
		}
	}

	/**
	 * Method for jailed players, giving them the option to pay bail if the have enough balance.
	 * @param tile
	 * @param player in jail
	 */
	public void jailEvent(Tile tile, Player player) {
		if (player.isPlayerInJail() == true && (player.getJailCounter()) < 2) {
			player.increaseJailCounter();
			if (player.getBalance() > (player.getJailCounter() * 50)) {
				jailDialog(player);
			} else {
				JOptionPane.showMessageDialog(null, "You can not afford the bail");

				SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON);

			}
		} else if (player.getJailCounter() >= 2) {
			player.setPlayerIsInJail(false);
			player.setJailCounter(0);

			//Log jail exit event
			gameHistoryLog.logJailExitEvent(player);

			gameFlowPanel.activateRollDice();
		}
	}

	/**
	 * Method to jail a player.
	 * @param tile
	 * @param player
	 */
	public void goToJailEvent(Tile tile, Player player) {
		player.setPlayerIsInJail(true);
		board.removePlayer(player);
		player.setPositionInSpecificIndex(10);
		board.setPlayer(player);
		JOptionPane.showMessageDialog(null, player.getName() + " got in jail.");
		SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON2);
		SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON3);


		//Log the event
		gameHistoryLog.logJailEnterEvent(player);
	}

	/**
	 * Method called if the player lands on sunday church. Pays out all the collected tax then resets the counter.
	 * @param player
	 */
	public void churchEvent(Player player) {
		//TODO church sound
		int taxPayout = 200*taxCounter;
		player.increaseBalance(taxPayout);
		player.increaseNetWorth(taxPayout);
		taxCounter = 0;

		//Log tax payout event
		gameHistoryLog.logTaxPayoutEvent(player, taxPayout);
	}

	/**
	 * Method for a dialog if the player is able to purchase a property.
	 * @param property in question.
	 * @param player in question.
	 */
	public void propertyDialog(Property property, Player player) {
		int yesOrNo = JOptionPane.showConfirmDialog(null,
				property.getName() + "\n" + "Do you want to purchase this property for " + property.getPrice() + " GC",
				"Decide your fate!", JOptionPane.YES_NO_OPTION);

		if (yesOrNo == 0 && (property.getPrice() <= player.getBalance())) {
			property.setOwner(player);
			player.addNewProperty(property);
			player.decreaseBalace(property.getPrice());
			gameHistoryLog.logPropertyBuyEvent(player, property);
			controller.drawBorderColors();
		} else {
			//Player did not purchase the property
			//TODO: Should this be logged?
		}
	}

	/**
	 * Method for a dialog if the player wants to purchase a tavern.
	 * @param tavern, the to buy.
	 * @param player, Model.player who landed on the tavern.
	 */
	public void tavernDialog(Tavern tavern, Player player) {
		int yesOrNo = JOptionPane.showConfirmDialog(null, "Do you want to purchase this property?", "JOption",
				JOptionPane.YES_NO_OPTION);

		if (yesOrNo == 0 && (tavern.getPrice() <= player.getBalance())) {
			tavern.setOwner(player);
			player.addNewTavern(tavern);
			player.decreaseBalace(tavern.getPrice());
			controller.drawBorderColors();
			//TODO Log tavern purchase
			//gameHistoryLog.logPropertyBuyEvent(player, tavern);
		} else {
			//Player did not purchase the tavern
			//TODO: Should this be logged?
		}
	}

	/**
	 * @return roll of the gameFlowPanel.
	 */
	public int getRoll() {
		return roll;
	}

	/**
	 * Sets the roll of the gameFlowPanel.
	 * @param roll combined value of the dice roll.
	 */
	public void setRoll(int roll) {
		this.roll = roll;
	}

	/**
	 * Message for the prisoner to choose if the Model.player wants to pay the bail and
	 * get free
	 * @param player in jail.
	 */
	public void jailDialog(Player player) {
		int yesOrNo = JOptionPane.showConfirmDialog(null,
				"Do you want to pay the bail\nWhich is " + (player.getJailCounter() * 50) + " GC?", "JOption",
				JOptionPane.YES_NO_OPTION);
		int totalBail = player.getJailCounter() * 50;
		if (yesOrNo == 0 && (totalBail <= player.getBalance())) {
			player.setJailCounter(0);
			player.setPlayerIsInJail(false);
			gameHistoryLog.logJailExitEvent(player);
			gameFlowPanel.activateRollDice();

			SoundService.instance().playSoundFx(SoundFx.SOUND_CHEER);

		} else {
			//Stay in jail

			SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON);

			//Log stay in jail
			gameHistoryLog.logJailStayEvent(player);
		}
	}

	/**
	 * Method for FortuneTeller, small chance for a secret event to trigger.
	 * @param tile, tile the Model.player landed on.
	 * @param player, Model.player in question.
	 */
	private void fortuneTellerEvent(Tile tile, Player player) {
		FortuneTeller tempCard = (FortuneTeller) tile;
		if (rand.nextInt(10) == 0) {
			new SecretGui();
			new Thread(new SecretSleeper(tempCard, player));
			//eastPanel.addPlayerList(playerList);
			eastPanel.addTabs();

			SoundService.instance().playSoundFx(SoundFx.SOUND_WITCH);

		} else {
			fortune(tempCard, player);
		}
	}

	/**
	 * Method that either withdraws or adds gold coins to a player depending on the type of fortune.
	 * @param tempCard, instance of FortuneTeller
	 * @param player, Model.player who landed on the tile
	 */
	public void fortune(FortuneTeller tempCard, Player player) {
		tempCard.setAmount(rand.nextInt(600) - 300);
		if (tempCard.getAmount() < 0) {
			int pay = (tempCard.getAmount() * -1);
			tempCard.setIsBlessing(false);
			tempCard.setFortune("CURSE");
			control(player, pay);
			if (player.isAlive()) {
				//TODO: Log Fortune event
				player.decreaseBalace(pay);
				player.decreaseNetWorth(pay);
				msgGUI.newFortune(false, pay);

				//TODO curse sound
			}

		} else {
			tempCard.setIsBlessing(true);
			tempCard.setFortune("BLESSING");
			player.increaseBalance(tempCard.getAmount());
			player.increaseNetWorth(tempCard.getAmount());
			//TODO: Log Fortune event
			msgGUI.newFortune(true, tempCard.getAmount());
			SoundService.instance().playSoundFx(SoundFx.SOUND_CHEER);
		}
	}

	/**
	 * This class is an easter egg. That gives the player 5 fortunes.
	 * @author Sebastian viro ,Muhammad Abdulkhuder
	 *
	 */
	private class SecretSleeper extends Thread {

		private FortuneTeller tempCard;
		private Player player;
		private Clip clip;

		/**
		 * @param tempCard
		 * @param player
		 * Starts the sleeper
		 */
		public SecretSleeper(FortuneTeller tempCard, Player player) {
			this.tempCard = tempCard;
			this.player = player;
			start();

		}

		public void run() {
			try {
				for (int i = 0; i < 5; i++) {
					File musicPath = new File("music/duraw.wav");
					AudioInputStream ais = AudioSystem.getAudioInputStream(musicPath);
					clip = AudioSystem.getClip();
					clip.open(ais);

					FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					float dB = (float) (Math.log(0.08) / Math.log(10.0) * 20.0);
					gainControl.setValue(dB);

					clip.start();
					fortune(tempCard, player);
					Thread.sleep(3000);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}
	}
}
