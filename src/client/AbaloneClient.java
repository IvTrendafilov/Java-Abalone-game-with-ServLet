package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import Exceptions.*;
import protocol.ProtocolMessages1;
import pp.Board;
import pp.Mark;
import pp.SmartStrategy;

/**
 * Client for Networked Abalone Application.
 *
 * @author Vladi/Ivan
 */
public class AbaloneClient {
    private String Abalonename;
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
    private AbaloneClientTUI views;
    private Board b = new Board(2);
    private int number_players = 0;
    private Mark mymark;

    /**
     * Constructs a new AbaloneClient. Initialises the view.
     *
     * @throws ExitProgram
     * @throws ProtocolException
     * @throws ServerUnavailableException
     */
    public AbaloneClient() {
        views = new AbaloneClientTUI(this);
    }

    public BufferedReader getIn() {
        return in;
    }

    public AbaloneClientTUI getViews() {
        return this.views;
    }

    /**
     * Checks if the command that is given by the player
     * is an integer
     *
     * @param s
     * @return true/false
     * @requires s != null
     */

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }


    /**
     * Starts a new AbaloneClient by creating a connection, followed by the
     * HELLO handshake as defined in the protocol. After a successful
     * connection and handshake, the view is started. The view asks for
     * <p>
     * When errors occur, or when the user terminates a server connection, the
     * user is asked whether a new connection should be made.
     *
     * @throws ExitProgram
     * @throws ProtocolException
     * @throws ServerUnavailableException
     * @throws IOException
     */
    public void start() throws ExitProgram, ServerUnavailableException, ProtocolException, IOException {
        try {
            this.createConnection();
            this.handleHello();
            //new Thread(views).start();
            //AbaloneCommandsHandler ach = new AbaloneCommandsHandler(this);
            //new Thread(ach).start();
            views.startt();

        } catch (ExitProgram e) {
            e.printStackTrace();
            if (views.getBoolean("Do you want to try again?")) {
                this.closeConnection();
                this.start();
            } else {
                this.sendExit();
            }
            // TODO Auto-generated catch block

        } catch (ServerUnavailableException s) {
            s.printStackTrace();
            if (views.getBoolean("Do you want to try again?")) {
                this.closeConnection();
                this.start();
            } else {
                this.sendExit();
            }

        } catch (ProtocolException p) {
            p.printStackTrace();
            if (views.getBoolean("Do you want to try again?")) {
                this.closeConnection();
                this.start();
            } else {
                this.sendExit();
            }
        }
    }

    /**
     * Creates a connection to the server. Requests the IP and port to
     * connect to at the view (TUI).
     * <p>
     * The method continues to ask for an IP and port and attempts to connect
     * until a connection is established or until the user indicates to exit
     * the program.
     *
     * @throws ExitProgram          if a connection is not established and the user
     *                              indicates to want to exit the program.
     * @throws UnknownHostException
     * @ensures serverSock contains a valid socket connection to a server
     */
    public void createConnection() throws ExitProgram, UnknownHostException {
        clearConnection();
        while (serverSock == null) {
            InetAddress addr = views.getIp();

            int port = views.getInt("Enter port number");
            try {
                System.out.println("Attempting to connect to " + addr + ":"
                    + port + "...");
                serverSock = new Socket(addr, port);
                in = new BufferedReader(new InputStreamReader(
                    serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(
                    serverSock.getOutputStream()));
                views.showMessage("Connected");
            } catch (IOException e) {
                System.out.println("ERROR: could not create a socket on "
                    + addr.getHostAddress() + " and port " + port + ".");


                if (views.getBoolean("Do you want to open a new socket?") == false) {
                    throw new ExitProgram("User indicated to exit.");
                }
            }
        }
    }

    private List<Integer> marbless = new ArrayList<>();

    /**
     * This method checks if the input of the user
     * is valid or not
     *
     * @param inp
     * @return true/false
     * @requires inp != null
     */

    public boolean inputValidation(String inp) {
        boolean ok = true;
        String[] array = inp.split(ProtocolMessages1.LDL);
        int len = array.length;

        if (len < 3 || len > 5) {
            views.showMessage("too short or too long baby, try again");
            ok = false;
            return ok;
        }

        String c = array[len - 1];
        if (isInt(c) == true) {
            views.showMessage("Command cannot be number");
            ok = false;
            return ok;
        }

        if (len > 3 && (c.equals("R") || c.equals("L"))) {
            int max = 0;
            if (len == 4) {
                max = 2;
            }
            if (len == 5) {
                max = 3;
            }
            for (int i = 1; i < max; i++) {
                if (b.row(Integer.parseInt(array[i])) != b.row(Integer.parseInt(array[i + 1]))) {
                    views.showMessage("Cannot do right or left on different rows");
                    ok = false;
                    return ok;
                }
            }
        }

        if (b.getNumberOfPlayers() == 4) {
            marbless.clear();
            if (len == 3) {
                marbless.add(Integer.parseInt(array[1]));
            } else if (len == 4) {
                marbless.add(Integer.parseInt(array[1]));
                marbless.add(Integer.parseInt(array[2]));
            } else if (len == 5) {
                marbless.add(Integer.parseInt(array[1]));
                marbless.add(Integer.parseInt(array[2]));
                marbless.add(Integer.parseInt(array[3]));
            }
            c = array[len - 1];

            if (b.getField(marbless.get(0)) != mymark) {
                views.showMessage("First marble should be yours!");
                ok = false;
                return ok;
            }
            for (int i = 0; i < marbless.size(); i++) {
                if (b.getField(marbless.get(i)) == Mark.E || b.isField(marbless.get(i)) != true) {
                    views.showMessage("Cannot touch empty fields or out of bounds!");
                    ok = false;
                    return ok;
                }
            }
            for (int i = 1; i < marbless.size(); i++) {
                if (b.getField(marbless.get(i)) != mymark && b.getField(marbless.get(i)) != mymark.other()) {
                    views.showMessage("Cannot touch enemy team marbles!");
                    ok = false;
                    return ok;
                }
            }
            Collections.sort(marbless);
            if (len == 5) {
                if (b.getField(marbless.get(1)) == mymark.other() && b.getField(marbless.get(2)) == mymark) {
                    System.out.println("Cannot have partner marble between yours!");
                    ok = false;
                    return ok;
                }
            }
            marbless.clear();

        }

        if (b.getNumberOfPlayers() < 4) {
            for (int i = 1; i < len - 1; i++) {
                if (b.getField(Integer.parseInt(array[i])) != mymark || b.isEmptyField(Integer.parseInt(array[i])) == true) {
                    System.out.println("Cannot touch other players marbles or empty fields");
                    ok = false;
                    return ok;
                }
            }
        }

        return ok;
    }

    /**
     * Resets the serverSocket and In- and OutputStreams to null.
     * <p>
     * Always make sure to close current connections via shutdown()
     * before calling this method!
     */
    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
    }

    /**
     * Sends a message to the connected server, followed by a new line.
     * The stream is then flushed.
     *
     * @param msg the message to write to the OutputStream.
     * @throws ServerUnavailableException if IO errors occur.
     * @requires msg != null
     */
    public synchronized void sendMessage(String msg)
        throws ServerUnavailableException {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ServerUnavailableException("Could not write "
                    + "to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not write "
                + "to server.");
        }
    }

    /**
     * Reads and returns one line from the server.
     *
     * @return the line sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readLineFromServer()
        throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                //System.out.println("I WILL READ A LINE");
                String answer = in.readLine();
                if (answer == null) {
                    throw new ServerUnavailableException("Could not read "
                        + "from server.");
                }
                //System.out.println("I CAN SEE THE LINE: " + answer);
                return answer;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read "
                    + "from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read "
                + "from server.");
        }
    }


    /**
     * Closes the connection by closing the In- and OutputStreams, as
     * well as the serverSocket.
     */
    public void closeConnection() {
        System.out.println("Closing the connection...");
        try {
            in.close();
            out.close();
            serverSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles the connection between the server
     * and the client
     *
     * @throws ServerUnavailableException
     * @throws ProtocolException
     */
    public void handleHello()
        throws ServerUnavailableException, ProtocolException {
        Scanner inp = new Scanner(System.in);
        System.out.print("Give me your username: ");
        String username = inp.nextLine();
        while(ProtocolMessages1.containsIllegalCharacter(username)==true){
            System.out.print("Give me your username again: ");
            username = inp.nextLine();
        }
        this.sendMessage(ProtocolMessages1.CONNECT + ProtocolMessages1.LDL + username);
        String shit = this.readLineFromServer();
        String[] array = shit.split(ProtocolMessages1.LDL);
        Abalonename = array[1];
        //System.out.println("> Welcome to server with name:" + Abalonename);
        views.showMessage("> Welcome to server with name:" + Abalonename);

    }

    /**
     * This method creates a room with an Abalone game in it
     *
     * @param roomName must be a string and not containing illegal characters
     * @param password must be a string and not containing illegal characters
     * @param cap must be >= 2 && <= 4
     * @throws ServerUnavailableException
     * @requires password != null && roomName != null && cap > 0
     */
    public void doCreate(String roomName, String password, int cap)
        throws ServerUnavailableException {
        if (roomName != null && cap > 0) {
            this.sendMessage(ProtocolMessages1.CREATE + ProtocolMessages1.LDL + roomName + ProtocolMessages1.LDL
                + password + ProtocolMessages1.LDL + cap);
            //System.out.println(">" + " " +  this.readLineFromServer());
            //views.showMessage(">" + " " + this.readLineFromServer());
            views.showMessage("Waiting...");
            /*while(this.readLineFromServer()!=ProtocolMessages1.START){
                views.showMessage("Waiting...");
            }*/
            //waitStart();
            //views.showMessage(">" + " " + this.readLineFromServer());
            //views.showMessage(">" + " " + this.readLineFromServer());
            views.showMessage("Place ready when you want to start");
        }
    }

    /**
     * This send a command Ready to say that the player is ready
     *
     * @throws ServerUnavailableException
     */

    public void doReady() throws ServerUnavailableException {
        this.sendMessage(ProtocolMessages1.READY);
        //views.showMessage(">" + " " + this.readLineFromServer());

    }

    /**
     * This method sends command Join to say to which room the player wants to join
     *
     * @param roomName name of a room that exists in the server must not contain illegal characters
     * @param password must not contain illegal characters necessary only if a room has a password
     * @throws ServerUnavailableException
     * @requires roomName != null && password != null
     */

    public void doJoin(String roomName, String password)
        throws ServerUnavailableException {
        if (roomName != null) {
            this.sendMessage(ProtocolMessages1.JOIN + ProtocolMessages1.LDL + roomName + ProtocolMessages1.LDL
                + password);
            //System.out.println(">" + " " +  this.readLineFromServer());
            //views.showMessage(">" + " " + this.readLineFromServer());
            views.showMessage("Waiting...");
            /*while(this.readLineFromServer()!=ProtocolMessages1.START){
                views.showMessage("Waiting...");
            }*/
            //waitStart();
            //views.showMessage(">" + " " + this.readLineFromServer());
            //views.showMessage(">" + " " + this.readLineFromServer());
            //views.showMessage("I'M OUT OF JOINING");
        }
    }

    /**
     * This method send command List so that the server can show a list
     * of all active rooms
     *
     * @throws ServerUnavailableException
     */

    public void doList()
        throws ServerUnavailableException {
        this.sendMessage(ProtocolMessages1.LIST);
        //System.out.println(">" + " " +  this.readLineFromServer());
        views.showMessage(">" + " " + this.readLineFromServer());

    }

    /**
     * This method sends a command Move only if the move is validated
     * from the method input validation
     *
     * @param input
     * @throws ServerUnavailableException
     * @requires input != null
     */

    public void doMove(String input)
        throws ServerUnavailableException {
        //this.sendMessage(input);
        if (inputValidation(input) == true) {
            try {
                views.setSercommand(true);
                out.write(input);
                out.newLine();
                out.flush();
                views.setSercommand(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            views.showMessage("Invalid move!");
        }

        //System.out.println(">" + " " +  this.readLineFromServer());
        //views.showMessage(">" + " " + this.readLineFromServer());

    }


    /**
     * This method send a command getplayers so that the player can see
     * with whom other is he in the room/game
     *
     * @throws ServerUnavailableException
     */

    public void doGetPlayers()
        throws ServerUnavailableException {
        this.sendMessage(ProtocolMessages1.GETPLAYERS);
        //System.out.println(">" + " " +  this.readLineFromServer());
        views.showMessage(">" + " " + this.readLineFromServer());

    }

    /**
     * This method sends a command to the server that he wants to quit
     * @throws ServerUnavailableException
     */

    public void doQuit()
        throws ServerUnavailableException {
        this.sendMessage(ProtocolMessages1.QUIT);
    }

    /**
     * This method sends command start to the server
     * only when all the players in the room/game are ready
     * @param num must be a string
     * @param m must be a string
     * @throws ServerUnavailableException
     * @requires num != null && m != null
     */

    public void handleStart(String num, String m) throws ServerUnavailableException {
        number_players = Integer.parseInt(num);
        //b.NUMBER_PLAYERS = Integer.parseInt(num);
        if (m.equals("R")) {
            mymark = Mark.R;
        } else if (m.equals("Y")) {
            mymark = Mark.Y;
        } else if (m.equals("G")) {
            mymark = Mark.G;
        } else if (m.equals("P")) {
            mymark = Mark.P;
        }
        views.showMessage("Everyone is connected, wait for your turn, you play with " + mymark);
    }

    /**
     * This method receives the string which is given from the server
     * and it converts it on a board which is simulated on the client
     * so that the client can have a visualisation
     * @param msg handles the string of the board
     * @throws ServerUnavailableException
     * @requires msg != null
     */

    public void handleBoard(String msg) throws ServerUnavailableException {
        //b.reset();
        String[] array = msg.split(ProtocolMessages1.DL);
        //System.out.println("my size: " + array.length);
        b.emptyBoard();

        /*for(int i = 0 ; i< array.length;i++){
            System.out.println(i+". "+array[i]);
        }*/
        //views.showMessage(b.toString());
        //views.showMessage(b.toString());
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("R")) {
                b.setField(i + 1, Mark.R);
            } else if (array[i].equals("G")) {
                b.setField(i + 1, Mark.G);
            } else if (array[i].equals("Y")) {
                b.setField(i + 1, Mark.Y);
            } else if (array[i].equals("P")) {
                b.setField(i + 1, Mark.P);
            } else if (array[i].equals("E")) {
                b.setField(i + 1, Mark.E);
            }
        }
        views.showMessage(b.toString());
    }

    /**
     * Gives a hint to the player when he writes hint
     * @return a string
     * @throws ServerUnavailableException
     */

    public String getHint() throws ServerUnavailableException {
        String o = "";
        SmartStrategy s = new SmartStrategy();
        o = s.determineMove(b, mymark);
        String[] array = o.split(ProtocolMessages1.LDL);
        o = "";
        o += array[0] + ProtocolMessages1.LDL + array[1] + ProtocolMessages1.LDL;
        if (array[2].equals("Rc")) {
            o += "R";
        } else if (array[2].equals("Lc")) {
            o += "L";
        } else if (array[2].equals("URc")) {
            o += "UR";
        } else if (array[2].equals("ULc")) {
            o += "UL";
        } else if (array[2].equals("DRc")) {
            o += "DR";
        } else if (array[2].equals("DLc")) {
            o += "DL";
        }
        return o;
    }


    public void sendExit() throws ServerUnavailableException {
        this.sendMessage(ProtocolMessages1.DISCONNECT);
        this.closeConnection();
    }

    /**
     * This method starts a new AbaloneClient.
     *
     * @param args
     * @throws ProtocolException
     * @throws ServerUnavailableException
     * @throws ExitProgram
     * @throws IOException
     */


    public static void main(String[] args) throws ExitProgram,
        ServerUnavailableException, ProtocolException, IOException {
        new AbaloneClient().start();
    }


    public static class ComputerAbalonePlayer {

        private String Abalonename;
        private Socket serverSock;
        private BufferedReader in;
        private BufferedWriter out;
        private ComputerAbaloneTUI views;
        private Board b = new Board(2);
        private int number_players = 0;
        private Mark mymark;

        /**
         * Constructs a new AbaloneClient. Initialises the view.
         *
         * @throws ExitProgram
         * @throws ProtocolException
         * @throws ServerUnavailableException
         */
        public ComputerAbalonePlayer() {
            views = new ComputerAbaloneTUI(this);
        }

        public BufferedReader getIn() {
            return in;
        }

        public ComputerAbaloneTUI getViews() {
            return this.views;
        }

        /**
         * Checks if the command that is given by the player
         * is an integer
         *
         * @param s
         * @return
         */

        private static boolean isInt(String s) {
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return false;
            } catch (NullPointerException e) {
                return false;
            }
            // only got here if we didn't return false
            return true;
        }


        /**
         * Starts a new AbaloneClient by creating a connection, followed by the
         * HELLO handshake as defined in the protocol. After a successful
         * connection and handshake, the view is started. The view asks for
         * <p>
         * When errors occur, or when the user terminates a server connection, the
         * user is asked whether a new connection should be made.
         *
         * @throws ExitProgram
         * @throws ProtocolException
         * @throws ServerUnavailableException
         * @throws IOException
         */
        public void start() throws ExitProgram, ServerUnavailableException, ProtocolException, IOException {
            try {
                this.createConnection();
                this.handleHello();
                //new Thread(views).start();
                //AbaloneCommandsHandler ach = new AbaloneCommandsHandler(this);
                //new Thread(ach).start();
                views.startt();

            } catch (ExitProgram e) {
                e.printStackTrace();
                if (views.getBoolean("Do you want to try again?")) {
                    this.closeConnection();
                    this.start();
                } else {
                    this.sendExit();
                }
                // TODO Auto-generated catch block

            } catch (ServerUnavailableException s) {
                s.printStackTrace();
                if (views.getBoolean("Do you want to try again?")) {
                    this.closeConnection();
                    this.start();
                } else {
                    this.sendExit();
                }

            } catch (ProtocolException p) {
                p.printStackTrace();
                if (views.getBoolean("Do you want to try again?")) {
                    this.closeConnection();
                    this.start();
                } else {
                    this.sendExit();
                }
            }


        }

        /**
         * Creates a connection to the server. Requests the IP and port to
         * connect to at the view (TUI).
         * <p>
         * The method continues to ask for an IP and port and attempts to connect
         * until a connection is established or until the user indicates to exit
         * the program.
         *
         * @throws ExitProgram          if a connection is not established and the user
         *                              indicates to want to exit the program.
         * @throws UnknownHostException
         * @ensures serverSock contains a valid socket connection to a server
         */
        public void createConnection() throws ExitProgram, UnknownHostException {
            clearConnection();
            while (serverSock == null) {
                InetAddress addr = views.getIp();
                int port = views.getInt("Enter port number");
                /*int port;
                Scanner sc = new Scanner(System.in);
                System.out.println("Give me an ip you want to connect to");
                String i = sc.nextLine();
                System.out.println("Give me a port: ");
                port = sc.nextInt();
                InetAddress addr = InetAddress.getByName(i);*/
                // try to open a Socket to the server
                try {
                    System.out.println("Attempting to connect to " + addr + ":"
                        + port + "...");
                    serverSock = new Socket(addr, port);
                    in = new BufferedReader(new InputStreamReader(
                        serverSock.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(
                        serverSock.getOutputStream()));
                    views.showMessage("Connected");
                } catch (IOException e) {
                    System.out.println("ERROR: could not create a socket on "
                        + addr.getHostAddress() + " and port " + port + ".");


                    if (views.getBoolean("Do you want to open a new socket?") == false) {
                        throw new ExitProgram("User indicated to exit.");
                    }
                }
            }
        }

        private List<Integer> marbless = new ArrayList<>();


        /**
         * Resets the serverSocket and In- and OutputStreams to null.
         * <p>
         * Always make sure to close current connections via shutdown()
         * before calling this method!
         */
        public void clearConnection() {
            serverSock = null;
            in = null;
            out = null;
        }

        /**
         * Sends a message to the connected server, followed by a new line.
         * The stream is then flushed.
         *
         * @param msg the message to write to the OutputStream.
         * @throws ServerUnavailableException if IO errors occur.
         */
        public synchronized void sendMessage(String msg)
            throws ServerUnavailableException {
            if (out != null) {
                try {
                    out.write(msg);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new ServerUnavailableException("Could not write "
                        + "to server.");
                }
            } else {
                throw new ServerUnavailableException("Could not write "
                    + "to server.");
            }
        }

        /**
         * Reads and returns one line from the server.
         *
         * @return the line sent by the server.
         * @throws ServerUnavailableException if IO errors occur.
         */
        public String readLineFromServer()
            throws ServerUnavailableException {
            if (in != null) {
                try {
                    // Read and return answer from Server
                    //System.out.println("I WILL READ A LINE");
                    String answer = in.readLine();
                    if (answer == null) {
                        throw new ServerUnavailableException("Could not read "
                            + "from server.");
                    }
                    //System.out.println("I CAN SEE THE LINE: " + answer);
                    return answer;
                } catch (IOException e) {
                    throw new ServerUnavailableException("Could not read "
                        + "from server.");
                }
            } else {
                throw new ServerUnavailableException("Could not read "
                    + "from server.");
            }
        }


        /**
         * Closes the connection by closing the In- and OutputStreams, as
         * well as the serverSocket.
         */
        public void closeConnection() {
            System.out.println("Closing the connection...");
            try {
                in.close();
                out.close();
                serverSock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * This method handles the connection between the server
         * and the client
         *
         * @throws ServerUnavailableException
         * @throws ProtocolException
         */
        public void handleHello()
            throws ServerUnavailableException, ProtocolException {
            //Scanner inp = new Scanner(System.in);
            //System.out.print("Give me your username: ");
            String username = "Bot";
            this.sendMessage(ProtocolMessages1.CONNECT + ProtocolMessages1.LDL + username);
            String shit = this.readLineFromServer();
            String[] array = shit.split(ProtocolMessages1.LDL);
            Abalonename = array[1];
            //System.out.println("> Welcome to server with name:" + Abalonename);
            views.showMessage("> Welcome to server with name:" + Abalonename);

        }

        /**
         * This method creates a room with an Abalone game in it
         *
         * @param roomName
         * @param password
         * @param cap
         * @throws ServerUnavailableException
         */
        public void doCreate(String roomName, String password, int cap)
            throws ServerUnavailableException {
            if (roomName != null && cap > 0) {
                this.sendMessage(ProtocolMessages1.CREATE + ProtocolMessages1.LDL + roomName + ProtocolMessages1.LDL
                    + password + ProtocolMessages1.LDL + cap);
                //System.out.println(">" + " " +  this.readLineFromServer());
                //views.showMessage(">" + " " + this.readLineFromServer());
                views.showMessage("Waiting...");
                /*while(this.readLineFromServer()!=ProtocolMessages1.START){
                    views.showMessage("Waiting...");
                }*/
                //waitStart();
                //views.showMessage(">" + " " + this.readLineFromServer());
                //views.showMessage(">" + " " + this.readLineFromServer());
                views.showMessage("Place ready when you want to start");
            }
        }

        /**
         * This send a command Ready to say that the player is ready
         *
         * @throws ServerUnavailableException
         */
        public void doReady() throws ServerUnavailableException {
            this.sendMessage(ProtocolMessages1.READY);
        }

        /**
         * This method sends command Join to say to which room the player wants to join
         *
         * @param roomName
         * @param password
         * @throws ServerUnavailableException
         */

        public void doJoin(String roomName, String password)
            throws ServerUnavailableException {
            if (roomName != null) {
                this.sendMessage(ProtocolMessages1.JOIN + ProtocolMessages1.LDL + roomName + ProtocolMessages1.LDL
                    + password);
                //System.out.println(">" + " " +  this.readLineFromServer());
                //views.showMessage(">" + " " + this.readLineFromServer());
                views.showMessage("Waiting...");
                /*while(this.readLineFromServer()!=ProtocolMessages1.START){
                    views.showMessage("Waiting...");
                }*/
                //waitStart();
                //views.showMessage(">" + " " + this.readLineFromServer());
                //views.showMessage(">" + " " + this.readLineFromServer());
                //views.showMessage("I'M OUT OF JOINING");
            }
        }

        /**
         * This method send command List so that the server can show a list
         * of all active rooms
         *
         * @throws ServerUnavailableException
         */
        public void doList()
            throws ServerUnavailableException {
            this.sendMessage(ProtocolMessages1.LIST);
            //System.out.println(">" + " " +  this.readLineFromServer());
            views.showMessage(">" + " " + this.readLineFromServer());

        }

        /**
         * This method sends a command Move only if the move is validated
         * from the method input validation
         *
         * @param input
         * @throws ServerUnavailableException
         */
        public void doMove(String input)
            throws ServerUnavailableException {
            //this.sendMessage(input);
            try {
                views.setSercommand(true);
                out.write(input);
                out.newLine();
                out.flush();
                views.setSercommand(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * This method send a command getplayers so that the player can see
         * with whom other is he in the room/game
         *
         * @throws ServerUnavailableException
         */
        public void doGetPlayers()
            throws ServerUnavailableException {
            this.sendMessage(ProtocolMessages1.GETPLAYERS);
            //System.out.println(">" + " " +  this.readLineFromServer());
            views.showMessage(">" + " " + this.readLineFromServer());

        }

        /**
         * This method sends a command to the server that he wants to quit
         *
         * @throws ServerUnavailableException
         */
        public void doQuit()
            throws ServerUnavailableException {
            this.sendMessage(ProtocolMessages1.QUIT);
        }

        /**
         * This method sends command start to the server
         * only when all the players in the room/game are ready
         *
         * @param num
         * @param m
         * @throws ServerUnavailableException
         */
        public void handleStart(String num, String m) throws ServerUnavailableException {
            number_players = Integer.parseInt(num);
            //b.NUMBER_PLAYERS = Integer.parseInt(num);
            if (m.equals("R")) {
                mymark = Mark.R;
            } else if (m.equals("Y")) {
                mymark = Mark.Y;
            } else if (m.equals("G")) {
                mymark = Mark.G;
            } else if (m.equals("P")) {
                mymark = Mark.P;
            }
            views.showMessage("Everyone is connected, wait for your turn, you play with " + mymark);
        }

        /**
         * This method receives the string which is given from the server
         * and it converts it on a board which is simulated on the client
         * so that the client can have a visualisation
         *
         * @param msg
         * @throws ServerUnavailableException
         */
        public void handleBoard(String msg) throws ServerUnavailableException {
            //b.reset();
            String[] array = msg.split(ProtocolMessages1.DL);
            //System.out.println("my size: " + array.length);
            b.emptyBoard();

            /*for(int i = 0 ; i< array.length;i++){
                System.out.println(i+". "+array[i]);
            }*/
            //views.showMessage(b.toString());
            //views.showMessage(b.toString());
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals("R")) {
                    b.setField(i + 1, Mark.R);
                } else if (array[i].equals("G")) {
                    b.setField(i + 1, Mark.G);
                } else if (array[i].equals("Y")) {
                    b.setField(i + 1, Mark.Y);
                } else if (array[i].equals("P")) {
                    b.setField(i + 1, Mark.P);
                } else if (array[i].equals("E")) {
                    b.setField(i + 1, Mark.E);
                }
            }
            views.showMessage(b.toString());
        }

        /**
         * Gives a hint to the player when he writes hint
         * @return
         * @throws ServerUnavailableException
         */
        public String getHint() throws ServerUnavailableException {
            String o = "";
            SmartStrategy s = new SmartStrategy();
            o = s.determineMove(b, mymark);
            String[] array = o.split(ProtocolMessages1.LDL);
            o = "";
            o += array[0] + ProtocolMessages1.LDL + array[1] + ProtocolMessages1.LDL;
            if (array[2].equals("Rc")) {
                o += "R";
            } else if (array[2].equals("Lc")) {
                o += "L";
            } else if (array[2].equals("URc")) {
                o += "UR";
            } else if (array[2].equals("ULc")) {
                o += "UL";
            } else if (array[2].equals("DRc")) {
                o += "DR";
            } else if (array[2].equals("DLc")) {
                o += "DL";
            }
            return o;
        }


        //@Override
        public void sendExit() throws ServerUnavailableException {
            this.sendMessage(ProtocolMessages1.DISCONNECT);
            this.closeConnection();
        }

        /**
         * This method starts a new AbaloneClient.
         *
         * @param args
         * @throws ProtocolException
         * @throws ServerUnavailableException
         * @throws ExitProgram
         * @throws IOException
         */


        public static void main(String[] args) throws ExitProgram,
            ServerUnavailableException, ProtocolException, IOException {
            //predi beshe new ComputerAbalonePlayer().start() pak raboteshe ne znam dali e bilo greshno ili ne
            new AbaloneClient().start();

        }

    }
}
