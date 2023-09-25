import java.util.ArrayList;
import java.util.List;

public class Bot {
    public static final int BOARD_SIZE = 8;
    public static final char EMPTY = '.';
    public static final char X_PLAYER = 'X';
    public static final char O_PLAYER = 'O';
    public static final int MAX_DEPTH = 4;

    private char[][] board;

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

    public int evaluate() {
        int xCount = 0;
        int oCount = 0;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] == X_PLAYER) {
                    xCount++;
                } else if (board[x][y] == O_PLAYER) {
                    oCount++;
                }
            }
        }
        return oCount - xCount;
    }

    public int minimax(int depth, double alpha, double beta, boolean isMaximizingPlayer) {
        if (getLegalMoves().isEmpty() || depth == 0) {
            return evaluate();
        }

        if (isMaximizingPlayer) {
            double maxScore = Double.NEGATIVE_INFINITY;
            for (int[] move : getLegalMoves()) {
                // Pre move and flip the neighboring opponent's pieces
                board[move[0]][move[1]] = O_PLAYER;
                List<int[]> neighbors = getNeighbors(move[0], move[1]);
                List<int[]> flipped = new ArrayList<>();
                for (int[] neighbor : neighbors) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (board[nx][ny] == X_PLAYER) {
                        board[nx][ny] = O_PLAYER;
                        flipped.add(new int[]{nx, ny});
                    }
                }
                double score = minimax(depth - 1, alpha, beta, false);
                // Undo the move
                board[move[0]][move[1]] = EMPTY;
                for (int[] neighbor : flipped) {
                    board[neighbor[0]][neighbor[1]] = X_PLAYER;
                }
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, maxScore);
                if (beta <= alpha) {
                    return (int) maxScore;
                }
            }
            return (int) maxScore;
        } else {
            double minScore = Double.POSITIVE_INFINITY;
            for (int[] move : getLegalMoves()) {
                // Pre move and flip the neighboring opponent's pieces
                board[move[0]][move[1]] = X_PLAYER;
                List<int[]> neighbors = getNeighbors(move[0], move[1]);
                List<int[]> flipped = new ArrayList<>();
                for (int[] neighbor : neighbors) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (board[nx][ny] == O_PLAYER) {
                        board[nx][ny] = X_PLAYER;
                        flipped.add(new int[]{nx, ny});
                    }
                }
                double score = minimax(depth - 1, alpha, beta, true);
                // Undo the move
                board[move[0]][move[1]] = EMPTY;
                for (int[] neighbor : flipped) {
                    board[neighbor[0]][neighbor[1]] = O_PLAYER;
                }
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, minScore);
                if (beta <= alpha) {
                    return (int) minScore;
                }
            }
            return (int) minScore;
        }
    }

    public int[] findBestMove(int depth) {
        int[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (int[] move : getLegalMoves()) {
            // Make the move
            board[move[0]][move[1]] = O_PLAYER;

            // Flip the neighboring opponent's pieces
            List<int[]> neighbors = getNeighbors(move[0], move[1]);
            List<int[]> flipped = new ArrayList<>();
            for (int[] neighbor : neighbors) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                if (board[nx][ny] == X_PLAYER) {
                    board[nx][ny] = O_PLAYER;
                    flipped.add(new int[]{nx, ny});
                }
            }

            double alpha = Double.NEGATIVE_INFINITY;
            double beta = Double.POSITIVE_INFINITY;

            // Compute the score
            int score = minimax(depth - 1, alpha, beta, false);

            // Undo the move
            board[move[0]][move[1]] = EMPTY;

            // Undo the flip
            for (int[] neighbor : flipped) {
                board[neighbor[0]][neighbor[1]] = X_PLAYER;
            }

            // Update the best score and best move
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public int[] move(char[][] board) {
        this.board = board;
        return findBestMove(MAX_DEPTH);
    }

    // Random move
    public int[] moveRandom(char[][] board) {
        this.board = board;
        return getLegalMoves().get((int) (Math.random() * getLegalMoves().size()));
    }
}
