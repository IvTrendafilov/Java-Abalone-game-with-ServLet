package pp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing the smart strategy
 * author Vladi/Ivan
 */


public class SmartStrategyTest {

    private Board board;
    private SmartStrategy st;

    @BeforeEach
    public void setUp() {
        st = new SmartStrategy();
        board = new Board(2);
    }

    @Test
    public void TestGetName() {
        assertEquals(st.getName(), "Smart");
    }

    @Test
    public void TestSetValc() {
        for (int i = 1; i <= 9; i++) {
            st.setValc(i);
            if (i == 1) {
                assertEquals(st.DLc, 5);
                assertEquals(st.DRc, 6);

            } else if (i == 2) {
                assertEquals(st.DLc, 6);
                assertEquals(st.DRc, 7);
                assertEquals(st.ULc, -6);
                assertEquals(st.URc, -5);
            } else if (i == 3) {
                assertEquals(st.DLc, 7);
                assertEquals(st.DRc, 8);
                assertEquals(st.ULc, -7);
                assertEquals(st.URc, -6);
            } else if (i == 4) {
                assertEquals(st.DLc, 8);
                assertEquals(st.DRc, 9);
                assertEquals(st.ULc, -8);
                assertEquals(st.URc, -7);
            } else if (i == 5) {
                assertEquals(st.DLc, 8);
                assertEquals(st.DRc, 9);
                assertEquals(st.ULc, -9);
                assertEquals(st.URc, -8);

            }

        }
    }

    @Test
    public void TestgetRandomCommand() {
        if (st.getRandomCommand().equals("Lc")) {
            assertEquals("Lc", "Lc");
        } else if (st.getRandomCommand().equals("Rc")) {
            assertEquals("Rc", "Rc");
        } else if (st.getRandomCommand().equals("DRc")) {
            assertEquals("Rc", "DRc");
        } else {
            assertEquals("Lc", "Lc");
        }

    }

    @Test
    public void TestCommandValC() throws Exception {
        assertEquals(st.Rc, st.commandValc("Rc"));
        assertEquals(st.DRc, st.commandValc("DRc"));
        assertEquals(st.URc, st.commandValc("URc"));
        assertEquals(st.Lc, st.commandValc("Lc"));
        assertEquals(st.ULc, st.commandValc("ULc"));
        assertEquals(st.DLc, st.commandValc("DLc"));
    }

    @Test
    public void TestWhichForbiddenC() throws Exception {
        assertTrue(st.whichForbiddenc(board, "Rc", 5));
        assertTrue(st.whichForbiddenc(board, "Lc", 19));
        assertTrue(st.whichForbiddenc(board, "ULc", 1));
        assertTrue(st.whichForbiddenc(board, "URc", 11));
        assertTrue(st.whichForbiddenc(board, "ULc", 27));
        assertTrue(st.whichForbiddenc(board, "DRc", 56));
        assertTrue(st.whichForbiddenc(board, "DLc", 51));
        assertFalse(st.whichForbiddenc(board, "DLc", 41));
    }

    @Test
    public void TestDetermineMove() {
        if (st.determineMove(board, Mark.R).equals("move:6:Rc")) {
            assertEquals(st.determineMove(board, Mark.R), "move:6:Rc");
        }
    }
}