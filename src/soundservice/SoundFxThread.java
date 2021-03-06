package soundservice;

import javax.sound.sampled.*;
import java.io.File;

public class SoundFxThread extends Thread {
	private Buffer<SoundFx> soundBuffer;
	private boolean soundFxOn = true;
	private double gain = 0.08;

	public SoundFxThread() {
		this.soundBuffer = new Buffer<>();
	}

	@Override
	public void run() {
		SoundFx sound;

		while (true) {
			try {
				sound = soundBuffer.get();
				if(soundFxOn) {
					new Thread(new SoundFxConsumer(sound)).start();
				}
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

	public void setSoundFxOnOff()
	{
		soundFxOn = !soundFxOn;
	}

	public void changeVolume(double gain)
	{
		this.gain = gain;
	}

	private class SoundFxConsumer implements Runnable {

		private String filePath;

		public SoundFxConsumer(SoundFx soundFx) {
			this.filePath = soundFx.getSoundFilePath();
		}

		@Override
		public void run() {
			try {
				AudioInputStream  in = AudioSystem.getAudioInputStream(new File(filePath));
				AudioFormat af = new AudioFormat(44100, 16, 1, true, false);
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(af, in);

				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);

				// set the gain (between 0.0 and 1.0)
				setVolume(clip, gain);

				clip.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void setVolume(Clip clip, double gain)
		{
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
	}
}