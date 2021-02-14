package UnusedClasses.cheat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controller.GameFlowPanel;

/**
 * This class is used for testing purposes only.
 * It is a GUI through which you can move a player to specific tiles to test the game.
 *
 * @author Sebastian Viro, Muhammad Abdulkhuder
 */
public class CheatGui extends JPanel implements ActionListener {

	private JTextField inputTF = new JTextField("");
	private JButton btnTeleport = new JButton("Teleport");
	private GameFlowPanel betterDice;
	private int index;

	/**
	 * Constructor
	 *
	 * @param gameFlowPanel gameFlowPanel reference
	 */
	public CheatGui(GameFlowPanel gameFlowPanel) {
		this.betterDice = gameFlowPanel;
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

	/**
	 * This is what happens when a button is pressed
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnTeleport) {
			try {
				setIndex(Integer.parseInt(inputTF.getText()));
				betterDice.moveWCheat(getIndex());

			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
