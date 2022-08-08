package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Exceptions.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import pp.*;
import protocol.ProtocolMessages1;

/**
 * Server TUI for Networked Abalone Application
 * <p>
 * Intended Functionality: interactively set up & monitor a new server
 *
 * @author Vladi/Ivan
 */
public class AbaloneServer implements Runnable {

    private Scanner sc;

    /**
     * The ServerSocket of this AbaloneServer
     */
    private ServerSocket ssock;

    /**
     * List of AbaloneClientHandlers, one for each connected client
     */
    protected List<AbaloneClientHandler> clients;

    /**
     * Next client number, increasing for every new connection
     */
    private int next_client_no;

    /**
     * The view of this AbaloneServer
     */
    public AbaloneServerTUI view;

    /**
     * The name of the Abalone
     */
    private String ServerNAME = "U ParkAbalone";

    protected Map<Socket, String> sendd = new HashMap<>();

    protected Map<String, Integer> capacity = new HashMap<>();

    protected Map<String, String> passwords = new HashMap<>();

    protected Map<Socket, String> players = new HashMap<>();

    protected Map<String, String> rooms = new HashMap<>();

    protected Map<String, NetGame> games = new HashMap<>();


    /**
     * Constructs a new AbaloneServer. Initializes the clients list,
     * the view and the next_client_no.
     */
    public AbaloneServer() {
        clients = new ArrayList<>();
        view = new AbaloneServerTUI();
        next_client_no = 1;
    }

    /**
     * Makes a copie of a map
     *
     * @param original
     * @param <K>
     * @param <V>
     * @return
     */

    public static <K, V> Map<K, V> cloneMap(Map<K, V> original) {
        Map<K, V> copy = new HashMap<>();

        for (Map.Entry<K, V> entry : original.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }

        return copy;
    }

    /**
     * Returns the name of the Abalone
     *
     * @return the name of the Abalone.
     * @requires Abalone != null;
     */
    public String getAbaloneName() {
        return ServerNAME;
    }

    /**
     * Opens a new socket by calling {@link #setup()} and starts a new
     * AbaloneClientHandler for every connecting client.
     * <p>
     * If {@link #setup()} throws a ExitProgram exception, stop the program.
     * In case of any other errors, ask the user whether the setup should be
     * ran again to open a new socket.
     */
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                // Sets up the Abalone application
                setup();
                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Client "
                        + String.format("%02d", next_client_no++);
                    view.showMessage("New client [" + name + "] connected!");
                    AbaloneClientHandler handler =
                        new AbaloneClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }

            } catch (ExitProgram e1) {
                // If setup() throws an ExitProgram exception,
                // stop the program.
                openNewSocket = false;
            } catch (IOException e) {
                System.out.println("A server IO error occurred: "
                    + e.getMessage());

                if (!view.getBoolean("Do you want to open a new socket?")) {
                    openNewSocket = false;
                }
            }
        }
        view.showMessage("See you later!");
    }

    /**
     * Sets up a new Abalone using {@link #setupAbalone()} and opens a new
     * ServerSocket at localhost on a user-defined port.
     * <p>
     * The user is asked to input a port, after which a socket is attempted
     * to be opened. If the attempt succeeds, the method ends, If the
     * attempt fails, the user decides to try again, after which an
     * ExitProgram exception is thrown or a new port is entered.
     *
     * @throws ExitProgram if a connection can not be created on the given
     *                     port and the user decides to exit the program.
     * @ensures a serverSocket is opened.
     */
    public void setup() throws ExitProgram {
        // First, initialize the Abalone.
        setupAbalone();

        ssock = null;
        while (ssock == null) {
            int port = view.getInt("Please enter the server port.");

            // try to open a new ServerSocket
            try {
                final DatagramSocket socket = new DatagramSocket();
                String i;
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                i = socket.getLocalAddress().getHostAddress();
                view.showMessage("Attempting to open a socket at localhost \r\n"
                    + "on port " + port + " at IP: " + i + "...");
                ssock = new ServerSocket(port, 0,
                    InetAddress.getByName(i));
                view.showMessage("Server started at port " + port + " at IP " + i);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                    + "localhost" + " and port " + port + ".");

                if (!view.getBoolean("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the "
                        + "program.");
                }
            }
        }
    }

    /**
     * Asks the user for a Abalone name and initializes
     * a new Abalone with this name.
     */
    public void setupAbalone() {
        System.out.println("Please enter server name:");
        sc = new Scanner(System.in);
        String input = sc.nextLine();
        this.ServerNAME = input;
    }

    /**
     * Removes a clientHandler from the client list.
     *
     * @requires client != null
     */
    public void removeClient(AbaloneClientHandler client) {
        this.clients.remove(client);
    }

    // ------------------ Server Methods --------------------------

    //@Override
    public String getHello() {
        return ProtocolMessages1.CONNECT + ProtocolMessages1.LDL + this.ServerNAME;
    }

    /**
     * Creates a room with a name a capacity for the players and a password(not necessary)
     * it also creates a game
     *
     * @param parm
     * @param password
     * @param cap
     * @param ss
     * @param username
     * @param a
     * @return
     */


    public synchronized Boolean doCreate(String parm, String password, int cap, Socket ss, String username, AbaloneClientHandler a) {

        Boolean ret = false;
        if (rooms.containsValue(parm)) {
            ret = false;
        } else {
            sendd.put(ss, parm);
            capacity.put(parm, cap);
            passwords.put(parm, password);
            players.put(ss, username);
            rooms.put(username, parm);
            NetGame g = new NetGame(username, cap, a);
            games.put(parm, g);
            //view.showMessage(g.getGameInfo());
            ret = true;
        }
        return ret;
    }

    /**
     * Joins a player to a room depending of the information he provides
     *
     * @param parm
     * @param password
     * @param ss
     * @param username
     * @param a
     * @throws CapacityOverflow
     */

    public synchronized Boolean doJoin(String parm, String password, Socket ss, String username,
                                       AbaloneClientHandler a) throws CapacityOverflow {

        NetGame g = games.get(parm);
        if (g.getCurrent_number_of_players() < capacity.get(parm)) {

            sendd.put(ss, parm);
            passwords.put(parm, password);
            players.put(ss, username);
            rooms.put(username, parm);
            g = games.get(parm);
            g.add(username, a);
            //view.showMessage(g.getGameInfo());

            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends a list of all rooms and their capacity to the player who has requested it
     *
     * @return
     */

    public synchronized String doList() {
        String o = "Rooms - ";
        Set<String> s = games.keySet();
        for (String ss : s) {
            o += "Name " + ss + ", size " + capacity.get(ss) + ", pass ";
            if (passwords.get(ss).equals("")) {
                o += "no | ";
            } else {
                o += "yes | ";
            }
        }
        return o;
    }


    /**
     * Gets the room in which the player is in,
     * checks the capacity and sets one more player to ready.
     *
     * @param username
     */

    protected synchronized void doReady(String username) {
        String uroom = rooms.get(username);
        NetGame s = games.get(uroom);
        s.readyInc();
        //view.showMessage(s.getGameInfo());
    }


    /**
     * removes a player from a game/room and cleanse the room
     *
     * @param roomname
     */


    protected synchronized void doQuit(String roomname) {
        NetGame g = games.get(roomname);
        g.send(ProtocolMessages1.QUIT);
        doClean(roomname);
    }

    /**
     * cleans all the information about
     *
     * @param roomname
     */


    protected synchronized void doClean(String roomname) {
        games.remove(roomname);
        capacity.remove(roomname);
        passwords.remove(roomname);
        Map<String, String> temp = cloneMap(rooms);
        Set<String> s = temp.keySet();
        for (String ss : s) {
            if (rooms.get(ss).equals(roomname)) {
                rooms.remove(ss);
            }
        }
    }

    /**
     * Gets all the players in a room when requested
     *
     * @param p
     * @return
     */

    public synchronized String doGetPlayers(String p) {
        List<String> roomz = new ArrayList<String>();
        roomz.addAll(rooms.keySet());
        String o = "Players in room ";
        //Iterator<String> i = rooms.iterator();
        Map<String, String> temp = cloneMap(rooms);

        for (int i = 0; i < roomz.size(); i++) {
            if (temp.get(roomz.get(i)).equals(p)) {
                o += roomz.get(i) + " ";
            }
        }

        return o;
    }

    /**
     * Creates a game, gives a turn to random player and sends the board
     * to all the players
     *
     * @param g
     */

    public synchronized void playGame(NetGame g) {
        List<AbaloneClientHandler> cl = g.getClients();

        //int current = g.randomWithRange(0, (clients.size() - 1));
        int current = g.randomWithRange(0, (g.NUMBER_PLAYERS - 1));
        Board b = g.getBoard();
        g.setCurrent(current);
        AbaloneClientHandler curr = cl.get(current);
        g.send(ProtocolMessages1.GIVE_BOARD + ProtocolMessages1.LDL + g.boardSend());
        try {
            curr.getBufferedWriter().write(ProtocolMessages1.TURN);
            curr.getBufferedWriter().newLine();
            curr.getBufferedWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //view.showMessage("I've sent the turn");

    }


    private int partnerIndex(int p) {
        int k = 0;
        if (p == 0) {
            k = 2;
        } else if (p == 1) {
            k = 3;
        } else if (p == 2) {
            k = 0;
        } else if (p == 3) {
            k = 1;
        }

        return k;
    }

    /**
     * Sends a move to all the players
     * updates the board and gives a turn to other players
     *
     * @param g
     * @param inp
     */

    public synchronized void doMove(NetGame g, String inp, String roomname) {
        List<AbaloneClientHandler> cl = g.getClients();
        //view.showMessage("THE CURRENT MOVE IS:" + inp);
        Board b = g.getBoard();
        //System.out.println("My current color is " + g.getMarkCurrent());
        boolean movepassed = g.play(inp);
        int cc = g.getCurrent();
        Boolean over = false;
        Boolean draw = false;
        Boolean winner23 = false;
        Boolean winner4 = false;
        //view.showMessage("> Score of " + g.getMarkCurrent() + " is " + g.getTheScore(cc) + ", current number of moves " + g.getMoves());

        if (g.getMoves() == 96) {
            draw = true;
        } else {
            if (capacity.get(roomname) < 4 && g.getTheScore(cc) >= 6) {
                //view.showMessage("SCORRR: " + g.getTheScore(cc));
                //view.showMessage("Capacity: " + capacity.get(roomname));
                winner23 = true;
            } else if (capacity.get(roomname) == 4 && (g.getTheScore(cc) + (g.getTheScore(partnerIndex(cc))) >= 6)) {
                winner4 = true;
            }
        }


        if (draw == true) {
            g.send(ProtocolMessages1.QUIT + ProtocolMessages1.LDL + "GAME WAS DRAW");
            doClean(roomname);
        } else if (winner23 == true) {
            g.send(ProtocolMessages1.QUIT + ProtocolMessages1.LDL + "Winner is " + g.getUsername(cc) + " with mark " + g.getMarkCurrent());
            doClean(roomname);
        } else if (winner4 == true) {
            g.send(ProtocolMessages1.QUIT + ProtocolMessages1.LDL + "Winners are " + g.getUsername(cc) +
                " and " + g.getUsername(partnerIndex(cc)));
            doClean(roomname);
        } else {
            if (movepassed == false) {
                AbaloneClientHandler curr1 = cl.get(g.getCurrent());
                try {
                    curr1.getBufferedWriter().write(ProtocolMessages1.FAIL);
                    curr1.getBufferedWriter().newLine();
                    curr1.getBufferedWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                AbaloneClientHandler curr1 = cl.get(g.getCurrent());
                try {
                    curr1.getBufferedWriter().write(ProtocolMessages1.UPDATE);
                    curr1.getBufferedWriter().newLine();
                    curr1.getBufferedWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AbaloneClientHandler curr = cl.get(g.nextCurrent());
                g.send(ProtocolMessages1.GIVE_BOARD + ProtocolMessages1.LDL + g.boardSend());
                try {
                    curr.getBufferedWriter().write(ProtocolMessages1.TURN);
                    curr.getBufferedWriter().newLine();
                    curr.getBufferedWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println("My current color is: " + players[current].getMark());

        //view.showMessage("I've sent the turn after move");

        /*while(b.gameOver()!=true){

        }*/


    }


    // ------------------ Main --------------------------

    /**
     * Start a new AbaloneServer
     */
    public static void main(String[] args) {
        AbaloneServer server = new AbaloneServer();
        System.out.println("Welcome to the Abalone Server! Starting...");
        new Thread(server).start();
    }

}
