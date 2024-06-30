import java.util.*;

// ? ):
public class UCS {
    public static void main(String[] args) {
        Maze maze = new Maze(5, 5);
        Environment environment = new Environment(maze);
        Agent agent = new Agent(environment);
        List<Action> path = agent.UCS();

        if (path != null) {
            System.out.println("Path found:");
            for (Action action : path) {
                System.out.println(action);
            }
        } else {
            System.out.println("Path not found.");
        }
    }
}

enum CellContent {
    EMPTY,
    WALL,
    AGENT,
    GOAL
}

class CellPosition {
    int row;
    int col;

    CellPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

class State {
    CellContent content;
    CellPosition position;

    State(CellContent content, CellPosition position) {
        this.content = content;
        this.position = position;
    }
}

class Maze {
    int rows;
    int cols;
    State[][] cells;

    Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new State[rows][cols];
    }
}

class Environment {
    Maze maze;

    Environment(Maze maze) {
        this.maze = maze;
    }

    State getState(CellPosition position) {
        return maze.cells[position.row][position.col];
    }

    void setState(CellPosition position, CellContent content) {
        maze.cells[position.row][position.col].content = content;
    }
}

enum Action {
    LEFT,
    RIGHT,
    UP,
    DOWN
}

class Node {
    State state;
    Node parent;
    Action action;
    int cost;

    Node(State state, Node parent, Action action, int cost) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.cost = cost;
    }
}

class Frontier {
    Queue<Node> nodes;

    Frontier() {
        this.nodes = new LinkedList<>();
    }

    void addNode(Node node) {
        nodes.add(node);
    }

    Node removeNode() {
        return nodes.remove();
    }

    boolean isEmpty() {
        return nodes.isEmpty();
    }
}

class ExploredSet {
    Set<State> exploredSet;

    ExploredSet() {
        this.exploredSet = new HashSet<>();
    }

    void addState(State state) {
        exploredSet.add(state);
    }

    boolean containsState(State state) {
        return exploredSet.contains(state);
    }
}

class ActuatedAction {
    Action action;
    boolean isExecuted;

    ActuatedAction(Action action) {
        this.action = action;
        this.isExecuted = false;
    }
}

class History {
    List<State> states;
    List<ActuatedAction> actions;

    History() {
        this.states = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    void addState(State state) {
        states.add(state);
    }

    void addAction(ActuatedAction action) {
        actions.add(action);
    }
}

class Agent {
    Environment environment;

    Agent(Environment environment) {
        this.environment = environment;
    }

    List<Action> UCS() {
        Node initialNode = new Node(environment.getState(new CellPosition(0, 0)), null, null, 0);
        Frontier frontier = new Frontier();
        frontier.addNode(initialNode);
        ExploredSet exploredSet = new ExploredSet();

        while (!frontier.isEmpty()) {
            Node node = frontier.removeNode();

            if (isGoalState(node.state)) {
                return getPath(node);
            }

            exploredSet.addState(node.state);

            for (Action action : Action.values()) {
                State newState = applyAction(node.state, action);
                if (newState != null && !exploredSet.containsState(newState)) {
                    int newCost = node.cost + 1;
                    Node newNode = new Node(newState, node, action, newCost);
                    frontier.addNode(newNode);
                }
            }
        }

        return null;
    }

    boolean isGoalState(State state) {
        return state.content == CellContent.GOAL;
    }

    State applyAction(State state, Action action) {
        CellPosition newPosition = calculateNewPosition(state.position, action);

        if (isValidPosition(newPosition)) {
            CellContent newContent = environment.getState(newPosition).content;
            if (newContent != CellContent.WALL) {
                return new State(newContent, newPosition);
            }
        }

        return null;
    }

    CellPosition calculateNewPosition(CellPosition position, Action action) {
        int newRow = position.row;
        int newCol = position.col;

        switch (action) {
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
        }

        return new CellPosition(newRow, newCol);
    }

    boolean isValidPosition(CellPosition position) {
        int row = position.row;
        int col = position.col;
        return row >= 0 && row < environment.maze.rows && col >= 0 && col < environment.maze.cols;
    }

    List<Action> getPath(Node node) {
        List<Action> path = new ArrayList<>();
        Node currentNode = node;

        while (currentNode.parent != null) {
            path.add(0, currentNode.action);
            currentNode = currentNode.parent;
        }

        return path;
    }
}

