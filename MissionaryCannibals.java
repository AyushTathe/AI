import java.util.*;
import java.time.Duration;
import java.time.Instant;

class BFS {

    int nodeNo;
    State[] stateArray;
    State initialState;

    public BFS(State initialState) {
        this.initialState = initialState;
        stateArray = new State[1000005];
        nodeNo = 0;
    }

    public State getFinalState() {

        Instant start = Instant.now();

        if (initialState.isItTheGoalState()) {
            return initialState;
        }

        nodeNo = 0;

        Queue<State> q = new LinkedList<>();

        HashMap<State, Integer> map = new HashMap<>();

        map.put(initialState, nodeNo);

        initialState.setParentState(-1);

        stateArray[nodeNo] = initialState;

        q.add(initialState);

        while (!q.isEmpty()) {

            State u = q.poll();
            int indexU = map.get(u);

            List<State> successors = u.getSuccessors();

            for (State v : successors) {

                Instant t = Instant.now();
                Duration dur = Duration.between(start, t);
                double till = dur.toMillis();
                if (till >= Constants.timeLimit * 1000) {
                    System.out.println("Time limit " + Constants.timeLimit + " s exceeded.");
                    return null;
                }

                if (!map.containsKey(v)) {
                    nodeNo++;
                    v.setParentState(indexU);
                    stateArray[nodeNo] = v;

                    if (v.isItTheGoalState()) {
                        Instant end = Instant.now();
                        Duration timeElapsed = Duration.between(start, end);
                        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");

                        return v;
                    }

                    map.put(v, nodeNo);
                    q.add(v);
                }
            }
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");

        return null;
    }

    public void printPath() {
        int t = 0;
        State s = getFinalState();
        System.out.println("Number of generated nodes: " + nodeNo);

        if (s == null) {
            System.out.println("No solution found.");
            return;
        }

        String[] str = new String[100005];

        while (!s.equals(initialState)) {
            str[t] = s.toString();
            t++;
            s = stateArray[s.getParentState()];
//            System.out.println(s);
        }
        str[t] = s.toString();

        System.out.println("BFS takes " + t + " steps.\n");

        for (int i = t; i >= 0; i--) {
            System.out.print(str[i]);
            if (i != 0) {
                System.out.println(" --> ");
            }
        }
        System.out.println("");
    }
}
class Constants {
    static int LEFT = 0; 
    static int RIGHT = 1; 
    static boolean visited = true; 
    static boolean notVisited = false; 
    static int timeLimit = 100; 
}
class DFS {

    int nodeNo;
    State[] stateArray;
    State initialState;

    public DFS(State initialState) {
        this.initialState = initialState;
        stateArray = new State[1000005];
        nodeNo = 0;
    }

    public State getFinalState() {

        Instant start = Instant.now();

        if (initialState.isItTheGoalState()) {
            return initialState;
        }

        nodeNo = 0;

        Stack<State> stack = new Stack<>();

        HashMap<State, Integer> map = new HashMap<>();

        map.put(initialState, nodeNo);

        initialState.setParentState(-1);

        stateArray[nodeNo] = initialState;

        stack.push(initialState);

        while (!stack.isEmpty()) {

            State u = stack.pop();
            int indexU = map.get(u);

            List<State> successors = u.getSuccessors();

            for (State v : successors) {

                Instant t = Instant.now();
                Duration dur = Duration.between(start, t);
                double till = dur.toMillis();
                if (till >= Constants.timeLimit * 1000) {
                    System.out.println("Time limit " + Constants.timeLimit + " s exceeded.");
                    return null;
                }

                if (!map.containsKey(v)) {
                    nodeNo++;
                    v.setParentState(indexU);
                    stateArray[nodeNo] = v;

                    if (v.isItTheGoalState()) {
                        Instant end = Instant.now();
                        Duration timeElapsed = Duration.between(start, end);
                        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");

                        return v;
                    }

                    map.put(v, nodeNo);
                    stack.push(v);
                }
            }
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");

        return null;
    }

    public void printPath() {
        int t = 0;
        State s = getFinalState();
        System.out.println("Number of generated nodes: " + nodeNo);
        if (s == null) {
            System.out.println("No solution found.");
            return;
        }

        String[] str = new String[100005];

        while (!s.equals(initialState)) {
            str[t] = s.toString();
            t++;
            s = stateArray[s.getParentState()];
//            System.out.println(s);
        }
        str[t] = s.toString();

        System.out.println("DFS takes " + t + " steps.\n");

        for (int i = t; i >= 0; i--) {
            System.out.print(str[i]);
            if (i != 0) {
                System.out.println(" --> ");
            }
        }
        System.out.println("");
    }
}
class State {

    int parentState;
    int MissionaryOnLeft;
    int MissionaryOnRight;
    int CannibalOnLeft;
    int CannibalOnRight;
    int boatCapacity;
    int side;
    

    public State(int MissionaryOnLeft, int CannibalOnLeft, int MissionaryOnRight, int CannibalOnRight,
            int boatCapacity, int side) {

        this.MissionaryOnLeft = MissionaryOnLeft;
        this.CannibalOnLeft = CannibalOnLeft;

        this.MissionaryOnRight = MissionaryOnRight;
        this.CannibalOnRight = CannibalOnRight;

        this.boatCapacity = boatCapacity;

        this.side = side;
    }

    public boolean isThisAValidState() {
        
        if (MissionaryOnLeft >= 0 && CannibalOnLeft >= 0 && MissionaryOnRight >= 0 && CannibalOnRight >= 0
                && (MissionaryOnLeft == 0 || MissionaryOnLeft >= CannibalOnLeft)
                && (MissionaryOnRight == 0 || MissionaryOnRight >= CannibalOnRight)) {
            return true;
        }
        
//        System.out.println(this);
        
        return false;
    }

    public boolean isItTheGoalState() {
        if (MissionaryOnLeft == 0 && CannibalOnLeft == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        }

        State s = (State) obj;
        return (s.CannibalOnLeft == CannibalOnLeft && s.MissionaryOnLeft == MissionaryOnLeft
                && s.side == side && s.CannibalOnRight == CannibalOnRight
                && s.MissionaryOnRight == MissionaryOnRight);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.MissionaryOnLeft;
        hash = 61 * hash + this.MissionaryOnRight;
        hash = 61 * hash + this.CannibalOnLeft;
        hash = 61 * hash + this.CannibalOnRight;
        hash = 61 * hash + this.side;
        return hash;
    }

    

   

    public List<State> getSuccessors() {
        List<State> successors = new ArrayList<>();
        generateSuccessors(successors);
//        printAll(successors);
        return successors;
    }

    public void generateSuccessors(List<State> successors) {
        if (side == Constants.LEFT) {
            for (int i = 0; i <= MissionaryOnLeft; i++) {
                for (int j = 0; j <= CannibalOnLeft; j++) {
                    // (i == 0 || i >= j) ---> if i is 0, no check is needed. otherwise, i >= j is a must
                    if ((i + j) != 0 && ((i + j) <= boatCapacity) && (i == 0 || i >= j)) {
                        State tem = new State(MissionaryOnLeft - i, CannibalOnLeft - j, MissionaryOnRight + i,
                                CannibalOnRight + j, boatCapacity, Constants.RIGHT);
                        if (tem.isThisAValidState()) {
                            successors.add(tem);
//                            System.out.println(tem);
                        }
                    }
                }
            }
        } else if (side == Constants.RIGHT) {
            for (int i = 0; i <= MissionaryOnRight; i++) {
                for (int j = 0; j <= CannibalOnRight; j++) {

                    if ((i + j) != 0 && ((i + j) <= boatCapacity)) {
                        State tem = new State(MissionaryOnLeft + i, CannibalOnLeft + j, MissionaryOnRight - i,
                                CannibalOnRight - j, boatCapacity, Constants.LEFT);

                        if (tem.isThisAValidState()) {
                            successors.add(tem);
//                            System.out.println(tem);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        if (side == Constants.LEFT) {
            return "(" + MissionaryOnLeft + "," + CannibalOnLeft + ",Left,"
                    + MissionaryOnRight + "," + CannibalOnRight + ")";
        } else {
            return "(" + MissionaryOnLeft + "," + CannibalOnLeft + ",Right,"
                    + MissionaryOnRight + "," + CannibalOnRight + ")";
        }
    }

    public void printAll(List<State> l) {
        for (State s : l) {
            System.out.println(s);
        }
    }

    public int getParentState() {
        return parentState;
    }

    public void setParentState(int parentState) {
        this.parentState = parentState;
    }

    public int getMissionaryOnLeft() {
        return MissionaryOnLeft;
    }

    public void setMissionaryOnLeft(int MissionaryOnLeft) {
        this.MissionaryOnLeft = MissionaryOnLeft;
    }

    public int getMissionaryOnRight() {
        return MissionaryOnRight;
    }

    public void setMissionaryOnRight(int MissionaryOnRight) {
        this.MissionaryOnRight = MissionaryOnRight;
    }

    public int getCannibalOnLeft() {
        return CannibalOnLeft;
    }

    public void setCannibalOnLeft(int CannibalOnLeft) {
        this.CannibalOnLeft = CannibalOnLeft;
    }

    public int getCannibalOnRight() {
        return CannibalOnRight;
    }

    public void setCannibalOnRight(int CannibalOnRight) {
        this.CannibalOnRight = CannibalOnRight;
    }

    public int getBoatCapacity() {
        return boatCapacity;
    }

    public void setBoatCapacity(int boatCapacity) {
        this.boatCapacity = boatCapacity;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }



}

public class Main {

    private static int initialMissionaryOnLeft;
    private static int initialCannibalOnLeft;
    private static int boatCapacity;
    private static int choinceNo;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Enter initial number of Missionaries on the left bank: ");
            initialMissionaryOnLeft = sc.nextInt();
            System.out.println("Enter initial number of Cannibals on the left bank: ");
            initialCannibalOnLeft = sc.nextInt();
            System.out.println("Enter the capcity of the boat: ");
            boatCapacity = sc.nextInt();

            System.out.println("Which algorithm do you want to use?: ");
            System.out.println("1. BFS\n2. DFS");
            System.out.println("Enter your choice: ");

            choinceNo = sc.nextInt();

            State initialState = new State(initialMissionaryOnLeft, initialCannibalOnLeft, 0, 0, boatCapacity, Constants.LEFT);
            initialState.setParentState(-1);

            if (choinceNo == 1) {
                BFS bfs = new BFS(initialState);
                bfs.printPath();
            } else if (choinceNo == 2) {
                DFS dfs = new DFS(initialState);
                dfs.printPath();
            } else {
                System.out.println("You entered an invalid choice. Try again.");
            }
        }
    }
}