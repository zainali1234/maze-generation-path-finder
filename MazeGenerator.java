// Run in awt supported IDE (Ex: Visual Studio Code)
// Run "java MazeGenerator.java" to compile and execute program from VS terminal
// Click screen to animate the solution to the maze

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;

public class MazeGenerator extends JPanel {
    // Enum for direction and their bit representation
    enum Dir {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
        final int bit;
        final int dx;
        final int dy;
        Dir opposite;
    
        // Set the opposite direction
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        // Constructor for direction
        Dir(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }
    };

    // Variables for maze generation
    final int nCols;
    final int nRows;
    final int cellSize = 25;
    final int marginVal = 25;
    final int[][] maze;
    LinkedList<Integer> solution;

    // Constructor for MazeGenerator
    public MazeGenerator(int n, int m) {
        // Set preferred size and background color for JPanel
        setPreferredSize(new Dimension(650, 650));
        setBackground(Color.white);

        // Initialize variables for maze generation
        nCols = m;
        nRows = n;
        maze = new int[nRows][nCols];
        solution = new LinkedList<>();

        // Generate the maze
        generateMaze(0, 0);
        
        // Add a mouse listener to the JPanel to trigger the animation
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Thread(() -> {
                    solve(0);
                }).start();
            }
        });
    }

    // Override the paintComponent method to draw the maze and the animation
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphicsComponent = (Graphics2D) graphics;

        // Override the paintComponent method to draw the maze and the animation
        graphicsComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke size and color for the maze lines
        graphicsComponent.setStroke(new BasicStroke(5));
        graphicsComponent.setColor(Color.black);

        // draw maze
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {

                int x = marginVal + c * cellSize;
                int y = marginVal + r * cellSize;

                if ((maze[r][c] & 1) == 0) // N
                    graphicsComponent.drawLine(x, y, x + cellSize, y);

                if ((maze[r][c] & 2) == 0) // S
                    graphicsComponent.drawLine(x, y + cellSize, x + cellSize, y + cellSize);

                if ((maze[r][c] & 4) == 0) // E
                    graphicsComponent.drawLine(x + cellSize, y, x + cellSize, y + cellSize);

                if ((maze[r][c] & 8) == 0) // W
                    graphicsComponent.drawLine(x, y, x, y + cellSize);
            }
        }

        // draw pathfinding animation
        int offset = marginVal + cellSize / 2;

        Path2D path = new Path2D.Float();
        path.moveTo(offset, offset);

        for (int pos : solution) {
            int x = pos % nCols * cellSize + offset;
            int y = pos / nCols * cellSize + offset;
            path.lineTo(x, y);
        }

        graphicsComponent.setColor(Color.orange);
        graphicsComponent.draw(path);

        graphicsComponent.setColor(Color.blue);
        graphicsComponent.fillOval(offset - 5, offset - 5, 10, 10);

        graphicsComponent.setColor(Color.green);
        int x = offset + (nCols - 1) * cellSize;
        int y = offset + (nRows - 1) * cellSize;
        graphicsComponent.fillOval(x - 5, y - 5, 10, 10);

    }

    void generateMaze(int r, int c) {
        // Sets all possible directions
        Dir[] dirs = Dir.values();

        // Shuffles directions
        Collections.shuffle(Arrays.asList(dirs));
        
        // Iterates through each direction
        for (Dir dir : dirs) {
            // Calculates new row and column positions
            int nc = c + dir.dx;
            int nr = r + dir.dy;

            // Check if new position has not been visted and within bounds
            if (withinBounds(nr, nc) && maze[nr][nc] == 0) {
                // Mark current and new positions as connected
                maze[r][c] |= dir.bit;
                maze[nr][nc] |= dir.opposite.bit;
                generateMaze(nr, nc);
            }
        }
    }

    //  Checks if the given row and column position is within the bounds of the maze.
    boolean withinBounds(int r, int c) {
        return c >= 0 && c < nCols && r >= 0 && r < nRows;
    }

    // Solves maze from given position
    boolean solve(int pos) {
        // Check if the current position is the end of the maze
        if (pos == nCols * nRows - 1)
            return true;

        // Calculated row and column positions
        int c = pos % nCols;
        int r = pos / nCols;
        
        for (Dir dir : Dir.values()) {
            int nc = c + dir.dx;
            int nr = r + dir.dy;
            if (withinBounds(nr, nc) && (maze[r][c] & dir.bit) != 0
                    && (maze[nr][nc] & 16) == 0) {

                int newPos = nr * nCols + nc;
                
                // Add the new position to the solution and update the maze
                solution.add(newPos);
                maze[nr][nc] |= 16;

                animate();
                
                // Prints the direction of movement
                int newC = newPos % nCols;
                int newR = newPos / nCols;

                if (newC > pos % nCols) {
                    System.out.print("E");
                }
                else if (newC < pos % nCols) {
                    System.out.print("W");
                }
                else if (newR > pos % nCols) {
                    System.out.print("S");
                }
                else if (newR < pos % nCols) {
                    System.out.print("N");
                }
                
                // Recursively call the solve method on the new position
                if (solve(newPos))
                    return true;

                // If the recursive call fails, animate the backtracking
                animate();
                
                // Remove the new position from the solution and update the maze
                solution.removeLast();
                maze[nr][nc] &= ~16;
            }
        }

        return false;
    }
    // Animates the movement of the solution
    void animate() {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException ignored) {
        }
        repaint();
    }
    // creates the maze and displays it on the screen
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get n and m values from user
        System.out.print("Enter the value of n: ");
        int n = scanner.nextInt();

        System.out.print("Enter the value of m: ");
        int m = scanner.nextInt();

        scanner.close();

        SwingUtilities.invokeLater(() -> {
            JFrame screen = new JFrame();
            screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            screen.setTitle("Maze Generator");
            screen.setResizable(false);
            screen.add(new MazeGenerator(n, m), BorderLayout.CENTER);
            screen.pack();
            screen.setLocationRelativeTo(null);
            screen.setVisible(true);
        });
    }
}
