package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;

import Model.player.Player;

/**
 * @author Muhammad abdulkhuder, AevanDino, Sebastian Viro, .
 */
public class Work implements Tile {
	private String name = "Work";
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/Work.png");
	private Player player;
	private int roll;

	public void payPlayer(int nbrOfDots) {
		setRoll(nbrOfDots);
		player.increaseBalance(player.getPlayerRank().getSalary(nbrOfDots));
		player.increaseNetWorth(getPay());
	}

	public int getPay() {
		return player.getPlayerRank().getSalary(getRoll());
	}

	public Player setPlayer(Player player) {
		return this.player = player;
	}

	public int getRoll() {
		return roll;
	}

	public void setRoll(int roll) {
		this.roll = roll;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public String getTileInfo() {
		info = getName() + "\nPlayer gets to roll the dice, and depending \n"
				+ "on his or her rank they are rewarded gold coins for their effort. \n"
				+ "Peasant: 20 times the sum of the roll \n" + "Knight: 25 times the sum of the roll \n"
				+ "Lord: 30 times the sum of the roll \n" + "Ruler: 40 times the sum of the roll";

		return info;
	}

	@Override
	public ImageIcon getPicture() {
		return this.img;
	}
}