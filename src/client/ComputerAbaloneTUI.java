package client;

import Exceptions.ExitProgram;
import Exceptions.ServerUnavailableException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import protocol.ProtocolMessages1;

public class ComputerAbaloneTUI implements ComputerAbaloneView {
    AbaloneClient.ComputerAbalonePlayer client;
    private PrintWriter console;
    private String menu = "d: Disconnect +";
    private Scanner sc;
    private boolean sercommand;
    private boolean inroom;
    private boolean ingame;
    private boolean botingame;

    public ComputerAbaloneTUI(AbaloneClient.ComputerAbalonePlayer client) {
        this.client = client;
        console = new PrintWriter(System.out, true);
        sercommand = false;
        inroom = false;
        ingame = false;
        botingame = false;
        //setDaemon(true);
    }

    public AbaloneClient.ComputerAbalonePlayer getClient() {
        return client;
    }

    public void setSercommand(boolean sercommand) {
        this.sercommand = sercommand;
    }

    @Override
    public void startt() throws ExitProgram, ServerUnavailableException, IOException {
        try {
            this.printHelpMenu();
            //AbaloneCommandsHandler ach = new AbaloneCommandsHandler(this.client);
            //new Thread(ach).start();
            while (true) {
                if (botingame == true) {
                    //SmartStrategy s = new SmartStrategy();
                    if (sercommand == true) {
                        String inp = client.readLineFromServer();
                        this.handleServerCommand(inp);
                    } else {
                        String s = client.getHint();
                        this.handleUserInput(s);
                    }

                } else if (botingame == false) {
                    if (sercommand == true) {
                        String inp = client.readLineFromServer();
                        this.handleServerCommand(inp);
                    } else {
                        String imp = this.getString("Enter command");
                        this.handleUserInput(imp);
                    }
                }
            }
        } catch (ExitProgram e) {
            client.sendExit();
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void handleServerCommand(String input) {
        String[] array = input.split(ProtocolMessages1.LDL);
        String command = array[0];
        String parm;
        String password;
        int cap;

        if (array.length > 1) {
            parm = array[1];
        } else {
            parm = "";
        }
        ;

        if (array.length > 2) {
            password = array[2];

        } else {
            password = "";
            //password = null;
        }
        if (array.length > 3) {
            cap = Integer.parseInt(array[3]);

        } else {
            cap = 0;
        }

        if (command.length() > 0) {
            switch (command) {

                case ProtocolMessages1.OK:
                    if (parm.equals("")) {
                        showMessage("OK");
                    } else {
                        showMessage("OK, room created");
                        inroom = true;
                    }
                    sercommand = false;
                    break;

                case ProtocolMessages1.START:
                    try {
                        client.handleStart(parm, password);
                        ingame = true;
                        botingame = true;
                    } catch (Exception e) {
                        //System.out.println("wtf");
                        e.printStackTrace();
                    }
                    break;


                case ProtocolMessages1.TURN:
                    showMessage("It's your turn");
                    sercommand = false;
                    break;

                case ProtocolMessages1.QUIT:
                    if (parm.equals("")) {
                        showMessage("Someone has quit!");
                    } else {
                        showMessage(parm);
                    }
                    sercommand = false;
                    inroom = false;
                    ingame = false;
                    botingame  = false;
                    break;

                case ProtocolMessages1.FAIL:
                    if (parm.equals("")) {
                        showMessage("Invaild move");
                    } else {
                        showMessage(parm);
                    }
                    sercommand = false;
                    break;

                case ProtocolMessages1.UPDATE:
                    showMessage("Updating");
                    setSercommand(true);
                    break;

                case ProtocolMessages1.GIVE_BOARD:
                    try {
                        client.handleBoard(parm);
                    } catch (ServerUnavailableException e) {
                        e.printStackTrace();
                    }
                    break;


                default:
                    this.showMessage("I can see you cant read");

            }

        } else {
            this.printHelpMenu();
        }

    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException, IOException {
        String[] array = input.split(ProtocolMessages1.LDL);
        String command = array[0];

        String parm;
        String password;
        int cap;
        Boolean dd = false;
        if (command.equals(ProtocolMessages1.MOVE)) {
            client.doMove(input);

            dd = true;
        }

        if (array.length > 1 && dd == false) {
            parm = array[1];
        } else {
            parm = null;
        }
        ;

        if (array.length > 2 && dd == false) {
            password = array[2];

        } else {
            password = null;
        }
        if (array.length > 3 && dd == false) {
            cap = Integer.parseInt(array[3]);

        } else {
            cap = 0;
        }

        if (command.length() > 0 && dd == false) {
            switch (command) {

                case ProtocolMessages1.CONNECT:
                    try {
                        client.start();
                    } catch (Exception e) {
                        System.out.println("wtf");
                    }
                    break;

                case ProtocolMessages1.DISCONNECT:
                    client.sendExit();
                    break;

                case ProtocolMessages1.CREATE:
                    if (array.length > 3) {
                        if (inroom == false) {
                            client.doCreate(parm, password, cap);
                            sercommand = true;
                        } else {
                            showMessage("Can't create, you are already in a room");
                        }
                    } else {
                        showMessage("Not enough arguments for creation");
                    }
                    break;


                case ProtocolMessages1.JOIN:
                    if (inroom == false) {
                        client.doJoin(parm, password);
                        sercommand = true;
                    } else {
                        showMessage("Can't join, you are already in a room");
                    }

                    break;

                case ProtocolMessages1.LIST:
                    client.doList();
                    break;


                case ProtocolMessages1.QUIT:
                    client.doQuit();
                    sercommand = true;
                    break;

                case ProtocolMessages1.GETPLAYERS:
                    if (inroom == true) {
                        client.doGetPlayers();
                    } else {
                        showMessage("Not in a room yet, can't get players");
                    }

                    break;

                case ProtocolMessages1.READY:
                    if (inroom == true) {
                        client.doReady();
                        sercommand = true;
                    } else {
                        showMessage("Not in a room yet, can't start a game");
                    }
                    break;

                case ProtocolMessages1.HINT:
                    if (ingame == true) {
                        showMessage(client.getHint());
                        //sercommand = true;
                    } else {
                        showMessage("Cannot use a hint when not in game");
                    }
                    break;

                case ProtocolMessages1.MOVE:
                    if (inroom == true) {
                        client.doMove(input);
                        sercommand = true;
                    } else {
                        showMessage("Not in a room yet");
                    }

                    break;


                default:
                    this.showMessage("I can see you cant read");

            }
            ;
        } else {
            this.printHelpMenu();
        }

    }

    @Override
    public void showMessage(String message) {
        console.println(message);
        // TODO Auto-generated method stub

    }

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

    @Override
    public InetAddress getIp() throws UnknownHostException {
        //System.out.println("Enter IP address");

        String input = "";
        Boolean pass = false;
        String[] array;
        while (pass == false) {
            System.out.println("Enter IP address");
            sc = new Scanner(System.in);
            input = sc.next();
            array = input.split("\\.");
            pass = true;
            //System.out.println("len " + array.length);

            for (int i = 0; i < array.length ; i++) {
                if(isInt(array[i])==false){
                    pass=false;
                }

            }
            if(array.length == 0){
                pass = false;
            }
        }
        //System.out.println("im out");
        InetAddress address = InetAddress.getByName(input);

        return address;
    }

    @Override
    public String getString(String question) {
        System.out.println(question);
        sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
        // TODO Auto-generated method stub

    }

    @Override
    public int getInt(String question) {
        System.out.println(question);
        sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (isInt(input) == false) {
            System.out.println(question);
            input = sc.nextLine();
        }
        int res = Integer.parseInt(input);
        return res;
    }

    @Override
    public boolean getBoolean(String question) {
        System.out.println(question);
        sc = new Scanner(System.in);
        String input = "";
        Boolean result;
        while (!input.equals("yes") && !input.equals("y") && !input.equals("true") && !input.equals("n")
            && !input.equals("no") && !input.equals("false")) {
            input = sc.next();
        }
        //Boolean input = sc.nextBoolean();
        if (input.equals("yes") || input.equals("y") || input.equals("true")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public void printHelpMenu() {
        System.out.println(menu);

    }

}
