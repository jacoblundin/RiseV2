package View.WestGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import Controller.Main;
import View.BoardGUI.Rules;
import View.BoardGUI.GamePanels;
import View.StartMenuGUI.StartingScreen;
import soundservice.SoundService;

/**
 * This class builds and handles the menu bar for the main game window
 */
public class Menu extends JPanel {

	private JMenu jMenu = new JMenu("Menu");
	private JMenuBar jMenuBar = new JMenuBar();

	private JMenuItem menuItemMusic = new JMenuItem("Play/Pause Music");
	private JMenuItem menuItemSoundEffects = new JMenuItem("Sound effects on/off");
	private JMenuItem menuItemRules = new JMenuItem("Read Rules");
	private JMenuItem menuItemRestart = new JMenuItem("Restart Game");
	private JMenuItem menuItemExit = new JMenuItem("Exit");

	private Rules rules = new Rules();
	
	/**
	 * Constructor which draws the gui
	 */
	public Menu() {
		setOpaque(false);
		setPreferredSize(new Dimension(400, 40));
		setLayout(new BorderLayout());
		jMenuBar.setPreferredSize(new Dimension(100,5));

		initActionListeners();

		jMenu.add(menuItemMusic);
		jMenu.add(menuItemSoundEffects);
		jMenu.add(menuItemRules);
		jMenu.add(menuItemRestart);
		jMenu.add(menuItemExit);
		jMenuBar.add(jMenu);
		
		add(jMenuBar, BorderLayout.WEST);
		setBackground(Color.black);
	}

	/**
	 * This method initiates all the action listeners of the menu's menu items.
	 */
	private void initActionListeners() {

		menuItemExit.addActionListener((event) -> {
			System.exit(0);
		});

		menuItemMusic.addActionListener((event) -> {
			SoundService.instance().toggleBgMusic();
		});

		menuItemSoundEffects.addActionListener((event) -> {
			SoundService.instance().toggleSoundFx();
		});

		menuItemRules.addActionListener((event) -> {
			rules.showRules();
		});

		menuItemRestart.addActionListener((event) -> {
			StartingScreen ss = new StartingScreen();
			ss.initializeGUI();
//			GamePanels gp = new GamePanels();
//			gp.Dispose();
		});
	}

}
