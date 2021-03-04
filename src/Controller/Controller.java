package Controller;

import Model.Tiles.Property;
import Model.player.Player;
import Model.player.PlayerList;
import View.BoardGUI.Board;
import View.Duel.Duel;
import View.EastGUI.EastSidePanel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;
import dice.Dice;
import gamehistorylog.GameHistoryLog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final Board board;
    private final PlayerList playerList;
    private final WestSidePanel westSidePanel;
    private final GameFlowPanel gameFlowPanel;
    private final EastSidePanel eastSidePanel;
    private final ManageEvents manageEvents;
    private final Dice dice;

    public Controller(Board board, PlayerList playerList, WestSidePanel westPanel, GameFlowPanel gameFlowPanel,
                      EastSidePanel eastSidePanel, Dice dice) {
        this.board = board;
        this.playerList = playerList;
        this.westSidePanel = westPanel;
        this.gameFlowPanel = gameFlowPanel;
        this.eastSidePanel = eastSidePanel;
        this.manageEvents = new ManageEvents(board, playerList, westPanel, gameFlowPanel, eastSidePanel, this);
        this.dice = dice;
    }

    public void rollDice() {
        gameFlowPanel.setRollButton(false);
        dice.roll(this::diceRoll);
    }

    private void diceRoll(int roll) {
        Player activePlayer = playerList.getActivePlayer();
        activePlayer.checkPlayerRank();
        manageEvents.setRoll(roll);
        Thread movePlayerThread = new Thread(new PlayerMover(roll, this));
        movePlayerThread.start();
        redrawPlayerInfo();
    }

    public void endTurn() {
        /*
         * When a player ends their turn
         * If the next player is in jail they will not have the ability to roll the
         * dice and will only have the ability to end their turn if they have not paid the bail
         * If the player is not in jail they can roll the dice
         */
        playerList.switchToNextPlayer();
        gameFlowPanel.updateShowPlayersTurn();

        if (playerList.getActivePlayer().isPlayerInJail()) {
            gameFlowPanel.setRollButton(false);
            gameFlowPanel.setEndTurnButton(true);
            manageEvents.newEvent(board.getDestinationTile(playerList.getActivePlayer().getPosition()),
                    playerList.getActivePlayer());
        } else if (!playerList.getActivePlayer().isPlayerInJail()) {
            gameFlowPanel.setRollButton(true);
            gameFlowPanel.setEndTurnButton(false);
        }

        redrawPlayerInfo();
        eastSidePanel.setTab();
    }

    public void upgradeProperty(Property property) {
        property.increaseLevel();
        redrawPlayerInfo();
    }

    public void sellProperty(Property property) {
        var owner = property.getOwner();
        owner.sellProperty(property);
        redrawPlayerInfo();
        board.drawBorders();
    }

    public void drawBorderColors() {
        board.drawBorders();
    }

    public void redrawPlayerInfo() {
        eastSidePanel.addTabs();
    }

    public void downgradeProperty(Property property) {
        property.decreaseLevel();
        redrawPlayerInfo();
    }

	public void goEvent() {
		Player activePlayer = playerList.getActivePlayer();
		int payout = 200;

		if (activePlayer.passedGo()) {

			activePlayer.increaseBalance(200);
			activePlayer.increaseNetWorth(200);

			//Log the pass go event
			GameHistoryLog.instance().logPassGoEvent(activePlayer, payout);
			activePlayer.resetPassedGo();
		}
	}

	/* Move the player i steps */
    public void moveWCheat(int i) {
        playerList.getActivePlayer().checkPlayerRank();
        board.removePlayer(playerList.getActivePlayer());
        playerList.getActivePlayer().setPosition(i);
        board.setPlayer(playerList.getActivePlayer());
        manageEvents.setRoll(i);
        goEvent();
        manageEvents.newEvent(board.getDestinationTile(playerList.getActivePlayer().getPosition()),
                playerList.getActivePlayer());
        redrawPlayerInfo();
        gameFlowPanel.setEndTurnButton(true);
    }

    public void duelWinner(Player winner, Player loser)
    {
        winner.increaseBalance(500);
        loser.decreaseBalace(500);
    }

    private class PlayerMover implements Runnable {
        //TODO: Needs refactoring, this should only graphically move the players piece.
        // But it handles more than just the view, which is unnecessary.
        int roll;
        Player activePlayer;
        Controller controller;

        public PlayerMover(int roll, Controller controller) {
            this.roll = roll;
            this.controller = controller;
            this.activePlayer = playerList.getActivePlayer();
        }

        public void run() {
            for (int i = 0; i < roll; i++) {
                board.removePlayer(activePlayer);
                activePlayer.setPosition(1);
                board.setPlayer(activePlayer);

                if (i == (roll - 1)) {
                    //Log dice roll event
                    GameHistoryLog.instance().logDiceRollEvent(activePlayer, board.getDestinationTile(activePlayer.getPosition()), roll);

                    //Create the event the landing tile
                    manageEvents.newEvent(board.getDestinationTile(activePlayer.getPosition()),
                            activePlayer);

                    goEvent();
                    redrawPlayerInfo();
                    checkDuel();
                    gameFlowPanel.setEndTurnButton(true);
                }

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        /**
         * Checks if two players are on the same tile and should start a duel.
         */
        private void checkDuel()
        {

            /*
            ArrayList playersOnTile = new ArrayList() är lite problematiskt.

            ArrayList är en "generisk" klass vilket innebär att man kan specificera vilken typ av objekt som
            kan placeras i listan. Man kan t ex skriva ArrayList<String> foo = new ArrayList<>(); för att skapa
            en ArrayList som kompilatorn garanterar att man enbart kan stoppa in strängar i.

            Generiska klasser som ArrayList kan dock användas "raw" vilket du gjort här. Då specificerar man inte
            typen och från kompilatorns perspektiv så är det Object man jobbar med när man lägger till/tar bort.
            Denna möjlighet finns eftersom generiska klasser inte fanns från början utan lades till vid något senare
            tillfälle (runt 2005 tror jag). ArrayList skulle alltså kunna användas i en väldigt tidig javaversion
            som inte har generics detta sätt.

            Det praktiska problem du får i just denna kod är att kompilatorn inte fattar att det är Player-objekt
            som stoppats in och du måste därför casta till Player längre ner i koden när du använder get-metoden. Ett
            ännu större problem, som dock inte finns här, är att man kan råka stoppa in något som inte är Player och
            sen försöker casta till Player när man tar ut det: crash vid runtime.

            Det rimliga är därför att ändra till något av följande:
            a) ArrayList<Player> playersOnTile = new ArrayList<>(); // basic lösning.
            b) var playersOnTile = new ArrayList<Player>();         // variant med local type inference. Notera att typen måste specificeras på högersidan här.
            c) List<Player> playersOnTile = new ArrayList<>();      // List-interface istället för att hårdkoda klassen. Mer relevant när listan inte är en lokal variabel.

            Variant c är nog det som traditionellt skulle ansetts vara det bästa. Jag föredrar variant b så länge man
            kan köra på relativt ny Java-version som stödjer var.


             */
            ArrayList playersOnTile = new ArrayList();
            Player player = null;

            for (int i = 0; i < playerList.getLength() ; i++)
            {
                player = (Player) playerList.getActivePlayers().get(i);
                int positionOfPlayer = player.getPosition();
                int index = 0;

                if(activePlayer.getPosition() == positionOfPlayer && !activePlayer.getName().equals(player.getName()))
                {
                        playersOnTile.add(player);
                    System.out.println("Player added to playersOnTile: " + player.getName());
                }
            }

            if(!playersOnTile.isEmpty() && playersOnTile.size() > 1)
            {
                int playerNbr = Integer.parseInt(JOptionPane.showInputDialog("Which player would you like to meet in a duel? Write their number:"));
                for(int i = 0; i<playersOnTile.size() ; i++)
                {
                    Player playerCheck = (Player)playersOnTile.get(i);
                    if(playerNbr == playerCheck.getPlayerIndex())
                    {
                        Duel duel = new Duel(activePlayer, playerCheck, controller);
                    }
                }
            }
            else if(!playersOnTile.isEmpty() && playersOnTile.size() == 1){
                player = (Player)playersOnTile.get(0);
                Duel duel = new Duel(activePlayer, player, controller);
            }

        }
    }
}
