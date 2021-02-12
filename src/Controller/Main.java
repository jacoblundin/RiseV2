package Controller;

import View.StartMenuGUI.StartingScreen;
import soundservice.SoundService;

public class Main {

    public static void main(String[] args) {

        SoundService.instance(); //Initiate SoundService

        StartingScreen su = new StartingScreen();
        su.initializeGUI();
    }
}
