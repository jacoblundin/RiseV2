package Model.colorsAndIcons;

import java.awt.Color;
import java.util.HashMap;

/**
 * This class returns a color with a string 
 * @author Seth Oberg
 *
 */
public class StringColorMap {
	private HashMap<String, Color> colorMap = new HashMap<String, Color>();

	/**
	 * Add colors to hashmap
	 */
	public StringColorMap() {
		addColorsToMap();
	}

	private void addColorsToMap() {
		colorMap.put("MAGENTA", new Color(220, 137, 211));
		colorMap.put("RED", new Color(158, 66, 69, 255));
		colorMap.put("ORANGE", new Color(231, 144, 78, 255));
		colorMap.put("YELLOW", new Color(232, 210, 84, 255));
		colorMap.put("GREEN", new Color(71, 142, 64, 255));
		colorMap.put("CYAN", new Color(51, 123, 139, 255));
		colorMap.put("PURPLE", Color.decode("#9542f4"));
	}

	/**
	 * Either magenta, red, orange, yellow, green, cyan, purple
	 * @param chosenColor color to Get
	 * @return
	 */
	public Color getColorFromMap(String chosenColor) {
		return colorMap.get(chosenColor);
	}
}