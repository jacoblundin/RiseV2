package Model.player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BoardPiece {

    private static final String peasantImgPath = "file:images/peasantPiece.png";
    private static final String knightImgPath = "file:images/knightPiece.png";
    private static final String lordImgPath = "file:images/lordPiece.png";
    private static final String kingImgPath = "";

    public static ImageIcon newPiece(PlayerRanks rank, Color color) {
        try {
            String baseImagePath;

            switch (rank) {
                case PEASANT:
                    baseImagePath = peasantImgPath;
                    break;
                case KNIGHT:
                    baseImagePath = knightImgPath;
                    break;
                case LORD:
                    baseImagePath = lordImgPath;
                    break;
                case KINGS:
                default:
                    baseImagePath = peasantImgPath; //TODO get default and king piece images
                    break;
            }

            BufferedImage img = ImageIO.read(new URL(baseImagePath));
            int w = img.getWidth();
            int h = img.getHeight();

            BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = dyed.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.setComposite(AlphaComposite.SrcAtop);
            g.setColor(color);
            g.fillRect(0, 0, w, h);
            g.dispose();

            return new ImageIcon(dyed);
        } catch (IOException e) {
            // This exception cannot be handled by the client of this class. It's a bug in the code to
            // not have these files available. It should therefore not be propagated as a checked exception.
            throw new RuntimeException("Couldn't open board piece files.");
        }
    }

    public static void main(String[] args) throws IOException {
        JLabel lblPieceIcon1 = new JLabel();
        lblPieceIcon1.setPreferredSize(new Dimension(20, 20));
        JLabel lblPieceIcon2 = new JLabel();
        JLabel lblPieceIcon3 = new JLabel();
        JLabel lblPieceIcon4 = new JLabel();
        JLabel lblPieceIcon5 = new JLabel();
        JLabel lblPieceIcon6 = new JLabel();
        JPanel pnlMain = new JPanel();
        pnlMain.setBackground(Color.gray);

        lblPieceIcon1.setIcon(BoardPiece.newPiece(PlayerRanks.PEASANT, Color.BLUE));
        lblPieceIcon2.setIcon(BoardPiece.newPiece(PlayerRanks.PEASANT, Color.RED));
        lblPieceIcon3.setIcon(BoardPiece.newPiece(PlayerRanks.KNIGHT, Color.MAGENTA));
        lblPieceIcon4.setIcon(BoardPiece.newPiece(PlayerRanks.KNIGHT, Color.ORANGE));
        lblPieceIcon5.setIcon(BoardPiece.newPiece(PlayerRanks.LORD, Color.decode("#224488")));
        lblPieceIcon6.setIcon(BoardPiece.newPiece(PlayerRanks.LORD, Color.black));

        JFrame frame = new JFrame("BoardPieceTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pnlMain.add(lblPieceIcon1);
        pnlMain.add(lblPieceIcon2);
        pnlMain.add(lblPieceIcon3);
        pnlMain.add(lblPieceIcon4);
        pnlMain.add(lblPieceIcon5);
        pnlMain.add(lblPieceIcon6);
        frame.add(pnlMain);
        frame.pack();
        frame.setVisible(true);
    }
}
