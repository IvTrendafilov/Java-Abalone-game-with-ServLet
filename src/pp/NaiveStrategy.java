package pp;

/**
 * Class for a naive strategy
 * author Vladi/Ivan
 */

public class NaiveStrategy implements Strategy {
    private String name;

    public NaiveStrategy() {
        this.name = "Naive";
    }

    @Override
    public String getName() {
        return this.name;
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
     * makes a random move depending of the marble that is choosen
     * the method checks if that move is possible and returns ok if it is
     * @param board
     * @param mark
     * @return
     */

    @Override
    public String determineMove(Board board, Mark mark) {
        int position;
        if (mark == Mark.R) {
            int i;
            for(i = 12 ; i <=18 ; i++){
                if(board.isEmptyField(i)==false || board.getField(i)!=mark){
                    break;
                }
            }
            int tries = 0;
            //System.out.println("i="+ i);
            if(i == 18){
                return "ok";
            }
            boolean ready = false;
            while (ready == false) {
                position = randomWithRangeCP(12, 18);
                if (board.getField(position) == Mark.R) {
                    if (position >= 13 && position <= 17) {
                        if (board.isEmptyField(position + 1)) {
                            board.setField(position, Mark.E);
                            board.setField(position + 1, Mark.R);
                            ready = true;
                            return "ok";
                        } else if (board.isEmptyField(position - 1)) {
                            board.setField(position, Mark.E);
                            board.setField(position - 1, Mark.R);
                            ready = true;
                            return "ok";
                        }
                    } else if (position == 12) {
                        if (board.isEmptyField(position + 1)) {
                            board.setField(position, Mark.E);
                            board.setField(position + 1, Mark.R);
                            ready = true;
                            return "ok";
                        }
                    } else if (position == 18) {
                        if (board.isEmptyField(position - 1)) {
                            board.setField(position, Mark.E);
                            board.setField(position - 1, Mark.R);
                            ready = true;
                            return "ok";
                        }
                    }
                }
                tries++;
                if(tries == 2000){
                    System.out.println("I'm skipping");
                    ready = true;
                    return "ok";
                }
            }
        }
        return "ok";
    }

}
