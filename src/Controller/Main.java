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

    public static void main(String[] args) {
        try {
            SoundService.instance().playBgMusic();
            TileCollection tileCollection = null;
            PlayerList playerList = null;

            if (args.length == 0) {
                tileCollection = new TileCollection();
                var retrievePlayerList = new CompletableFuture<PlayerList>();
                var su = new StartingScreen(retrievePlayerList);
                su.initializeGUI();
                playerList = retrievePlayerList.get();
            } else if (args.length == 1) {
                var playersFromJSON = loadPlayerData(args[0]);
                var playerAndTiles = buildPlayerList(playersFromJSON);
                tileCollection = playerAndTiles.tileCollection;
                playerList = playerAndTiles.playerList;
            } else {
                System.err.println("Too many command line arguments.");
                System.exit(1);
            }

            var intro = new Introduction();
            var gamePanels = new GamePanels(playerList, tileCollection, intro);
            gamePanels.startBoard();

            var controller = new Controller(gamePanels.getBoard(), playerList, gamePanels.getWestPanel(),
                    gamePanels.getGameFlowPanel(), gamePanels.getEastSidePanel(), gamePanels.getGameFlowPanel().getDice());

            gamePanels.setController(controller);

            // If you want the cheat GUI:
//        GameFlowPanel gameFlowPanel = gamePanels.getGameFlowPanel();
//        gameFlowPanel.setCheatGUI(new CheatGui(controller));
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("ExecutionException or InterruptedException when loading game. Not handled.");
            System.exit(1);
        }
    }
}
