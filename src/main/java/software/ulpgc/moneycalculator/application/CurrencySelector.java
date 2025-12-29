package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.model.Currency;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.Optional;

public class CurrencySelector extends JPanel {
    private final List<Currency> currencies;
    private final CurrencySelectionListener selectionListener;

    @FunctionalInterface
    public interface CurrencySelectionListener {
        void onCurrencySelected(String currencyCode, boolean isSource);
    }

    public CurrencySelector(List<Currency> currencies, CurrencySelectionListener selectionListener) {
        this.currencies = currencies;
        this.selectionListener = selectionListener;
        this.setLayout(new BorderLayout());
        this.add(createScrollPane(createTable()));
        this.add(createInfoLabel(), BorderLayout.SOUTH);
    }

    private JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("\uD83D\uDD0D Revisa los códigos de moneda..."));
        return scrollPane;
    }

    private JTable createTable() {
        JTable table = new JTable(getCurrencyData(), new String[]{"Código", "País"});
        configureTableAppearance(table);
        applyStripedRenderer(table);
        configureHeader(table.getTableHeader());

        addClickInteraction(table);
        addCursorInteraction(table);

        return table;
    }

    private void addClickInteraction(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Optional.of(table.rowAtPoint(e.getPoint()))
                        .filter(r -> r >= 0)
                        .map(r -> (String) table.getValueAt(r, 0))
                        .ifPresent(code -> selectionListener.onCurrencySelected(code, SwingUtilities.isLeftMouseButton(e)));
            }
        });
    }

    private void addCursorInteraction(JTable table) {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                table.setCursor(Optional.of(table.rowAtPoint(e.getPoint()))
                        .filter(r -> r >= 0)
                        .map(_ -> Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                        .orElse(Cursor.getDefaultCursor()));
            }
        });
    }

    private String[][] getCurrencyData() {
        return currencies.stream()
                .map(c -> new String[]{c.code(), c.country()})
                .toArray(String[][]::new);
    }

    private void configureTableAppearance(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setBackground(Color.gray);
        table.setGridColor(Color.gray);
    }

    private void configureHeader(JTableHeader header) {
        header.setBackground(Color.darkGray);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
    }

    private void applyStripedRenderer(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                c.setBackground(row % 2 == 0 ? new Color(255, 255, 224) : Color.WHITE);
                return c;
            }
        });
    }

    private JLabel createInfoLabel() {
        JLabel label = new JLabel("Click Izq: From | Click Der: To");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        label.setFont(new Font("SansSerif", Font.ITALIC, 13));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }
}
