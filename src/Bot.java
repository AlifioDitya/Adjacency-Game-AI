import java.util.ArrayList;
import java.util.List;

// Bot abstract class
public abstract class Bot {
    public static final int BOARD_SIZE = 8;
    public static final char EMPTY = '.';
    public static final char X_PLAYER = 'X';
    public static final char O_PLAYER = 'O';

    protected char[][] board;

    public Bot() {
        board = initializeBoard();
    }

    public char[][] initializeBoard() {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[7][0] = board[7][1] = board[6][0] = board[6][1] = X_PLAYER;
        board[1][7] = board[1][6] = board[0][6] = board[0][7] = O_PLAYER;
        return board;
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    public List<int[]> getNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];
            if (isValidMove(newX, newY)) {
                neighbors.add(new int[]{newX, newY});
            }
        }
        return neighbors;
    }

    public List<int[]> getLegalMoves() {
        List<int[]> legalMoves = new ArrayList<>();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] == EMPTY) {
                    legalMoves.add(new int[]{x, y});
                }
            }
        }
        return legalMoves;
    }

    // Abstract methods
    public abstract int evaluate();

    public abstract int[] findBestMove();

    public abstract int[] move(char[][] board);
}