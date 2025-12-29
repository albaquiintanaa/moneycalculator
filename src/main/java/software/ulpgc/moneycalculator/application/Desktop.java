package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.control.Command;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.Money;
import software.ulpgc.moneycalculator.architecture.ui.CurrencyDialog;
import software.ulpgc.moneycalculator.architecture.ui.MoneyDialog;
import software.ulpgc.moneycalculator.architecture.ui.MoneyDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Desktop extends JFrame {
    private final Map<String, Command> commands;
    private final List<Currency> currencies;

    private JTextField inputAmount;
    private JComboBox<Currency> inputCurrency;
    private JTextField outputAmount;
    private JComboBox<Currency> outputCurrency;

    public Desktop(List<Currency> currencies) throws HeadlessException {
        this.commands = new HashMap<>();
        this.currencies = currencies;

        buildFrame();
    }

    private void buildFrame() {
        this.setTitle("Money Calculator");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.setLayout(new BorderLayout());

        this.add(header(), BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridLayout(2, 1));
        centerContainer.add(panel());

        centerContainer.add(new CurrencySelector(currencies, this::updateSelectedCurrency));
        
        this.add(centerContainer, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private void updateSelectedCurrency(String code, boolean isLeftClick) {
        Map.of(true, inputCurrency, false, outputCurrency)
                .get(isLeftClick)
                .setSelectedItem(findCurrencyByCode(code));
    }

    private Currency findCurrencyByCode(String code) {
        return currencies.stream()
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(currencies.getFirst());
    }

    private JPanel header() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255,215,0));
        panel.add(setTitle());
        return panel;
    }

    private JLabel setTitle() {
        JLabel title = new JLabel("\uD83E\uDE99 Money Calculator \uD83E\uDE99");
        title.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        return title;
    }

    private JPanel panel() {
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createInputsSection());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createButtonSection());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createResultSection());
        return mainPanel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        return panel;
    }

    private JPanel createInputsSection() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(label("Amount: "));
        panel.add(inputAmount = amountInput());
        panel.add(label("From: "));
        panel.add(inputCurrency = currencySelector());
        panel.add(label("To: "));
        panel.add(outputCurrency = currencySelector());
        return panel;
    }

    private JPanel createButtonSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(calculateButton());
        return panel;
    }

    private JPanel createResultSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(label("Result: "));
        panel.add(outputAmount = amountOutput());
        return panel;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        return label;
    }

    private Component calculateButton() {
        JButton button = new JButton("Exchange");
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        button.setBackground(new Color(120, 220, 120));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(_ -> executeExchange());
        return button;
    }

    private void executeExchange() {
        commands.get(isInputValid() ? "exchange" : "error").execute();
    }

    private boolean isInputValid() {
        return parseAmount(inputAmount.getText()) >0;
    }

    private JTextField amountInput() {
        return new JTextField(10);
    }

    private JTextField amountOutput() {
        JTextField textField = new JTextField(10);
        textField.setEditable(false);
        return textField;
    }

    private JComboBox<Currency> currencySelector() {
        return new JComboBox<>(toArray(currencies));
    }

    private Currency[] toArray(List<Currency> currencies) {
        return currencies.toArray(new Currency[0]);
    }

    public void addCommand(String name, Command command) {
        this.commands.put(name, command);
    }

    public MoneyDialog moneyDialog() {
        return () -> new Money(inputAmount(), inputCurrency());
    }

    public CurrencyDialog currencyDialog() {
        return this::outputCurrency;
    }

    public MoneyDisplay moneyDisplay() {
        return money -> outputAmount.setText(
                formatAmount(money.amount())
        );
    }

    private String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }

    private double inputAmount() {
        return parseAmount(inputAmount.getText());
    }

    private double parseAmount(String text) {
        return Optional.ofNullable(text)
                .map(this::normalize)
                .map(this::tryParseDouble)
                .orElse(0.0);
    }

    private double tryParseDouble(String text) {
        try {
            return Double.parseDouble(text);
        }  catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String normalize(String text) {
        return text.replace(",", ".");
    }

    private Currency inputCurrency() {
        return (Currency) inputCurrency.getSelectedItem();
    }

    private Currency outputCurrency() {
        return (Currency) outputCurrency.getSelectedItem();
    }
}
