
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * تست در لخظه تولید
 * @author Sadegh Kiyanzade
 */
public class DFS_1 {
    public static void main(String[] args) throws FileNotFoundException {

        /**
         * 6,7
         * 1,2
         * *******
         * **a---*
         * **-*-**
         * ***--**
         * *----f*
         * *******
         */
//        Maze maze = new Maze();                          // input

//        Maze maze = new Maze("maze_1.txt");              // from file

        Maze maze = new Maze(10, 15);  // random

        SolveMaze solveMaze = new SolveMaze(maze);
        solveMaze.solve();
        System.out.println("Memory: \n" + solveMaze.getMemory());
    }

    static class Maze {
        private int numOfRows;
        private int numOfCols;
        private Percept agentCell;
        private ArrayList<ArrayList<Cell>> cells;
        private Percept foodCell;

        public Maze(String path) throws FileNotFoundException {  // read from file
            cells = readMazeFromFile(path);
        }

        public Maze() {                                         // input
            this.cells = inputMaze();
        }

        public Maze(int numOfRows, int numOfCols) {             // random maze generator
            this.numOfRows = numOfRows;
            this.numOfCols = numOfCols;
            this.cells = randomMazeGenerator(numOfRows, numOfCols);
        }


        public int getNumOfRows() {
            return numOfRows;
        }

        public int getNumOfCols() {
            return numOfCols;
        }

        public Percept getAgentCell() {
            return agentCell;
        }


        private ArrayList<ArrayList<Cell>> readMazeFromFile(String path) throws FileNotFoundException {
            ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
            Scanner input = new Scanner(new File(path));
            for (int i = 0; input.hasNextLine(); i++) {
                String row = input.nextLine();
                ArrayList<Cell> tCells = new ArrayList<>();
                for (int j = 0; j < row.length(); j++)
                    if (row.charAt(j) == '*') {
                        tCells.add(Cell.WALL);
                    } else if (row.charAt(j) == '-') {
                        tCells.add(Cell.EMPTY);
                    } else if (row.charAt(j) == 'a') {
                        agentCell = new Percept(new CellPosition(i, j), Cell.AGENT, null);
                        tCells.add(Cell.AGENT);
                    } else if (row.charAt(j) == 'f') {
                        foodCell = new Percept(new CellPosition(i, j), Cell.FOOD, null);
                        tCells.add(Cell.FOOD);
                    }
                cells.add(tCells);
            }
            numOfRows = cells.size();
            numOfCols = cells.get(0).size();
            return cells;

        }

        private ArrayList<ArrayList<Cell>> randomMazeGenerator(int numOfRows, int numOfCols) {
            ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
            Random rand = new Random();
            CellPosition agentPos = CellPosition.randomPos(numOfRows, numOfCols);
            agentCell = new Percept(agentPos, Cell.AGENT, null);
            CellPosition foodPos;
            do {
                foodPos = CellPosition.randomPos(numOfRows, numOfCols);
            } while (agentPos.equals(foodPos));
            foodCell = new Percept(foodPos, Cell.FOOD, null);
            for (int i = 0; i < numOfRows; i++) {
                ArrayList<Cell> tCells = new ArrayList<>();
                for (int j = 0; j < numOfCols; j++) {
                    if (j == 0 || j == numOfCols - 1 || i == 0 || i == numOfRows - 1)
                        tCells.add(Cell.WALL);
                    else if (foodPos.X == i && foodPos.Y == j)
                        tCells.add(Cell.FOOD);
                    else if (agentPos.X == i && agentPos.Y == j)
                        tCells.add(Cell.AGENT);
                    else
                        tCells.add(rand.nextInt(3) == 0 ? Cell.WALL : Cell.EMPTY);
                }
                cells.add(tCells);
            }
            return cells;
        }

        private ArrayList<ArrayList<Cell>> inputMaze() {
            ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
            Scanner input = new Scanner(System.in);
            String[] in = input.nextLine().split(",");
            numOfRows = Integer.parseInt(in[0]);
            numOfCols = Integer.parseInt(in[1]);
            input.nextLine();
            for (int i = 0; i < numOfRows; i++) {
                String row = input.nextLine();
                ArrayList<Cell> tCells = new ArrayList<>();
                for (int j = 0; j < numOfCols; j++)
                    if (row.charAt(j) == '*') {
                        tCells.add(Cell.WALL);
                    } else if (row.charAt(j) == '-') {
                        tCells.add(Cell.EMPTY);
                    } else if (row.charAt(j) == 'a') {
                        agentCell = new Percept(new CellPosition(i, j), Cell.AGENT, null);
                        tCells.add(Cell.AGENT);
                    } else if (row.charAt(j) == 'f') {
                        foodCell = new Percept(new CellPosition(i, j), Cell.FOOD, null);
                        tCells.add(Cell.FOOD);
                    }
                cells.add(tCells);
            }
            return cells;
        }

        public Cell getStatusCell(int X, int Y) {
            return this.cells.get(X).get(Y);
        }

        public Percept getFoodCell() {
            return foodCell;
        }

        @Override
        public String toString() {
            String maze = "";
            for (ArrayList<Cell> cell : cells) {
                for (Cell value : cell) {
                    switch (value) {
                        case WALL -> maze += "*";
                        case EMPTY -> maze += "-";
                        case AGENT -> maze += "a";
                        case FOOD -> maze += "f";
                    }
                }
                maze += "\n";
            }
            return maze;
        }


    }

    enum Cell {
        EMPTY,
        WALL,
        AGENT,
        FOOD
    }

    static class CellPosition {
        int X;
        int Y;

        public CellPosition(int x, int y) {
            X = x;
            Y = y;
        }


        public int getX() {
            return X;
        }

        public int getY() {
            return Y;
        }

        @Override
        public boolean equals(Object o) {
            CellPosition cellPosition = (CellPosition) o;
            return cellPosition.getX() == this.getX() && cellPosition.getY() == this.getY();
        }

        public static CellPosition randomPos(int numOfRows, int numOfCols) {
            Random rand = new Random();
            return new CellPosition(rand.nextInt(numOfRows - 2) + 1, rand.nextInt(numOfCols - 2) + 1);
        }
    }

    static class Memory {
        private int numOfRows;
        private int numOfCols;
        private char[][] cells;

        public Memory(int numOfRows, int numOfCols) {
            this.numOfRows = numOfRows;
            this.numOfCols = numOfCols;
            cells = new char[numOfRows][numOfCols];
            for (int i = 0; i < numOfRows; i++) {
                for (int j = 0; j < numOfCols; j++) {
                    if (j == 0 || j == numOfCols - 1 || i == 0 || i == numOfRows - 1)
                        cells[i][j] = '*';
                    else
                        cells[i][j] = '?';
                }
            }
        }

        public void setPosition(CellPosition pos, char obj) {
            cells[pos.X][pos.Y] = obj;
        }


        public boolean checkUp(CellPosition pos) {
            return cells[pos.X - 1][pos.Y] == '?';
        }

        public boolean checkRight(CellPosition pos) {
            return cells[pos.X][pos.Y + 1] == '?';
        }

        public boolean checkLeft(CellPosition pos) {
            return cells[pos.X][pos.Y - 1] == '?';
        }

        public boolean checkDown(CellPosition pos) {
            return cells[pos.X + 1][pos.Y] == '?';
        }

        @Override
        public String toString() {
            String str = "";
            for (int i = 0; i < numOfRows; i++) {
                for (int j = 0; j < numOfCols; j++) {
                    str += cells[i][j];
                }
                str += '\n';
            }
            return str;
        }
    }

    static class SolveMaze {
        private Maze maze;
        private Percept currentAgentPercept;
        private Stack<Action> historyAction;
        private Memory memory;
        private View view;
        private int numOfMoves;

        public SolveMaze(Maze maze) {

            view = new View(maze);
            view.setVisible(true);
            view.paint(view.getGraphics());

            this.maze = maze;

            currentAgentPercept = maze.getAgentCell();
            historyAction = new Stack<>();
            memory = new Memory(maze.getNumOfRows(), maze.getNumOfCols());
            memory.setPosition(currentAgentPercept.getCellPosition(), 'a');
            numOfMoves = -1;
        }

        private Action agent(Percept percept) {
            if (percept.getCellStatus() == Cell.FOOD) {
                memory.setPosition(percept.getCellPosition(), 'f');
                return Action.GOAL;
            }
            if (percept.getCellStatus() != Cell.WALL) {
                if (percept.getCellStatus() != Cell.AGENT)
                    memory.setPosition(percept.getCellPosition(), '-');
                if (memory.checkRight(percept.getCellPosition())) {
                    historyAction.push(Action.RIGHT);
                    return Action.RIGHT;
                } else if (memory.checkDown(percept.getCellPosition())) {
                    historyAction.push(Action.DOWN);
                    return Action.DOWN;
                } else if (memory.checkLeft(percept.getCellPosition())) {
                    historyAction.push(Action.LEFT);
                    return Action.LEFT;
                } else if (memory.checkUp(percept.getCellPosition())) {
                    historyAction.push(Action.UP);
                    return Action.UP;
                } else {                // dead end
                    if (historyAction.isEmpty())
                        return null;    // no solution
                    return reverseAction(historyAction.pop());
                }
            } else {
                memory.setPosition(percept.getCellPosition(), '*');
                historyAction.pop();
                return reverseAction(percept.getAction());
            }

        }


        private Percept environment(Action action) {
            Cell newCellStatus = getStatusCell();
            Percept percept = new
                    Percept(new CellPosition(currentAgentPercept.getCellPosition().X, currentAgentPercept.getCellPosition().Y), newCellStatus, action);  // TODO
            return percept;
        }

        public void solve() {
            int x = currentAgentPercept.getCellPosition().X;  // TODO
            int y = currentAgentPercept.getCellPosition().Y;
            System.out.println("Agent Position: [" + x + "," + y + "]");
            System.out.println("Food Position: [" + maze.getFoodCell().getCellPosition().X + "," + maze.getFoodCell().getCellPosition().Y + "]");
            System.out.println("Moves Steps: ");
            Cell cell = currentAgentPercept.getCellStatus();
            Percept percept = new Percept(new CellPosition(x, y), cell, null);
            while (true) {
                Action action = agent(percept);
                if (action != null)
                    moveAgent(action);
                if (action == Action.GOAL)
                    break;
                if (action == null) {
                    System.err.println("NO SOLUTION.");
                    break;
                }
                percept = environment(action);
            }
            System.out.println("\nNumber of moves: " + numOfMoves);
        }

        private Action reverseAction(Action action) {
            return switch (action) {
                case UP -> Action.DOWN;
                case DOWN -> Action.UP;
                case RIGHT -> Action.LEFT;
                default -> Action.RIGHT;
            };
        }


        private Cell getStatusCell() {
            return maze.getStatusCell(currentAgentPercept.getCellPosition().X, currentAgentPercept.getCellPosition().Y);
        }

        private void moveAgent(Action action) {
            System.out.print("[" + currentAgentPercept.getCellPosition().X + "," + currentAgentPercept.getCellPosition().Y + "]");
            System.out.print("--" + action + "--");
            switch (action) {
                case UP -> currentAgentPercept.getCellPosition().X--;
                case DOWN -> currentAgentPercept.getCellPosition().X++;
                case RIGHT -> currentAgentPercept.getCellPosition().Y++;
                case LEFT -> currentAgentPercept.getCellPosition().Y--;
            }

            numOfMoves++;
            view.repaint();
        }

        public Memory getMemory() {
            return memory;
        }
    }


    enum Action {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        GOAL;
    }

    static class Percept {
        private CellPosition cellPosition;
        private Cell cellStatus;
        private Action action;

        public Percept(CellPosition cellPosition, Cell cellStatus, Action action) {
            this.cellPosition = cellPosition;
            this.cellStatus = cellStatus;
            this.action = action;
        }

        public Action getAction() {
            return action;
        }


        public CellPosition getCellPosition() {
            return cellPosition;
        }

        public Cell getCellStatus() {
            return cellStatus;
        }

        @Override
        public boolean equals(Object o) {
            Percept percept = (Percept) o;
            return (percept.cellStatus == this.cellStatus && percept.getCellPosition().equals(this.getCellPosition()));
        }

    }

    static class View extends JFrame {
        Maze maze;

        public View(Maze maze) {
            setTitle("Maze");
            setSize(1024, 768);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setMaximumSize(getMinimumSize());
            setMinimumSize(getMinimumSize());
            setPreferredSize(getPreferredSize());

            this.maze = maze;
        }

        @Override
        public void paint(Graphics g) {
            int s = 35;
            g.translate(240, 210);
            for (int i = 0; i < maze.getNumOfRows(); i++) {
                for (int j = 0; j < maze.getNumOfCols(); j++) {
                    Color color = switch (maze.getStatusCell(i, j)) {
                        case WALL -> Color.DARK_GRAY;
                        case AGENT -> Color.cyan;
                        case FOOD -> Color.GREEN;
                        default -> Color.WHITE;
                    };

                    g.setColor(color);
                    g.fillRect(s * j, s * i, s, s);
                    g.setColor(Color.BLACK);
                    g.drawRect(s * j, s * i, s, s);
                }
            }

            g.setColor(Color.RED);
            g.fillOval(maze.getAgentCell().getCellPosition().Y * s, maze.getAgentCell().getCellPosition().X * s, s, s);

        }

        @Override
        public void repaint() {
            try {
                sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paint(getGraphics());
        }
    }

}

