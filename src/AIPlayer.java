public class AIPlayer {

    public static void makeAIMove(ChessBoard board, int depth) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        Piece[][] matrix = board.getPieceMatrix();

        for (int i = 0; i < ChessBoard.SIZE; i++) {
            for (int j = 0; j < ChessBoard.SIZE; j++) {
                Piece p = matrix[i][j];
                if (p != null && !p.isWhite()) { // Only Black pieces (AI)
                    for (int x = 0; x < ChessBoard.SIZE; x++) {
                        for (int y = 0; y < ChessBoard.SIZE; y++) {
                            if (p.isValidMove(i, j, x, y, matrix)) {
                                Piece[][] newBoard = copyBoard(matrix);
                                Piece captured = newBoard[x][y];
                                newBoard[x][y] = p;
                                newBoard[i][j] = null;

                                int score = minimax(newBoard, depth - 1, true);

                                if (score > bestValue) {
                                    bestValue = score;
                                    bestMove = new Move(i, j, x, y);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (bestMove != null) {
            board.movePiece(bestMove); // triggers AI move on real board
        }
    }

    private static int minimax(Piece[][] board, int depth, boolean maximizing) {
        if (depth == 0) return evaluate(board);

        int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < ChessBoard.SIZE; i++) {
            for (int j = 0; j < ChessBoard.SIZE; j++) {
                Piece p = board[i][j];
                if (p != null && p.isWhite() == maximizing) {
                    for (int x = 0; x < ChessBoard.SIZE; x++) {
                        for (int y = 0; y < ChessBoard.SIZE; y++) {
                            if (p.isValidMove(i, j, x, y, board)) {
                                Piece captured = board[x][y];
                                board[x][y] = p;
                                board[i][j] = null;

                                int score = minimax(board, depth - 1, !maximizing);

                                board[i][j] = p;
                                board[x][y] = captured;

                                best = maximizing ? Math.max(best, score) : Math.min(best, score);
                            }
                        }
                    }
                }
            }
        }

        return best;
    }

    private static int evaluate(Piece[][] board) {
        int score = 0;
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            for (int j = 0; j < ChessBoard.SIZE; j++) {
                Piece p = board[i][j];
                if (p != null) {
                    int value = 0;
                    if (p instanceof Pawn) value = 1;
                    else if (p instanceof Knight || p instanceof Bishop) value = 3;
                    else if (p instanceof Rook) value = 5;
                    else if (p instanceof Queen) value = 9;
                    else if (p instanceof King) value = 100;

                    score += p.isWhite() ? value : -value;
                }
            }
        }
        return score;
    }

    private static Piece[][] copyBoard(Piece[][] original) {
        Piece[][] copy = new Piece[ChessBoard.SIZE][ChessBoard.SIZE];
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            for (int j = 0; j < ChessBoard.SIZE; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    // Move class to store AI moves
    public static class Move {
        public final int fromRow, fromCol, toRow, toCol;

        public Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }
}
