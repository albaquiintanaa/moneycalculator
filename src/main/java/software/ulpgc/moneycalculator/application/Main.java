package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.control.ExchangeMoneyCommand;
import software.ulpgc.moneycalculator.architecture.ui.SoundPlayer;

import javax.swing.*;

public class Main {
    private static final ClipSoundPlayer clipSoundPlayer = new ClipSoundPlayer();
    static void main() {
        Desktop desktop = new Desktop(new WebService.CurrencyLoader().loadAll());
        desktop.addCommand("exchange", new ExchangeMoneyCommand(
                desktop.moneyDialog(),
                desktop.currencyDialog(),
                new WebService.ExchangeRateLoader(),
                desktop.moneyDisplay(),
                clipSoundPlayer
        ));
        desktop.addCommand("error", Main::showError);
        desktop.setVisible(true);
    }

    private static void showError() {
        clipSoundPlayer.play(SoundPlayer.Sound.Error);
        JOptionPane.showMessageDialog(
                null,
                "Invalid input. Por favor, introduce un número positivo mayor que 0",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
