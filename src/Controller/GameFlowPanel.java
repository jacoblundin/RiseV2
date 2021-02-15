package Controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

import UnusedClasses.cheat.CheatGui;
import View.BoardGUI.ShowPlayersTurn;
import View.WestGUI.WestSidePanel;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import Model.player.PlayerList;
import dice.Dice;


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
	 * @param playerList method used for updating the list of players
	 */
	public void addPlayerList(PlayerList playerList) {

		this.playerList = playerList;

		showPlayersTurn.uppdateGUI(playerList.getActivePlayer().getName(),
				playerList.getActivePlayer().getPlayerColor());

		manageEvents = new ManageEvents(board, playerList, westSidePnl, this, eastSidePnl);

	}

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

		setRoll(sumRoll);

		playerList.getActivePlayer().checkPlayerRank();
		manageEvents.setRoll(this.getRoll());

		movePlayerThread = new Thread(new LoopThread(this.getRoll()));
		movePlayerThread.start();

		goEvent();

		eastSidePnl.addPlayerList(playerList);

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

		showPlayersTurn.uppdateGUI(playerList.getActivePlayer().getName(),
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

		eastSidePnl.addPlayerList(playerList);
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
		eastSidePnl.addPlayerList(playerList);
	}


	/**
	 * To free the prisoner
	 */
	public void activateRollDice() {
		btnRollDice.setEnabled(true);
		btnEndTurn.setEnabled(false);
	}

	/**
	 * Ends the turn if player is eliminated
	 */
	public void endTurnIfPlayerEliminated() {
		btnRollDice.setEnabled(true);
		btnEndTurn.setEnabled(false);
	}

	/**
	 * @param playerList
	 */
	public void setPlayerList(PlayerList playerList) {
		this.playerList = playerList;
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

	/**
	 * @author Seth �berg, Muhammad Abdulkhuder
	 * Moves the player with a thread.
	 */
	private class LoopThread implements Runnable {

		int roll;

		public LoopThread(int roll) { this.roll = roll; }

		public void run() {

			for (int i = 0; i < roll; i++) {
				board.removePlayer(playerList.getActivePlayer());
				playerList.getActivePlayer().setPosition(1);
				board.setPlayer(playerList.getActivePlayer());

				if (i == (roll - 1)) {
					manageEvents.newEvent(board.getDestinationTile(playerList.getActivePlayer().getPosition()),
							playerList.getActivePlayer());
					eastSidePnl.addPlayerList(playerList);
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

		if (playerList.getActivePlayer().passedGo()) {

			playerList.getActivePlayer().increaseBalance(200);
			playerList.getActivePlayer().increaseNetWorth(200);

			westSidePnl.append("Passed Go and received 200 GC\n");
			playerList.getActivePlayer().resetPassedGo();
		}
	}
}