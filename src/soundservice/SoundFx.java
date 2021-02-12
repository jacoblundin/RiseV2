package soundservice;

public enum SoundFx {

	SOUND_COIN {
		@Override
		public String getSoundFilePath() {
			return "sounds/CoinDrop.wav";
		}
	},
	SOUND_DICE {
		@Override
		public String getSoundFilePath() {
			return "sounds/RollDice.wav";
		}
	};

	public String getSoundFilePath() {
		return "";
	}
}
