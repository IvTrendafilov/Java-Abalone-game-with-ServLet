package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import Exceptions.*;
import java.util.ArrayList;
import java.util.List;
import pp.NetGame;
import protocol.ProtocolMessages1;

/**
 * AbaloneClientHandler for the Abalone Server application.
 * This class can handle the communication with one
 * client.
 *
 * @author Vladi/Ivan
 */
public class AbaloneClientHandler implements Runnable {

    /**
     * The socket and In- and OutputStreams
     */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;
    private String username;

    /**
     * The connected AbaloneServer
     */
    private AbaloneServer srv;

    /**
     * Name of this ClientHandler
     */
    private String name;
    //private boolean servercom;

    /**
     * Constructs a new AbaloneClientHandler. Opens the In- and OutputStreams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public AbaloneClientHandler(Socket sock, AbaloneServer srv, String name) {
        try {
            in = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.srv = srv;
            this.name = name;
            //servercom = false;
        } catch (IOException e) {
            shutdown();
        }
    }

    public Socket getSock() {
        return sock;
    }

    public BufferedWriter getBufferedWriter() {
        return out;
    }

    public BufferedReader getBufferedReader() {
        return in;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.newLine();
                out.flush();
                msg = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Reads a message from the server
     */
    public void readMessage() {
        String msg;
        try {
            msg = in.readLine();

            System.out.println("> [" + name + "] Incoming: " + msg);
            handleCommand(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            shutdown();
        }

    }

    /**
     * sends a message to the server
     *
     * @param msg
     * @throws ServerUnavailableException
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
     * Handles commands received from the client by calling the according
     * methods at the AbaloneServer. For example, when the message "i Name"
     * is received, the method doIn() of AbaloneServer should be called
     * and the output must be sent to the client.
     * <p>
     * If the received input is not valid, send an "Unknown Command"
     * message to the server.
     *
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    private void handleCommand(String msg) throws IOException {
        String[] array = msg.split(ProtocolMessages1.LDL, 10);
        String command = array[0];
        String parm;
        String password;
        int cap;
        int nights = 0;
        if (array.length > 1) {
            parm = array[1];
        } else {
            parm = null;
        }


        if (array.length > 2) {
            password = array[2];

        } else {
            password = null;
        }

        if (array.length > 3 && !command.equals(ProtocolMessages1.MOVE)) {
            cap = Integer.parseInt(array[3]);
        } else {
            cap = 0;
        }


        if (command.equals(ProtocolMessages1.CONNECT)) {
            this.username = parm;
            out.write(srv.getHello());
        } else if (command.equals(ProtocolMessages1.CREATE)) {

            if (srv.doCreate(parm, password, cap, sock, this.username, this) == true) {
                out.write(ProtocolMessages1.OK + ProtocolMessages1.LDL + "inroom");
            } else {
                out.write(ProtocolMessages1.FAIL + ProtocolMessages1.LDL + "Room with this name exists. Choose a new one.");
            }

        } else if (command.equals(ProtocolMessages1.JOIN)) {
            try {

                if (srv.doJoin(parm, password, sock, this.username, this)) {
                    out.write(ProtocolMessages1.OK + ProtocolMessages1.LDL + "inroom");
                } else {
                    out.write(ProtocolMessages1.FAIL + ProtocolMessages1.LDL + "Room already full.");
                }


            } catch (CapacityOverflow e) {
                System.out.println("Capacity is met");
            }


        } else if (command.equals(ProtocolMessages1.LIST)) {
            out.write(srv.doList());

        } else if (command.equals(ProtocolMessages1.MOVE)) {
            parm = srv.rooms.get(username);
            NetGame g = srv.games.get(parm);
            srv.doMove(g, msg, parm);


        } else if (command.equals(ProtocolMessages1.GETPLAYERS)) {
            List<String> roomz = new ArrayList<String>();
            String k = "";

            if (srv.sendd.containsKey(sock)) {
                k = srv.sendd.get(sock);
            }
            //System.out.println("My key is: " + k);
            out.write(srv.doGetPlayers(k));


        } else if (command.equals(ProtocolMessages1.READY)) {
            srv.doReady(this.username);
            parm = srv.rooms.get(username);

            if (srv.games.get(parm).getReady() == srv.capacity.get(parm)) {
                for (int i = 0; i < srv.games.get(parm).getClients().size(); i++) {
                    AbaloneClientHandler a;
                    a = srv.games.get(parm).getClients().get(i);
                    try {
                        a.sendMessage(ProtocolMessages1.START + ProtocolMessages1.LDL +
                            srv.games.get(parm).NUMBER_PLAYERS + ProtocolMessages1.LDL + srv.games.get(parm).getMarkOfAPlayer(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                srv.playGame(srv.games.get(parm));
            }
        } /*else if (command.equals(ProtocolMessages1.SAY)) {
            System.out.println("I see you want to send a chat");
            String user = srv.players.get(this.getSock());
            //sendChat(parm, user);
        }*/
        else if (command.equals(ProtocolMessages1.QUIT)) {
            //srv.doReady(this.username);
            parm = srv.rooms.get(username);
            srv.doQuit(parm);
        } else {
            out.write("Problem");
        }
    }

    /**
     * Shut down the connection to this client by closing the socket and
     * the In- and OutputStreams.
     */
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
            if(srv.rooms.containsKey(username)==true) {
                String parm = srv.rooms.get(username);
                srv.doQuit(parm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        srv.removeClient(this);
    }
}
