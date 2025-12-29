package software.ulpgc.moneycalculator.architecture.ui;

public interface SoundPlayer {
    void play(Sound sound);

    enum Sound {
        Success, Error
    }
}
