package tictactoe.model;

import java.io.Serializable;

/**
 * 3x3 board for Tic-Tac-Toe. Uses 'X' for player, 'O' for AI, ' ' for empty.
 */
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;
    private char[][] cells;

    public Board() {
        cells = new char[3][3];
        reset();
    }

    public void reset() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                cells[r][c] = ' ';
    }

    public char getCell(int row, int col) {
        return cells[row][col];
    }

    public void setCell(int row, int col, char value) {
        cells[row][col] = value;
    }

    public boolean isFull() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (cells[r][c] == ' ')
                    return false;
        return true;
    }

    public boolean hasWinner(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (cells[i][0] == symbol && cells[i][1] == symbol && cells[i][2] == symbol)
                return true;
            if (cells[0][i] == symbol && cells[1][i] == symbol && cells[2][i] == symbol)
                return true;
        }
        if (cells[0][0] == symbol && cells[1][1] == symbol && cells[2][2] == symbol)
            return true;
        if (cells[0][2] == symbol && cells[1][1] == symbol && cells[2][0] == symbol)
            return true;
        return false;
    }
}
