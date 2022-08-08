package pp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing the human player
 * author Vladi/Ivan
 */
public class HumanPlayerTest {
    private Board board;
    private HumanPlayer humanPlayer;

    @BeforeEach
    public void setUp() {
        humanPlayer = new HumanPlayer("Ivan", Mark.R);
        board = new Board();
    }

    @Test
    public void TestSetVal() {
        for (int i = 1; i <= 9; i++) {
            humanPlayer.setVal(i);
            if (i == 1) {
                assertEquals(humanPlayer.DL, 5);
                assertEquals(humanPlayer.DR, 6);

            } else if (i == 2) {
                assertEquals(humanPlayer.DL, 6);
                assertEquals(humanPlayer.DR, 7);
                assertEquals(humanPlayer.UL, -6);
                assertEquals(humanPlayer.UR, -5);
            }

        }

    }

    @Test
    public void TestGetScore() {
        assertEquals(humanPlayer.getScore(), 0);
    }

    @Test
    public void TestIncrementScore() {
        humanPlayer.score = 0;
        humanPlayer.incrementScore();
        assertEquals(1, humanPlayer.score);
    }

    @Test
    public void TestIsInteger() {
        assertTrue(HumanPlayer.isInteger("5"));
        assertFalse(HumanPlayer.isInteger("s"));
    }

    //tva e mnogo izchiiteno ama nishto sushtoto shte vaji za vsqko edno sumito
    //shoto ne znam kak nie shte im dadem input :D
    @Test
    public void TestIsSumito3Stan() {
        board = new Board(2);
        assertFalse(humanPlayer.isSumito3Standard(board, "UD"));
    }

    @Test
    public void TestIsSumito4Stan() {
        board = new Board(3);
        assertFalse(humanPlayer.isSumito4Standard(board, "ml"));
    }

    @Test
    public void TestIsSumito4Max() {
        board = new Board(3);
        humanPlayer.marbles.add(1);
        assertFalse(humanPlayer.isSumito4Max(board, "kz"));
    }

    @Test
    public void TestCommandVal() throws Exception {
        assertEquals(humanPlayer.R, humanPlayer.commandVal("R"));
        assertEquals(humanPlayer.DR, humanPlayer.commandVal("DR"));
        assertEquals(humanPlayer.UR, humanPlayer.commandVal("UR"));
        assertEquals(humanPlayer.L, humanPlayer.commandVal("L"));
        assertEquals(humanPlayer.UL, humanPlayer.commandVal("UL"));
        assertEquals(humanPlayer.DL, humanPlayer.commandVal("DL"));
    }

    @Test
    public void TestWhichForbiden() throws Exception {
        assertTrue(humanPlayer.whichForbidden(board, "R", 5));
        assertTrue(humanPlayer.whichForbidden(board, "L", 19));
        assertTrue(humanPlayer.whichForbidden(board, "UL", 1));
        assertTrue(humanPlayer.whichForbidden(board, "UR", 11));
        assertTrue(humanPlayer.whichForbidden(board, "UL", 27));
        assertTrue(humanPlayer.whichForbidden(board, "DR", 56));
        assertTrue(humanPlayer.whichForbidden(board, "DL", 51));
        assertFalse(humanPlayer.whichForbidden(board, "DL", 41));
    }
}
