package EightPuzzle;

import java.util.*;

public class EightPuzzleDFS {
    public static void main(String[] args) {
        int[][] initialBoard = {
                { 1, 0, 2 },
                { 8, 6, 3 },
                { 7, 5, 4 }
        };

        int[][] goalBoard = {
                { 1, 2, 3 },
                { 8, 0, 4 },
                { 7, 6, 5 }
        };

        solveEightPuzzleDFS(initialBoard, goalBoard);
    }

    public static void solveEightPuzzleDFS(int[][] initialBoard, int[][] goalBoard) {
        State initialState = new State(initialBoard);
        Stack<State> stack = new Stack<>();  //to keep track of States to explore
        Set<State> visited = new HashSet<>(); //to keep track of visited states
        Map<State, State> parentMap = new HashMap<>(); //to keep track of parents and child relationships

        stack.push(initialState);
        visited.add(initialState);

        while (!stack.isEmpty()) {
            State currentState = stack.pop();

            if (Arrays.deepEquals(currentState.board, goalBoard)) {
                // We found a solution
                printSolution(currentState, parentMap);
                return;
            }

            List<State> neighbors = getNeighbors(currentState);

            for (State neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, currentState);
                }
            }
        }

        System.out.println("No solution found.");
    }

    public static List<State> getNeighbors(State currentState) {
        List<State> neighbors = new ArrayList<>();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Possible movements: up, down, left, right

        for (int[] dir : directions) {
            int newRow = currentState.blankRow + dir[0];
            int newCol = currentState.blankCol + dir[1];

            if (isValid(newRow, newCol)) {
                int[][] newBoard = Arrays.stream(currentState.board)
                        .map(int[]::clone)
                        .toArray(int[][]::new);

                // Swap the blank tile with the neighboring tile
                newBoard[currentState.blankRow][currentState.blankCol] = newBoard[newRow][newCol];
                newBoard[newRow][newCol] = 0;

                neighbors.add(new State(newBoard, newRow, newCol));
            }
        }

        return neighbors;
    }

    public static boolean isValid(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    public static void printSolution(State currentState, Map<State, State> parentMap) {
        List<State> solutionPath = new ArrayList<>();
        solutionPath.add(currentState);

        while (parentMap.containsKey(currentState)) {
            currentState = parentMap.get(currentState);
            solutionPath.add(currentState);
        }

        Collections.reverse(solutionPath);

        System.out.println("Solution Path:");
        for (State state : solutionPath) {
            printBoard(state.board);
            System.out.println();
        }
        System.out.println("Total Moves: " + (solutionPath.size() - 1));
    }

    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    static class State {
        int[][] board;
        int blankRow;
        int blankCol;

        public State(int[][] board) {
            this.board = board;
            findBlankPosition();
        }

        public State(int[][] board, int blankRow, int blankCol) {
            this.board = board;
            this.blankRow = blankRow;
            this.blankCol = blankCol;
        }

        private void findBlankPosition() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        blankRow = i;
                        blankCol = j;
                        return;
                    }
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            State state = (State) obj;
            return Arrays.deepEquals(board, state.board);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(board);
        }
    }
}
