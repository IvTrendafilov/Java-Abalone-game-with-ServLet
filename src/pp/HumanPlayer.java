package pp;

import protocol.ProtocolMessages1;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;

/**
 * Class for human player
 * @author Vladi/Ivan
 */

public class HumanPlayer extends Player {
    protected final int R = 1;
    protected final int L = -1;
    protected int DL;
    protected int DR;
    protected int UL;
    protected int UR;
    Scanner sc = new Scanner(System.in);
    String[] array;
    String command;
    protected ArrayList<Integer> marbles = new ArrayList<Integer>();
    protected ArrayList<Integer> outmarbles = new ArrayList<Integer>();
    protected int score;
    protected String res;

    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
        setScore(0);
    }

    /**
     * This method looks at which roll we are and sets the values
     * of our constants so that we can move marbles in different directions
     *
     * @param r
     * @requires r > 0
     * ensures this.setVal == val
     */
    public void setVal(int r) {
        if (r == 1) {
            this.DL = 5;
            this.DR = 6;
        }
        if (r == 2) {
            this.DL = 6;
            this.DR = 7;
            this.UL = -6;
            this.UR = -5;
        }
        if (r == 3) {
            this.DL = 7;
            this.DR = 8;
            this.UL = -7;
            this.UR = -6;
        }
        if (r == 4) {
            this.DL = 8;
            this.DR = 9;
            this.UL = -8;
            this.UR = -7;
        }
        if (r == 5) {
            this.DL = 8;
            this.DR = 9;
            this.UL = -9;
            this.UR = -8;
        }
        if (r == 6) {
            this.DL = 7;
            this.DR = 8;
            this.UL = -9;
            this.UR = -8;
        }
        if (r == 7) {
            this.DL = 6;
            this.DR = 7;
            this.UL = -8;
            this.UR = -7;
        }
        if (r == 8) {
            this.DL = 5;
            this.DR = 6;
            this.UL = -7;
            this.UR = -6;
        }
        if (r == 9) {
            this.UL = -6;
            this.UR = -5;
        }


    }


    /**
     * These returns the score of a player
     *
     * @return number must be between >=0 <=6
     * @requires score >=0
     * @ensures score >=0 && <= 6
     */

    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * increments the score of a player
     * @requires score >= 0
     */

    public void incrementScore() {
        this.score++;
    }

    /**
     * sets the score of a player
     *
     * @param s
     * @requires s >= 0
     * @ensures this.setScore == s
     */

    public void setScore(int s) {
        this.score = s;
    }

    /**
     * This method checks if a given command is not valid
     * for example if a player has given only a direction
     *
     * @param s
     * @return true/false
     * @requires s != null
     */

    public static boolean isInteger(String s) {
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
     * This method returns the value of how much positions the marble
     * need to be moved depending of the direction to which the player
     * wants to move it
     * @param c must be a command of direction
     * @return returns a number which is by how much a marble should be moved
     * @throws Exception
     * @requires c !=null && c.equals(R|L|UL|UR|DR|DL)
     * @ensures commandVal> 0
     */

    public int commandVal(String c) throws Exception {
        boolean pass = false;
        if (c.equals("R")) {
            return R;
        } else if (c.equals("L")) {
            return L;
        } else if (c.equals("UL")) {
            return UL;
        } else if (c.equals("UR")) {
            return UR;
        } else if (c.equals("DL")) {
            return DL;
        } else if (c.equals("DR")) {
            return DR;
        }
        if (pass == false) {
            throw new Exception("invalid command");
        }
        return 0;
    }

    /**
     * this method checks the position of a marble and
     * the direction to which a players wants to move the marble
     * and return false if the player wants to move the marble to a
     * possition which is invalid
     * @param b board
     * @param c string which is a command
     * @param p a position on the board
     * @return true/false
     * @throws Exception
     * @requires c!=null && p > 0
     */

    public boolean whichForbidden(Board b, String c, int p) throws Exception {

        boolean pass = false;
        if (c.equals("R")) {
            return b.isForbiddenRight(p);
        } else if (c.equals("L")) {
            return b.isForbiddenLeft(p);
        } else if (c.equals("UL")) {
            return b.isForbiddenUpLeft(p);
        } else if (c.equals("UR")) {
            return b.isForbiddenUpRight(p);
        } else if (c.equals("DL")) {
            return b.isForbiddenDownLeft(p);
        } else if (c.equals("DR")) {
            return b.isForbiddenDownRight(p);
        }

        if (pass == false) {
            //System.out.println("I wont print forbidden");
            throw new Exception("invalid command");
        }
        return false;
    }
    /**
     * this method return which marbles is to be used for moving
     * and making a summito
     * @param c
     * @return
     * @throws Exception
     */


    public int initUseSumito4(String c) throws Exception {
        boolean pass = false;
        if (c.equals("R")) {
            return 2;
        } else if (c.equals("L")) {
            return 0;
        } else if (c.equals("UL")) {
            return 0;
        } else if (c.equals("UR")) {
            return 0;
        } else if (c.equals("DL")) {
            return 2;
        } else if (c.equals("DR")) {
            return 2;
        }
        if (pass == false) {
            throw new Exception("invalid command");
        }
        return 0;
    }

    /**
     * this method return which marbles is to be used for moving
     * and making a summito
     * @param command
     * @param board
     * @return
     * @throws Exception
     * @requires command != null && command.equals(R|L|UL|UR|DR|DL)
     */

    public void drawSummito(Board board, String command, String res) {
        Collections.sort(marbles);
        if (command.equals("UL") || command.equals("UR") || command.equals("L")) {
            Collections.reverse(marbles);
        }
        try {
            if (whichForbidden(board, command, marbles.get(marbles.size() - 1))) {
                incrementScore();
            } else {
                int neww = marbles.get(marbles.size() - 1);
                setVal(board.row(neww));
                neww += commandVal(command);
                marbles.add(neww);
            }

            //System.out.println("Marbles last time before recolor " + marbles.toString());
            Mark m;
            for (int i = marbles.size() - 1; i >= 1; i--) {
                m = board.getField(marbles.get(i - 1));
                board.setField(marbles.get(i), m);
            }

            board.setField(marbles.get(0), Mark.E);
            marbles.clear();
            //System.out.println("Player " + getName() + " " + getMark() + " score is " + getScore());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method works for a standart 2 or 3 players game
     * this method checks if a summito for 2vs1 marbles is possible.
     * @param b
     * @param c
     * @return true/false
     * @requires c != null
     */

    public boolean isSumito3Standard(Board b, String c) {
        int curr;
        if (c.equals("R")) {
            Collections.sort(marbles);
            if (b.isField(marbles.get(1) + 1) == true && b.getField(marbles.get(1) + 1) != getMark() && b.getField(marbles.get(1) + 1) != Mark.E) {
                if (b.isForbiddenRight(marbles.get(1) + 1) == true) {
                    marbles.add(marbles.get(1) + 1);
                    return true;
                } else if (b.isEmptyField(marbles.get(1) + 2) == true) {
                    marbles.add(marbles.get(1) + 1);
                    return true;
                }
            }
        } else if (c.equals("L")) {
            Collections.sort(marbles);
            if (b.isField(marbles.get(0) - 1) == true && b.getField(marbles.get(0) - 1) != getMark() && b.getField(marbles.get(0) - 1) != Mark.E) {
                if (b.isForbiddenLeft(marbles.get(0) - 1) == true) {
                    marbles.add(marbles.get(0) - 1);
                    return true;
                } else if (b.isEmptyField(marbles.get(0) - 2) == true) {
                    marbles.add(marbles.get(0) - 1);
                    return true;
                }
            }
        } else if (c.equals("UL")) {
            Collections.sort(marbles);
            curr = marbles.get(0);
            setVal(b.row(curr));
            if (b.isField(curr + UL) == true && b.getField(curr + UL) != getMark() && b.getField(curr + UL) != Mark.E) {
                if (b.isForbiddenUpLeft(curr + UL) == true) {
                    marbles.add(curr + UL);
                    return true;
                }
                int UL_old = UL;
                setVal(b.row(curr + UL));
                if (b.isEmptyField(curr + UL + UL_old) == true) {
                    marbles.add(curr + UL_old);
                    return true;
                }
            }
        } else if (c.equals("UR")) {
            Collections.sort(marbles);
            curr = marbles.get(0);
            setVal(b.row(curr));
            if (b.isField(curr + UR) == true && b.getField(curr + UR) != getMark() && b.getField(curr + UR) != Mark.E) {
                if (b.isForbiddenUpRight(curr + UR) == true) {
                    marbles.add(curr + UR);
                    return true;
                }
                int UR_old = UR;
                setVal(b.row(curr + UR));
                if (b.isEmptyField(curr + UR + UR_old) == true) {
                    marbles.add(curr + UR_old);
                    return true;
                }
            }
        } else if (c.equals("DL")) {
            Collections.sort(marbles);
            curr = marbles.get(1);
            setVal(b.row(curr));
            if (b.isField(curr + DL) == true && b.getField(curr + DL) != getMark() && b.getField(curr + DL) != Mark.E) {
                if (b.isForbiddenDownLeft(curr + DL) == true) {
                    marbles.add(curr + DL);
                    return true;
                }
                int DL_old = DL;
                setVal(b.row(curr + DL));
                if (b.isEmptyField(curr + DL + DL_old) == true) {
                    marbles.add(curr + DL_old);
                    return true;
                }
            }
        } else if (c.equals("DR")) {
            Collections.sort(marbles);
            curr = marbles.get(1);
            setVal(b.row(curr));
            if (b.isField(curr + DR) == true && b.getField(curr + DR) != getMark() && b.getField(curr + DR) != Mark.E) {
                if (b.isForbiddenDownRight(curr + DR) == true) {
                    marbles.add(curr + DR);
                    Collections.reverse(marbles);
                    return true;
                }
                int DR_old = DR;
                setVal(b.row(curr + DR));
                if (b.isEmptyField(curr + DR + DR_old) == true) {
                    marbles.add(curr + DR_old);
                    return true;
                }
            }
        }
        marbles.clear();
        return false;
    }

    /**
     * this method works for a standart 2 or 3 player game
     * this method checks if a summito for 3vs1 or 3vs2 marbles is possible
     * @param b
     * @param c
     * @return true/false
     * @requires c != null
     */

    public boolean isSumito4Standard(Board b, String c) {
        Collections.sort(marbles);
        //System.out.println("Sorted marbles " + marbles.toString());
        int use = 0;
        int curr = 0;
        if (c.equals("R")) {
            use = marbles.get(2);
        } else if (c.equals("L")) {
            use = marbles.get(0);
        } else if (c.equals("UL")) {
            use = marbles.get(0);
        } else if (c.equals("UR")) {
            use = marbles.get(0);
        } else if (c.equals("DL")) {
            use = marbles.get(2);
        } else if (c.equals("DR")) {
            use = marbles.get(2);
        }
        try {
            setVal(b.row(use));
            curr = use + commandVal(c);
            if (b.isEmptyField(curr) == true) {
                marbles.clear();
                return false;
            } else if (b.getField(curr) != getMark()) {
                if (whichForbidden(b, c, curr) == true) {
                    marbles.add(curr);
                    return true;
                }
                marbles.add(curr);
                use = use + commandVal(c);
                setVal(b.row(use));
                curr = use + commandVal(c);
                if (b.isEmptyField(curr) == true) {
                    return true;
                } else if (b.getField(curr) != getMark()) {
                    if (whichForbidden(b, c, curr) == true) {
                        marbles.add(curr);
                        return true;
                    }
                    marbles.add(curr);
                    use = use + commandVal(c);
                    setVal(b.row(use));
                    curr = use + commandVal(c);
                    //System.out.println("I'm the latest curr in sumito 4 " + curr);
                    if (b.isEmptyField(curr)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception for whole sumito4");
            e.printStackTrace();
        }
        marbles.clear();
        return false;
    }

    /**
     * this method parses the marbles from a command to integers
     * an array puts them into an array
     * @param len
     * @requires len > 0
     */


    public void marblesCreation(int len) {
        if (len == 3) {
            marbles.add(Integer.parseInt(array[1]));
        } else if (len == 4) {
            marbles.add(Integer.parseInt(array[1]));
            marbles.add(Integer.parseInt(array[2]));
        } else if (len == 5) {
            marbles.add(Integer.parseInt(array[1]));
            marbles.add(Integer.parseInt(array[2]));
            marbles.add(Integer.parseInt(array[3]));
        }
        command = array[len - 1];
    }

    /**
     * this method checks if a summito is possible for 2vs1 mabrles
     * this method works only for a 4 player game
     * @param b
     * @param c
     * @return true/false
     * @requires c != null
     */

    public boolean isSumito3Max(Board b, String c) {
        if (b.getField(marbles.get(0)) != getMark()) {
            //System.out.println("First marble should be yours");
            return false;
        }
        Mark t = getMark();
        if (b.getField(marbles.get(1)) == getMark() || b.getField(marbles.get(1)) == t.other()) {
            Collections.sort(marbles);
            //System.out.println("Marbles to be used by sumito3max " + marbles.toString());
            if (c.equals("L") || c.equals("UR") || c.equals("UL")) {
                Collections.reverse(marbles);
            }
            //System.out.println("Marbles to be used by sumito3max after sorting" + marbles.toString());
            int curr;
            curr = marbles.get(1);
            //System.out.println("current marble to be used for val" + curr);
            setVal(b.row(curr));
            try {
                int addition = commandVal(c);
                t = b.getField(curr + addition);
                if (b.getField(curr + addition) == getMark() || b.getField(curr + addition) == getMark().other()) {
                    //System.out.println("Don't try to touch your friends again!");
                    return false;
                }
                if (b.isField(curr + addition) == true &&
                    (b.getField(curr + addition) != getMark() || b.getField(curr + addition) != t.other()) &&
                    b.getField(curr + addition) != Mark.E) {
                    if (whichForbidden(b, c, (curr + addition)) == true) {
                        //System.out.println("I'm forbiddddeeeennnnnn summmmiiitoooo3maxxx");
                        marbles.add(curr + addition);
                        Collections.reverse(marbles);
                        return true;
                    }
                    int addition_old = addition;
                    setVal(b.row(curr + addition_old));
                    addition = commandVal(c);
                    if (b.isEmptyField(curr + addition + addition_old) == true) {
                        //System.out.println("I'm ok");
                        marbles.add(curr + addition_old);
                        return true;
                    }
                }

            } catch (Exception e) {
                //System.out.println("Fucked inside the isSumito3Max");
            }
        }
        //System.out.println("izobshto ne vlizammmmm");
        marbles.clear();
        return false;
    }

    /**
     * this method checks if a summito for 3vs1 or 3vs2 marbles is possible
     * this method works only for a 4 player game
     * game
     * @param b
     * @param c
     * @return true/false
     * @requires c != null
     */

    public boolean isSumito4Max(Board b, String c) {
        Collections.sort(marbles);
        //System.out.println("Sorted marbles " + marbles.toString());
        int use = 0;
        int curr = 0;
        if (c.equals("R")) {
            use = marbles.get(2);
        } else if (c.equals("L")) {
            use = marbles.get(0);
        } else if (c.equals("UL")) {
            use = marbles.get(0);
        } else if (c.equals("UR")) {
            use = marbles.get(0);
        } else if (c.equals("DL")) {
            use = marbles.get(2);
        } else if (c.equals("DR")) {
            use = marbles.get(2);
        }
        try {
            setVal(b.row(use));
            curr = use + commandVal(c);
            if (b.isEmptyField(curr) == true || b.getField(curr) == getMark() || b.getField(curr) == getMark().other()) {
                marbles.clear();
                return false;
            } else if (b.getField(curr) != getMark()) {
                if (whichForbidden(b, c, curr) == true) {
                    marbles.add(curr);
                    return true;
                }
                marbles.add(curr);
                use = use + commandVal(c);
                setVal(b.row(use));
                curr = use + commandVal(c);
                if (b.isEmptyField(curr) == true) {
                    return true;
                } else if (b.getField(curr) != getMark() && b.getField(curr) != getMark().other()) {
                    if (whichForbidden(b, c, curr) == true) {
                        marbles.add(curr);
                        return true;
                    }
                    marbles.add(curr);
                    use = use + commandVal(c);
                    setVal(b.row(use));
                    curr = use + commandVal(c);
                    //System.out.println("I'm the latest curr in sumito 4 " + curr);
                    if (b.isEmptyField(curr)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception for whole sumito4");
            e.printStackTrace();
        }

        marbles.clear();
        return false;
    }

    /**
     * this method checks if a move which a player wants to perform is valid
     * depending on the marbel he chooses and the direction to which he wants to move it
     * it also paints the board with the marbles which have been moved or if a summito was performed
     * @param board
     * @return true/fasle
     * @requires input != null
     * @ensures res.determineMove.equals(move) && res.determineMove != null
     */

    @Override
    public String determineMove(Board board, String input) throws NumberFormatException, IndexOutOfBoundsException {
        String res = "";
        //System.out.println("Give me your choice: ");
        array = input.split(ProtocolMessages1.LDL);
        int len = array.length;

        /*
        if (array[0].equals("skip")) {
            return "ok";
        }
         */

        if (len < 3 || len > 5) {
            //System.out.println("too short or too long baby, try again");
            return "";
        }

        command = array[len - 1];
        if (isInteger(command) == true) {
            //System.out.println("Command cannot be number");
            return "";
        }

        if (len > 3 && (command.equals("R") || command.equals("L"))) {
            int max = 0;
            if (len == 4) {
                max = 2;
            }
            if (len == 5) {
                max = 3;
            }
            for (int i = 1; i < max; i++) {
                if (board.row(Integer.parseInt(array[i])) != board.row(Integer.parseInt(array[i + 1]))) {
                    //System.out.println("Cannot do right or left on different rows");
                    return "";
                }
            }
        }
        marbles.clear();
        boolean summito = false;
        if (board.getNumberOfPlayers() == 4) {
            marblesCreation(len);

            if (board.getField(marbles.get(0)) != getMark()) {
                //System.out.println("First marble should be yours!");
                return "";
            }
            for (int i = 0; i < marbles.size(); i++) {
                if (board.getField(marbles.get(i)) == Mark.E || board.isField(marbles.get(i)) != true) {
                    //System.out.println("Cannot touch empty fields or out of bounds!");
                    return "";
                }
            }
            for (int i = 1; i < marbles.size(); i++) {
                if (board.getField(marbles.get(i)) != getMark() && board.getField(marbles.get(i)) != getMark().other()) {
                    //System.out.println("Cannot touch enemy team marbles!");
                    return "";
                }
            }
            Collections.sort(marbles);
            if (len == 5) {
                if (board.getField(marbles.get(1)) == getMark().other() && board.getField(marbles.get(2)) == getMark()) {
                    //System.out.println("Cannot have partner marble between yours!");
                    return "";
                }
            }
            marbles.clear();
            if (len == 4) {
                //System.out.println("She chekna za summito3max3");
                marblesCreation(len);
                if (isSumito3Max(board, command) == true) {
                    //System.out.println("I'm in sumito3Max");
                    summito = true;
                    //System.out.println("Marbles for recolor" + marbles.toString());
                    this.drawSummito(board, command, res);
                    marbles.clear();
                    return "ok";
                }
                marbles.clear();
            } else if (len == 5) {
                marblesCreation(len);
                //System.out.println("She chekna za summito4max");
                if (isSumito4Max(board, command) == true) {
                    //System.out.println("I'm in sumito4Max");
                    summito = true;
                    //System.out.println("Marbles for recolor" + marbles.toString());
                    this.drawSummito(board, command, res);
                    marbles.clear();
                    return "ok";
                }
                marbles.clear();
            }
            if (summito == false) {
                outmarbles.clear();
                //System.out.println("Shte mestq a 4ma");
                //System.out.println("Command in string " + command);
                marblesCreation(len);
                //System.out.println("Marbles to be used " + marbles.toString());
                Collections.sort(marbles);
                if (command.equals("UR") || command.equals("UL") || command.equals("L")) {
                    Collections.reverse(marbles);
                }
                //System.out.println("Marbles to be used " + marbles.toString());
                int last = marbles.get(marbles.size() - 1);
                int last_in_array = marbles.get(marbles.size() - 1);
                setVal(board.row(last));
                try {
                    last += commandVal(command);
                    if (whichForbidden(board, command, last_in_array) == true || board.isEmptyField(last) == false) {
                        //System.out.println("e forbidden li e?" + whichForbidden(board, command, last_in_array));
                        //System.out.println("color 5 " + board.getField(5));
                        marbles.clear();
                        //System.out.println("Ima forbidden");
                        return "";
                    }
                    Map<Integer, Mark> colors = new HashMap<>();
                    for (int i = 0; i < marbles.size(); i++) {
                        setVal(board.row(marbles.get(i)));
                        outmarbles.add(marbles.get(i) + commandVal(command));
                        colors.put(outmarbles.get(i), board.getField(marbles.get(i)));
                        colors.put(marbles.get(i), board.getField(marbles.get(i)));
                        board.setField(marbles.get(i), Mark.E);
                    }
                    for (int i = 0; i < marbles.size(); i++) {
                        if (board.getField(outmarbles.get(i)) != Mark.E) {
                            //System.out.println("Nope, new fields are not empty");
                            for (int j = 0; j < marbles.size(); j++) {
                                board.setField(marbles.get(i), colors.get(marbles.get(i)));
                            }
                            return "";
                        }
                    }
                    Map<Integer, Mark> newcolors = new HashMap<>();
                    for (int i = 0; i < marbles.size(); i++) {
                        newcolors.put(outmarbles.get(i), colors.get(marbles.get(i)));
                    }
                    //System.out.println("Let's see the result: " + outmarbles.toString());
                    marbles.clear();
                    //return res;
                    for (int i = 0; i < outmarbles.size(); i++) {
                        board.setField(outmarbles.get(i), newcolors.get(outmarbles.get(i)));
                    }
                    //System.out.println("I'm at the end of move");
                } catch (Exception e) {
                    //System.out.println("Maikata si eba v add za 4");
                }
                outmarbles.clear();
                marbles.clear();
                return "ok";
            }
        }
        if (board.getNumberOfPlayers() < 4) {
            for (int i = 1; i < len - 1; i++) {
                if (board.getField(Integer.parseInt(array[i])) != getMark() || board.isEmptyField(Integer.parseInt(array[i])) == true) {
                    //System.out.println(" purvo az Cannot touch other players marbles or empty fields");
                    return "";
                }
            }
            if (len == 4) {
                marblesCreation(len);
                if (isSumito3Standard(board, command) == true) {
                    //System.out.println("I'm in sumito for 3");
                    //System.out.println("Ready marbles for recolor " + marbles.toString());
                    summito = true;
                    drawSummito(board, command, res);
                    return "ok";
                }
                marbles.clear();
            } else if (len == 5) {
                marblesCreation(len);
                if (isSumito4Standard(board, command) == true) {
                    //System.out.println("I'm in sumito for 4");
                    //System.out.println("Marbles for recolor" + marbles.toString());
                    summito = true;

                    drawSummito(board, command, res);
                    return "ok";
                }
                marbles.clear();
            }
            if (summito == false) {
                outmarbles.clear();
                //System.out.println("Shte mestq");
                marblesCreation(len);
                //System.out.println("Command in string " + command);
                if (isInteger(command) == true) {
                    //System.out.println("Second time: Command cannot be number");
                    return "";
                }

                Collections.sort(marbles);
                if (command.equals("UR") || command.equals("UL") || command.equals("L")) {
                    Collections.reverse(marbles);
                }
                //System.out.println("Marbles to be used " + marbles.toString());
                int last = marbles.get(marbles.size() - 1);
                int last_in_array = marbles.get(marbles.size() - 1);
                setVal(board.row(last));
                try {
                    //System.out.println("Ima li forbidden pri move " + whichForbidden(board, command, last));

                    last += commandVal(command);
                    outmarbles.clear();
                    if (whichForbidden(board, command, last_in_array) == true || board.isField(last) != true || board.isEmptyField(last) == false) {
                        marbles.clear();
                        return "";
                    }
                    for (int i = 0; i < marbles.size(); i++) {
                        board.setField(marbles.get(i), Mark.E);
                    }
                    for (int i = 0; i < marbles.size(); i++) {
                        setVal(board.row(marbles.get(i)));
                        outmarbles.add(marbles.get(i) + commandVal(command));
                    }

                    for (int i = 0; i < marbles.size(); i++) {
                        setVal(board.row(marbles.get(i)));
                        if (board.getField(marbles.get(i) + commandVal(command)) != Mark.E) {
                            for (int j = 0; j < marbles.size(); j++) {
                                board.setField(marbles.get(j), getMark());
                            }
                            //System.out.println("MM no no no");
                            return "";
                        }
                    }
                    //System.out.println("Let's see the result: " + res);
                    marbles.clear();

                    for (int i = 0; i < outmarbles.size(); i++) {
                        board.setField(outmarbles.get(i), getMark());
                    }
                    //System.out.println("I'm at the end of move");
                    outmarbles.clear();
                    return "ok";
                } catch (Exception e) {
                    //System.out.println("naebah se v adda");
                }
            }
        }
        return "";
    }
}
