package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * Fortune teller class, either returns a fortune where the Model.player
 * gains money or has to pay.
 *
 * @author SebastianViro, AevanDino, MuhammadAbdulkhuder
 */
public class FortuneTeller implements Tile {

	private Color color = Color.DARK_GRAY;
	private String name = "Fortune Teller";
	private ImageIcon img = new ImageIcon("tilePics/fortune.png");

	private String fortune = "";
	private Boolean isBlessing = false;

	private int amount;


	/**
	 * Empty constructor
	 */
	public FortuneTeller() {

	}

	/**
	 * @return the fortune
	 */
	public String getFortune() {
		return fortune;
	}

	/**
	 * @param fortune the fortune to set
	 */
	public void setFortune(String fortune) {
		this.fortune = fortune;
	}

	/**
	 * @return the isBlessing
	 */
	public Boolean getIsBlessing() {
		return isBlessing;
	}

	/**
	 * @param isBlessing the isBlessing to set
	 */
	public void setIsBlessing(Boolean isBlessing) {
		this.isBlessing = isBlessing;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
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
		return "There are two types of cards, blessings and curses." +
				"\nBlessing affect the player in a positive way." +
				"\nCurses affect the player in a negative way.";
	}

	@Override
	public ImageIcon getPicture() {
		return this.img;
	}
}