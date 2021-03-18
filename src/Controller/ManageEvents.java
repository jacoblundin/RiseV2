package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;

import View.Duel.Duel;
import View.GameFlowGUI.GameFlowPanel;
import View.WestGUI.WestSidePanel;
import View.BoardGUI.Board;
import View.EastGUI.EastSidePanel;
import View.PopUpGUI.DeathGUI;
import View.PopUpGUI.FortuneTellerGUI;
import View.PopUpGUI.SecretGui;
import View.PopUpGUI.WinGui;
import Model.player.Player;
import Model.player.PlayerList;
import Model.player.PlayerRanks;
import Model.Tiles.FortuneTeller;
import Model.Tiles.GoToJail;
import Model.Tiles.Jail;
import Model.Tiles.Property;
import Model.Tiles.SundayChurch;
import Model.Tiles.Tavern;
import Model.Tiles.Tax;
import Model.Tiles.Tile;
import Model.Tiles.Work;
import gamehistorylog.GameHistoryLog;
import soundservice.SoundFx;
import soundservice.SoundService;

/**
 * The class handles all the Controller.events that occur when a Model.player lands on a tile.
 *
 * @author Seth Oberg, Rohan Samandari, Muhammad Abdulkhuder, Sebastian Viro, Aevan Dino.
 */
public class ManageEvents {
    private PlayerList playerList;
    private Board board;
    private GameFlowPanel gameFlowPanel;
    private DeathGUI deathGUI;
    private FortuneTellerGUI msgGUI;
    private EastSidePanel eastPanel;
    private Random rand = new Random();
    private int roll;
    private int taxCounter = 0;
    private WestSidePanel westPanel;
    private GameHistoryLog gameHistoryLog;
    private Controller controller;

    /**
     * Constructor initializes objects in the parameter. Creates Death -and MessageGUI.
     *
     * @param board
     * @param playerList
     * @param pnlWest
     * @param gameFlowPanel
     * @param eastPanel
     */
    public ManageEvents(Board board, PlayerList playerList, WestSidePanel pnlWest, GameFlowPanel gameFlowPanel,
                        EastSidePanel eastPanel, Controller controller) {
        this.gameFlowPanel = gameFlowPanel;
        this.westPanel = pnlWest;
        this.board = board;
        this.playerList = playerList;
        this.eastPanel = eastPanel;
        deathGUI = new DeathGUI();
        msgGUI = new FortuneTellerGUI();
        gameHistoryLog = GameHistoryLog.instance();
        this.controller = controller;
    }

    /**
     * Method checks what type of tile the Model.player has landed on.
     *
     * @param tile    the Model.player landed on.
     * @param player, Model.player who landed on a tile.
     */
    public void newEvent(Tile tile, Player player) {
        player.checkPlayerRank();
        checkDuel(player);

        if (player.getPlayerRank() == PlayerRanks.KINGS) {
            new WinGui(player);
        }

        if (playerList.getLength() == 1) {
            new WinGui(player);
        }

        if (tile instanceof Property) {
            propertyEvent(tile, player);
        }

        if (tile instanceof Tax) {
            taxEvent(tile, player);
        }

        if (tile instanceof Jail) {
            jailEvent(tile, player);
        }

        if (tile instanceof GoToJail) {
            goToJailEvent(tile, player);
        }

        if (tile instanceof Tavern) {
            tavernEvent(tile, player);
        }

        if (tile instanceof SundayChurch) {
            churchEvent(player);
        }

        if (tile instanceof Work) {
            workEvent(tile, player);
        }

        if (tile instanceof FortuneTeller) {
            fortuneTellerEvent(tile, player);
        }

        controller.updatePlayerRanks();
        controller.redrawPlayerInfo();
    }

    /**
     * This method is supposed to be called from any class that requires the current
     * player to pay any amount, if the user does not have the amount required they
     * should be removed from the game
     */
    public void control(Player player, int amount) {
        if (player.getBalance() < amount) {
            player.setIsAlive(false);
            playerList.switchToNextPlayer();
            playerList.eliminatePlayer(player);
            playerList.updatePlayerList();
            eastPanel.addTabs();
            board.removePlayer(player);
            deathGUI.addGui(player);
            controller.drawBorderColors();
            gameHistoryLog.logPlayerEliminatedEvent(player);
        }
    }

    /**
     * Method called when player lands on a property. Checks if it's availability and if the Model.player has to pay rent or
     * can purchase the property.
     *
     * @param tile
     * @param player
     */
    public void propertyEvent(Tile tile, Player player) {
        Property property = (Property) tile;

        if (property.getPurchasable()) {
            if (player.getBalance() < property.getPrice()) {
                JOptionPane.showMessageDialog(null, "Not enough funds to purchase this property.");
            } else {
                propertyDialog(property, player);
            }
        } else {
            int rent = property.getTotalRent();
            control(player, rent);
            if (player.isAlive()) {
                var owner = property.getOwner();
                JOptionPane.showMessageDialog(null, player.getName() + " you have to pay " + rent + " gold in rent to "
                        + owner.getName() + ".");
                gameHistoryLog.logPropertyRentEvent(player, property);
                player.decreaseBalance(rent);
                player.decreaseNetWorth(rent);
                owner.increaseBalance(rent);
                owner.increaseNetWorth(rent);
            }
        }
    }

    /**
     * Method called when the player lands on a work tile.
     *
     * @param tile
     * @param player
     */
    public void workEvent(Tile tile, Player player) {
        Work tempWorkObject = (Work) tile;
        tempWorkObject.setPlayer(player);
        tempWorkObject.payPlayer(getRoll());

        gameHistoryLog.logWorkEvent(player, tempWorkObject.getPay());
        SoundService.instance().playSoundFx(SoundFx.SOUND_WORK);
        JOptionPane.showMessageDialog(null,
                "You're a hard worker. \nThe dice are rolled and shows " + roll + "\n" + " therefore you get " + tempWorkObject.getPay() + " gold for your hard work.");
    }

    /**
     * Method called when the player lands on a tax tile.
     *
     * @param tile
     * @param player
     */
    public void taxEvent(Tile tile, Player player) {
        Tax tempTaxObject = (Tax) tile;
        int chargePlayer = tempTaxObject.getTax();
        control(player, chargePlayer);

        if (player.isAlive()) {
            SoundService.instance().playSoundFx(SoundFx.SOUND_CHURCHTAX);
            gameHistoryLog.logTaxEvent(player, 200);
            JOptionPane.showMessageDialog(null, "You have to pay 200 gold in tax to the Church.");
            player.decreaseBalance(chargePlayer);
            player.decreaseNetWorth(chargePlayer);
            taxCounter++;
            SoundService.instance().playSoundFx(SoundFx.SOUND_MONEY);
        }
    }

    /**
     * Gets the total tax paid by players
     *
     * @return total tax
     */
    public int getChurchTax() {
        int totalTax = taxCounter * 200;
        return totalTax;
    }

    /**
     * Method called when players lands on a tavern tile, checks it's availability.
     *
     * @param tile
     * @param player
     */
    public void tavernEvent(Tile tile, Player player) {
        Tavern tempTavernObj = (Tavern) tile;

        if (tempTavernObj.getPurchasable()) {
            if (player.getBalance() < tempTavernObj.getPrice()) {
                JOptionPane.showMessageDialog(null, "You have not enough funds to purchase this tavern.");
            } else {
                tavernDialog(tempTavernObj, player);
            }
        } else {
            int randomValue = 0;

            if (tempTavernObj.getOwner().getAmountOfTaverns() == 1) {
                randomValue = (getRoll() * 10);
            } else if (tempTavernObj.getOwner().getAmountOfTaverns() == 2) {
                randomValue = (getRoll() * 20);
            }

            control(player, randomValue);
            if (player.isAlive() == true) {
                JOptionPane.showMessageDialog(null, player.getName() + " you have to pay " + randomValue + " gold in rent to "
                        + tempTavernObj.getOwner().getName() + ".");
                gameHistoryLog.logTavernRentEvent(player, tempTavernObj, randomValue);
                tempTavernObj.getOwner().increaseBalance(randomValue);
                tempTavernObj.getOwner().increaseNetWorth(randomValue);
                player.decreaseBalance(randomValue);
                player.decreaseNetWorth(randomValue);
            }
        }
    }

    /**
     * Method for jailed players, giving them the option to pay bail if the have enough balance.
     *
     * @param tile
     * @param player in jail
     */
    public void jailEvent(Tile tile, Player player) {
        if (player.isPlayerInJail() && (player.getJailCounter()) < 2) {
            player.increaseJailCounter();
            if (player.getBalance() > (player.getJailCounter() * 50)) {
                jailDialog(player);
            } else {
                JOptionPane.showMessageDialog(null, "Sorry, you can not afford the bail.");
                SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON);
            }
        } else if (player.getJailCounter() >= 2) {
            player.setPlayerIsInJail(false);
            player.setJailCounter(0);
            gameHistoryLog.logJailExitEvent(player);
            gameFlowPanel.activateRollDice();
        }
    }

    /**
     * Method to jail a player.
     *
     * @param tile
     * @param player
     */
    public void goToJailEvent(Tile tile, Player player) {
        player.setPlayerIsInJail(true);
        board.removePlayer(player);
        player.setPositionInSpecificIndex(10);
        board.setPlayer(player);
        JOptionPane.showMessageDialog(null, "Sorry, " + player.getName() + " you are going to jail.");
        SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON2);
        SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON3);
        gameHistoryLog.logJailEnterEvent(player);
    }

    /**
     * Method called if the player lands on sunday church. Pays out all the collected tax then resets the counter.
     *
     * @param player
     */
    public void churchEvent(Player player) {
        SoundService.instance().playSoundFx(SoundFx.SOUND_SUNDAYCHURCH);
        int taxPayout = 200 * taxCounter;
        player.increaseBalance(taxPayout);
        player.increaseNetWorth(taxPayout);
        taxCounter = 0;
        gameHistoryLog.logTaxPayoutEvent(player, taxPayout);
    }

    /**
     * Checks if two players are on the same tile and should start a duel.
     */
    private void checkDuel(Player activePlayer) {
        List<Player> playersOnTile = new ArrayList<Player>();
        Player player = null;

        for (int i = 0; i < playerList.getLength(); i++) {
            player = playerList.getActivePlayers().get(i);
            int positionOfPlayer = player.getPosition();

            if (activePlayer.getPosition() == positionOfPlayer && !activePlayer.getName().equals(player.getName())) {
                playersOnTile.add(player);
            }
        }

        if (!playersOnTile.isEmpty() && playersOnTile.size() > 1) {
            int playerNbr = 0;
            boolean validInt = true;

            while (validInt == true) {
                try {
                    playerNbr = Integer.parseInt(JOptionPane.showInputDialog("Which player would you like to meet in a duel? Write their number:"));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "You did not enter a valid number. Please try again.");
                }

                for (Player playerCheck : playersOnTile) {
                    if (playerNbr == (playerCheck.getPlayerIndex() + 1)) {
                        validInt = false;
                        Duel duel = new Duel(activePlayer, playerCheck, controller);
                    }
                }
            }
        } else if (playersOnTile.size() == 1) {
            player = playersOnTile.get(0);
            Duel duel = new Duel(activePlayer, player, controller);
        }
    }

    /**
     * Method for a dialog if the player is able to purchase a property.
     *
     * @param property in question.
     * @param player   in question.
     */
    public void propertyDialog(Property property, Player player) {
        int yesOrNo = JOptionPane.showConfirmDialog(null,
                property.getName() + "\n" + "Do you want to purchase this property for " + property.getPrice() + " gold coins?",
                "Decide your fate!", JOptionPane.YES_NO_OPTION);

        if (yesOrNo == 0 && (property.getPrice() <= player.getBalance())) {
            property.setOwner(player);
            player.addNewProperty(property);
            player.decreaseBalance(property.getPrice());
            gameHistoryLog.logPropertyBuyEvent(player, property);
            controller.drawBorderColors();
        }
    }

    /**
     * Method for a dialog if the player wants to purchase a tavern.
     *
     * @param tavern, the to buy.
     * @param player, Model.player who landed on the tavern.
     */
    public void tavernDialog(Tavern tavern, Player player) {
        int yesOrNo = JOptionPane.showConfirmDialog(null, "Do you want to purchase this tavern for " + tavern.getPrice() + " gold coins?",
                "Decide your fate!", JOptionPane.YES_NO_OPTION);

        if (yesOrNo == 0 && (tavern.getPrice() <= player.getBalance())) {
            tavern.setOwner(player);
            player.addNewTavern(tavern);
            player.decreaseBalance(tavern.getPrice());
            controller.drawBorderColors();
            gameHistoryLog.logTavernBuyEvent(player, tavern);
        }
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    /**
     * Message for the prisoner to choose if the Model.player wants to pay the bail and
     * get free
     *
     * @param player in jail.
     */
    public void jailDialog(Player player) {
        int yesOrNo = JOptionPane.showConfirmDialog(null,
                "Do you want to pay the bail\nof " + (player.getJailCounter() * 50) + " gold coins?", "Bail",
                JOptionPane.YES_NO_OPTION);
        int totalBail = player.getJailCounter() * 50;
        if (yesOrNo == 0 && (totalBail <= player.getBalance())) {
            player.setJailCounter(0);
            player.setPlayerIsInJail(false);
            gameHistoryLog.logJailExitCostEvent(player, totalBail);
            gameFlowPanel.activateRollDice();
            SoundService.instance().playSoundFx(SoundFx.SOUND_CHEER);
        } else {
            SoundService.instance().playSoundFx(SoundFx.SOUND_PRISON);
            gameHistoryLog.logJailStayEvent(player);
        }
    }

    /**
     * Method for FortuneTeller, small chance for a secret event to trigger.
     *
     * @param tile,   tile the Model.player landed on.
     * @param player, Model.player in question.
     */
    private void fortuneTellerEvent(Tile tile, Player player) {
        FortuneTeller tempCard = (FortuneTeller) tile;
        if (rand.nextInt(10) == 0) {
            new SecretGui();
            new Thread(new SecretSleeper(tempCard, player));
            eastPanel.addTabs();
        } else {
            fortune(tempCard, player);
        }
    }

    /**
     * Method that either withdraws or adds gold coins to a player depending on the type of fortune.
     *
     * @param tempCard, instance of FortuneTeller
     * @param player,   Model.player who landed on the tile
     */
    public void fortune(FortuneTeller tempCard, Player player) {
        tempCard.setAmount(rand.nextInt(600) - 300);
        if (tempCard.getAmount() < 0) {
            int pay = (tempCard.getAmount() * -1);
            tempCard.setIsBlessing(false);
            tempCard.setFortune("CURSE");
            control(player, pay);
            if (player.isAlive()) {
                player.decreaseBalance(pay);
                player.decreaseNetWorth(pay);
                msgGUI.newFortune(false, pay);
                gameHistoryLog.logFortuneCurseEvent(player, pay);
                SoundService.instance().playSoundFx(SoundFx.SOUND_FORTUNE_CURSE);
            }
        } else {
            int pay = (tempCard.getAmount());
            tempCard.setIsBlessing(true);
            tempCard.setFortune("BLESSING");
            player.increaseBalance(tempCard.getAmount());
            player.increaseNetWorth(tempCard.getAmount());
            msgGUI.newFortune(true, tempCard.getAmount());
            gameHistoryLog.logFortuneBlessingEvent(player, pay);
            SoundService.instance().playSoundFx(SoundFx.SOUND_FORTUNE_BLESSING);
        }
    }

    /**
     * This class is an easter egg. That gives the player 5 fortunes.
     *
     * @author Sebastian viro ,Muhammad Abdulkhuder
     */
    private class SecretSleeper extends Thread {
        private FortuneTeller tempCard;
        private Player player;
        private Clip clip;

        /**
         * @param tempCard
         * @param player   Starts the sleeper
         */
        public SecretSleeper(FortuneTeller tempCard, Player player) {
            JOptionPane.showMessageDialog(null, "You got an easter egg!\n You get five fortunes");
            this.tempCard = tempCard;
            this.player = player;
            start();
        }

        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    File musicPath = new File("music/duraw.wav");
                    AudioInputStream ais = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB = (float) (Math.log(0.08) / Math.log(10.0) * 20.0);
                    gainControl.setValue(dB);
                    clip.start();
                    fortune(tempCard, player);
                    Thread.sleep(4000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }
}