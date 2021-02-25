package Controller;

import Model.Tiles.TileCollection;
import Model.player.PlayerList;
import UnusedClasses.cheat.CheatGui;
import View.BoardGUI.GamePanels;
import View.GameFlowGUI.GameFlowPanel;
import View.StartMenuGUI.Introduction;
import View.StartMenuGUI.StartingScreen;
import soundservice.SoundService;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SoundService.instance().playBgMusic(); //Start the background music

        // Creating TileCollection here to enable injection of different TileCollection than the default one.
        TileCollection tileCollection = new TileCollection();

        /* Returning the PlayerList from the StartingScreen. This should let us inject a different
        player list and bypass the starting screen. */
        CompletableFuture<PlayerList> retrievePlayerList = new CompletableFuture<>();
        StartingScreen su = new StartingScreen(retrievePlayerList);
        su.initializeGUI();

        // Blocks here until the user clicks on the start game button.
        PlayerList playerList = retrievePlayerList.get();

        GamePanels gamePanels = new GamePanels(playerList, tileCollection);
        gamePanels.startBoard();
        new Introduction();

        // Panels have now been initialized in the GamePanels object. Get them and put them in the controller.
        Controller controller = new Controller(gamePanels.getBoard(), playerList, gamePanels.getWestPanel(),
                gamePanels.getGameFlowPanel(), gamePanels.getEastSidePanel(), gamePanels.getGameFlowPanel().getDice());

        // Finally, set the controller in the panels.
        gamePanels.setController(controller);

        // If you want the cheat GUI:
//        GameFlowPanel gameFlowPanel = gamePanels.getGameFlowPanel();
//        gameFlowPanel.setCheatGUI(new CheatGui(controller));

    }
}
