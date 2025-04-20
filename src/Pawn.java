public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "♙" : "♟";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int direction = isWhite ? -1 : 1;

        // Move forward 1
        if (endCol == startCol && endRow == startRow + direction && board[endRow][endCol] == null) {
            return true;
        }

        // Move forward 2 from starting position
        if (endCol == startCol &&
                ((isWhite && startRow == 6) || (!isWhite && startRow == 1)) &&
                endRow == startRow + 2 * direction &&
                board[startRow + direction][endCol] == null &&
                board[endRow][endCol] == null) {
            return true;
        }

        // Capture diagonally
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction &&
                board[endRow][endCol] != null && board[endRow][endCol].isWhite() != isWhite) {
            return true;
        }

        return false;
    }
}
