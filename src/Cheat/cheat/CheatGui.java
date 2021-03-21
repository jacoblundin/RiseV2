package Cheat.cheat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Controller.Controller;

/**
 * This class is used for testing purposes only.
 * It is a GUI through which you can move a player to specific tiles to test the game.
 *
 * @author Sebastian Viro, Muhammad Abdulkhuder
 */
public class CheatGui extends JPanel implements ActionListener {
	private final JTextField inputTF = new JTextField("");
	private final JButton btnTeleport = new JButton("Teleport");
	private final Controller controller;

	public CheatGui(Controller controller) {
	    this.controller = controller;
		startGUI();
	}

	/**
	 * The method that draws the gui
	 */
	private void startGUI() {
		setPreferredSize(new Dimension(100, 100));
		setLayout(new BorderLayout());
		btnTeleport.setPreferredSize(new Dimension(300, 50));
		add(inputTF, BorderLayout.CENTER);
		add(btnTeleport, BorderLayout.SOUTH);
		btnTeleport.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnTeleport) {
			try {
				var steps = Integer.parseInt(inputTF.getText());
				controller.moveWCheat(steps);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
	}
}