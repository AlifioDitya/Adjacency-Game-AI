import java.util.ArrayList;
import java.util.List;

// Local search using hill climbing with sideways move
public class LocalSearchBot extends Bot {
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

    public int[] findBestMove() {
        int[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

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
            int score = evaluate();
            // Undo the move
            board[move[0]][move[1]] = EMPTY;
            for (int[] neighbor : flipped) {
                board[neighbor[0]][neighbor[1]] = X_PLAYER;
            }

            // Allows sideways move
            if (score >= bestScore) {
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
