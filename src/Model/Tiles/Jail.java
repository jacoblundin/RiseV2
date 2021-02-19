package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * Class for jail tile.
 *
 * @author Sebastian Viro, Aevan Dino
 */
public class Jail implements Tile {

	//Tile variables
	private String name = "Go to Jail";
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/jail.png");

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
		return "Jail + \nPlayer can choose to spend three rounds here"
				+ "\nor pay the bail.\nFirst day costs 50 gold coins"
				+ "\nSecond time costs 100 gold coins"
				+ "\nThird time Model.player has to pay 200 and is set free";
	}

	@Override
	public ImageIcon getPicture() {
		return img;
	}
}
