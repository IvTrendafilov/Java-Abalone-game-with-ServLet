package pp;

import protocol.ProtocolMessages1;

/**
 * Class for a smart strategy
 * author Vladi/Ivan
 */

public class SmartStrategy implements Strategy {
    private String name;
    protected final int Rc = 1;
    protected final int Lc = -1;
    protected int DLc;
    protected int DRc;
    protected int ULc;
    protected int URc;

    public SmartStrategy() {
        this.name = "Smart";
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * This method looks at which roll we are and sets the values
     * of our constants so that we can move marbles in different directions
     *
     * @param r
     */

    public void setValc(int r) {
        if (r == 1) {
            this.DLc = 5;
            this.DRc = 6;
        }
        if (r == 2) {
            this.DLc = 6;
            this.DRc = 7;
            this.ULc = -6;
            this.URc = -5;
        }
        if (r == 3) {
            this.DLc = 7;
            this.DRc = 8;
            this.ULc = -7;
            this.URc = -6;
        }
        if (r == 4) {
            this.DLc = 8;
            this.DRc = 9;
            this.ULc = -8;
            this.URc = -7;
        }
        if (r == 5) {
            this.DLc = 8;
            this.DRc = 9;
            this.ULc = -9;
            this.URc = -8;
        }
        if (r == 6) {
            this.DLc = 7;
            this.DRc = 8;
            this.ULc = -9;
            this.URc = -8;
        }
        if (r == 7) {
            this.DLc = 6;
            this.DRc = 7;
            this.ULc = -8;
            this.URc = -7;
        }
        if (r == 8) {
            this.DLc = 5;
            this.DRc = 6;
            this.ULc = -7;
            this.URc = -6;
        }
        if (r == 9) {
            this.ULc = -6;
            this.URc = -5;
        }


    }

    /**
     * This method returns a random command which will be used
     * later for making a move
     *
     * @return
     */

    public String getRandomCommand() {
        int p = randomWithRangeCP(1, 6);

        if (p == 1) {
            return "Lc";
        } else if (p == 2) {
            return "Rc";
        } else if (p == 3) {
            return "ULc";
        } else if (p == 4) {
            return "URc";
        } else if (p == 5) {
            return "DLc";
        } else if (p == 6) {
            return "DRc";
        }
        return "Lc";
    }

    /**
     * this method return a random number in the given
     * min< int i < max
     *
     * @param min
     * @param max
     * @return
     */

    public static int randomWithRangeCP(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    /**
     * This method returns the value of how much positions the marble
     * need to be moved depending of the direction to which the player
     * wants to move it
     *
     * @param c
     * @return
     * @throws Exception
     */

    public int commandValc(String c) throws Exception {
        boolean pass = false;
        if (c.equals("Rc")) {
            return Rc;
        } else if (c.equals("Lc")) {
            return Lc;
        } else if (c.equals("ULc")) {
            return ULc;
        } else if (c.equals("URc")) {
            return URc;
        } else if (c.equals("DLc")) {
            return DLc;
        } else if (c.equals("DRc")) {
            return DRc;
        }
        if (pass == false) {
            System.out.println("wtf v commandvalc");
            throw new Exception("invalid command");
        }
        return 0;
    }

    /**
     * this method checks the possition of a marbles and
     * the direction to which a players wants to move the marble
     * and return false if the player wants to move the marble to a
     * possition which is invalid
     *
     * @param b
     * @param c
     * @param p
     * @return true/false
     * @throws Exception
     */

    public boolean whichForbiddenc(Board b, String c, int p) throws Exception {
        boolean pass = false;
        if (c.equals("Rc")) {
            return b.isForbiddenRight(p);
        } else if (c.equals("Lc")) {
            return b.isForbiddenLeft(p);
        } else if (c.equals("ULc")) {
            return b.isForbiddenUpLeft(p);
        } else if (c.equals("URc")) {
            return b.isForbiddenUpRight(p);
        } else if (c.equals("DLc")) {
            return b.isForbiddenDownLeft(p);
        } else if (c.equals("DRc")) {
            return b.isForbiddenDownRight(p);
        }

        if (pass == false) {
            System.out.println("I wont print forbidden");
            throw new Exception("invalid command");
        }
        return false;
    }

    /**
     * This method determines a move randomly
     * using random commands and random positions of marbles
     * it also checks if the move which the smart strategy
     * wants to performe is possible
     * @param board
     * @param mark
     * @return
     */

    @Override
    public String determineMove(Board board, Mark mark) {
        int tries = 0;
        boolean ready = false;
        int marb;
        String c = "";
        String o = "";
        try {
            while (ready == false) {
                marb = randomWithRangeCP(1, 61);
                for (int i = 0; i < 300; i++) {
                    c = getRandomCommand();
                    setValc(board.row(marb));
                    if (whichForbiddenc(board, c, marb) == false) {
                        if (board.isEmptyField(marb + commandValc(c)) && board.getField(marb) == mark) {
                            ready = true;
                            o += ProtocolMessages1.MOVE + ProtocolMessages1.LDL + marb + ProtocolMessages1.LDL + c;
                            return o;

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" ");
        }
        return "I can't help";
    }
}
