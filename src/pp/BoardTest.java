package pp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class for testing the board
 * author Ivan/Vladi
 */

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }


    @Test
    public void TestEmptyBoard() {
        for (int i = 1; i <= 61; i++) {
            board.setField(i, Mark.R);

        }
        board.emptyBoard();
        for (int i = 1; i <= 61; i++) {
            assertEquals(Mark.E, board.getField(i));
        }

    }


    @Test
    public void testIsFieldIndex() {
        assertFalse(board.isField(-1));
        assertFalse(board.isField(0));
        assertTrue(board.isField(8));
        assertTrue(board.isField(9));
    }


    @Test
    public void testSetAndGetFieldIndex() {
        board.setField(31, Mark.R);
        assertEquals(Mark.R, board.getField(31));
        assertEquals(Mark.E, board.getField(32));
    }

    @Test
    public void testReset() {
        board.reset();
        assertEquals(Mark.E, board.getField(1));
        assertEquals(Mark.E, board.getField(31));
    }

    @Test
    public void testIsEmptyFieldIndex() {
        board.setField(31, Mark.R);
        assertFalse(board.isEmptyField(31));
        assertTrue(board.isEmptyField(32));
    }


    @Test
    public void testSetup() {
        assertEquals(Mark.E, board.getField(1));
        assertEquals(Mark.E, board.getField(42));
    }

    @Test
    public void testSetup1() {
        board = new Board(2);
        assertEquals(Mark.R, board.getField(1));
        assertEquals(Mark.R, board.getField(16));
        assertEquals(Mark.Y, board.getField(46));
        assertEquals(Mark.Y, board.getField(61));
        assertEquals(Mark.E, board.getField(31));
        assertEquals(Mark.E, board.getField(41));
    }

    @Test
    public void testSetup2() {
        board = new Board(3);
        assertEquals(Mark.R, board.getField(1));
        assertEquals(Mark.R, board.getField(36));
        assertEquals(Mark.Y, board.getField(10));
        assertEquals(Mark.Y, board.getField(43));
        assertEquals(Mark.G, board.getField(51));
        assertEquals(Mark.G, board.getField(61));
        assertEquals(Mark.E, board.getField(41));
        assertEquals(Mark.E, board.getField(3));
    }

    @Test
    public void testSetup3() {
        board = new Board(4);
        assertEquals(Mark.R, board.getField(1));
        assertEquals(Mark.R, board.getField(15));
        assertEquals(Mark.Y, board.getField(11));
        assertEquals(Mark.Y, board.getField(33));
        assertEquals(Mark.G, board.getField(47));
        assertEquals(Mark.G, board.getField(61));
        assertEquals(Mark.P, board.getField(29));
        assertEquals(Mark.P, board.getField(51));
        assertEquals(Mark.E, board.getField(39));
        assertEquals(Mark.E, board.getField(5));
    }

    @Test
    public void TestIsForbidenRight() {
        assertTrue(board.isForbiddenRight(5));
        assertTrue(board.isForbiddenRight(43));
        assertFalse(board.isForbiddenRight(1));
        assertFalse(board.isForbiddenRight(27));
    }

    @Test
    public void TestIsForbidenLeft() {
        assertTrue(board.isForbiddenLeft(1));
        assertTrue(board.isForbiddenLeft(6));
        assertFalse(board.isForbiddenLeft(5));
        assertFalse(board.isForbiddenLeft(43));
    }

    @Test
    public void TestIsForbidenUpLeft() {
        assertTrue(board.isForbiddenUpLeft(1));
        assertTrue(board.isForbiddenUpLeft(27));
        assertFalse(board.isForbiddenUpLeft(13));
        assertFalse(board.isForbiddenUpLeft(30));
    }

    @Test
    public void TestIsForbidenUpRight() {
        assertTrue(board.isForbiddenUpRight(4));
        assertTrue(board.isForbiddenUpRight(11));
        assertFalse(board.isForbiddenUpRight(15));
        assertFalse(board.isForbiddenUpRight(48));
    }

    @Test
    public void TestIsForbidenDownRight() {
        assertTrue(board.isForbiddenDownRight(57));
        assertTrue(board.isForbiddenDownRight(43));
        assertFalse(board.isForbiddenDownRight(14));
        assertFalse(board.isForbiddenDownRight(24));
    }

    @Test
    public void TestIsForbidenDownLeft() {
        assertTrue(board.isForbiddenDownLeft(60));
        assertTrue(board.isForbiddenDownLeft(36));
        assertFalse(board.isForbiddenDownLeft(38));
        assertFalse(board.isForbiddenDownLeft(5));
    }

    @Test
    public void TestRow() {
        int row = board.row(0);
        int row1 = board.row(5);
        int row2 = board.row(9);
        int row3 = board.row(17);
        int row4 = board.row(24);
        int row5 = board.row(32);
        int row6 = board.row(40);
        int row7 = board.row(47);
        int row8 = board.row(53);
        int row9 = board.row(59);

        assertEquals(0, row);
        assertEquals(1, row1);
        assertEquals(2, row2);
        assertEquals(3, row3);
        assertEquals(4, row4);
        assertEquals(5, row5);
        assertEquals(6, row6);
        assertEquals(7, row7);
        assertEquals(8, row8);
        assertEquals(9, row9);
    }

    @Test
    public void TestGameOver() {
        board.moves = 1;
        assertFalse(board.gameOver());
        board.incrementMoves();
        assertFalse(board.gameOver());
    }

    @Test
    public void TestIncrementMoves() {
        board.moves = 1;
        board.incrementMoves();
        assertEquals(2, board.moves);

    }
    @Test
    public void TestToString(){
        this.board = new Board(2);
        assertEquals(board.toString(),board.toString());
    }

}
