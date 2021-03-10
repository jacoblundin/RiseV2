package Model.Tiles;

import Model.player.Player;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * Player does not have to pay anything and doesn't get paid for it. // But the player gets all the collected church taxes.
 *
 * @author AevanDino, SebastianViro
 */
public class SundayChurch implements Tile {
	private String name = "Sunday Church";
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/church.png");
	private int collectedGold = 0;

	public void payTax(int amount) {
		collectedGold += amount;
	}

	public void onVisit(Player player) {
		player.increaseBalance(collectedGold);
		collectedGold = 0;
	}

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	public String getTileInfo() {
		info = "Sunday Church \nPlayer attends church, free of charge";
		return info;
	}

	public ImageIcon getPicture() {
		return this.img;
	}
}