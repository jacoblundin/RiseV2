package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * @author Sebastian Viro, Aevan Dino, MUHAMMAD ABDULKHUDER
 */
public class GoToJail implements Tile {
	private String name = "Go to Jail";
	private Color color = Color.DARK_GRAY;
	private String info;
	private ImageIcon img = new ImageIcon("tilePics/gojail.png");

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
		return null; //TODO: this should not be null
	}

	@Override
	public ImageIcon getPicture() {
		return img;
	}
}