package soundservice;

public enum SoundFx {

	SOUND_COIN {
		@Override
		public String getSoundFilePath() {
			return "sounds/CoinDrop.wav";
		}
	},

	SOUND_WITCH {
		@Override
		public String getSoundFilePath() {
			return "sounds/cackle3.wav";
		}
	},

	SOUND_WORK {
		@Override
		public String getSoundFilePath() {
			return "sounds/Scissor_Snip.wav";
		}
	},

	SOUND_MONEY {
		@Override
		public String getSoundFilePath() {
			return "sounds/Coins1.wav";
		}
	},

	SOUND_PRISON {
		@Override
		public String getSoundFilePath() {
			return "sounds/punches.wav";
		}
	},

	SOUND_PRISON2 {
		@Override
		public String getSoundFilePath() {
			return "sounds/Cry+2.wav";
		}
	},

	SOUND_PRISON3 {
		@Override
		public String getSoundFilePath() {
			return "sounds/gateclosing.wav";
		}
	},

	SOUND_CHEER {
		@Override
		public String getSoundFilePath() {
			return "sounds/cheerPrison.wav";
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
