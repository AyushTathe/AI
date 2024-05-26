import java.util.*;

public class Main {
    static int[][] board = {
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0}
    };

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("TIC-TAC-TOE using MINIMAX with ALPHA-BETA Pruning");
        System.out.println("=================================================");
        pvc();
    }

    public static void Gameboard(int[][] board) {
        char[] chars = { 'X', ' ', 'O' }; // Mapping: 0 -> ' ', 1 -> 'X', -1 -> 'O'
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print("| " + chars[cell + 1] + " |"); // cell + 1 for correct indexing
            }
            System.out.println("\n---------------");
        }
        System.out.println("===============");
    }

    public static void Clearboard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], 0);
        }
    }

    public static boolean winningPlayer(int[][] board, int player) {
        int[][] conditions = {
            {board[0][0], board[0][1], board[0][2]},
            {board[1][0], board[1][1], board[1][2]},
            {board[2][0], board[2][1], board[2][2]},
            {board[0][0], board[1][0], board[2][0]},
            {board[0][1], board[1][1], board[2][1]},
            {board[0][2], board[1][2], board[2][2]},
            {board[0][0], board[1][1], board[2][2]},
            {board[0][2], board[1][1], board[2][0]}
        };

        for (int[] condition : conditions) {
            if (condition[0] == player && condition[1] == player && condition[2] == player) {
                return true;
            }
        }
        return false;
    }

    public static boolean gameWon(int[][] board) {
        return winningPlayer(board, 1) || winningPlayer(board, -1);
    }

    public static void printResult(int[][] board) {
        if (winningPlayer(board, 1)) {
            System.out.println("X has won!\n");
        } else if (winningPlayer(board, -1)) {
            System.out.println("O's have won!\n");
        } else {
            System.out.println("Draw\n");
        }
    }

    public static List<int[]> blanks(int[][] board) {
        List<int[]> blank = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    blank.add(new int[]{i, j});
                }
            }
        }
        return blank;
    }

    public static boolean boardFull(int[][] board) {
        return blanks(board).isEmpty();
    }

    public static void setMove(int[][] board, int x, int y, int player) {
        board[x][y] = player;
    }

    public static void playerMove(int[][] board) {
        Scanner scanner = new Scanner(System.in);
        boolean e = true;
        Map<Integer, int[]> moves = new HashMap<>();
        moves.put(1, new int[]{0, 0});
        moves.put(2, new int[]{0, 1});
        moves.put(3, new int[]{0, 2});
        moves.put(4, new int[]{1, 0});
        moves.put(5, new int[]{1, 1});
        moves.put(6, new int[]{1, 2});
        moves.put(7, new int[]{2, 0});
        moves.put(8, new int[]{2, 1});
        moves.put(9, new int[]{2, 2});
        
        while (e) {
            try {
                System.out.print("Enter a number between 1-9: ");
                int move = scanner.nextInt();
                if (move < 1 || move > 9) {
                    System.out.println("Invalid Move! Try again!");
                } else {
                    int[] cell = moves.get(move);
                    if (board[cell[0]][cell[1]] != 0) {
                        System.out.println("Invalid Move! Try again!");
                    } else {
                        setMove(board, cell[0], cell[1], 1);
                        Gameboard(board);
                        e = false;
                    }
                }
            } catch (InputMismatchException ex) {
                System.out.println("Enter a number!");
                scanner.next(); // clear the invalid input
            }
        }
    }

    public static int getScore(int[][] board) {
        if (winningPlayer(board, 1)) {
            return 10;
        } else if (winningPlayer(board, -1)) {
            return -10;
        } else {
            return 0;
        }
    }

    public static int[] abminimax(int[][] board, int depth, int alpha, int beta, int player) {
        int row = -1;
        int col = -1;
        if (depth == 0 || gameWon(board)) {
            return new int[]{row, col, getScore(board)};
        } else {
            for (int[] cell : blanks(board)) {
                setMove(board, cell[0], cell[1], player);
                int[] score = abminimax(board, depth - 1, alpha, beta, -player);
                if (player == 1) {
                    if (score[2] > alpha) {
                        alpha = score[2];
                        row = cell[0];
                        col = cell[1];
                    }
                } else {
                    if (score[2] < beta) {
                        beta = score[2];
                        row = cell[0];
                        col = cell[1];
                    }
                }
                setMove(board, cell[0], cell[1], 0);
                if (alpha >= beta) {
                    break;
                }
            }
            return new int[]{row, col, player == 1 ? alpha : beta};
        }
    }

    public static void o_comp(int[][] board) {
        Random rand = new Random();
        if (blanks(board).size() == 9) {
            int x = rand.nextInt(3);
            int y = rand.nextInt(3);
            setMove(board, x, y, -1);
            Gameboard(board);
        } else {
            int[] result = abminimax(board, blanks(board).size(), Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
            setMove(board, result[0], result[1], -1);
            Gameboard(board);
        }
    }

    public static void x_comp(int[][] board) {
        Random rand = new Random();
        if (blanks(board).size() == 9) {
            int x = rand.nextInt(3);
            int y = rand.nextInt(3);
            setMove(board, x, y, 1);
            Gameboard(board);
        } else {
            int[] result = abminimax(board, blanks(board).size(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
            setMove(board, result[0], result[1], 1);
            Gameboard(board);
        }
    }

    public static void makeMove(int[][] board, int player, int mode) {
        if (mode == 1) {
            if (player == 1) {
                playerMove(board);
            } else {
                o_comp(board);
            }
        } else {
            if (player == 1) {
                o_comp(board);
            } else {
                x_comp(board);
            }
        }
    }

    public static void pvc() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter to play 1st or 2nd: ");
                int order = scanner.nextInt();
                if (order != 1 && order != 2) {
                    System.out.println("Please pick 1 or 2");
                } else {
                    Clearboard(board);
                    int currentPlayer = order == 2 ? -1 : 1;
                    while (!boardFull(board) && !gameWon(board)) {
                        makeMove(board, currentPlayer, 1);
                        currentPlayer *= -1;
                    }
                    printResult(board);
                    break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Enter a number!");
                scanner.next(); // clear the invalid input
            }
        }
    }
}
