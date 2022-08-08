package client;

import Exceptions.ExitProgram;
import Exceptions.ServerUnavailableException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public interface ComputerAbaloneView {
    /**
     * Asks for user input continuously and handles communication accordingly using
     * the {@link #handleUserInput(String input)} method.
     * <p>
     * If an ExitProgram exception is thrown, stop asking for input, send an exit
     * message to the server according to the protocol and close the connection.
     *
     * @throws ServerUnavailableException in case of IO exceptions.
     * @throws IOException
     */
    public void startt() throws ServerUnavailableException, ExitProgram, IOException;

    /**
     * Split the user input on a ":" and handle it accordingly.
     * - If the input is valid, take the corresponding action (for example,
     * when "create:room:2" is called, send a createRoom())
     * - If the input is invalid, show a message to the user and print the help menu.
     *
     * @param input The user input.
     * @throws ExitProgram                When the user has indicated to exit the
     *                                    program.
     * @throws ServerUnavailableException if an IO error occurs in taking the
     *                                    corresponding actions.
     * @throws IOException
     */
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException, IOException;

    /**
     * Writes the given message to standard output.
     *
     * @param message the message to write to the standard output.
     */
    public void showMessage(String message);

    /**
     * Ask the user to input a valid IP. If it is not valid, show a message and ask
     * again.
     *
     * @return a valid IP
     */
    public InetAddress getIp() throws UnknownHostException;

    /**
     * Prints the question and asks the user to input a String.
     *
     * @param question The question to show to the user
     * @return The user input as a String
     */
    public String getString(String question);

    /**
     * Prints the question and asks the user to input an Integer.
     *
     * @param question The question to show to the user
     * @return The written Integer.
     */
    public int getInt(String question);

    /**
     * Prints the question and asks the user for a yes/no answer.
     *
     * @param question The question to show to the user
     * @return The user input as boolean.
     */
    public boolean getBoolean(String question);

    /**
     * Prints the help menu with available input options.
     */
    public void printHelpMenu();
}
