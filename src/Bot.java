public class Bot {
    public static int Move(Board board, int depth) {
        int bestCol = -1;
        int bestEval = Integer.MIN_VALUE;
        for (int col : board.getAvailableColumns()) {
            Board cloneBoard = board.clone();
            if(cloneBoard.placePiece(col) == -1) {
                return col;
            }
            int eval = minimax(cloneBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (eval > bestEval) {
                bestEval = eval;
                bestCol = col;
            }
        }
        return bestCol;
    }

    public static int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return evaluateBoard(board);
        }
        
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : board.getAvailableColumns()) {
                Board cloneBoard = board.clone();
                if(cloneBoard.placePiece(col) == -1) {
                    return Integer.MAX_VALUE;
                } // Assuming "X" is bot's color
                int eval = minimax(cloneBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : board.getAvailableColumns()) {
                Board cloneBoard = board.clone();
                if(cloneBoard.placePiece(col) == -1) {
                    return Integer.MIN_VALUE;
                }
                int eval = minimax(cloneBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return minEval;
        }
    }

    public static int evaluateBoard(Board board) {
        int score = 0;
        // Evaluate rows
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                score += evaluateLine(board, row, col, 0, 1); // Evaluate horizontal
            }
        }
        // Evaluate columns
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                score += evaluateLine(board, row, col, 1, 0); // Evaluate vertical
            }
        }
        // Evaluate diagonals
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                score += evaluateLine(board, row, col, 1, 1); // Evaluate diagonal
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 3; col < 7; col++) {
                score += evaluateLine(board, row, col, 1, -1); // Evaluate diagonal
            }
        }
        return score;
    }
    
    private static int evaluateLine(Board board, int startRow, int startCol, int dRow, int dCol) {
        int score = 0;
        int countBot = 0; // Count bot pieces
        int countOpponent = 0; // Count opponent pieces
        for (int i = 0; i < 4; i++) {
            int row = startRow + i * dRow;
            int col = startCol + i * dCol;
            if (board.isCellEmpty(col, row)) {
                score += 1;
            } else if (board.isCellMine(col, row)) {
                countBot++;
            } else if (board.isCellOpponent(col, row)) {
                countOpponent++;
            }
        }
        if (countBot == 0) {
            score -= (int)Math.pow(10, countOpponent); 
        } else if (countOpponent == 0){
            score += (int)Math.pow(10, countBot);
        }
        return score;
    }
}
