package Model.Tiles;

import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * All Model.tileCollection.tiles will implement this interface. Methods listed are
 * common methods for all types of Model.tileCollection.tiles.
 *
 * @author AevanDino, SebastianViro
 */
public interface Tile {

	/**
	 * @return tileName, name of current tile.
	 */
	public String getName();

	/**
	 * @return color, returns a color-object representing color of tile.
	 */
	public Color getColor();

	/**
	 * @return picture of the tile.
	 */
	public ImageIcon getPicture();

	/**
	 * @return info, information about tile. (Price, owner, stuff).
	 */
	public String getTileInfo();
}