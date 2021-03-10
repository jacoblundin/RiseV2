package View.BoardGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import Controller.Controller;
import Model.Tiles.TileCollection;
import View.GameFlowGUI.GameFlowPanel;
import View.EastGUI.EastSidePanel;
import View.StartMenuGUI.Introduction;
import View.WestGUI.Menu;
import View.WestGUI.WestSidePanel;
import Model.player.PlayerList;

/**
 * This class combines most of the panels in the game and adds appropriate
 * references.
 *
 * @author Abdulkhuder Muhammad
 */
public class GamePanels extends JPanel {
    private static final long serialVersionUID = 1L;
    private EastSidePanel eastSidePanel;
    private WestSidePanel westPanel;
    private Board board;
    private GameFlowPanel gameFlowPanel;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private PlayerList playerList;
    private JFrame frame = new JFrame();
    private JLabel lblPic = new JLabel();
    private Menu menu;
    private int width = (int) screenSize.getWidth();
    private int height = (int) screenSize.getHeight();
    private Introduction introduction;

    /**
     * adds the panels and sets the bounds
     */
    public GamePanels(PlayerList playerList, TileCollection tileCollection, Introduction introduction) {
        this.playerList = playerList;
        this.eastSidePanel = new EastSidePanel(playerList);
        this.westPanel = new WestSidePanel();
        this.board = new Board(playerList, westPanel, tileCollection);
        this.gameFlowPanel = new GameFlowPanel(board, playerList, westPanel, eastSidePanel);
        this.introduction = introduction;
        this.menu = new Menu(this, introduction);

        setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.BLACK));
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        eastSidePanel.setOpaque(false);
        eastSidePanel.setBounds(1095, 0, 345, 860);
        add(eastSidePanel);

        westPanel.setBounds(0, 0, 345, 860);
        add(westPanel);

        board.setBounds(346, 0, 750, 750);
        add(board);

        gameFlowPanel.setBounds(346, 751, 750, 109);
        add(gameFlowPanel);
        menu.setBounds(0, 0, 50, 18);
        add(menu);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images/back2jpg.jpg"));

        } catch (IOException e) {

            e.printStackTrace();
        }

        Image bimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        lblPic.setBounds(0, 0, width, height);
        lblPic.setIcon(new ImageIcon(bimg));
        add(lblPic);
    }

    /**
     * This is where we call the frame
     */
    public void startBoard() {
        frame = new JFrame("Change your fate");
        frame.setPreferredSize(new Dimension(width + 18, height + 10));
        frame.setLocation(-9, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().add(this);
        frame.pack();
    }

    public EastSidePanel getEastSidePanel() {
        return eastSidePanel;
    }

    public WestSidePanel getWestPanel() {
        return westPanel;
    }

    public Board getBoard() {
        return board;
    }

    public GameFlowPanel getGameFlowPanel() {
        return gameFlowPanel;
    }

    public void setController(Controller controller) {
        gameFlowPanel.setController(controller);
        eastSidePanel.setController(controller);
    }

    public void destroyFrame() {
        frame.dispose();
    }
}