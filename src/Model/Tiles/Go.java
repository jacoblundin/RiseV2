package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * Simple Go tile class.
 *
 * @author AevanDino, SebastianViro
 */
public class Go implements Tile {

	//Tile variables
	private String name = "Go";
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/Go.png");

	//Class variables
	private int goldReward = 200;

	public Go() {}

	public int getGoldReward() {
		return goldReward;
	}


	//Tile methods

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
		info = name + "\n" + "Every time a player passes by, he or she \nis rewarded 200 gold coins";
		return info;
	}

	@Override
	public ImageIcon getPicture() {
		return this.img;
	}
}
