package pp;

import Server.AbaloneClientHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for maintaining the Abalone game.
 * @author Vladi/Ivan
 */
public class NetGame{
    public static int NUMBER_PLAYERS;
    Scanner sc = new Scanner(System.in);
    /**
     * The board.
     *
     * @invariant board is never null
     */
    private Board board;

    /**
     * The 2 players of the game.
     *
     * @invariant the length of the array equals NUMBER_PLAYERS
     * @invariant all array items are never null
     */
    private Player[] players;

    /**
     * Index of the current player.
     *
     * @invariant the index is always between 0 and NUMBER_PLAYERS
     */
    private int current;
    private int current_number_of_players;
    private static boolean isFull;
    private List<AbaloneClientHandler> clients = new ArrayList<>();
    private int ready;
    private static boolean allReady;
    private boolean isMoveHere;
    private int moves;
    // -- Constructors -----------------------------------------------

    public String getGameInfo() {
        String o = "";
        o += "all number of players: " + NUMBER_PLAYERS + ", current number: " + current_number_of_players +
            ",\n isGameFull?: " + isGameFull() + ", number of clients registered: " + clients.size() + ", usernames: \n" +
            "isthegame ready " + arePlayersReady() + ", ready players: " + ready + "\n";
        for (int i = 0; i < clients.size(); i++) {
            o += (i + 1) + ". " + clients.get(i).getUsername() + "\n";
        }
        return o;
    }


    public Board getBoard() {
        return board;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int nextCurrent() {
        this.current++;
        if (this.current == NUMBER_PLAYERS) {
            setCurrent(0);
        }
        return current;
    }

    public List<AbaloneClientHandler> getClients() {
        return clients;
    }

    /**
     * Creates a game with a name a number of players and a client
     * @param name
     * @param n
     * @param a
     */

    public NetGame(String name, int n, AbaloneClientHandler a) {
        Player p = new HumanPlayer(name, Mark.R);
        NUMBER_PLAYERS = n;
        board = new Board(NUMBER_PLAYERS);
        players = new Player[NUMBER_PLAYERS + 1];
        players[0] = p;
        current_number_of_players = 1;
        isFull = false;
        clients.add(a);
        this.ready = 0;
        this.allReady = false;
        this.moves = 0;
    }

    public Mark getMarkCurrent(){
        return players[current].getMark();
    }

    public int getCurrent_number_of_players() {
        return current_number_of_players;
    }

    public Mark getMarkOfAPlayer(int n){
        return players[n].getMark();
    }

    public int getCurrent() {
        return current;
    }

    public int getReady() {
        return ready;
    }

    public void add(String name, AbaloneClientHandler a) {
        if (current_number_of_players == 1) {
            Player p = new HumanPlayer(name, Mark.Y);
            players[1] = p;
            clients.add(a);
        } else if (current_number_of_players == 2) {
            Player p = new HumanPlayer(name, Mark.G);
            players[2] = p;
            clients.add(a);
        } else if (current_number_of_players == 3) {
            Player p = new HumanPlayer(name, Mark.P);
            players[3] = p;
            clients.add(a);
        }
        current_number_of_players++;
        if (current_number_of_players == NUMBER_PLAYERS) {
            isFull = true;
        }
    }

    /**
     * Acceptes ready from players
     */

    public void readyInc() {
        this.ready++;
        if (ready == NUMBER_PLAYERS) {
            this.allReady = true;
        }
    }

    public static boolean arePlayersReady() {
        return allReady;
    }

    public static boolean isGameFull() {
        return isFull;
    }

    // -- Commands ---------------------------------------------------

    /**
     * Starts the Abalone game. <br>
     * Asks after each ended game if the user want to continue. Continues until
     * the user does not want to play anymore.
     */
    public void startt() {
        boolean continueGame = true;
        while (continueGame) {
            reset();
            //play();
            System.out.println("\n> Play another time? (y/n)?");
            continueGame = choiceForNewGame();
        }
    }

    /**
     * Asks if the player/s want to play another game of not
     * @return true/false
     */

    public boolean choiceForNewGame() {
        boolean run = true;
        boolean fin = false;
        Scanner in = new Scanner(System.in);
        while (run == true) {
            String c = in.next();
            if (c.equals("yes")) {
                fin = true;
                run = false;
            }
            if (c.equals("no")) {
                fin = false;
                run = false;
            }
        }
        return fin;
    }

    /**
     * Makes a string of all the fields on the board
     * @return
     */

    public String boardSend() {
        String o = "";
        for (int i = 1; i <= 60; i++) {
            if (board.getField(i) == Mark.R) {
                o += "R,";
            } else if (board.getField(i) == Mark.G) {
                o += "G,";
            } else if (board.getField(i) == Mark.Y) {
                o += "Y,";
            } else if (board.getField(i) == Mark.P) {
                o += "P,";
            } else if (board.getField(i) == Mark.E) {
                o += "E,";
            }
        }
        if (board.getField(61) == Mark.R) {
            o += "R";
        } else if (board.getField(61) == Mark.G) {
            o += "G";
        } else if (board.getField(61) == Mark.Y) {
            o += "Y";
        } else if (board.getField(61) == Mark.P) {
            o += "P";
        } else if (board.getField(61) == Mark.E) {
            o += "E";
        }
        return o;
    }

    /**
     * Resets the game. <br>
     * The board is emptied and player[0] becomes the current player.
     */
    private void reset() {
        current = 0;
        board.reset();
    }

    /**
     * this method return a random number in the given
     * min< int i < max
     * @param min
     * @param max
     * @return
     */

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    /**
     * Sends a messages
      * @param msg
     */
    public void send(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            AbaloneClientHandler a;
            a = clients.get(i);
            try {
                a.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Plays the Abalone game. <br>
     * First the (still empty) board is shown. Then the game is played
     * until it is over. Players can make a move one after the other.
     * After each move, the changed game situation is printed.
     */
    public boolean play(String input) {

        //System.out.println("My current color is: " + players[current].getMark());
        board.incrementMoves();
        if(players[current].makeMove(board,input)==true){
                this.moves++;
                return true;
        }else{
            return false;
        }

    }

    public int getMoves() {
        return moves;
    }

    public int getTheScore(int pos){
        return players[pos].getScore();
    }

    public String getUsername(int pos){
        return players[pos].getName();
    }

    /**
     * Prints the game situation.
     */
    private void update() {
        System.out.println("\ncurrent game situation: \n\n" + board.toString()
            + "\n");
    }

    /**
     * Prints the result of the last game. <br>
     *
     * @requires the game to be over
     */

}