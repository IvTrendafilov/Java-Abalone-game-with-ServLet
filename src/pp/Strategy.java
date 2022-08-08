package pp;

/**
 * Interface for strategy
 * author Vladi/Ivan
 */

public interface Strategy {
    public String getName();
    public String determineMove(Board board, Mark mark);
}
