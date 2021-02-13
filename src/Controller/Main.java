package Controller;

import View.StartMenuGUI.StartingScreen;
import soundservice.SoundService;

public class Main {

    public static void main(String[] args) {

        SoundService.instance().playBgMusic(); //Start the background music

        StartingScreen su = new StartingScreen();
        su.initializeGUI();
    }
}
