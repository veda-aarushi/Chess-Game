
import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int SIZE = 8;
    private Square[][] board = new Square[SIZE][SIZE];
    private Square selectedSquare = null;
    private boolean whiteTurn = true;
    private Square enPassantTarget = null;

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
            if (clickedPiece != null && clickedPiece.isWhite() == whiteTurn) {
                selectedSquare = clicked;
                clicked.setBackground(Color.YELLOW);
            }
        } else {
            if (clicked == selectedSquare) {
                resetSquareColor(clicked);
                selectedSquare = null;
                return;
            }

            Piece selectedPiece = selectedSquare.getPiece();
            int startRow = selectedSquare.getRow();
            int startCol = selectedSquare.getCol();
            int endRow = clicked.getRow();
            int endCol = clicked.getCol();
            Piece[][] matrix = getPieceMatrix();

            boolean valid = selectedPiece.isValidMove(startRow, startCol, endRow, endCol, matrix);

            if (selectedPiece instanceof King && Math.abs(endCol - startCol) == 2 && startRow == endRow) {
                valid = canCastle(startRow, startCol, endCol);
                if (valid) doCastle(startRow, startCol, endCol);
            }

            if (selectedPiece instanceof Pawn && endCol != startCol && clicked.getPiece() == null) {
                if (enPassantTarget != null && endRow == enPassantTarget.getRow() && endCol == enPassantTarget.getCol()) {
                    clicked.setPiece(selectedPiece);
                    board[startRow][startCol].setPiece(null);
                    board[startRow][endCol].setPiece(null);
                    finishTurn(clicked, selectedPiece);
                    return;
                }
            }

            if (valid) {
                clicked.setPiece(selectedPiece);
                selectedSquare.setPiece(null);

                if (selectedPiece instanceof Pawn && (endRow == 0 || endRow == 7)) {
                    clicked.setPiece(new Queen(selectedPiece.isWhite()));
                }

                if (selectedPiece instanceof Pawn && Math.abs(endRow - startRow) == 2) {
                    enPassantTarget = board[(startRow + endRow) / 2][endCol];
                } else {
                    enPassantTarget = null;
                }

                finishTurn(clicked, selectedPiece);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid move!");
                resetSquareColor(selectedSquare);
                selectedSquare = null;
            }
        }
    }

    private void finishTurn(Square movedTo, Piece movedPiece) {
        resetSquareColor(selectedSquare);
        resetSquareColor(movedTo);
        whiteTurn = !whiteTurn;
        selectedSquare = null;

        if (isKingInCheck(!whiteTurn)) {
            if (isCheckmate(!whiteTurn)) {
                JOptionPane.showMessageDialog(this, (whiteTurn ? "White" : "Black") + " wins by checkmate!");
                disableBoard();
            } else {
                JOptionPane.showMessageDialog(this, (whiteTurn ? "Black" : "White") + " is in check!");
            }
        }

        if (!whiteTurn) {
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    AIPlayer.makeAIMove(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void movePiece(AIPlayer.Move move) {
        Piece p = board[move.fromRow][move.fromCol].getPiece();
        board[move.toRow][move.toCol].setPiece(p);
        board[move.fromRow][move.fromCol].setPiece(null);

        if (p instanceof Pawn && (move.toRow == 0 || move.toRow == 7)) {
            board[move.toRow][move.toCol].setPiece(new Queen(p.isWhite()));
        }

        whiteTurn = true;
        repaint();

        if (isKingInCheck(!whiteTurn)) {
            if (isCheckmate(!whiteTurn)) {
                JOptionPane.showMessageDialog(this, "White wins by checkmate!");
                disableBoard();
            } else {
                JOptionPane.showMessageDialog(this, "White is in check!");
            }
        }
    }

    private boolean canCastle(int row, int kingCol, int endCol) {
        if (isKingInCheck(whiteTurn)) return false;
        int direction = endCol - kingCol > 0 ? 1 : -1;
        int rookCol = (direction == 1) ? 7 : 0;
        Piece rook = board[row][rookCol].getPiece();
        if (!(rook instanceof Rook) || rook.isWhite() != whiteTurn) return false;

        for (int c = kingCol + direction; c != rookCol; c += direction) {
            if (board[row][c].getPiece() != null) return false;
        }

        for (int c = kingCol; c != endCol + direction; c += direction) {
            Piece backup = board[row][c].getPiece();
            board[row][c].setPiece(new King(whiteTurn));
            if (isKingInCheck(whiteTurn)) {
                board[row][c].setPiece(backup);
                return false;
            }
            board[row][c].setPiece(backup);
        }

        return true;
    }

    private void doCastle(int row, int kingCol, int endCol) {
        int direction = endCol - kingCol > 0 ? 1 : -1;
        int rookCol = (direction == 1) ? 7 : 0;
        int newRookCol = endCol - direction;
        Piece king = board[row][kingCol].getPiece();
        Piece rook = board[row][rookCol].getPiece();

        board[row][endCol].setPiece(king);
        board[row][kingCol].setPiece(null);
        board[row][newRookCol].setPiece(rook);
        board[row][rookCol].setPiece(null);
    }

    private void resetSquareColor(Square square) {
        int r = square.getRow();
        int c = square.getCol();
        square.setBackground((r + c) % 2 == 0 ? Color.WHITE : Color.GRAY);
    }

    public Piece[][] getPieceMatrix() {
        Piece[][] matrix = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = board[i][j].getPiece();
            }
        }
        return matrix;
    }

    private Square findKing(boolean isWhite) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece p = board[i][j].getPiece();
                if (p instanceof King && p.isWhite() == isWhite) {
                    return board[i][j];
                }
            }
        }
        return null;
    }

    private boolean isKingInCheck(boolean isWhiteKing) {
        Square kingSquare = findKing(isWhiteKing);
        if (kingSquare == null) return false;

        int kingRow = kingSquare.getRow();
        int kingCol = kingSquare.getCol();
        Piece[][] matrix = getPieceMatrix();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece attacker = matrix[i][j];
                if (attacker != null && attacker.isWhite() != isWhiteKing) {
                    if (attacker.isValidMove(i, j, kingRow, kingCol, matrix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(boolean defendingWhite) {
        Piece[][] matrix = getPieceMatrix();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece p = matrix[i][j];
                if (p != null && p.isWhite() == defendingWhite) {
                    for (int x = 0; x < SIZE; x++) {
                        for (int y = 0; y < SIZE; y++) {
                            if (p.isValidMove(i, j, x, y, matrix)) {
                                Piece backup = matrix[x][y];
                                matrix[x][y] = p;
                                matrix[i][j] = null;

                                boolean stillInCheck = isKingInCheck(defendingWhite);

                                matrix[i][j] = p;
                                matrix[x][y] = backup;

                                if (!stillInCheck) return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void disableBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].setEnabled(false);
            }
        }
    }
}
