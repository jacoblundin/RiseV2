package View.PopUpGUI;

import Model.player.Player;
import gamehistorylog.GameHistoryLog;
import soundservice.SoundFx;
import soundservice.SoundService;

import java.awt.Dimension;
import javax.swing.*;

/**
 * Class to display a gui for the winner.
 * @author Mohammad Abdulkhuder.
 */
public class WinGui extends JPanel {
	private JLabel lblLblpic = new JLabel("lblPic");

	/**
	 * Constructor calls the method to draw the gui.
	 */
	public WinGui(Player player) {
		addgui(player);
	}
	
	/**
	 * Draws the View.GUI.
	 */
	public void addgui(Player player) {
		setPreferredSize(new Dimension(1000, 500));
		setLayout(null);
		lblLblpic.setBounds(0, 0, 1027, 500);
		lblLblpic.setIcon(new ImageIcon("images/Rise Winner.png"));
		add(lblLblpic);
		getFrame();
		victory();
		GameHistoryLog.instance().logPlayerWinEvent(player);
		JOptionPane.showMessageDialog(null, "Congratulations " + player.getName()
				+ " you are king and won the game");
	}

	/**
	 * Creates the frame.
	 */
	public void getFrame() {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	private void victory() {
		SoundService.instance().playSoundFx(SoundFx.SOUND_VICTORY);
	}
}