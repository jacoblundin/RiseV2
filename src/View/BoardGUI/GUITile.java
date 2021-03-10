package View.BoardGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Model.player.Player;

/**
 * Every tile is a guiTile object they are used to display the players. It could
 * also be used to show the level on the properties.
 *
 * @author sethoberg
 */

public class GUITile extends JLabel {

    private static final long serialVersionUID = 1L;
    private Font labelFont = new Font("Arial", Font.BOLD, 10);
    private JLabel infoLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel labelArray = new JLabel();
    private JLabel label1 = new JLabel();
    private JLabel label2 = new JLabel();
    private JLabel label3 = new JLabel();
    private JLabel label4 = new JLabel();
    private JLabel[] labels = {label1, label2, label3, label4};
    private int alignment = 1;
    private Border tileBorder = BorderFactory.createLineBorder(Color.decode("#ff7723"));

    /**
     * Constructor receiving an int gets the location of the info JLabel object
     *
     * @param SouthWestNorthEast either 1, 2, 3, 4
     */
    public GUITile(int SouthWestNorthEast) {
        alignment = SouthWestNorthEast;

        setPreferredSize(new Dimension(200, 300));
        setLayout(new BorderLayout());

        labelArray.setLayout(new GridLayout(2, 2));
        labelArray.setOpaque(false);
        labelArray.setBackground(Color.decode("#ffe9c6"));

        styleAndAddInfoLabel();
        addLabelsToGrid();
    }

    /**
     * JLabel object gets each Tile showing which level a property is in
     */
    public void styleAndAddInfoLabel() {
        infoLabel.setPreferredSize(new Dimension(200, 20));
        infoLabel.setOpaque(false);
        infoLabel.setForeground(Color.white);
        infoLabel.setFont(labelFont);
        infoLabel.setText("");
        infoLabel.setBackground(Color.magenta);

        if (alignment == 1) {
            add(infoLabel, BorderLayout.NORTH);
        } else if (alignment == 2) {
            infoLabel.setPreferredSize(new Dimension(20, 200));
            add(infoLabel, BorderLayout.EAST);
        } else if (alignment == 3) {
            add(infoLabel, BorderLayout.SOUTH);
        } else if (alignment == 4) {
            infoLabel.setPreferredSize(new Dimension(20, 200));
            add(infoLabel, BorderLayout.WEST);
        }
    }

    /**
     * Adds four JLabels
     */
    public void addLabelsToGrid() {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setPreferredSize(new Dimension(200, 200));
            labelArray.add(labels[i]);
        }
        add(labelArray, BorderLayout.CENTER);
    }

    /**
     * update level on property
     */
    public void changeLevel(String level) {
        infoLabel.setText(level);
    }

    /**
     * Each gui tile has 4 places where a player can be placed
     * Place a players icon in the GUITile. The tile has 4 positions for a player icon.
     * The position of the icon is determined by the players player index.
     *
     * @param player player to set in the GUITile
     */
    public void setPlayer(Player player) {
        labels[player.getOriginalPlayerIndex()].setIcon(player.getImage());
        labels[player.getOriginalPlayerIndex()].setHorizontalAlignment(CENTER);
    }

    public void removePlayer(Player player) {
        labels[player.getOriginalPlayerIndex()].setIcon(null);
        labels[player.getOriginalPlayerIndex()].setHorizontalAlignment(CENTER);
    }
}