# Maze Generator
This Java program generates a maze using a recursive backtracking algorithm and visualizes the solution animation upon user interaction. The maze is displayed in a graphical user interface (GUI) implemented using Java's AWT and Swing libraries.

## Instructions
To run the program, make sure you are using an AWT-supported IDE (e.g., Visual Studio Code). Follow these steps:

1. Open the program in your AWT-supported IDE.
2. Compile and execute the program from the IDE terminal using the command:
```
java MazeGenerator.java
```
3. Click on the screen to animate the solution to the generated maze.
4. 
## Maze Generation Algorithm
The maze generation uses a recursive backtracking algorithm. It starts at a random cell and moves in a random direction until it reaches a dead-end. The algorithm then backtracks, creating paths and forming the maze structure.

## Maze Solution Animation
Upon clicking the screen, the program triggers a solution animation. The solution is found using a recursive depth-first search algorithm. The animation visually represents the path from the maze's start to its end.

## Code Structure
The MazeGenerator class extends JPanel and handles maze generation, solution finding, and GUI components.
Directional movements are represented using an enum Dir.
The generateMaze method initializes the maze by creating paths and connections between cells.
The solve method recursively finds the solution path and animates the process.
The animate method introduces a delay to visualize the solution movement.

## Running the Program
1. Input the dimensions of the maze (n x m) when prompted.
2. The maze and solution animation will be displayed in a GUI window.
3. Click on the window to start the solution animation.
