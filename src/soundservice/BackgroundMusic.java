package soundservice;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * This class handles the background music.
 */
public class BackgroundMusic {
	private Clip clip;
	private int musicPausedAt;
	private double gain;

	public BackgroundMusic() {
		clip = null;
		musicPausedAt = 0;
		initClip();
	}

	/**
	 * Initialize clip
	 * Load audio file and setup clip behavior
	 */
	private void initClip() {
		try {
			File musicPath = new File("music/bgMusic.wav");
			AudioInputStream ais = AudioSystem.getAudioInputStream(musicPath);
			clip = AudioSystem.getClip();
			clip.open(ais);

			// Set the gain (between 0.0 and 1.0)
			setVolume(0.05);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			System.out.println("===== Exception occurred =====");
			e.printStackTrace();
			System.out.println("===== ===== =====");
		}
	}

	/**
	 * Start/Resume playing of the background music
	 */
	public void playMusic() {
		if (clip != null && !clip.isRunning()) {
			clip.setFramePosition(musicPausedAt);
			clip.start();
		}
	}

	/**
	 * Pause the background music
	 */
	public void pauseMusic() {
		if (clip != null && clip.isRunning()) {
			musicPausedAt = clip.getFramePosition();
			clip.stop();
		}
	}

	/**
	 * Toggle the music on or off.
	 * If the music is playing it will pause and if it is paused it will resume.
	 */
	public void toggleMusic() {
		if (clip.isRunning()) {
			pauseMusic();
		} else {
			playMusic();
		}
	}

	/**
	 * Sets the volume of the sound effects
 	 */
	public void setVolume(double gain)
	{
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		gainControl.setValue(dB);
	}

	public void changeVolume(double gain)
	{
		this.gain = gain;
		setVolume(gain);
	}
}