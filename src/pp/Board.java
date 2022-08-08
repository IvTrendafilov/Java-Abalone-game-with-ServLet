package pp;

/**
 * Class for the board
 * author Ivan/Vladi
 */

public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    /**
     * These arrays we use to make the boudaries of the board
     * from which onwards a player can not move a marble in its respective direction
     */
    public static final int[] ForbiddenRight = {5, 11, 18, 26, 35, 43, 50, 56, 61};
    public static final int[] ForbiddenLeft = {1, 6, 12, 19, 27, 36, 44, 51, 57};
    public static final int[] ForbiddenUpLeft = {1, 2, 3, 4, 5, 6, 12, 19, 27};
    public static final int[] ForbiddenUpRight = {1, 2, 3, 4, 5, 11, 18, 26, 35};
    public static final int[] ForbiddenDownLeft = {57, 58, 59, 60, 61, 27, 36, 44, 51};
    public static final int[] ForbiddenDownRight = {57, 58, 59, 60, 61, 35, 43, 50, 56};
    public static final int MAX_NUMBER_OF_MOVES = 96;

    private Mark fields[];
    private int number_of_players;
    protected int moves;
    protected String res;

    /**
     * This board creates an array which is
     * used for the board and sets all the
     * fields in the board to an empty state
     */
    public Board() {
        fields = new Mark[66];
        for (int i = 1; i <= 61; i++) {
            setField(i, Mark.E);
        }
    }

    /**
     * From here we choose the design of the
     * board depending on the number of players
     * @param num
     * @require num >=2 && num <= 4;
     */
    public Board(int num) {
        fields = new Mark[66];
        this.number_of_players = num;
        this.moves = 0;
        emptyBoard();
        if (num == 2) {
            build2();
        }
        if (num == 3) {
            build3();
        }
        if (num == 4) {
            build4();
        }
    }


    public void setField(int p, Mark m) {
        this.fields[p] = m;
    }

    /**
     * This build creates the layout of the board for 2 players
     */
    public void build2() {
        for (int i = 1; i <= 11; i++) {
            setField(i, Mark.R);
        }
        for (int i = 14; i <= 16; i++) {
            setField(i, Mark.R);
        }
        for (int i = 51; i <= 61; i++) {
            setField(i, Mark.Y);
        }
        for (int i = 46; i <= 48; i++) {
            setField(i, Mark.Y);
        }
        this.moves = 0;
    }

    /**
     * This build creates the layout of the board for 3 players
     */
    public void build3() {
        int k = -1;
        for (int i = 1; i <= 27; i += 5 + k) {
            setField(i, Mark.R);
            setField(i + 1, Mark.R);
            k++;
        }
        setField(36, Mark.R);

        k = -1;
        for (int i = 5; i <= 35; i += 6 + k) {
            setField(i, Mark.Y);
            setField(i - 1, Mark.Y);
            k++;
        }
        setField(43, Mark.Y);

        for (int i = 51; i <= 61; i++) {
            setField(i, Mark.G);
        }
        this.moves = 0;
    }

    /**
     * This build creates the layout of the board for 4 players
     */

    public void build4() {
        for (int i = 1; i <= 15; i++) {
            if (i == 5) {
                i = 7;
            }
            if (i == 10) {
                i = 14;
            }
            setField(i, Mark.R);
        }

        for (int i = 11; i <= 35; i++) {
            if (i == 12) {
                i = 17;
            }
            if (i == 19) {
                i = 24;
            }
            if (i == 27) {
                i = 33;
            }
            setField(i, Mark.Y);
        }

        for (int i = 61; i >= 47; i--) {
            if (i == 57) {
                i = 55;
            }
            if (i == 52) {
                i = 48;
            }
            setField(i, Mark.G);
        }

        for (int i = 27; i <= 51; i++) {
            if (i == 30) {
                i = 36;
            }
            if (i == 39) {
                i = 44;
            }
            if (i == 46) {
                i = 51;
            }
            setField(i, Mark.P);
        }
        this.moves = 0;
    }

    /**
     * emptyboard empties all the fields on our board
     */
    public void emptyBoard() {
        for (int i = 1; i <= 61; i++) {
            setField(i, Mark.E);
        }
    }

    /**
     * This method visualizes our board
     *
     * @return res
     * @ensure res.toString.eqauals(res) &&  res.toString != null
     */
    public String toString() {
        String res = "";
        int start = 0;
        int end = 0;
        for (int j = 10; j >= 2; j -= 2) {
            for (int i = j; i >= 0; i -= 2) {
                if (i == 10) {
                    System.out.print(" ");
                } else if (i == 8 || i == 6 || i == 4 || i == 2) {
                    System.out.print("  ");
                } else {
                    System.out.print(" ");
                }
            }
            if (j == 10) {
                start = 1;
                end = 5;
            }
            if (j == 8) {
                start = 6;
                end = 11;
            }
            if (j == 6) {
                start = 12;
                end = 18;
            }
            if (j == 4) {
                start = 19;
                end = 26;
            }
            if (j == 2) {
                start = 27;
                end = 35;
            }
            for (int k = start; k <= end; k++) {
                if ((k >= 7 && k <= 9) || (k >= 2 && k <= 5)) {
                    if (fields[k] == Mark.R) {
                        System.out.print(" | " + ANSI_RED_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.P) {
                        System.out.print(" | " + ANSI_PURPLE_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.G) {
                        System.out.print(" | " + ANSI_GREEN_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.Y) {
                        System.out.print(" | " + ANSI_YELLOW_BACKGROUND + k + "" + ANSI_RESET);
                    } else {
                        System.out.print(" | " + k + "");
                    }
                } else if (k == 1 || k == 6 || k == 12 || k == 19 || k == 27) {
                    if (fields[k] == Mark.R) {
                        System.out.print(" /" + ANSI_RED_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.P) {
                        System.out.print(" /" + ANSI_PURPLE_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.G) {
                        System.out.print(" /" + ANSI_GREEN_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.Y) {
                        System.out.print(" /" + ANSI_YELLOW_BACKGROUND + k + "" + ANSI_RESET);
                    } else {
                        System.out.print(" /" + k + "");
                    }

                } else {
                    if (fields[k] == Mark.P) {
                        System.out.print("| " + ANSI_PURPLE_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.R) {
                        System.out.print("| " + ANSI_RED_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.G) {
                        System.out.print("| " + ANSI_GREEN_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.Y) {
                        System.out.print("| " + ANSI_YELLOW_BACKGROUND + k + "" + ANSI_RESET);
                    } else {
                        System.out.print("| " + k + "");
                    }
                }
            }
            System.out.print("\\");
            System.out.print("\n");
        }
        for (int j = 4; j <= 10; j += 2) {
            for (int i = 0; i <= j; i++) {
                System.out.print(" ");
            }
            if (j == 10) {
                start = 57;
                end = 61;
            }
            if (j == 8) {
                start = 51;
                end = 56;
            }
            if (j == 6) {
                start = 44;
                end = 50;
            }
            if (j == 4) {
                start = 36;
                end = 43;
            }
            for (int k = start; k <= end; k++) {
                if (k == 36 || k == 44 || k == 51 || k == 57) {
                    if (fields[k] == Mark.P) {
                        System.out.print("\\ " + ANSI_PURPLE_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.R) {
                        System.out.print("\\ " + ANSI_RED_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.G) {
                        System.out.print("\\ " + ANSI_GREEN_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.Y) {
                        System.out.print("\\ " + ANSI_YELLOW_BACKGROUND + k + "" + ANSI_RESET);
                    } else {
                        System.out.print("\\ " + k + "");
                    }
                } else {
                    if (fields[k] == Mark.P) {
                        System.out.print("| " + ANSI_PURPLE_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.R) {
                        System.out.print("| " + ANSI_RED_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.G) {
                        System.out.print("| " + ANSI_GREEN_BACKGROUND + k + "" + ANSI_RESET);
                    } else if (fields[k] == Mark.Y) {
                        System.out.print("| " + ANSI_YELLOW_BACKGROUND + k + "" + ANSI_RESET);
                    } else {
                        System.out.print("| " + k + "");
                    }
                }
            }
            System.out.print("  /");
            System.out.print("\n");
        }
        return res;
    }

    /**
     * from here we get the number of players which
     * we use for the creation of the board
     *
     * @return a number which is >=2 && <= 4
     * @requires number_of_players > 0
     * @ensures number_of_players >=2 && number_of_players <= 4
     */
    public int getNumberOfPlayers() {
        return this.number_of_players;
    }

    /**
     * this methods resets the board
     */
    public void reset() {
        this.moves = 0;
        if (this.getNumberOfPlayers() == 2) {
            emptyBoard();
            build2();
        }
        if (this.getNumberOfPlayers() == 3) {
            emptyBoard();
            build3();
        }
        if (this.getNumberOfPlayers() == 4) {
            emptyBoard();
            build4();
        }
    }

    /**
     * isEmptyField checks if a given position
     * on the board is empty or not
     * @param p
     * @return true||false
     * @requires p > 0
     * @ensures if (p >=1 && p <= 61) && p == Mark.E return true else false
     */
    public boolean isEmptyField(int p) {
        if (fields[p] == Mark.E) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * isField checks if a given position
     * on the board is the board or not
     *
     * @param p
     * @return true||false
     * @requires p > 0
     * @ensures  if(p >=1 && p <= 61) return true else false
     */
    public boolean isField(int p) {
        if (p >= 1 && p <= 61) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * gameOver checks if the number of moves which have been played
     * by all players is equal to 96 or if there is a game winner
     *
     * @return true/false
     * @requires moves >=0
     * @ensures if(moves == 96) return true else false
     */
    public boolean gameOver() {
        if (moves == MAX_NUMBER_OF_MOVES) {
            return true;
        }

        return false;
    }

    public void incrementMoves() {
        this.moves++;
    }

    /**
     * used to check if the coordinate that is given by the player
     * is on the boundaries of the board if it is it returns true
     *
     * @param coordinate must be on ForbiddenRight
     * @return true/false
     * @requires coordinate > 0
     * @ensures if(coordinate == ForbiddenRight) return true else false
     */
    protected boolean isForbiddenRight(int coordinate) {
        for (int i = 0; i < ForbiddenRight.length; i++) {
            if (ForbiddenRight[i] == coordinate) {
                return true;
            }
        }

        return false;
    }

    /**
     * used to check if the coordinate that is given by the player
     * is on the boundaries of the board if it is it returns true
     *
     * @param coordinate must be on ForbiddenLeft
     * @return true/false
     * @requires coordinate > 0
     * @ensures if(coordinate == ForbiddenLeft) return true else false
     */

    protected boolean isForbiddenLeft(int coordinate) {
        for (int i = 0; i < ForbiddenLeft.length; i++) {
            if (ForbiddenLeft[i] == coordinate) {
                return true;
            }
        }
        return false;
    }

    /**
     * used to check if the coordinate that is given by the player
     * is on the boundaries of the board if it is it returns true
     *
     * @param coordinate must be on ForbiddenUpLeft
     * @return true/false
     * @requires coordinate > 0
     * @ensures if(coordinate == ForbiddenUpLeft) return true else false
     */
    protected boolean isForbiddenUpLeft(int coordinate) {
        for (int i = 0; i < ForbiddenUpLeft.length; i++) {
            if (ForbiddenUpLeft[i] == coordinate) {
                return true;
            }
        }
        return false;
    }

    /**
     * used to check if the coordinate that is given by the player
     * is on the boundaries of the board if it is it returns true
     *
     * @param coordinate must be on ForbiddenUPRight
     * @return true/false
     * @requires coordinate > 0
     * @ensures if(coordinate == ForbiddenUpRight) return true else false
     */

    protected boolean isForbiddenUpRight(int coordinate) {
        for (int i = 0; i < ForbiddenUpRight.length; i++) {
            if (ForbiddenUpRight[i] == coordinate) {
                return true;
            }
        }
        return false;
    }

    /**
     * used to check if the coordinate that is given by the player
     * is on the boundaries of the board if it is it returns true
     *
     * @param coordinate must be on ForbiddenDownLeft
     * @return true/false
     * @requires coordinate > 0
     * @ensures if(coordinate == ForbiddenDownLeft) return true else false
     */

    protected boolean isForbiddenDownLeft(int coordinate) {
        for (int i = 0; i < ForbiddenDownLeft.length; i++) {
            if (ForbiddenDownLeft[i] == coordinate) {
                return true;
            }
        }
        return false;
    }

    /**
     * used to check if the coordinate that is given by the player
     * is on the boundaries of the board if it is it returns true
     *
     * @param coordinate must be on ForbiddenDownLeft
     * @return true/false
     * @requires coordinate > 0
     * @ensures if(coordinate == ForbiddenDownRight) return true else false
     */

    protected boolean isForbiddenDownRight(int coordinate) {
        for (int i = 0; i < ForbiddenDownRight.length; i++) {
            if (ForbiddenDownRight[i] == coordinate) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method finds the row of the marble
     *
     * @param marbell >= 1 && <= 9
     * @return row
     * @requires marbell > 0
     * @ensures row > 0
     */
    public int row(int marbell) {
        int row = 0;
        if (marbell >= 1 && marbell <= 5) {
            return row = 1;
        } else if (marbell >= 6 && marbell <= 11) {
            return row = 2;
        } else if (marbell >= 12 && marbell <= 18) {
            return row = 3;
        } else if (marbell >= 19 && marbell <= 26) {
            return row = 4;
        } else if (marbell >= 27 && marbell <= 35) {
            return row = 5;
        } else if (marbell >= 36 && marbell <= 43) {
            return row = 6;
        } else if (marbell >= 44 && marbell <= 50) {
            return row = 7;
        } else if (marbell >= 51 && marbell <= 56) {
            return row = 8;
        } else if (marbell >= 57 && marbell <= 61) {
            return row = 9;
        }
        return row;
    }

    /**
     * this method return the position on the board
     * of a given marble
     *
     * @param position
     * @return
     * @requires position > 0
     */

    public Mark getField(int position) {
        return fields[position];
    }

}
