package dice;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * DiceTestGUI can be used to test the Dice functionality
 *
 * This class provides a GUI to test some of the functions of the dice.
 */
public class DiceTestGUI extends JPanel {

	private Dice dice;
	private DicePanel dicePanel;

	private JPanel pnlMain = new JPanel();

	private JButton btnRoll = new JButton("Roll");
	private JButton btnDoubleRoll = new JButton("Double Roll");

	public DiceTestGUI(Dice diceRef) {

		this.dice = diceRef;
		this.dicePanel = dice.getDicePanel();

		pnlMain.setBackground(new Color(150, 150, 150));
		pnlMain.setPreferredSize(new Dimension(320, 320));

		pnlMain.add(btnRoll);
		pnlMain.add(btnDoubleRoll);

		pnlMain.add(dicePanel);

		add(pnlMain);

		this.addBtnListeners();

		JFrame frame = new JFrame("DiceTestGUI");
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

		btnRoll.addActionListener(e -> {
			dice.roll((sumRoll) -> {
				System.out.println("The roll value is " + sumRoll);
			});
		});

		btnDoubleRoll.addActionListener(e -> {
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			dice.getDicePanel().animateRoll(roll, roll, null);
		});
	}

	public static void main(String[] args) {

		Dice dice = new Dice();
		DiceTestGUI testGUI = new DiceTestGUI(dice);

	}

}
