import java.util.Scanner;

public class Main {
    private static final int[] magic = {8, 3, 4, 1, 5, 9, 6, 7, 2};
    private static final int[] board = new int[9];
    private static int symbol = 1;
    private static int startHuman = 1;
    private static int human = 0;
    private static int computer = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            newGame();
            boolean gameRunning = true;

            while (gameRunning) {
                renderBoard();
                if (symbol == 1) {
                    System.out.println("Your Turn (Enter position 1-9): ");
                    int move = scanner.nextInt() - 1;
                    if (move >= 0 && move < 9 && board[move] == -1) {
                        board[move] = symbol;
                        if (checkWin(symbol)) {
                            renderBoard();
                            System.out.println("You Win!");
                            human++;
                            gameRunning = false;
                        } else {
                            symbol = 0;
                            if (count() == 0) {
                                renderBoard();
                                System.out.println("Draw!");
                                gameRunning = false;
                            }
                        }
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                } else {
                    move(0);
                    if (checkWin(0)) {
                        renderBoard();
                        System.out.println("Computer Wins!");
                        computer++;
                        gameRunning = false;
                    } else {
                        symbol = 1;
                        if (count() == 0) {
                            renderBoard();
                            System.out.println("Draw!");
                            gameRunning = false;
                        }
                    }
                }
            }

            System.out.println("New Game? (yes/no): ");
            String newGame = scanner.next();
            if (!newGame.equalsIgnoreCase("yes")) {
                break;
            }
        }
        scanner.close();
    }

    private static void newGame() {
        for (int i = 0; i < 9; i++) {
            board[i] = -1;
        }
        symbol = startHuman;
        startHuman = 1 - startHuman;
    }

    private static void renderBoard() {
        System.out.println("Current Board:");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) System.out.println();
            char mark = board[i] == -1 ? '-' : (board[i] == 1 ? 'X' : 'O');
            System.out.print(mark + " ");
        }
        System.out.println();
    }

    private static int count() {
        int c = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] == -1) c++;
        }
        return c;
    }

    private static boolean checkWin(int symbol) {
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (board[i] == symbol && board[j] == symbol && board[k] == symbol &&
                        magic[i] + magic[j] + magic[k] == 15) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void move(int comp) {
        // Empty board
        if (count() == 9) {
            board[4] = comp;
            return;
        }

        // If comp first move is second
        if (count() == 8) {
            if (board[4] == 1 - comp) {
                board[0] = comp;
                return;
            }
            int[] candidates = {4, 1, 3, 5, 7};
            for (int key : candidates) {
                if (board[key] == -1) {
                    board[key] = comp;
                    return;
                }
            }
        }

        // Win in 1 move for comp exists
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (i == j || j == k || k == i) continue;
                    if (board[i] == comp && board[j] == comp && board[k] == -1 &&
                        magic[i] + magic[j] + magic[k] == 15) {
                        board[k] = comp;
                        return;
                    }
                }
            }
        }

        // Stop opponents win in 1 move for opponent exists
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (i == j || j == k || k == i) continue;
                    if (board[i] == 1 - comp && board[j] == 1 - comp && board[k] == -1 &&
                        magic[i] + magic[j] + magic[k] == 15) {
                        board[k] = comp;
                        return;
                    }
                }
            }
        }

        // Check if opponent has force win in 2 and stop it
        for (int i = 0; i < 9; i++) {
            if (board[i] != -1) continue; // Box should be empty
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (i == j || j == k || k == i) continue;
                    if (board[j] != 1 - comp || board[k] != 1 - comp) continue;
                    int p1 = position(15 - magic[i] - magic[j]);
                    int p2 = position(15 - magic[i] - magic[k]);
                    if (p1 >= 0 && p1 != i && p1 != j && p1 != k && board[p1] == -1 &&
                        p2 >= 0 && p2 != i && p2 != j && p2 != k && board[p2] == -1) {
                        board[i] = comp;
                        return;
                    }
                }
            }
        }

        // Checking if a win in 2 can be forced over opponent
        for (int i = 0; i < 9; i++) {
            if (board[i] != -1) continue; // Box should be empty
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (i == j || j == k || k == i) continue;
                    if (board[j] != comp || board[k] != comp) continue;
                    int p1 = position(15 - magic[i] - magic[j]);
                    int p2 = position(15 - magic[i] - magic[k]);
                    if (p1 >= 0 && p1 != i && p1 != j && p1 != k && board[p1] == -1 &&
                        p2 >= 0 && p2 != i && p2 != j && p2 != k && board[p2] == -1) {
                        board[i] = comp;
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board[i] == -1) {
                board[i] = comp;
                return;
            }
        }
    }

    private static int position(int value) {
        for (int i = 0; i < magic.length; i++) {
            if (magic[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
