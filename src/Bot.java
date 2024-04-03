//Michael Mocioiu 101569108
//Jason Gunawan 101465525
public class Bot {
    //Method for returning the best columnn to place a piece
    public static int Move(Board board, int depth) {
        //initializes the selection to be lower than any possible eval/column, ensuring a proper output
        int bestCol = -1;
        int bestEval = Integer.MIN_VALUE;
        //iterate through columns on the game board
        for (int col : board.getAvailableColumns()) {
            //clone the board to simulate placements without affecting the actual game
            Board cloneBoard = board.clone();
            //method to immediately return the column if it reults in a win
            if(cloneBoard.placePiece(col) == -1) {
                return col;
            }
            //calling minimax to search the possible board states and return the evaluation
            int eval = minimax(cloneBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (eval > bestEval) {
                bestEval = eval;
                bestCol = col;
            }
        }
        return bestCol;
    }

    //minimax algorithm with alpha beta pruning
    public static int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        //base case, when recursively calling minimax() it reduces depth by 1 until it reaches 0 and then returns the eval
        if (depth == 0) {
            return evaluateBoard(board);
        }
        
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : board.getAvailableColumns()) {
                //once again cloning the board for simulating the next steps
                Board cloneBoard = board.clone();
                //and checking for win, returning max value
                if(cloneBoard.placePiece(col) == -1) {
                    return Integer.MAX_VALUE;
                }
                int eval = minimax(cloneBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : board.getAvailableColumns()) {
                //same as above
                Board cloneBoard = board.clone();
                //returns min value to indicate that this is a 'hard' lose position (opponnent will win)
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
    // method for evaluating the board state by checking for each type of win configuration and assigning a score
    public static int evaluateBoard(Board board) {
        int score = 0;
        //the loops run 4 times instead of 7 because the evaluateline checks the next 3 spots over, which ends up covering the whole board
        // Evaluate rows
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                score += evaluateLine(board, row, col, 0, 1);
            }
        }
        // Evaluate columns
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                score += evaluateLine(board, row, col, 1, 0); 
            }
        }
        // Evaluate diagonals
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                score += evaluateLine(board, row, col, 1, 1);
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 3; col < 7; col++) {
                score += evaluateLine(board, row, col, 1, -1);
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
        //calculates the score of the board
        if (countBot == 0) {
            score -= (int)Math.pow(10, countOpponent); 
        } else if (countOpponent == 0){
            score += (int)Math.pow(10, countBot);
        }
        return score;
    }
}
