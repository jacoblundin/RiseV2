package dice;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * DicePanel is a JPanel that graphically represents 2 dice.
 *
 * By calling animateRoll the dice in the panel will "animate" a roll by shifting
 * between different face values, and finally land of the specified face values.
 */
public class DicePanel extends JPanel {

	private Thread diceAnimationThread;

	private Dimension dieDimension = new Dimension(64, 64);

	private JLabel leftDie;
	private JLabel rightDie;

	private ImageIcon[] dieFaceIcons;
	private ImageIcon[] doubleRollDieFaceIcons;

	public DicePanel() {
		this.setPreferredSize(new Dimension(154, 74));
		this.setBackground(new Color(100, 100, 100));

		initDieFaceIcons();

		ImageIcon initialDieFaceIcon = dieFaceIcons[0];

		leftDie = new JLabel();
		leftDie.setIcon(initialDieFaceIcon);
		rightDie = new JLabel();
		rightDie.setIcon(initialDieFaceIcon);

		add(leftDie);
		add(rightDie);

		this.setToolTipText("If you roll a double you will move twice as far");

	}

	private void initDieFaceIcons() {

		dieFaceIcons = new ImageIcon[6];

		for (int i = 0; i < dieFaceIcons.length; i++) {
			dieFaceIcons[i] = new ImageIcon(DiceData.dieFaceFilePaths[i]);
			rescaleImageIcon(dieFaceIcons[i]);
		}

		doubleRollDieFaceIcons = new ImageIcon[6];
		for (int i = 0; i < doubleRollDieFaceIcons.length; i++) {
			doubleRollDieFaceIcons[i] = new ImageIcon(DiceData.doubleRollDieFaceFilePaths[i]);
			rescaleImageIcon(doubleRollDieFaceIcons[i]);
		}
	}

	/**
	 * Rescale the provided ImageIcon object to the size defined by dieDimension
	 *
	 * @param input imageIcon object that will be rescaled to the die dimension
	 */
	private void rescaleImageIcon(ImageIcon input) {
		input.setImage(input.getImage().getScaledInstance(dieDimension.width, dieDimension.height, Image.SCALE_SMOOTH));
	}


	/**
	 * Call this method to start "rolling" the dice in the panel.
	 * The Dice images will shift and eventually end upon the specified d1 and d2 values.
	 * After the animation is complete the callback supplied will be executed. The callback value is always null
	 *
	 * @param d1       Face value that the first dice will end upon.
	 * @param d2       Face value that the second dice will end upon.
	 * @param callback This parameter is a callback and will be executed once the animation is done.
	 */
	public void animateRoll(int d1, int d2, ICallback<Void> callback) {

		if (diceAnimationThread == null || !diceAnimationThread.isAlive()) {
			diceAnimationThread = new Thread(() -> {

				boolean doubleRoll = (d1 == d2);

				Random rand = new Random();
				int rollSequenceLength = rand.nextInt(4) + 6;

				//roll animation
				for (int i = 0; i < rollSequenceLength; i++) {
					leftDie.setIcon(dieFaceIcons[(i % 6)]);
					rightDie.setIcon(dieFaceIcons[((i + 3) % 6)]);
					try {
						Thread.sleep(rand.nextInt(200) + 100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				//final face values
				if (doubleRoll) {
					leftDie.setIcon(doubleRollDieFaceIcons[d1 - 1]);
					rightDie.setIcon(doubleRollDieFaceIcons[d2 - 1]);
				} else {
					leftDie.setIcon(dieFaceIcons[d1 - 1]);
					rightDie.setIcon(dieFaceIcons[d2 - 1]);
				}

				if (callback != null) {
					callback.callback(null);
				}

			});

			diceAnimationThread.start();

		}

	}

}
