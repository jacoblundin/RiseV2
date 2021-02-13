package soundservice;

/**
 * SoundService is a class for interacting with the background music and sound effect systems.
 *
 * SoundService is modeled as a Singleton, this is because the current architecture
 * of the system isn't very well structured. The different sound services will most
 * likely be accessed from widely different scopes.
 */
public class SoundService {

	private SoundFxThread soundFxThread;
	private BackgroundMusic backgroundMusic;

	private static SoundService instance;

	public static SoundService instance() {

		if (instance == null) {
			instance = new SoundService();
		}

		return instance;
	}

	private SoundService(){
		soundFxThread = new SoundFxThread();
		soundFxThread.start();

		backgroundMusic = new BackgroundMusic();

	}

	//SoundFx methods

	/**
	 * Call this method to play a sound effect.
	 *
	 * @param soundFx
	 */
	public void playSoundFx(SoundFx soundFx) {
		soundFxThread.playSoundFx(soundFx);
	}

	public void muteSoundFx() {

	}

	public void unmuteSoundFx() {

	}

	//Background music methods

	/**
	 * Call this method to start/resume the background music
	 */
	public void playBgMusic() {
		backgroundMusic.playMusic();
	}

	/**
	 * Call this method to pause the background music
	 */
	public void pauseBgMusic() {
		backgroundMusic.pauseMusic();
	}

	/**
	 * Call this method to toggle the playing state of the background music
	 * If the music is playing it will pause and if it is paused it will resume.
	 */
	public void toggleBgMusic() {
		backgroundMusic.toggleMusic();
	}


}
