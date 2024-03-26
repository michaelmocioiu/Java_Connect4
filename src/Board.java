import java.util.Arrays;

public class Board {
    //the board data will be stored sideways to make the action code simpler
    //top row in the array is the leftmost board column, and leftmost column is the bottom row
    public String[][] boardData = new String[7][6];

    public Board() {
        for (String[] col : boardData) {
            Arrays.fill(col, " ");
        }  
    }

    public int[] getEmptyColumns() {
        String out = ""; 
        for (int i = 1; i < 7; i++) {
            if (getEmptySlot(i - 1) != -1) {
                out += i + " ";
            }
        }
        int[] a = Arrays.stream(out.split("\\s+")).mapToInt(Integer::parseInt).toArray();
        return a;
    }

    public int getEmptySlot(int col) {
        if (col < 0 || col > 6) {
            throw new IllegalArgumentException("Invalid column: must be 0-6");
        }
        for (int i = 0; i < 6; i++) {
            if (boardData[col][i].equals(" ")) {
                return i;
            }
        }
        return -1;
    }


    public boolean isPlayerWin(int x, int y) {
        String player = boardData[x][y];

        //horizontal
        int count = 0;
        for (int col = Math.max(0, x - 3); col <= Math.min(6, x + 3); col++) {
            if (boardData[col][y].equals(player)) {
                count++;
                if (count == 4) return true;
            } else {
                count = 0;
            }
        }
        
        //vertical
        count = 0;
        for (int row = Math.max(0, y - 3); row <= Math.min(5, y + 3); row++) {
            if (boardData[x][row].equals(player)) {
                count++;
                if (count == 4) return true;
            } else {
                count = 0;
            }
        }

        //diagonal (NE)
        count = 0;
        int startRow = y - Math.min(y, x);
        int startCol = x - Math.min(y, x);
        for (int i = 0; i <= Math.min(6 - startRow, 5 - startCol); i++) {
            int currentRow = startRow + i;
            int currentCol = startCol + i;
            if (currentRow >= 0 && currentRow < 6 && currentCol >= 0 && currentCol < 7) {
                if (boardData[currentCol][currentRow].equals(player)) {
                    count++;
                    if (count == 4) return true;
                } else {
                    count = 0;
                }
            }
        }

        //diagonal (NW)
        count = 0;
        startRow = y + Math.min(5 - y, x);
        startCol = x - Math.min(5 - y, x);
        for (int i = 0; i <= Math.min(startRow, 5 - startCol); i++) {
            int currentRow = startRow - i;
            int currentCol = startCol + i;
            if (currentRow >= 0 && currentRow < 6 && currentCol >= 0 && currentCol < 7) {
                if (boardData[currentCol][currentRow].equals(player)) {
                    count++;
                    if (count == 4) return true;
                } else {
                    count = 0;
                }
            }
        }
        return false;
    }


    public int placePiece(int posX, String colour) {
        if (posX < 0 || posX > 6) {
            throw new IllegalArgumentException("Invalid column: must be 0-6");
        }
        int posY = getEmptySlot(posX);
        if (posY >= 0){
            boardData[posX][posY] = getPieceString(colour);
            System.out.println(posY);
            return posY;
        } else {
            throw new IllegalArgumentException("Column is full!");
        }
    }

    public String getPieceString(String colour) {
        return (colour.equals("r") ? "\u001B[31m" : "\u001B[33m") + "\u2B24\u001B[0m";
    }
    

    @Override
    public String toString(){
        String topBorder = "┌───┬───┬───┬───┬───┬───┬───┐\n";
        String middleBorder = "├───┼───┼───┼───┼───┼───┼───┤\n";
        String bottomBorder = "└───┴───┴───┴───┴───┴───┴───┘\n";
        String[] indicator = new String[]{"",""};
        String out = topBorder;
        for (int y = 5; y >= 0; y--) {
            out += "│";
            for (int x = 0; x < 7; x++) {
                out += " " + boardData[x][y] + " │";
            }
            out += "\n" + ((y > 0) ? middleBorder : bottomBorder);
        }
        for (int i = 0; i < 7; i++) {
            indicator[0] += (getEmptySlot(i) == -1) ? UI.colourText("  ^ ", "red") : UI.colourText("  ^ ", "green");
            indicator[1] += (getEmptySlot(i) == -1) ? UI.colourText(" (" + (i + 1) + ")", "red") : UI.colourText(" (" + (i + 1) + ")", "green");
        }
        out += indicator[0] + " \n" + indicator[1] + " ";


        return out;
    }
}
