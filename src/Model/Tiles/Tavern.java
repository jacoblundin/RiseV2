package Model.Tiles;

import java.awt.Color;
import javax.swing.ImageIcon;
import Model.player.Player;

public class Tavern implements Tile {
	private String name;
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/tavern.png");
	private Player owner;
	private int price;

	public Tavern(String namn, int price) {
		this.name = namn;
		this.price = price;
		this.owner = null;
	}

	public void clearTavern() {
		this.owner = null;
	}

	public Boolean getPurchasable() {
		return this.owner == null;
	}

	public void setOwner(Player newOwner) {
		this.owner = newOwner;
	}

	public Player getOwner() {
		return owner;
	}

	public int getPrice() {
		return price;
	}

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	public String getTileInfo() {
		String ownerName = "";
		if (owner == null) {
			ownerName = "No Owner";
		} else {
			ownerName = owner.getName();
		}
		info = "\nOwner: \t         " + ownerName + "\n"
				+ "Price:\t\t" + price + "\n"
				+ "Default rent:    Read Rules\n"
				+ "Rent with Levels:\t" + 0 + "\n"
				+ "Total rent:        Read Rules";
		return info;
	}

	public ImageIcon getPicture() {
		return this.img;
	}
}