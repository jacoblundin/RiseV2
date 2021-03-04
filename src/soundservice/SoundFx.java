package soundservice;

public enum SoundFx {
    SOUND_COIN("sounds/CoinDrop.wav"),
    SOUND_WITCH("sounds/cackle3.wav"),
    SOUND_WORK("sounds/Scissor_Snip.wav"),
    SOUND_MONEY("sounds/Coins1.wav"),
    SOUND_PRISON("sounds/punches.wav"),
    SOUND_PRISON2("sounds/Cry+2.wav"),
    SOUND_PRISON3("sounds/gateclosing.wav"),
    SOUND_CHEER("sounds/cheerPrison.wav"),
    SOUND_DICE("sounds/RollDice.wav"),
    SOUND_RANK("sounds/MagicWand.wav"),
    SOUND_DEFEND("sounds/defend_self.wav"),
    SOUND_WAR_NOT_OVER("sounds/war_not_over.wav"),
    SOUND_AWKWARD("sounds/awkward.wav");

    private final String filePath;

    SoundFx(String filePath) {
        this.filePath = filePath;
    }

    public String getSoundFilePath() {
        return filePath;
    }
}
