package Controller;

import Model.Tiles.Property;
import Model.player.Player;
import Model.player.PlayerList;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;
import dice.Dice;
import gamehistorylog.GameHistoryLog;

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
        this.manageEvents = new ManageEvents(board, playerList, westPanel, gameFlowPanel, eastSidePanel);
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
        Thread movePlayerThread = new Thread(new PlayerMover(roll));
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

    private class PlayerMover implements Runnable {
        //TODO: Needs refactoring, this should only graphically move the players piece.
        // But it handles more than just the view, which is unnecessary.
        int roll;
        Player activePlayer;

        public PlayerMover(int roll) {
            this.roll = roll;
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
                    gameFlowPanel.setEndTurnButton(true);
                }

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
