public class AIPlayer {
    public static final int MAX_DEPTH = 2;

    public static void makeAIMove(ChessBoard board) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        Piece[][] matrix = board.getPieceMatrix();

        for (int i = 0; i < ChessBoard.SIZE; i++) {
            for (int j = 0; j < ChessBoard.SIZE; j++) {
                Piece p = matrix[i][j];
                if (p != null && !p.isWhite()) {
                    for (int x = 0; x < ChessBoard.SIZE; x++) {
                        for (int y = 0; y < ChessBoard.SIZE; y++) {
                            if (p.isValidMove(i, j, x, y, matrix)) {
                                Piece[][] newBoard = copyBoard(matrix);
                                newBoard[x][y] = p;
                                newBoard[i][j] = null;

                                int score = minimax(newBoard, MAX_DEPTH - 1, true);

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
            board.movePiece(bestMove);
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
                                Piece temp = board[x][y];
                                board[x][y] = p;
                                board[i][j] = null;

                                int score = minimax(board, depth - 1, !maximizing);

                                board[i][j] = p;
                                board[x][y] = temp;

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

    static class Move {
        int fromRow, fromCol, toRow, toCol;

        Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }
}
