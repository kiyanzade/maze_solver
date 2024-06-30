# Man-Pac Game Project

## Project Description
This project simulates a game similar to the classic game "Pac-Man". The game board consists of m rows and n columns. Each cell can either be a wall or an empty space. There is one agent (player) and one piece of food randomly placed on the board. The goal is to implement two search algorithms, DFS (Depth-First Search) and UCS (Uniform Cost Search), to navigate the agent to the food.

### Implementation Details
- **DFS Algorithm**:
  - Implement DFS in both "goal test at generation time" and "goal test at expansion time" variants.
  
- **UCS Algorithm**:
  - Implement UCS considering four input weights for right, down, left, and up movements. The implementation should prioritize movements in the order: right, down, left, up.

- **Environment and Agent Functions**:
  - **Environment Function ([Percept] = Environment(Action))**: 
    - This function takes an action and returns the percept, which includes the current state of the agent's position and the status of the cell (empty, wall, or food). Global variables can be used to maintain the overall state of cells and the agent's current position.
    
  - **Agent Function ([Action] = Agent(Percept))**: 
    - This function takes a percept as input and returns an action. It should handle the agent's actions based solely on the percept received, without accessing any additional information.
    - 
- **Random Game Environment Creation**:
  - Implement a function to randomly generate an m x n game board with walls, placing the agent and food randomly. This extends the basic implementation where the environment is predefined.

- **Graphical Interface**:
  - Create a graphical representation of the game mechanics and the agent's pathfinding.
