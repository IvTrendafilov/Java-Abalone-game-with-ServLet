package Server;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Abalone Server TUI for user input and user messages
 *
 * @author Vladi/Ivan
 */
public class AbaloneServerTUI implements AbaloneServerView {

    /** The PrintWriter to write messages to */
    private PrintWriter console;

    private Scanner sc;

    /**
     * Constructs a new AbaloneServerTUI. Initializes the console.
     */
    public AbaloneServerTUI() {
        console = new PrintWriter(System.out, true);
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
    public void showMessage(String message) {
        console.println(message);
    }

    @Override
    public String getString(String question) {
        System.out.println(question );
        sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
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

}
