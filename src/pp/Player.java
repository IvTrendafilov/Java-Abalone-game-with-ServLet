package pp;

import java.util.Scanner;
import java.lang.NumberFormatException;

public abstract class Player {
    private String name;
    private Mark mark;

    public Player(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public abstract String determineMove(Board board,String input);

    public abstract int getScore();

    /**
     * Makes a move depending of the results which has been received from
     * determineMove
     * @param board
     * @param input
     * @return
     */

    public boolean makeMove(Board board,String input) {
        String choice = "";
        boolean answer = true;
            try{
            choice = determineMove(board,input);
            //System.out.println("Your validated choice: " + choice);
            if(choice.equals("ok")){
                answer = true;
            }else{
                answer = false;
            }
            } catch (NumberFormatException e) {
            	System.out.println("number format error");
            }catch (IndexOutOfBoundsException e) {
            	System.out.println("index oopsie");
            }
            return answer;

    }
}
