package javaChatRoom.server.serverApp;

import javax.swing.*;
import java.awt.*;

class UserListCellRenderer extends DefaultListCellRenderer {
    private static final Color evenColor = new Color(240, 240, 255);
    private static final Font font = new Font("JetBrains Mono", Font.BOLD, 16);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(font);

        String text = String.format("%d. %s", index + 1, value.toString());
        label.setText(text);

        if (!isSelected) {
            label.setBackground(index % 2 == 0 ? evenColor : list.getBackground());
        }
        return label;
    }
}
