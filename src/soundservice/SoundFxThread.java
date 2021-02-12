package soundservice;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundFxThread extends Thread {

	private Buffer<SoundFx> soundBuffer;

	public SoundFxThread() {
		this.soundBuffer = new Buffer<>();
	}

	@Override
	public void run() {

		SoundFx sound;

		while (true) {
			try {
				sound = soundBuffer.get();

				new Thread(new SoundFxConsumer(sound)).start();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to play a sound effect.
	 * Pass a soundFx as the parameter and that soundFx will be played.
	 *
	 * @param soundFx Sound effect that will be played.
	 */
	public void playSoundFx(SoundFx soundFx) {
		this.soundBuffer.put(soundFx);
	}

	private class SoundFxConsumer implements Runnable {

		private String filePath;

		public SoundFxConsumer(SoundFx soundFx) {
			this.filePath = soundFx.getSoundFilePath();
		}

		@Override
		public void run() {
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
