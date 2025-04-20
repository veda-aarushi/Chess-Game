import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    private int row, col;
    private Piece piece;

    public Square(int row, int col, boolean isWhite) {
        this.row = row;
        this.col = col;

        setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
        setFont(new Font("SansSerif", Font.PLAIN, 48));
        setMargin(new Insets(0, 0, 0, 0));
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        setText(piece != null ? piece.getSymbol() : "");
    }

    public Piece getPiece() {
        return piece;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}
