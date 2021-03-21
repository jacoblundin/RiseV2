package View.GameFlowGUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import Controller.Controller;
import Cheat.cheat.CheatGui;
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
	private Controller controller;
	private JButton btnEndTurn = new JButton("End Turn");
	private JButton btnRollDice = new JButton("Roll Dice");

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
		btnEndTurn.setEnabled(false);
	}

	public void updateShowPlayersTurn() {
		showPlayersTurn.updateGUI(playerList.getActivePlayer().getName(),
				playerList.getActivePlayer().getPlayerColor());
	}

	private void initActionListeners() {
		btnRollDice.addActionListener((event) -> controller.rollDice());
		btnEndTurn.addActionListener((event) -> controller.endTurn());
	}

	public void setRollButton(boolean flag) {
		btnRollDice.setEnabled(flag);
	}

	public void setEndTurnButton(boolean flag) {
		btnEndTurn.setEnabled(flag);
	}

	public void setCheatGUI(CheatGui cheatGUI) {
		add(cheatGUI);
	}

	public void activateRollDice() {
		btnRollDice.setEnabled(true);
		btnEndTurn.setEnabled(false);
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Dice getDice() {
		return dice;
	}
}