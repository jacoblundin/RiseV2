package soundservice;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * This class is used to select a music file. The chosen music file
 * can be Model.player and stopped via the View.GUI.
 *
 * @author AevanDino
 */
public class BackgroundMusicThread extends Thread {

	private Clip clip;
	private Thread musicPlayer;
	private int musicPausedAt = 0;

	public BackgroundMusicThread() {
		this.clip = null;
	}

	/**
	 * If music isn't already playing, this method will start
	 * playing the chosen file, that is if there is a file to be played.
	 */
	public void startMusic() {

		if (clip != null) {
			clip.setFramePosition(musicPausedAt);
			clip.start();
		}
	}

	/**
	 * Pauses music, music continues where it stopped when Model.player asks for music again.
	 */
	public void pauseMusic() {
		if (clip != null) {
			musicPausedAt = clip.getFramePosition();
			clip.stop();
		}
	}

	/**
	 * Run method from Thread class. This method starts playing music until told to stop.
	 */
	@Override
	public void run() {
			try {
				File musicPath = new File("music/bgMusic.wav");
				AudioInputStream ais = AudioSystem.getAudioInputStream(musicPath);
				clip = AudioSystem.getClip();
				clip.open(ais);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.start();
			} catch (Exception e) {
				System.out.println("===== Exception occurred =====");
				e.printStackTrace();
				System.out.println("===== ===== =====");
			}
	}
}