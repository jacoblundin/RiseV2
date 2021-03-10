package soundservice;

import javax.swing.*;
import java.awt.*;

/**
 * SoundServiceTestGUI can be used to test the SoundService class
 *
 * This class provides a GUI to test some of the functions of the SoundService class.
 */
public class SoundServiceTestGUI extends JPanel {
	private JPanel pnlMain = new JPanel();

	//SoundFx elements
	//Add more buttons here to test additional sounds that may be added.
	//Button listeners are added to the buttons in the addBtnListeners method.
	private JButton btnCoin = new JButton("Coin");
	private JButton btnDice = new JButton("Dice");
	private JButton btnDoubleRoll = new JButton("Dice Double");
	private JButton btnWork = new JButton("Work");
	private JButton btnJail = new JButton("Jail");
	private JButton btnRankUp = new JButton("Rank Up");
	private JButton btnFortuneCurse = new JButton("Curse");

	private JLabel lblBackgroundMusic = new JLabel("Background Music");
	private JButton btnStartBgMusic = new JButton("Start");
	private JButton btnStopBgMusic = new JButton("Stop");
	private JButton btnToggleBgMusic = new JButton("Toggle");

	public SoundServiceTestGUI() {
		pnlMain.setBackground(new Color(150, 150, 150));
		pnlMain.setPreferredSize(new Dimension(320, 320));

		pnlMain.add(btnCoin);
		pnlMain.add(btnDice);
		pnlMain.add(btnFortuneCurse);

		btnDoubleRoll.setEnabled(false);
		pnlMain.add(btnDoubleRoll);

		btnWork.setEnabled(false);
		pnlMain.add(btnWork);

		btnJail.setEnabled(false);
		pnlMain.add(btnJail);

		btnRankUp.setEnabled(false);
		pnlMain.add(btnRankUp);

		pnlMain.add(lblBackgroundMusic);
		pnlMain.add(btnStartBgMusic);
		pnlMain.add(btnStopBgMusic);
		pnlMain.add(btnToggleBgMusic);

		add(pnlMain);
		this.addBtnListeners();
	}

	/**
	 * Adds listeners to all of the buttons
	 */
	private void addBtnListeners() {
		btnCoin.addActionListener(e -> { SoundService.instance().playSoundFx(SoundFx.SOUND_COIN); });

		btnDice.addActionListener(e -> { SoundService.instance().playSoundFx(SoundFx.SOUND_DICE); });

		btnFortuneCurse.addActionListener(e -> { SoundService.instance().playSoundFx(SoundFx.SOUND_CHURCHBELLS); });

		btnStartBgMusic.addActionListener(e -> { SoundService.instance().playBgMusic(); });

		btnStopBgMusic.addActionListener(e -> { SoundService.instance().pauseBgMusic(); });

		btnToggleBgMusic.addActionListener(e -> { SoundService.instance().toggleBgMusic(); });
	}

	public static void main(String[] args) {
		SoundServiceTestGUI testGui = new SoundServiceTestGUI();
		JFrame frame = new JFrame("SoundServiceTestGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(testGui);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
