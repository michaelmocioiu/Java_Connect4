import java.util.Scanner;

public class Game {
    public boolean isAiMatch;
    public String P1Colour,P1Name, P2Name, err, currentTurn;
    public Board board = new Board();
    Scanner scanner = new Scanner(System.in);
    UI Ui = new UI(130);

    public Game () {
      P1Colour = "";
      P1Name = "";
      P2Name = "CPU";
      currentTurn = "r";
    }


    public void Launch(){
        int selection;
        while (true){
            err = "";
            selection = getMCInput(" Home ", "Connect 4!, Choose player count or exit:", "Singleplayer,Multiplayer,Exit");
            if (selection == 1) {
                isAiMatch = true;
                StartGame();
                break;
            } if (selection == 2) {
                isAiMatch = false;
                StartGame();
            } else {
            }
        }
    }

    public void StartGame() {
        if (isAiMatch) {
            if (getMCInput("Team Selection", "Player 1 select your colour:", "Red,Yellow") == 1) {
                P1Colour = "r";
            } else P1Colour = "y";
            P1Name = getNameInput(1);
            while (true) {
                boolean winState = resolveTurn(getGameInput(), currentTurn, board);
                if (winState == true) {
                    break;
                }
                currentTurn = (currentTurn.equals("r") ? "y" : "r");
                
                
            }
            return;
        } else {
            while(true){
                Ui.printBoardPage(this, board.toString(), err);
                return;
            }
        }
    }

    public void StartTurn(){

    }


    public boolean resolveTurn(int posX, String colour, Board board){
        int y = board.placePiece(posX, colour);
        if (board.isPlayerWin(posX, y)) {
            System.out.println((colour == "r" ? "Red" : "Yellow") + " Player Wins!");
            return true;
        }
        return false;
    }

    public int getMCInput(String title, String prompt, String options) {
        String[] opts = options.split(",");
        int[] ints = new int[opts.length];
        for (int i = 0; i < opts.length; i++) {
            ints[i] = i+1;
        }
        while (true) {
            try {
                Ui.printMCPage(title, prompt, options, err);
                err = "";
                int input = getValidInt(ints);
                return input;
            }
            catch (IllegalArgumentException e) {
                err = "Invalid input! Must be from 1 to " + ints.length;
                scanner.nextLine();
            }
        }
    }

    public String getNameInput(int player) {
        while (true) {
            try {
                Ui.printPage(" Player "+ player + " Name Entry ", "Please enter the name for Player " + player, "", err);
                err = "";
                String input = getValidString();
                return input;
            } catch (IllegalArgumentException e) {}
        }
    }

    public int getGameInput() {
        while (true) {
            try {
                Ui.printBoardPage(this, board.toString(), err);
                err = "";
                int input = getValidInt(board.getEmptyColumns());
                return input;
            } catch (IllegalArgumentException e){}
        }
    }

    public int getValidInt(int[] options) {
        String inputStr = scanner.nextLine().trim();
        if (inputStr.isEmpty()) {
            throw new IllegalArgumentException("Empty input");
        }
        int input = Integer.parseInt(inputStr);
        for (int i : options) {
            if (i == input) {
                return input;
            }
        }
        throw new IllegalArgumentException("Input not contained in list of options");
    }
    public String getValidString() {
        String input;
        input = scanner.nextLine();
        if (input != null && input.trim() != "") {
            return input;
        }
        throw new IllegalArgumentException("Invalid input!");
    }
}
