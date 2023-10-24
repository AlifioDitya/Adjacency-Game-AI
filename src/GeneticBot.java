import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticBot extends Bot {
    public static final int POPULATION_SIZE = 10;
    public static final int MAX_GENERATIONS = 20;
    public static final double MUTATION_PROBABILITY = 0.2;

    private char type = X_PLAYER;
    private char opp = O_PLAYER;

    public GeneticBot(char type) {
        super();
        this.type = type;
        this.opp = type == X_PLAYER ? O_PLAYER : X_PLAYER;
    }

    // Step I: Parent Selection
    public List<int[]> parentSelection() {
        List<int[]> parents = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            int bestScore = Integer.MIN_VALUE;
            int[] bestMove = null;

            for (int j = 0; j < POPULATION_SIZE; j++) {
                int[] move = findBestMove();
                int score = evaluate();
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                board = initializeBoard();
            }

            parents.add(bestMove);
        }

        return parents;
    }

    // Step II: Crossovers
    public List<int[]> crossovers(List<int[]> parents) {
        List<int[]> children = new ArrayList<>();
        int[] parent1 = parents.get(0);
        int[] parent2 = parents.get(1);

        // Randomly choose a crossover point
        int crossoverPoint = new Random().nextInt(parent1.length);

        // Create two children by combining the parents
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent1.length];

        for (int i = 0; i < crossoverPoint; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
        }

        for (int i = crossoverPoint; i < parent1.length; i++) {
            child1[i] = parent2[i];
            child2[i] = parent1[i];
        }

        children.add(child1);
        children.add(child2);

        return children;
    }

    // Step III: Mutation
    public void mutation(List<int[]> children) {
        for (int[] child : children) {
            if (Math.random() < MUTATION_PROBABILITY) {
                int randomIndex = new Random().nextInt(child.length);
                child[randomIndex] = 1;
            }
        }
    }

    public int[] findBestMove() {
        // Implement MiniMax algorithm here (similar to MiniMaxBot)
        // This is the part of the code where you use MiniMax to find the best move
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

            int score = evaluate();

            // Undo the move
            board[move[0]][move[1]] = EMPTY;

            // Undo the flip
            for (int[] neighbor : flipped) {
                board[neighbor[0]][neighbor[1]] = this.opp;
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
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

    public int[] move(char[][] board) {
        this.board = board;
        return findBestMove();
    }
}
