package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.ui.SoundPlayer;

import javax.sound.sampled.*;
import java.io.IOException;

public class ClipSoundPlayer implements SoundPlayer {

    @Override
    public void play(Sound sound)  {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputOf(sound));
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private AudioInputStream audioInputOf(Sound sound) throws UnsupportedAudioFileException, IOException {
        return AudioSystem.getAudioInputStream(ClipSoundPlayer.class.getResource("/sounds/" + sound.name() + ".wav"));
    }
}
