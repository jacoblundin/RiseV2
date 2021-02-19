package utils;

import java.awt.*;

public final class Utils {

	public static String colorToHexString(Color color) {
		return "#"+Integer.toHexString(color.getRGB()).substring(2);
	}

}
