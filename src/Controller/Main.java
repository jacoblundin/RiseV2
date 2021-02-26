package Controller;

import Model.Tiles.Property;
import Model.Tiles.Tavern;
import Model.Tiles.TileCollection;
import Model.player.PlayerAndTiles;
import Model.player.PlayerFromJSON;
import Model.player.PlayerList;
import Model.player.PlayerRanks;
import UnusedClasses.cheat.CheatGui;
import View.BoardGUI.GamePanels;
import View.GameFlowGUI.GameFlowPanel;
import View.StartMenuGUI.Introduction;
import View.StartMenuGUI.StartingScreen;
import com.google.gson.Gson;
import soundservice.SoundService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static final Gson gson = new Gson();

    static PlayerAndTiles buildPlayerList(List<PlayerFromJSON> playerListFromJSON) {
        var tileCollection = new TileCollection();
        var playerList = new PlayerList();
        for (int i = 0; i < playerListFromJSON.size(); i++) {
            var playerData = playerListFromJSON.get(i);
            var name = playerData.name;
            var gold = playerData.gold;
            var netWorth = playerData.netWorth;
            var rank = playerData.rank;
            var color = playerData.color;
            var owns = playerData.owns;

            playerList.addNewPlayer(name, color);
            var player = playerList.getPlayerFromIndex(i);
            player.setBalance(gold);
            player.setNetWorth(netWorth);
            player.setPlayerRank(PlayerRanks.valueOf(rank));

            for (var index : owns) {
                var tile = tileCollection.getTileAtIndex(index);
                if (tile instanceof Property) {
                    var property = (Property) tile;
                    property.setOwner(player);
                    player.addNewProperty(property);
                } else if (tile instanceof Tavern) {
                    var tavern = (Tavern) tile;
                    tavern.setOwner(player);
                    player.addNewTavern(tavern);
                } else {
                    throw new RuntimeException("Index " + index + " is not a Property or Tavern.");
                }
            }
        }
        return new PlayerAndTiles(tileCollection, playerList);
    }

    static List<PlayerFromJSON> loadPlayerData(String filename) {
        try {
            var data = Files.readString(Paths.get(filename));
            var players = gson.fromJson(data, PlayerFromJSON[].class);
            return Arrays.asList(players);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't open config file: " + filename);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SoundService.instance().playBgMusic();
        TileCollection tileCollection = null;
        PlayerList playerList = null;

        if (args.length == 0) {
            // Normal start loads the starting screen and lets you manually enter player information.
            tileCollection = new TileCollection();
            CompletableFuture<PlayerList> retrievePlayerList = new CompletableFuture<>();
            StartingScreen su = new StartingScreen(retrievePlayerList);
            su.initializeGUI();
            // Blocks here until the user clicks on the start game button.
            playerList = retrievePlayerList.get();
        } else if (args.length == 1){
            // Player state is loaded from JSON.
            var playersFromJSON = loadPlayerData(args[0]);
            PlayerAndTiles playerAndTiles = buildPlayerList(playersFromJSON);
            tileCollection = playerAndTiles.tileCollection;
            playerList = playerAndTiles.playerList;
        } else {
            // The program does not handle more than 1 argument. Closing to avoid confusion.
            System.err.println("Too many command line arguments.");
            System.exit(1);
        }

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
