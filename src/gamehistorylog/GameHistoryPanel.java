package gamehistorylog;

import java.awt.*;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * GameHistoryPanel is a JPanel which contains the game history log.
 *
 * Events / actions that happen in the game can be added to this log to help users follow the line of events in the game.
 * The JTextPane's content type is set to HTML. This is the pane that shows the log entries. The pane is also scrollable.
 */
public class GameHistoryPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblHistoryTitle;
	private JPanel pnlHistory;
	private Font headerFont = new Font("ALGERIAN", Font.BOLD, 19);
	private Font taFont = new Font("Arial", Font.PLAIN, 9);
	private JTextPane taGameHistory = new JTextPane();
	private HTMLDocument gameHistoryDocument;
	private HTMLEditorKit gameHistoryEditorKit;
	private JScrollPane scroller = new JScrollPane(taGameHistory);

	private DefaultCaret caret = (DefaultCaret) taGameHistory.getCaret();

	private Border border = BorderFactory.createLineBorder(Color.DARK_GRAY);

	public GameHistoryPanel() {

		setOpaque(false);
		setPreferredSize(new Dimension(345, 860));
		setBackground(Color.yellow);

		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		/**
		 * HistoryPanel
		 */
		pnlHistory = new JPanel();
		pnlHistory.setPreferredSize(new Dimension(340, 475));
		pnlHistory.setBackground(new Color(0, 0, 0, 20));

		taGameHistory.setFont(taFont);
		taGameHistory.setMargin(new Insets(10, 10, 10, 10));
		taGameHistory.setEditable(false);
		taGameHistory.setForeground(new Color(71, 60, 50, 225));
		taGameHistory.setContentType("text/html;charset=UTF-8");
		gameHistoryDocument = (HTMLDocument) taGameHistory.getDocument();
		gameHistoryEditorKit = (HTMLEditorKit) taGameHistory.getEditorKit();

		scroller.setBackground(Color.white);
		scroller.setForeground(Color.black);
		scroller.setForeground(new Color(71, 60, 50, 225));
		scroller.setPreferredSize(new Dimension(320, 405));
		scroller.setAutoscrolls(true);

		lblHistoryTitle = new JLabel("Game history");
		lblHistoryTitle.setPreferredSize(new Dimension(320, 40));
		lblHistoryTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblHistoryTitle.setForeground(Color.white);
		lblHistoryTitle.setFont(headerFont);
		pnlHistory.add(lblHistoryTitle);
		pnlHistory.add(scroller);

		setBorder(border);
		add(pnlHistory);
	}

	/**
	 * Append an event / action log entry to the log text pane.
	 */
	public void append(String msg) {

		try {
			gameHistoryEditorKit.insertHTML(gameHistoryDocument, gameHistoryDocument.getLength(), msg, 0, 0, null);
		} catch (BadLocationException | IOException e) {
			e.printStackTrace();
		}
	}
}
