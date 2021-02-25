package Model.Tiles;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import Model.player.Player;

/**
 * Class for property.
 * @author Sebastian Viro, Aevan Dino, Muhammad Abdulkhuder
 */
public class Property implements Tile {

	//Tile variables
	private String name;
	private Color color;
	private String info;
	private ImageIcon img;

	private int price, rentPerLevel, defaultRent, levels;
	private Player owner;
	private int levelPrice;

	/**
	 * Construtor which sets values of property
	 * @param name, of property
	 * @param price, of property
	 * @param defaultRent, rent for level 0
	 * @param rentPerLevel, amount to be increased by per level
	 * @param color, color of property
	 * @param levelPrice, cost of upgrade 
	 * @param img, image of tile
	 */
	public Property(String name, int price, int defaultRent, int rentPerLevel, Color color,int levelPrice , ImageIcon img) {
		this.name = name;
		this.color = color;
		this.img=img;

		this.price = price;
		setDefaultRent(defaultRent);
		setRentPerLevel(rentPerLevel);
		this.owner = null;
		this.levelPrice= levelPrice;
	}

	public Boolean getPurchasable() {
		//If there is no owner the property is purchasable
		return this.owner == null;
	}

	public int getPrice() {
		return this.price;
	}
	
	public void setOwner(Player newOwner) {
		this.owner = newOwner;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setDefaultRent(int defRent) {
		this.defaultRent = defRent;
	}

	public int getDefaultRent() {
		return this.defaultRent;
	}

	public void setRentPerLevel(int rentPerLevel) {
		this.rentPerLevel = rentPerLevel;
	}

	public int getRentPerLevel() {
		return this.rentPerLevel;
	}
	
	public int getTotalRent() {
		return this.defaultRent + (this.rentPerLevel * this.levels);
	}
	
	public void setLevel(int num) {
		this.levels=num;
	}
	
	public int getLevelPrice() {
		return levelPrice;
	}
 
	public void setLevelPrice(int levelPrice) {
		this.levelPrice = levelPrice;
	}
	
	public void increaseLevel() {
		int res = JOptionPane.showConfirmDialog(null, "Do you want to upgrade " + getName() + " for: " + getLevelPrice());
		if (res == 0 && owner.getPlayerRank().nbrOfLevels() > levels && owner.getBalance()>= getLevelPrice()) {
			this.levels += 1;
			owner.decreaseBalace(getLevelPrice());
		} else if (res == 0 && owner.getPlayerRank().nbrOfLevels() == levels){
			JOptionPane.showMessageDialog(null, "You cannot upgrade the property further at your current rank.");

		} else if (owner.getBalance() < getLevelPrice()){
			JOptionPane.showMessageDialog(null, "You do not have enough gold.");
			}
		}
	
	public void decreaseLevel() {
		int res = JOptionPane.showConfirmDialog(null, "Do you really want to downgrade " + getName() + " for: " + getLevelPrice());
		if (levels>0 && res == 0) {	
			this.levels-=1;
			owner.increaseBalance(getLevelPrice());
		}
	}
		
	public int getLevel() {
		return this.levels;
	}
	
	public void clearProperty() {
		owner = null;
		setLevel(0);
	}

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
		String ownerName = "";
		if(owner == null) {
			ownerName = "No Owner";
		} else {
			ownerName = owner.getName();
		}
		info =    "\nOwner: \t         " + ownerName + "\n"
				+ "Price:\t\t" + price + "\n"
				+ "Default rent:\t" + defaultRent + "\n"
				+ "Rent per level:\t" 	+ rentPerLevel + "\n"
				+ "Total rent:\t" 		+ getTotalRent();
		return info;
	}

	@Override
	public ImageIcon getPicture(){
		return this.img;
	}

}
