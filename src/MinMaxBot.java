import java.util.ArrayList;
import java.util.List;

public class MinMaxBot extends Bot {
    public static final int MAX_DEPTH = 5;
    private char type = X_PLAYER;
    private char opp = O_PLAYER;

    public MinMaxBot(char type) {
        super();
        this.type = type;
        this.opp = type == X_PLAYER ? O_PLAYER : X_PLAYER;
    }

    public int evaluate() {
        int countSelf = 0;
        int countOpp = 0;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] == this.type) {
                    countSelf++;
                } else if (board[x][y] == this.opp) {
                    countOpp++;
                }
            }
        }

        return countSelf - countOpp;
    }

    public int minimax(int depth, double alpha, double beta, boolean isMaximizingPlayer) {
        if (getLegalMoves().isEmpty() || depth == 0) {
            return evaluate();
        }

        if (isMaximizingPlayer) {
            double maxScore = Double.NEGATIVE_INFINITY;
            for (int[] move : getLegalMoves()) {
                // Pre move and flip the neighboring opponent's pieces
                board[move[0]][move[1]] = this.type;
                List<int[]> neighbors = getNeighbors(move[0], move[1]);
                List<int[]> flipped = new ArrayList<>();
                for (int[] neighbor : neighbors) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (board[nx][ny] == this.opp) {
                        board[nx][ny] = this.type;
                        flipped.add(new int[]{nx, ny});
                    }
                }
                double score = minimax(depth - 1, alpha, beta, false);
                // Undo the move
                board[move[0]][move[1]] = EMPTY;
                for (int[] neighbor : flipped) {
                    board[neighbor[0]][neighbor[1]] = this.opp;
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
                board[move[0]][move[1]] = this.opp;
                List<int[]> neighbors = getNeighbors(move[0], move[1]);
                List<int[]> flipped = new ArrayList<>();
                for (int[] neighbor : neighbors) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (board[nx][ny] == this.type) {
                        board[nx][ny] = this.opp;
                        flipped.add(new int[]{nx, ny});
                    }
                }
                double score = minimax(depth - 1, alpha, beta, true);
                // Undo the move
                board[move[0]][move[1]] = EMPTY;
                for (int[] neighbor : flipped) {
                    board[neighbor[0]][neighbor[1]] = this.type;
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

    public int[] findBestMove() {
        int[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (int[] move : getLegalMoves()) {
            // Make the move
            board[move[0]][move[1]] = this.type;

            // Flip the neighboring opponent's pieces
            List<int[]> neighbors = getNeighbors(move[0], move[1]);
            List<int[]> flipped = new ArrayList<>();
            for (int[] neighbor : neighbors) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                if (board[nx][ny] == this.opp) {
                    board[nx][ny] = this.type;
                    flipped.add(new int[]{nx, ny});
                }
            }

            double alpha = Double.NEGATIVE_INFINITY;
            double beta = Double.POSITIVE_INFINITY;

            // Compute the score
            int score = minimax(MAX_DEPTH - 1, alpha, beta, false);

            // Undo the move
            board[move[0]][move[1]] = EMPTY;

            // Undo the flip
            for (int[] neighbor : flipped) {
                board[neighbor[0]][neighbor[1]] = this.opp;
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
        return findBestMove();
    }
}
