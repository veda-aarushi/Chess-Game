import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    public Square(int row, int col, boolean isWhite) {
        setBackground(isWhite ? Color.WHITE : Color.GRAY);
    }
}
