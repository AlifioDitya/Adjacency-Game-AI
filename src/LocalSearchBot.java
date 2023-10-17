import java.util.ArrayList;
import java.util.List;

// Local search using hill climbing with sideways move
public class LocalSearchBot extends Bot {
    private char type = X_PLAYER;
    private char opp = O_PLAYER;

    public LocalSearchBot(char type) {
        super();
        this.type = type;
        this.opp = type == X_PLAYER ? O_PLAYER : X_PLAYER;
    }

    public int evaluate() {
        int countOpp = 0;
        int countSelf = 0;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] == this.opp) {
                    countOpp++;
                } else if (board[x][y] == this.type) {
                    countSelf++;
                }
            }
        }
        return countSelf - countOpp;
    }

    public int[] findBestMove() {
        int[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

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
            int score = evaluate();
            // Undo the move
            board[move[0]][move[1]] = EMPTY;
            for (int[] neighbor : flipped) {
                board[neighbor[0]][neighbor[1]] = this.opp;
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
