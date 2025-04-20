import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int SIZE = 8;
    private Square[][] board = new Square[SIZE][SIZE];
    private Square selectedSquare = null;
    private boolean whiteTurn = true;

    public ChessBoard() {
        this.setLayout(new GridLayout(SIZE, SIZE));
        boolean isWhite = false;

        for (int row = 0; row < SIZE; row++) {
            isWhite = !isWhite;
            for (int col = 0; col < SIZE; col++) {
                Square square = new Square(row, col, isWhite);
                board[row][col] = square;
                square.addActionListener(e -> handleClick(square));
                this.add(square);
                isWhite = !isWhite;
            }
        }

        initializePieces();
    }

    private void initializePieces() {
        for (int col = 0; col < SIZE; col++) {
            board[1][col].setPiece(new Pawn(false));
            board[6][col].setPiece(new Pawn(true));
        }

        board[0][0].setPiece(new Rook(false));
        board[0][7].setPiece(new Rook(false));
        board[7][0].setPiece(new Rook(true));
        board[7][7].setPiece(new Rook(true));

        board[0][1].setPiece(new Knight(false));
        board[0][6].setPiece(new Knight(false));
        board[7][1].setPiece(new Knight(true));
        board[7][6].setPiece(new Knight(true));

        board[0][2].setPiece(new Bishop(false));
        board[0][5].setPiece(new Bishop(false));
        board[7][2].setPiece(new Bishop(true));
        board[7][5].setPiece(new Bishop(true));

        board[0][3].setPiece(new Queen(false));
        board[0][4].setPiece(new King(false));
        board[7][3].setPiece(new Queen(true));
        board[7][4].setPiece(new King(true));
    }

    private void handleClick(Square clicked) {
        Piece clickedPiece = clicked.getPiece();

        if (selectedSquare == null) {
            // First click
            if (clickedPiece != null && clickedPiece.isWhite() == whiteTurn) {
                selectedSquare = clicked;
                clicked.setBackground(Color.YELLOW);
            }
        } else {
            if (clicked == selectedSquare) {
                // Deselect
                resetSquareColor(clicked);
                selectedSquare = null;
                return;
            }

            Piece selectedPiece = selectedSquare.getPiece();
            int startRow = selectedSquare.getRow();
            int startCol = selectedSquare.getCol();
            int endRow = clicked.getRow();
            int endCol = clicked.getCol();

            if (selectedPiece.isValidMove(startRow, startCol, endRow, endCol, getPieceMatrix())) {
                clicked.setPiece(selectedPiece);
                selectedSquare.setPiece(null);
                resetSquareColor(selectedSquare);
                resetSquareColor(clicked);
                whiteTurn = !whiteTurn;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid move!");
                resetSquareColor(selectedSquare);
            }

            selectedSquare = null;
        }
    }

    private void resetSquareColor(Square square) {
        int r = square.getRow();
        int c = square.getCol();
        square.setBackground((r + c) % 2 == 0 ? Color.WHITE : Color.GRAY);
    }

    private Piece[][] getPieceMatrix() {
        Piece[][] matrix = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = board[i][j].getPiece();
            }
        }
        return matrix;
    }
}
