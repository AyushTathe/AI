import java.util.*;

public class EightPuzzle {
    
    // Representing the goal state
    private static final int[][] GOAL_STATE = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
    };

    // Heuristic function - Manhattan distance
    private static int calculateHeuristic(int[][] state) {
        int heuristic = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int value = state[i][j];
                if (value != 0) {
                    int targetX = (value - 1) / state.length;
                    int targetY = (value - 1) % state.length;
                    heuristic += Math.abs(i - targetX) + Math.abs(j - targetY);
                }
            }
        }
        return heuristic;
    }

    // Node class to represent each state of the puzzle
    static class Node {
        int[][] state;
        int heuristic;
        int cost;
        Node parent;

        Node(int[][] state, Node parent) {
            this.state = state;
            this.parent = parent;
            this.heuristic = calculateHeuristic(state);
            if (parent != null) {
                this.cost = parent.cost + 1;
            } else {
                this.cost = 0;
            }
        }
    }

    // Best First Search algorithm
    private static Node bestFirstSearch(int[][] initial) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristic));
        Set<String> visited = new HashSet<>();
        Node root = new Node(initial, null);
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (Arrays.deepEquals(current.state, GOAL_STATE)) {
                return current;
            }
            visited.add(Arrays.deepToString(current.state));
            int[][] currentState = current.state;
            int zeroX = 0, zeroY = 0;

            // Find the position of the empty tile (zero)
            outerloop:
            for (zeroX = 0; zeroX < currentState.length; zeroX++) {
                for (zeroY = 0; zeroY < currentState[zeroX].length; zeroY++) {
                    if (currentState[zeroX][zeroY] == 0) {
                        break outerloop;
                    }
                }
            }

            // Generate child states by moving the empty tile
            int[] dx = {0, 0, -1, 1};
            int[] dy = {-1, 1, 0, 0};
            for (int i = 0; i < 4; i++) {
                int newX = zeroX + dx[i];
                int newY = zeroY + dy[i];
                if (newX >= 0 && newX < currentState.length && newY >= 0 && newY < currentState[newX].length) {
                    int[][] newState = new int[currentState.length][currentState[0].length];
                    for (int j = 0; j < currentState.length; j++) {
                        System.arraycopy(currentState[j], 0, newState[j], 0, currentState[j].length);
                    }
                    newState[zeroX][zeroY] = currentState[newX][newY];
                    newState[newX][newY] = 0;
                    Node child = new Node(newState, current);
                    if (!visited.contains(Arrays.deepToString(child.state))) {
                        queue.add(child);
                    }
                }
            }
        }
        return null; // No solution found
    }

    // Print the solution path
    private static void printSolution(Node solution) {
        List<int[][]> path = new ArrayList<>();
        while (solution != null) {
            path.add(solution.state);
            solution = solution.parent;
        }
        Collections.reverse(path);
        for (int[][] state : path) {
            printState(state);
        }
    }

    // Print the current state of the puzzle
    private static void printState(int[][] state) {
        System.out.println("-------------");
        for (int[] row : state) {
            System.out.print("| ");
            for (int value : row) {
                System.out.print(value == 0 ? " " : value);
                System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println("-------------");
    }

    public static void main(String[] args) {
        int[][] initial = {
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}
        };

        System.out.println("Initial State:");
        printState(initial);

        Node solution = bestFirstSearch(initial);

        if (solution != null) {
            System.out.println("Solution found:");
            printSolution(solution);
        } else {
            System.out.println("No solution found.");
        }
    }
}