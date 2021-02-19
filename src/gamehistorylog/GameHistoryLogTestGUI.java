package gamehistorylog;

import Model.Tiles.Property;
import Model.Tiles.Tile;
import Model.player.Player;

import javax.swing.*;
import java.awt.*;

/**
 * GameHistoryLogTestGUI can be used to test the GameHistoryLog and GameHistoryPanel
 *
 * This class mainly provides way to test the formatting of log entries and how they are displayed in the
 * GameHistoryPanel. It doesn't test how the components really interact with the rest of the System and its data.
 * Or how well the actual logging of events in the game have been implemented.
 */
public class GameHistoryLogTestGUI extends JPanel {

	private Player activePlayer;
	private Player playerOwner;
	private Player player1;
	private Player player2;
	private Tile landingTile;

	private GameHistoryLog gameHistoryLog;
	private GameHistoryPanel gameHistoryPanel;

	//Graphical components
	private JPanel pnlRight = new JPanel();

	private JLabel lblPlayerSelect = new JLabel("Player Selection");
	private JButton btnPlayer1 = new JButton("Player 1");
	private JButton btnPlayer2 = new JButton("Player 2");

	private JLabel lblRoll = new JLabel("Roll");
	private JButton btnRoll = new JButton("Roll");
	private JButton btnDoubleRoll = new JButton("Double Roll");

	private JLabel lblGoldCoin = new JLabel("Gold coin");
	private JButton btnWork = new JButton("Work");
	private JButton btnRent = new JButton("Rent");
	private JButton btnPayTax = new JButton("Pay Tax");
	private JButton btnTaxPayout = new JButton("Tax payout");
	private JButton btnPassGo = new JButton("Pass go");

	private JLabel lblProperty = new JLabel("Property");
	private JButton btnPropertyBuy = new JButton("Buy");
	private JButton btnPropertySell = new JButton("Sell");
	private JButton btnPropertyUpgrade = new JButton("Upgrade");
	private JButton btnPropertyDownGrade = new JButton("Downgrade");

	private JLabel lblPlayerStatus = new JLabel("Player status");
	private JButton btnPlayerRankUp = new JButton("Rank up");
	private JButton btnPlayerRankDown = new JButton("Rank down");
	private JButton btnPlayerEliminated = new JButton("Eliminated");

	private JLabel lblFortune = new JLabel("Fortune");
	private JButton btnFortuneBlessing = new JButton("Blessing");
	private JButton btnFortuneCurse = new JButton("Curse");

	private JLabel lblJail = new JLabel("Jail");
	private JButton btnJailEnter = new JButton("Enter Jail");
	private JButton btnJailExit = new JButton("Exit Jail");
	private JButton btnJailStay = new JButton("Stay Jail");

	public GameHistoryLogTestGUI(GameHistoryLog gameHistoryLog) {

		this.player1 = new Player("Tom", new ImageIcon(), new Color(61, 90, 135), 1);
		this.player2 = new Player("Jerry", new ImageIcon(), new Color(135, 100, 62), 2);
		this.playerOwner = new Player("Spike", new ImageIcon(), new Color(168, 168, 168), 3);
		this.activePlayer = player1;
		this.landingTile = new Property("Warehouse",220 ,18 , 130, new Color(169, 60, 48, 255),150, new ImageIcon("tilePics/warehouse.png"));
		((Property) landingTile).setOwner(this.playerOwner);

		this.gameHistoryLog = gameHistoryLog;
		this.gameHistoryPanel = gameHistoryLog.getGameHistoryPanel();

		pnlRight.setBackground(new Color(150, 150, 150));
		pnlRight.setPreferredSize(new Dimension(380, 900));

		lblPlayerSelect.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblPlayerSelect);
		pnlRight.add(btnPlayer1);
		pnlRight.add(btnPlayer2);

		lblRoll.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblRoll);
		pnlRight.add(btnRoll);
		pnlRight.add(btnDoubleRoll);

		lblGoldCoin.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblGoldCoin);
		pnlRight.add(btnWork);
		pnlRight.add(btnRent);
		pnlRight.add(btnPayTax);
		pnlRight.add(btnTaxPayout);
		pnlRight.add(btnPassGo);

		lblProperty.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblProperty);
		pnlRight.add(btnPropertyBuy);
		pnlRight.add(btnPropertySell);
		pnlRight.add(btnPropertyUpgrade);
		pnlRight.add(btnPropertyDownGrade);

		lblPlayerStatus.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblPlayerStatus);
		pnlRight.add(btnPlayerRankUp);
		pnlRight.add(btnPlayerRankDown);
		pnlRight.add(btnPlayerEliminated);

		lblFortune.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblFortune);
		pnlRight.add(btnFortuneBlessing);
		pnlRight.add(btnFortuneCurse);

		lblJail.setPreferredSize(new Dimension(320, 15));
		pnlRight.add(lblJail);
		pnlRight.add(btnJailEnter);
		pnlRight.add(btnJailExit);
		pnlRight.add(btnJailStay);

		pnlRight.add(gameHistoryPanel);

		add(pnlRight);

		this.addBtnListeners();

		JFrame frame = new JFrame("GameHistoryLogTestGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/**
	 * Adds listeners to all of the buttons
	 */
	private void addBtnListeners() {

		btnPlayer1.addActionListener(e -> {
			this.activePlayer = this.player1;
		});

		btnPlayer2.addActionListener(e -> {
			this.activePlayer = this.player2;
		});

		btnRoll.addActionListener(e -> {
			gameHistoryLog.logDiceRollEvent(this.activePlayer, this.landingTile, 4);
		});

		btnDoubleRoll.addActionListener(e -> {
		});

		btnWork.addActionListener(e -> {
			gameHistoryLog.logWorkEvent(this.activePlayer, 150);
		});

		btnRent.addActionListener(e -> {
			gameHistoryLog.logPropertyRentEvent(this.activePlayer, (Property) this.landingTile);
		});

		btnPayTax.addActionListener(e -> {
			gameHistoryLog.logTaxEvent(this.activePlayer, 200);
		});

		btnTaxPayout.addActionListener(e -> {
			gameHistoryLog.logTaxPayoutEvent(this.activePlayer, 400);
		});

		btnPassGo.addActionListener(e -> {
			gameHistoryLog.logPassGoEvent(this.activePlayer, 200);
		});

		btnPropertyBuy.addActionListener(e -> {
			gameHistoryLog.logPropertyBuyEvent(this.activePlayer, (Property) this.landingTile);
		});

		btnPropertySell.addActionListener(e -> {
			gameHistoryLog.logPropertySellEvent(this.activePlayer, (Property) this.landingTile);
		});

		btnPropertyUpgrade.addActionListener(e -> {
			gameHistoryLog.logPropertyUpgradeEvent(this.activePlayer, (Property) this.landingTile);
		});

		btnPropertyDownGrade.addActionListener(e -> {
			gameHistoryLog.logPropertyDownGradeEvent(this.activePlayer, (Property) this.landingTile);
		});

		btnPlayerRankUp.addActionListener(e -> {
			gameHistoryLog.logPlayerRankUpEvent(this.activePlayer);
		});

		btnPlayerRankDown.addActionListener(e -> {
			gameHistoryLog.logPlayerRankDownEvent(this.activePlayer);
		});

		btnPlayerEliminated.addActionListener(e -> {
			gameHistoryLog.logPlayerEliminatedEvent(this.activePlayer);
		});

		btnFortuneBlessing.addActionListener(e -> {

		});

		btnFortuneCurse.addActionListener(e -> {

		});

		btnJailEnter.addActionListener(e -> {
			gameHistoryLog.logJailEnterEvent(this.activePlayer);
		});

		btnJailExit.addActionListener(e -> {
			gameHistoryLog.logJailExitEvent(this.activePlayer);
		});

		btnJailStay.addActionListener(e -> {
			gameHistoryLog.logJailStayEvent(this.activePlayer);
		});


	}

	public static void main(String[] args) {

		GameHistoryLog gameHistoryLog = GameHistoryLog.instance();
		GameHistoryLogTestGUI testGUI = new GameHistoryLogTestGUI(gameHistoryLog);

	}

}
