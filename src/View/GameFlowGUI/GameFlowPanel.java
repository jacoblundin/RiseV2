package View.GameFlowGUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controller.ManageEvents;
import Model.player.Player;
import View.BoardGUI.ShowPlayersTurn;
import View.WestGUI.WestSidePanel;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import Model.player.PlayerList;
import dice.Dice;
import gamehistorylog.GameHistoryLog;

/**
 * @author Muhammad Abdulkhuder, Aevan Dino, Sebastian Viro, Seth Oberg
 */
public class GameFlowPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ShowPlayersTurn showPlayersTurn;
	private Board board;
	private PlayerList playerList;
	private WestSidePanel westSidePnl;
	private EastSidePanel eastSidePnl;
	private Dice dice;
	private Thread movePlayerThread;
	private ManageEvents manageEvents;
	private JButton btnEndTurn = new JButton("End Turn");
	private JButton btnRollDice = new JButton("Roll Dice");
	private int roll;

	/**
	 * Constructor
	 *
	 * @param board         The board object
	 * @param playerList    A list containing all the players in the game
	 * @param westSidePanel Panel containing all the information about the Model.tileCollection.tiles and the history of all the Controller.events
	 * @param eastSidePnl   Panel containing all the information about the players and their properties
	 */
	public GameFlowPanel(Board board, PlayerList playerList, WestSidePanel westSidePanel, EastSidePanel eastSidePnl) {
		this.board = board;
		this.playerList = playerList;
		this.westSidePnl = westSidePanel;
		this.eastSidePnl = eastSidePnl;
		this.dice = new Dice();

		initializePanel();

		showPlayersTurn.updateGUI(playerList.getActivePlayer().getName(),
				playerList.getActivePlayer().getPlayerColor());
	}

	/**
	 * initializes Panel
	 */
	public void initializePanel() {

		setPreferredSize(new Dimension(650, 120));
		setLayout(new FlowLayout());
		setOpaque(false);

		showPlayersTurn = new ShowPlayersTurn("Player");
		add(showPlayersTurn);

		add(dice.getDicePanel());

		btnRollDice.setFont(new Font("Algerian", Font.PLAIN, 14));
		btnEndTurn.setFont(new Font("Algerian", Font.PLAIN, 14));

		initActionListeners();

		add(btnRollDice);
		add(btnEndTurn);
		//add(new CheatGui(this));
		btnEndTurn.setEnabled(false);
	}

	/**
	 * This method initiates the action listeners of the buttons in this panel.
	 */
	private void initActionListeners() {

		btnRollDice.addActionListener((event) -> {

			btnRollDice.setEnabled(false);

			//Call the old code to make everything work
			dice.roll((sumRoll) -> legacyCodeRollDice(sumRoll));
		});

		btnEndTurn.addActionListener((event) -> {
			legacyCodeEndTurn();
		});

	}

	//This is old code that needs to be refactored
	private void legacyCodeRollDice(int sumRoll) {
		Player activePlayer = playerList.getActivePlayer();

		setRoll(sumRoll);

		activePlayer.checkPlayerRank();
		manageEvents.setRoll(this.getRoll());

		movePlayerThread = new Thread(new LoopThread(this.getRoll()));
		movePlayerThread.start();

		goEvent();

		eastSidePnl.addTabs();

	}

	//This is old code that needs to be refactored
	private void legacyCodeEndTurn() {

		/*
		 * When a player ends their turn
		 * If the next player is in jail they will not have the ability to roll the
		 * dice and will only have the ability to end their turn if they have not paid the bail
		 * If the player is not in jail they can roll the dice
		 */

		playerList.switchToNextPlayer();

		showPlayersTurn.updateGUI(playerList.getActivePlayer().getName(),
				playerList.getActivePlayer().getPlayerColor());

		if (playerList.getActivePlayer().isPlayerInJail()) {
			btnRollDice.setEnabled(false);
			btnEndTurn.setEnabled(true);
			manageEvents.newEvent(board.getDestinationTile(playerList.getActivePlayer().getPosition()),
					playerList.getActivePlayer());
		} else if (!playerList.getActivePlayer().isPlayerInJail()) {
			btnRollDice.setEnabled(true);
			btnEndTurn.setEnabled(false);
		}

		eastSidePnl.addTabs();
		eastSidePnl.setTab();
	}

	/**
	 * Method used for Testing
	 * Moves the active player to a specific index
	 *
	 * @param i index the player should be moved to.
	 */
	public void moveWCheat(int i) {

		setRoll(i);
		playerList.getActivePlayer().checkPlayerRank();
		board.removePlayer(playerList.getActivePlayer());
		playerList.getActivePlayer().setPosition(getRoll());
		board.setPlayer(playerList.getActivePlayer());


		manageEvents.setRoll(this.getRoll());
		goEvent();
		manageEvents.newEvent(board.getDestinationTile(playerList.getActivePlayer().getPosition()),
				playerList.getActivePlayer());
		eastSidePnl.addTabs();
	}


	/**
	 * To free the prisoner
	 */
	public void activateRollDice() {
		btnRollDice.setEnabled(true);
		btnEndTurn.setEnabled(false);
	}

	/**
	 * @return number of total roll
	 */
	public int getRoll() {
		return roll;
	}

	/**
	 * @param roll number of total roll
	 */
	public void setRoll(int roll) {
		this.roll = roll;
	}

	public void setController(ManageEvents manageEvents) {
		this.manageEvents = manageEvents;
	}

	/**
	 * @author Seth �berg, Muhammad Abdulkhuder
	 * Moves the player with a thread.
	 */
	private class LoopThread implements Runnable {
		//TODO: Needs refactoring, this should only graphically move the players piece.
		// But it handles more than just the view, which is unnecessary.
		int roll;
		Player activePlayer;

		public LoopThread(int roll) {
			this.roll = roll;
			this.activePlayer = playerList.getActivePlayer();
		}

		public void run() {

			for (int i = 0; i < roll; i++) {
				board.removePlayer(activePlayer);
				activePlayer.setPosition(1);
				board.setPlayer(activePlayer);

				if (i == (roll - 1)) {
					//Log dice roll event
					GameHistoryLog.instance().logDiceRollEvent(activePlayer, board.getDestinationTile(activePlayer.getPosition()), roll);

					//Create the event the landing tile
					manageEvents.newEvent(board.getDestinationTile(activePlayer.getPosition()),
							activePlayer);

					eastSidePnl.addTabs();
					btnEndTurn.setEnabled(true);

				}

				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * If a player passes go.
	 */
	private void goEvent() {
		//TODO: This method gets called the round after the player has passed go, this should happen the
		// same round as the player passes go

		Player activePlayer = playerList.getActivePlayer();
		int payout = 200;

		if (activePlayer.passedGo()) {

			activePlayer.increaseBalance(200);
			activePlayer.increaseNetWorth(200);

			//Log the pass go event
			GameHistoryLog.instance().logPassGoEvent(activePlayer, payout);
			activePlayer.resetPassedGo();
		}
	}
}