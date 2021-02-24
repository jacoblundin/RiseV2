package Controller;

import Model.player.PlayerList;
import View.StartMenuGUI.StartingScreen;
import soundservice.SoundService;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SoundService.instance().playBgMusic(); //Start the background music

        /* Returning the PlayerList from the StartingScreen. This should let us inject a different
        player list and bypass the starting screen. */
        CompletableFuture<PlayerList> retrievePlayerList = new CompletableFuture<>();
        StartingScreen su = new StartingScreen(retrievePlayerList);
        su.initializeGUI();

        // Blocks here until the user clicks on the start game button.
        PlayerList playerList = retrievePlayerList.get();
    }
}
