//Michael Mocioiu 101569108
//Jason Gunawan 101465525
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

    //used to clear the board and reset the winner and turns
    public void RestartMatch() {
        board = new Board();
        winner = "";
        board.currentTurn = "r";
    }

    //starts the game and prompts user for their input
    public void Launch(){
        int selection;
        while (true){
            err = "";
            selection = getMCInput(" Home ", "Connect 4!, Choose player count or exit:", "Singleplayer,Multiplayer,Exit");
            if (selection == 3) {
                return;
            }
            if (selection == 1) {
                isAiMatch = true;
            } else {
                isAiMatch = false;                    
            }
            StartGame();
        }
    }

    //main chunk of the game, contains both loops for the ai and multiplayer matches
    public void StartGame() {
        //team selection
        if (getMCInput("Team Selection", "Player 1 select your colour:", "Red,Yellow") == 2) {
            P1Colour = "y";
            P2Colour = "r";
        }
        //p1 name input
        P1Name = getNameInput(1);
        //AI match main loop
        if (isAiMatch) {
            while (true) {
                RestartMatch();
                //depth/difficulty selection for the ai
                int diffInput = getMCInput("Select Bot Difficulty", "Select the difficulty of the bot,(Smarter bots need more time to think)", "Normal,Hard,Genius");
                int depth = (diffInput == 1) ? 3 : (diffInput == 2) ? 5 : 7;
                while (winner.isEmpty()) {
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
            //Multiplayer main loop
            P2Name = getNameInput(2);
            while(true){
                RestartMatch();
                while (winner.isEmpty()) {
                    resolveTurn(getGameInput(), board);
                    currentTurn = (currentTurn.equals("r") ? "y" : "r");

                }
                if (!getRematchInput()) {
                    break;
                }
            }
        }
    }

    //turn handling method, will place the piece and then check if the game is either won or drawn
    public void resolveTurn(int posX, Board board){
        int y = board.placePiece(posX);
        if (y == -1) {
            winner = board.currentTurn;
        } else if (y == -2) {
            winner = "none";
        }
    }
    //top level input function for the rematch
    public boolean getRematchInput() {
        while (true) {
            try {
                if (winner.equals("none")) {

                } else {
                    Ui.printWinPage(this, err);
                }
                err = "";
                int input = getValidInt(new int[] {0,1});
                return input == 1;
            } catch (IllegalArgumentException e) {
                err = e.getMessage();
            }

        }
    }
    //top level input function for any multiple choice questions
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
    //top level input function for getting player names
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

    //top level input function for getting player moves in game
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

    //generic input function for getting an integer input from a list of options
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
    //generic input function for getting a non empty string input
    public String getValidString() {
        String input;
        input = scanner.nextLine();
        if (input != null && !input.trim().isEmpty()) {
            return input;
        }
        throw new IllegalArgumentException("Invalid input!");
    }
}
