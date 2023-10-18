import java.util.ArrayList;
import java.util.List;
// import java.util.Random;

public class GeneticBot extends Bot {
    // Jumlah populasi
    // private static final int POPULATION_SIZE = 10;
    private static final int MAX_DEPTH = 5;
    // Jumlah generasi
    private static final int NUM_GENERATIONS = 10;

    // Jumlah iterasi dalam Crossovers
    private static final int NUM_CROSSOVERS = 5;

    private char type = X_PLAYER;
    private char opp = O_PLAYER;

    public GeneticBot(char type) {
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

    // I. Parent Selection
    private List<int[]> parentSelection() {
        List<int[]> parents = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int bestScore = Integer.MIN_VALUE;
            int[] bestMove = null;
            for (int[] move : getLegalMoves()) {
                // Simpan papan saat ini
                char[][] originalBoard = new char[BOARD_SIZE][BOARD_SIZE];
                for (int x = 0; x < BOARD_SIZE; x++) {
                    System.arraycopy(board[x], 0, originalBoard[x], 0, BOARD_SIZE);
                }
                
                // Lakukan crossover dengan melakukan langkah di kotak kosong
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

                // Evaluasi langkah dan cari yang terbaik
                int score = minimax(MAX_DEPTH - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);

                // Cek apakah ini langkah terbaik selama ini
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

                // Kembalikan papan ke kondisi asal
                for (int[] neighbor : flipped) {
                    board[neighbor[0]][neighbor[1]] = this.opp;
                }
                for (int x = 0; x < BOARD_SIZE; x++) {
                    System.arraycopy(originalBoard[x], 0, board[x], 0, BOARD_SIZE);
                }
            }
            parents.add(bestMove);
        }
        return parents;
    }

    // II. Crossovers
    private void crossovers(List<int[]> parents) {
        for (int i = 0; i < NUM_CROSSOVERS; i++) {
            int[] parent1 = parents.get(0);
            int[] parent2 = parents.get(1);

            // Temukan LCA dari dua langkah induk
            int lcaX = -1;
            int lcaY = -1;
            for (int x = 0; x < BOARD_SIZE; x++) {
                for (int y = 0; y < BOARD_SIZE; y++) {
                    if (board[x][y] == EMPTY) {
                        List<int[]> path1 = findPathToRoot(parent1, x, y);
                        List<int[]> path2 = findPathToRoot(parent2, x, y);
                        int minLength = Math.min(path1.size(), path2.size());
                        for (int j = 0; j < minLength; j++) {
                            if (path1.get(j)[0] == path2.get(j)[0] && path1.get(j)[1] == path2.get(j)[1]) {
                                lcaX = path1.get(j)[0];
                                lcaY = path1.get(j)[1];
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            if (lcaX != -1 && lcaY != -1) {
                // Lakukan pertukaran langkah di LCA
                board[lcaX][lcaY] = this.type;
                List<int[]> neighbors = getNeighbors(lcaX, lcaY);
                List<int[]> flipped = new ArrayList<>();
                for (int[] neighbor : neighbors) {
                    int nx = neighbor[0];
                    int ny = neighbor[1];
                    if (board[nx][ny] == this.opp) {
                        board[nx][ny] = this.type;
                        flipped.add(new int[]{nx, ny});
                    }
                }
            }
        }
    }

    // III. Path Selection
    private int[] pathSelection() {
        return findBestMove();
    }

    // Temukan jalur dari langkah ke akar
    private List<int[]> findPathToRoot(int[] move, int x, int y) {
        List<int[]> path = new ArrayList<>();
        int currX = x;
        int currY = y;
        while (move[0] != currX || move[1] != currY) {
            path.add(new int[]{currX, currY});
            List<int[]> neighbors = getNeighbors(currX, currY);
            for (int[] neighbor : neighbors) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                if (board[nx][ny] == this.type) {
                    currX = nx;
                    currY = ny;
                    break;
                }
            }
        }
        path.add(new int[]{move[0], move[1]});
        return path;
    }

    // Implementasi Minimax (seperti yang Anda berikan)
    // ...

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

        // Iterasi GA-Minimax
        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            List<int[]> parents = parentSelection();
            crossovers(parents);
        }

        return pathSelection();
    }
}
