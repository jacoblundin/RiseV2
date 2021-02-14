package dice;

import soundservice.SoundFx;
import soundservice.SoundService;

import java.util.Random;

/**
 * This class wraps a DicePanel with code that generates a roll.
 *
 * The underlying DicePanel can be retrieved through getDicePanel
 */
public class Dice {

	private DicePanel dicePanel;
	private Random random;


	public Dice() {
		this.dicePanel = new DicePanel();
		this.random = new Random();
	}

	/**
	 * Call this method to "roll" the dice.
	 *
	 * This will generate the roll values and animate the dicePanel. The method takes
	 * a callback as a parameter. This callback will be executed once the dice rolling
	 * animation finishes. The callback parameter is the total roll value. This also accounts for double rolls.
	 *
	 * @param callback The callback that will be executed once the dice rolling animation finishes
	 */
	public void roll(ICallback<Integer> callback) {

		int d1Roll = random.nextInt(6) + 1;
		int d2Roll = random.nextInt(6) + 1;

		int sumRoll = d1Roll == d2Roll ? (d1Roll + d2Roll) : (d1Roll + d2Roll) * 2;

		//TODO There is probably a better way to synchronize than 2 callbacks.
		dicePanel.animateRoll(
				d1Roll,
				d2Roll,
				v -> {
					if (callback != null) {
						callback.callback(sumRoll);
					}
				}
		);

		//Play dice sound
		SoundService.instance().playSoundFx(SoundFx.SOUND_DICE);

		//TODO Log the roll to the history

	}

	/**
	 * Get the graphical panel of the dice.
	 *
	 * @return The DicePanel of the dice.
	 */
	public DicePanel getDicePanel() {
		return dicePanel;
	}
}