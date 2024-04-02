import java.util.Scanner;

public class Game {
    public boolean isAiMatch;
    public String P1Colour, P2Colour,P1Name, P2Name, err, currentTurn, winner;
    public Board board = new Board();
    Scanner scanner = new Scanner(System.in);
    UI Ui = new UI(130);

    public Game () {
      P1Colour = "r";
      P2Colour = "y";
      P1Name = "";
      P2Name = "CPU";
      currentTurn = "r";
      winner = "";
    }

    public void RestartMatch() {
        board = new Board();
        winner = "";
        board.currentTurn = "r";
    }


    public void Launch(){
        int selection;
        while (true){
            err = "";
            selection = getMCInput(" Home ", "Connect 4!, Choose player count or exit:", "Singleplayer,Multiplayer,Exit");
            if (selection == 3) {
                //exit
            }
            if (selection == 1) {
                isAiMatch = true;
            } else {
                isAiMatch = false;                    
            }
            StartGame();
        }
    }

    public void StartGame() {
        if (getMCInput("Team Selection", "Player 1 select your colour:", "Red,Yellow") == 2) {
            P1Colour = "y";
            P2Colour = "r";
        }
        P1Name = getNameInput(1);
        if (isAiMatch) {
            while (true) {
                RestartMatch();
                int diffInput = getMCInput("Select Bot Difficulty", "Select the difficulty of the bot,(Smarter bots need more time to think)", "Normal,Hard,Genius");
                int depth = (diffInput == 1) ? 3 : (diffInput == 2) ? 5 : 7;
                // board.boardData = new String[][] {
                //     {
                //         " ", " ", " ", " ", " ", " "
                //     },
                //     {
                //         "y", " ", " ", " ", " ", " "
                //     },
                //     {
                //         "y", "y", "y", " ", " ", " "
                //     },
                //     {
                //         " ", " ", " ", " ", " ", " "
                //     },
                //     {
                //         " ", " ", " ", " ", " ", " "
                //     },
                //     {
                //         " ", " ", " ", " ", " ", " "
                //     },
                //     {
                //         " ", " ", " ", " ", " ", " "
                //     }
                // };
                while (winner.equals("")) {
                    if (currentTurn == P1Colour) {
                        resolveTurn(getGameInput(), board);
                    } else {
                        int botMove = Bot.Move(board, depth);
                        resolveTurn(botMove, board);
                    }
                    currentTurn = (currentTurn.equals("r") ? "y" : "r"); 
                }
                if (!getRematchInput()) {
                    break;
                }
            }
        } else {
            P2Name = getNameInput(2);
            while(true){
                RestartMatch();
                while (winner.equals("")) {
                    resolveTurn(getGameInput(), board);
                    currentTurn = (currentTurn.equals("r") ? "y" : "r");

                }
                if (!getRematchInput()) {
                    break;
                }
            }
        }
    }


    public void resolveTurn(int posX, Board board){
        int y = board.placePiece(posX);
        if (y == -1) {
            winner = board.currentTurn;
        }
    }

    public boolean getRematchInput() {
        while (true) {
            try {
                Ui.printWinPage(this, err);
                err = "";
                int input = getValidInt(new int[] {0,1});
                return input == 1;
            } catch (IllegalArgumentException e) {
                err = e.getMessage();
            }

        }
    }

    public int getMCInput(String title, String prompt, String options) {
        String[] opts = options.split(",");
        int[] ints = new int[opts.length];
        for (int i = 0; i < opts.length; i++) {
            ints[i] = i;
        }
        while (true) {
            try {
                Ui.printMCPage(title, prompt, options, err);
                err = "";
                int input = getValidInt(ints);
                return input;
            }
            catch (IllegalArgumentException e) {
                err = e.getMessage();
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
            } catch (IllegalArgumentException e) {
                err = e.getMessage();
            }
        }
    }

    public int getGameInput() {
        while (true) {
            try {
                Ui.printBoardPage(this, err);
                err = "";
                int input = getValidInt(board.getAvailableColumns());
                return input - 1;
            } catch (IllegalArgumentException e){
                err = e.getMessage();
            }
        }
    }

    public int getValidInt(int[] options) {
        String inputStr = scanner.nextLine().trim();
        if (inputStr.isEmpty()) {
            throw new IllegalArgumentException("Empty input");
        }
        int input;
        try {
            input = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input must be a number!");
        }
        for (int i : options) {
            if (i + 1 == input) {
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
