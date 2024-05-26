import java.util.*;

public class TicTacToeAI {
    private static final int BOARD_EMPTY = 0;
    private static final int BOARD_PLAYER_X = 1;
    private static final int BOARD_PLAYER_O = -1;

    public static void main(String[] args) {
        int[] s = new int[9];
        Arrays.fill(s, BOARD_EMPTY);

        System.out.println("|------- WELCOME TO TIC TAC TOE -----------|");
        System.out.println("You are X while the Computer is O");

        while (terminal(s) == null) {
            int play = player(s);
            if (play == BOARD_PLAYER_X) {
                System.out.println("\n\nIt is your turn\n\n");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the x-coordinate [0-2]: ");
                int x = scanner.nextInt();
                System.out.print("Enter the y-coordinate [0-2]: ");
                int y = scanner.nextInt();
                int index = 3 * x + y;

                if (s[index] != BOARD_EMPTY) {
                    System.out.println("That coordinate is already taken. Please try again.");
                    continue;
                }

                s = result(s, new int[]{play, index});
                printBoard(s);
            } else {
                System.out.println("\n\nThe computer is playing its turn");
                int[] action = minimax(s);
                s = result(s, action);
                printBoard(s);
            }
        }

        int winner = utility(s, 1)[0];
        if (winner == BOARD_PLAYER_X) {
            System.out.println("You have won!");
        } else if (winner == BOARD_PLAYER_O) {
            System.out.println("You have lost!");
        } else {
            System.out.println("It's a tie.");
        }
    }

    private static int player(int[] s) {
        Map<Integer, Integer> counter = new HashMap<>();
        for (int num : s) {
            counter.put(num, counter.getOrDefault(num, 0) + 1);
        }

        int xPlaces = counter.getOrDefault(BOARD_PLAYER_X, 0);
        int oPlaces = counter.getOrDefault(BOARD_PLAYER_O, 0);

        if (xPlaces + oPlaces == 9) {
            return 0;
        } else if (xPlaces > oPlaces) {
            return BOARD_PLAYER_O;
        } else {
            return BOARD_PLAYER_X;
        }
    }

    private static List<int[]> actions(int[] s) {
        int play = player(s);
        List<int[]> actionsList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == BOARD_EMPTY) {
                actionsList.add(new int[]{play, i});
            }
        }
        return actionsList;
    }

    private static int[] result(int[] s, int[] a) {
        int play = a[0];
        int index = a[1];
        int[] sCopy = Arrays.copyOf(s, s.length);
        sCopy[index] = play;
        return sCopy;
    }

    private static Integer terminal(int[] s) {
        for (int i = 0; i < 3; i++) {
            if (s[3 * i] == s[3 * i + 1] && s[3 * i + 1] == s[3 * i + 2] && s[3 * i] != BOARD_EMPTY) {
                return s[3 * i];
            }
            if (s[i] == s[i + 3] && s[i + 3] == s[i + 6] && s[i] != BOARD_EMPTY) {
                return s[i];
            }
        }

        if (s[0] == s[4] && s[4] == s[8] && s[0] != BOARD_EMPTY) {
            return s[0];
        }
        if (s[2] == s[4] && s[4] == s[6] && s[2] != BOARD_EMPTY) {
            return s[2];
        }

        if (player(s) == 0) {
            return 0;
        }

        return null;
    }

    private static int[] utility(int[] s, int cost) {
        Integer term = terminal(s);
        if (term != null) {
            return new int[]{term, cost};
        }

        List<int[]> actionList = actions(s);
        List<int[]> utils = new ArrayList<>();
        for (int[] action : actionList) {
            int[] newS = result(s, action);
            utils.add(utility(newS, cost + 1));
        }

        int score = utils.get(0)[0];
        int idxCost = utils.get(0)[1];
        int play = player(s);
        if (play == BOARD_PLAYER_X) {
            for (int i = 1; i < utils.size(); i++) {
                if (utils.get(i)[0] > score) {
                    score = utils.get(i)[0];
                    idxCost = utils.get(i)[1];
                }
            }
        } else {
            for (int i = 1; i < utils.size(); i++) {
                if (utils.get(i)[0] < score) {
                    score = utils.get(i)[0];
                    idxCost = utils.get(i)[1];
                }
            }
        }
        return new int[]{score, idxCost};
    }

    private static int[] minimax(int[] s) {
	    List<int[]> actionList = actions(s);
	    List<int[]> utils = new ArrayList<>();
	    for (int[] action : actionList) {
	        int[] newS = result(s, action);
	        utils.add(new int[]{action[1], utility(newS, 1)[0]});
	    }
	
	    if (utils.size() == 0) {
	        return new int[]{0, 0};
	    }
	
	    int play = player(s);
	    if (play == BOARD_PLAYER_X) {
	        utils.sort(Comparator.comparingInt(l -> l[1]));
	    } else {
	        utils.sort((l1, l2) -> Integer.compare(l2[1], l1[1]));
	    }
	
	    int[] action = utils.get(0);
	    return new int[]{play, action[0]};
    }

    private static void printBoard(int[] s) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(convert(s[3 * i + j]) + " ");
            }
            System.out.println();
        }
    }

    private static char convert(int num) {
        if (num == BOARD_PLAYER_X) {
            return 'X';
        }
        if (num == BOARD_PLAYER_O) {
            return 'O';
        }
        return '_';
    }
}
