//Michael Mocioiu 101569108
//Jason Gunawan 101465525
import java.util.Arrays;

public class Board {
    // the board data will be stored sideways to make the action code simpler
    // top row in the array is the leftmost board column, and leftmost column is the  bottom row
    //board also handles the turns
    public String[][] boardData = new String[7][6];
    public String currentTurn;

    public Board() {
        //instantiate blank board
        for (String[] col : boardData) {
            Arrays.fill(col, " ");
        }
        this.currentTurn = "r";
    }

    public Board(String[][] boardData, String currentTurn) {
        //instantiate board from existing board data
        this.boardData = boardData;
        this.currentTurn = currentTurn;
    }

    public Board clone() {
        //used for the 'simulated' moves 
        String[][] clonedata = Arrays.copyOf(boardData, boardData.length);
        for (int i = 0; i < boardData.length; i++) {
            clonedata[i] = Arrays.copyOf(boardData[i], boardData[i].length);
        }
        return new Board(clonedata, this.currentTurn);
    }

    //methods to check a cell's contents
    public boolean isCellEmpty(int col, int row) {
        return boardData[col][row].equals(" ");
    }

    public boolean isCellMine(int col, int row) {
        return boardData[col][row].equals(currentTurn);
    }

    public boolean isCellOpponent(int col, int row) {
        return !isCellMine(col, row);
    }

    //method for gathering the columns which have empty spaces
    public int[] getAvailableColumns() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i <= 6; i++) {
            if (getEmptyRow(i) != -1) {
                out.append(i).append(" ");
            }
        }
        return Arrays.stream(out.toString().split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    //Method for retreiving the next available slot in a column, return -1 if it is full
    public int getEmptyRow(int col) {
        if (col < 0 || col > 6) {
            throw new IllegalArgumentException("Invalid column: must be 0-6");
        }
        for (int row = 0; row < 6; row++) {
            if (isCellEmpty(col, row)) {
                return row;
            }
        }
        return -1;
    }
    //method for checking if the game is won, by checking 3 peices on either side in the cardinal directions
    public boolean isGameWin(int startCol, int startRow) {
        // horizontal
        int count = 0;
        for (int col = Math.max(0, startCol - 3); col <= Math.min(6, startCol + 3); col++) {
            if (isCellMine(col, startRow)) {
                count++;
                if (count == 4)
                    return true;
            } else {
                count = 0;
            }
        }

        // vertical
        count = 0;
        for (int row = Math.max(0, startRow - 3); row <= Math.min(5, startRow + 3); row++) {
            if (isCellMine(startCol, row)) {
                count++;
                if (count == 4)
                    return true;
            } else {
                count = 0;
            }
        }

        // diagonal (NE)
        count = 0;
        int minNumb = Math.min(startCol, startRow);
        int currentRow = startRow - minNumb;
        int currentCol = startCol - minNumb;
        while (currentRow <= 5 && currentCol <= 6) {
            if (isCellMine(currentCol, currentRow)) {
                count++;
                if (count == 4)
                    return true;
            } else {
                count = 0;
            }
            currentRow++;
            currentCol++;
        }

        // diagonal (SE)
        count = 0;
        minNumb = Math.min(startCol, 5 - startRow);
        currentCol = startCol - minNumb;
        currentRow = startRow + minNumb;
        while (currentRow >= 0 && currentCol <= 6) {
            if (isCellMine(currentCol, currentRow)) {
                count++;
                if (count == 4)
                    return true;
            } else {
                count = 0;
            }
            currentRow--;
            currentCol++;
        }
        return false;
    }

    //places piece if valid, then checks if the game is won/drawn. Will return the y position otherwise
    public int placePiece(int col) {
        if (col < 0 || col > 6) {
            throw new IllegalArgumentException("Invalid column: must be 0-6");
        }
        int row = getEmptyRow(col);
        if (row >= 0) {
            boardData[col][row] = currentTurn;
            if (isGameWin(col, row)) {
                return -1;
            } else if (getAvailableColumns().length == 0) {
                return -2;
            }
            currentTurn = currentTurn.equals("r") ? "y" : "r";
            return row;
        } else {
            throw new IllegalArgumentException("Column " + (col + 1) + " is full!");
        }
    }

    //used to convert the 'r' and 'y' strings to a display red O and yellow X
    public String getDisplayPiece(String piece) {
        return (piece.equals("r") ? "\u001B[31mO" : (piece.equals("y")) ? "\u001B[33mX" : " ") + "\u001B[0m";
    }

    //used to present the board
    public String draw() {
        String topBorder = "┌───┬───┬───┬───┬───┬───┬───┐\n";
        String middleBorder = "├───┼───┼───┼───┼───┼───┼───┤\n";
        String bottomBorder = "└───┴───┴───┴───┴───┴───┴───┘\n";
        StringBuilder out = new StringBuilder(topBorder);
        for (int y = 5; y >= 0; y--) {
            out.append("│");
            for (int x = 0; x < 7; x++) {
                out.append(" ").append(getDisplayPiece(boardData[x][y])).append(" │");
            }
            out.append("\n").append((y > 0) ? middleBorder : bottomBorder);
        }
        return out.toString();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (String[] column : boardData) {
            for (String cell : column) {
                out.append(cell).append(",");
            }
        }
        return out.toString();
    }

}
