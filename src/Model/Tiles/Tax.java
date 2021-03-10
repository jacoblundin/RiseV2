package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;

import Model.player.Player;

public class Tax implements Tile {
	private String name = "Church Tax";
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/tax.png");

	private Player player;
	private SundayChurch church;
	private int baseTax = 200;

	public Tax(SundayChurch church) {
		this.church = church;
	}

	public int getTax() {
		return this.baseTax;
	}

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	public String getTileInfo() {
		info = getName() + "\n"
				+ "Owner: \t \t" + "The Church" + "\n"
				+ "Peasant tax: \t" + "100 gold coins" + "\n"
				+ "Knight tax: \t" + "150 gold coins" + "\n"
				+ "Lord tax: \t" + "200 gold coins" + "\n"
				+ "\n"
				+ "Your tax: \t" + player.getPlayerRank().calculateTax() + "\n";
		return info;
	}

	public ImageIcon getPicture() {
		return this.img;
	}
}