package protocol;

import java.util.ArrayList;


public abstract class ProtocolMessages1 {

    /**
     * For the terminology, see the comments behind the code or the protocol excel sheet.
     */

    // Delimiters
    public static final String DL = ",";    // A delimiter for separating data in objects
    public static final String LDL = ":";   // A delimiter for separating objects

    // Directions for moving marbles
    public static final String LEFT = "L";
    public static final String RIGHT = "R";
    public static final String UP_LEFT = "UL";
    public static final String UP_RIGHT = "UR";
    public static final String DOWN_LEFT = "DL";
    public static final String DOWN_RIGHT = "DR";

    // Colors
    public static final String BLACK = "b";                 // Represents a black marble
    public static final String WHITE = "W";                 // Represents a white marble
    public static final String RED = "R";                   // Represents a red marble
    public static final String BLUE = "B";                  // Represents a blue marble
    public static final String EMPTY = "E";                 // Represents an empty field

    // Keywords for the client
    public static final String CONNECT = "c";               // For saying you want to connect to the server
    public static final String DISCONNECT = "d";            // For saying you want to disconnect from the server
    public static final String CREATE = "create";           // For saying you want to create a room
    public static final String JOIN = "join";               // For saying you want to join a room
    public static final String LIST = "list";               // For saying you want to get a list of rooms
    public static final String READY = "ready";             // For stating you are ready to start the game
    public static final String MOVE = "move";               // For moving marbles
    public static final String GETPLAYERS = "getplayers";   // For getting the players in that room
    public static final String QUIT = "quit";               // For saying you exit the room
    public static final String SAY = "say";                 // For saying a chat message
    public static final String HINT = "hint";                // For asking for a hint

    // Keywords for the server
    public static final String START = "start";             // For telling clients the game starts
    public static final String TURN = "turn";               // For giving a client the turn to move
    public static final String UPDATE = "update";           // For updating a move to all clients
    public static final String GOVER = "gover";             // For telling clients the game has stopped
    public static final String SAYALL = "sayall";           // For sending a chat message
    public static final String ROOMS = "rooms";             // For responding with a list of current rooms
    public static final String PLAYERDATA = "playerdata";   // For responding with player data

    // Keywords for both the server and the client
    public static final String ASK_BOARD = "askboard";      // For asking the board from either client or server
    public static final String GIVE_BOARD = "giveboard";    // For responding with the board data

    // Error responses
    public static final String OK = "ok";
    public static final String FAIL = "fail";

    // If the request is sent at a strange moment.
    public static final String E_ILLEGAL_REQUEST = "illegal_request";
    // If the format of the message is unknown/incorrect.
    public static final String E_UNKNOWN_FORMAT = "unknown_format";
    // If a player wants to connect with a duplicate name.
    public static final String E_DUPLICATE_USERNAME = "duplicate_name";
    // If a player wants to connect with an illegal name.
    public static final String E_INVALID_USERNAME = "invalid_name";
    // If the player is already connected.
    public static final String E_ALREADY_CONNECTED = "already_connected";
    // If a player creates a room with a name that already exists.
    public static final String E_DUPLICATE_ROOM_NAME = "duplicate_game_name";
    // If a player creates a room with an illegal name.
    public static final String E_INVALID_ROOM_NAME = "invalid_game_name";
    // If a player creates a room with an illegal password.
    public static final String E_INVALID_PASSWORD = "invalid_password";
    // If a player creates a room with an invalid player capacity.
    public static final String E_INVALID_CAPACITY = "invalid_capacity";
    // If a player tries to join a non-existing room.
    public static final String E_ROOM_NOT_EXISTING = "not_existing";
    // If a player sends an incorrect password.
    public static final String E_INCORRECT_PASSWORD = "incorrect_password";
    // If a player tries to join a room that is full.
    public static final String E_ROOM_FULL = "room_full";
    // If a player wants to join a room while already being in one.
    public static final String E_ALREADY_IN_ROOM = "already_in_room";
    // If a player wants to start while.
    public static final String E_GAME_IN_PROGRESS = "game_in_progress";
    // If a player tries to make marbles move from an invalid position.
    public static final String E_INVALID_POSITION = "invalid_position";
    // If a player tries to move marbles in an invalid direction.
    public static final String E_INVALID_DIRECTION = "invalid_direction";
    // If a player breaks the rules by executing this move.
    public static final String E_ILLEGAL_MOVE = "illegal_move";
    // If a player tries to make a move while it's not his turn.
    public static final String E_NOT_YOUR_TURN = "not_your_turn";
    // If a player tries to send a chat message containing an illegal character.
    public static final String E_ILLEGAL_CHARACTER = "illegal_character";
    // If a player tries to to do something in a game while he is not in a game.
    public static final String E_NOT_IN_GAME = "not_in_game";
    // If a player tries to do somehting in a room while not being in a room.
    public static final String E_NOT_IN_ROOM = "not_in_room";

    // =============== Helpful functions =============== //

    /**
     * Check if a string contains an illegal character.
     *
     * @param text Text to check for errors
     * @return true if text contains an illegal character, false otherwise
     */
    public static boolean containsIllegalCharacter(String text) {
        String txt = (text == null) ? "" : text;
        return (txt.contains(DL) || txt.contains(LDL));
    }

    /**
     * Return the keyword of a message.
     *
     * @param message is the message that contains a keyword
     * @return the keyword
     */
    public static String getKeyWord(String message) {
        String msg = (message == null) ? "" : message;
        return msg.split(LDL)[0];
    }

    /**
     * Return data of a room in the right format to put it in a list.
     *
     * @param roomName        is the name of the room
     * @param amountOfPlayers is the amount of players that currently are in the room
     * @param capacity        is the maximum amount of players in that room
     * @param hasPassword     is the boolean value that tells if the room has a password
     * @return a string version of a room, e.g. "room1,2,3,true" or "room2,4,4,false"
     */
    public static String roomDataToString(String roomName, int amountOfPlayers, int capacity, boolean hasPassword) {
        String name = (roomName == null) ? "" : roomName;
        return name + DL + amountOfPlayers + DL + capacity + DL + hasPassword;
    }

    /**
     * Returns a string that contains a list of string representations of rooms.
     *
     * @param rooms is a list of rooms in string format
     * @return a string version of a list of rooms, e.g. "room1,2,3,true:room2,4,4,false: ... :room69,1,2,true"
     */
    public static String roomsListToString(ArrayList<String> rooms) {
        StringBuilder bs = new StringBuilder();
        String del = "";
        for (String room : rooms) {
            bs.append(del);
            bs.append(room);
            del = LDL;
        }
        return bs.toString();
    }

    /**
     * Returns a string that contains a list of colors in a field.
     * E.g. B,B,B,W,E,E,E,b,E,b,b,b,b,R,R,R ... , R,B
     * @param fields is an arraylist of color strings. Use the string in this class for the correct color
     * @return a string that can be built with for sending the board data through a socket
     *
     */
    public static String fieldListToString(ArrayList<String> fields) {
        StringBuilder bs = new StringBuilder();
        String del = "";
        for (String field : fields) {
            bs.append(del);
            bs.append(field);
            del = DL;
        }
        return bs.toString();
    }
    // =============== Client messages =============== //

    /**
     * Builds a string according to the protocol for sending a connectionRequest to the server.
     *
     * @param username Username to use for authentication
     * @return a string in the correct format that can be sent through the socket, E.g. "c:TheLegend27"
     *
     */
    public static String requestConnectToServer(String username) {
        String name = (username == null) ? "" : username;
        return CONNECT + LDL + name;
    }

    /**
     * Builds a string according to the protocol for sending a disconnectRequest to the server.
     *
     * @return a string in the correct format that can be sent through the socket, e.g. "d"
     */
    public static String requestDisconnectFromServer() {
        return DISCONNECT;
    }

    /**
     * Builds a string according to the protocol for sending a createGame request to the server.
     * E.g. "create:room1:qwerty:4" or "create:room1::4"
     * @param gameName is the name of the room
     * @param password is the password for joining the room. Leave empty String for no password
     * @param capacity is the maximum amount of players for that room
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestCreateRoom(String gameName, String password, int capacity) {
        String name = (gameName == null) ? "" : gameName;
        String pass = (password == null) ? "" : password;
        return CREATE + LDL + name + LDL + pass + LDL + capacity;
    }

    /**
     * Builds a string according to the protocol for sending a joinRoom request to the server.
     * E.g. "join:room1:qwerty" or "join:room1:"
     * @param gameName is the name of the room you want to join
     * @param password is the password of the room. Give empty string if there is no password
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestJoinRoom(String gameName, String password) {
        String name = (gameName == null) ? "" : gameName;
        String pass = (password == null) ? "" : password;
        return JOIN + LDL + name + LDL + pass;
    }

    /**
     * Builds a string according to the protocol for sending a roomList request to the server.
     * E.g. "list"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestListRooms() {
        return LIST;
    }

    /**
     * Builds a string according to the protocol for sending a ready message to the server.
     * E.g. "ready"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestReady() {
        return READY;
    }

    /**
     * Builds a string according to the protocol for making a move.
     * E.g. "move:3:4:5:L" or "move:3:::UR"
     * @param pos1      is the position of the first marble. Leave pos empty if no marble is moved
     * @param pos2      is the position of the second marble
     * @param pos3      is the position of the third marble
     * @param direction is the direction the marbles should move. Use a direction constant from this class
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestMove(int pos1, int pos2, int pos3, String direction) {
        String dir = (direction == null) ? "" : direction;
        return MOVE + LDL + pos1 + LDL + pos2 + LDL + pos3 + LDL + dir;
    }

    /**
     * Builds a string according to the protocol for requesting data of players in a room.
     * E.g. "getplayers"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestPlayerData() {
        return GETPLAYERS;
    }

    /**
     * Builds a string according to the protocol for leaving your current room.
     * E.g. "quit"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestQuitRoom() {
        return QUIT;
    }

    /**
     * Builds a string according to the protocol for sending a chat message.
     * E.g. "say:Knock knock"
     * @param message Message to send
     * @return a string in the correct format that can be sent through the socket
     */
    public static String requestChat(String message) {
        String msg = (message == null) ? "" : message;
        return SAY + LDL + msg;
    }

    // =============== Server messages =============== //

    /**
     * Builds a string according to the protocol for notifying clients the game starts.
     * E.g. "start"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String startGame() {
        return START;
    }

    /**
     * Builds a string according to the protocol for giving the turn to a player.
     * E.g. "turn:TheLegend27"
     * @param username is the user to which the turn is handed
     * @return a string in the correct format that can be sent through the socket
     */
    public static String giveTurn(String username) {
        String name = (username == null) ? "" : username;
        return TURN + LDL + name;
    }

    /**
     * Builds a string according to the protocol for updating a move to all players.
     * E.g. "update:TheLegend27:2:3:4:L" or "update:TheLegend27:4:::UL"
     * @param username  is the name of the user that made the move
     * @param pos1      is the position of the first marble. Enter -1 for no position
     * @param pos2      is the position of the second marble. Enter -1 for no position
     * @param pos3      is the position of the third marble. Enter -1 for no position
     * @param direction is the direction of the move
     * @return a string in the correct format that can be sent through the socket
     */
    public static String updateMove(String username, int pos1, int pos2, int pos3, String direction) {
        String name = (username == null) ? "" : username;
        String dir = (direction == null) ? "" : direction;
        String m1 = (pos1 <= -1) ? "" : Integer.toString(pos1);
        String m2 = (pos2 <= -1) ? "" : Integer.toString(pos2);
        String m3 = (pos3 <= -1) ? "" : Integer.toString(pos3);
        return UPDATE + LDL + name + LDL + m1 + LDL + m2 + LDL + m3 + LDL + dir;
    }

    /**
     * Builds a string according to the protocol for informing the player that the game stopped.
     * E.g. "gover:TheLegend27" or "gover:"
     * @param username is the name of the user that won. Leave empty if it is a draw
     * @return a string in the correct format that can be sent through the socket
     */
    public static String gameOver(String username) {
        String name = (username == null) ? "" : username;
        return GOVER + LDL + name;
    }

    /**
     * Builds a string according to the protocol for broadcasting a chat message to all players in a room.
     * E.g. "sayall:TheLegend27:GG ez, git gud u n00bs"
     * @param username is the name of the user that sent the message
     * @param message  is the message that has been sent
     * @return a string in the correct format that can be sent through the socket
     */
    public static String sayAll(String username, String message) {
        String name = (username == null) ? "" : username;
        String msg = (message == null) ? "" : message;
        return SAYALL + LDL + name + LDL + msg;
    }

    /**
     * Builds a string according to the protocol for giving a list of all current rooms.
     * E.g. "rooms:room1,2,4,true:room2,3,3,false: ... :room69,1,2,true"
     * @param rooms is the list of rooms and their data
     * @return a string in the correct format that can be sent through the socket
     */
    public static String giveRooms(String rooms) {
        String rms = (rooms == null) ? "" : rooms;
        return ROOMS + LDL + rms;
    }

    /**
     * Builds a string according to the protocol for responding with an ok to the client.
     * E.g. "ok"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String ok() {
        return OK;
    }

    /**
     * Builds a string according to the protocol for responding with a fail message to the client.
     * E.g. "fail:unknown_format"
     * @param error is the error that happened
     * @return a string in the correct format that can be sent through the socket
     */
    public static String error(String error) {
        String er = (error == null) ? "" : error;
        return FAIL + LDL + er;
    }

    // =============== Server/Client messages =============== //

    /**
     * Builds a string according to the protocol for asking the board of the client or server.
     * E.g. "askboard"
     * @return a string in the correct format that can be sent through the socket
     */
    public static String askBoard() {
        return ASK_BOARD;
    }

    /**
     * Builds a string according to the protocol for giving the board data to the server or client.
     * E.g. "giveboard:R,R,B,b,W,E,E,E, ... ,W,W"
     * @param boardData is the data of the board. Use
     * @return a string in the correct format that can be sent through the socket
     */
    public static String giveBoard(String boardData) {
        return GIVE_BOARD + LDL + boardData;
    }
}
