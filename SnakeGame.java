import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SnakeGame extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1L;
    
    private final int TILE_SIZE = 20;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    
    private ArrayList<Point> snake;
    private Point fruit;
    private int direction; // 0 - up, 1 - right, 2 - down, 3 - left
    private boolean running;
    private javax.swing.Timer timer;
    private boolean firstMove; // To track the first move
    
    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        addKeyListener(this);
        
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        
        placeFruit();
        
        direction = 1;
        running = false; // Game doesn't start running initially
        firstMove = false;
        
        // Create a timer without starting it
        timer = new javax.swing.Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    move();
                    checkCollision();
                    repaint();
                }
            }
        });
    }
    
    public void paint(Graphics g) {
        super.paint(g); // Call super.paint(g) to avoid flickering
        
        g.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        
        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        
        // Draw fruit
        g.setColor(Color.RED);
        g.fillRect(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
    
    private void move() {
        // Move snake
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        
        switch (direction) {
            case 0: // up
                newHead.y--;
                break;
            case 1: // right
                newHead.x++;
                break;
            case 2: // down
                newHead.y++;
                break;
            case 3: // left
                newHead.x--;
                break;
        }
        
        snake.add(0, newHead);
        
        // Check if snake eats fruit
        if (newHead.equals(fruit)) {
            placeFruit();
        } else {
            snake.remove(snake.size() - 1); // Remove tail only if it didn't eat fruit
        }
    }
    
    private void placeFruit() {
        Random rand = new Random();
        int x, y;
        
        // Ensure fruit doesn't spawn on the snake's body
        do {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        } while (snake.contains(new Point(x, y)));
        
        fruit = new Point(x, y);
    }
    
    private void checkCollision() {
        // Check wall collision
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver();
            return;
        }
        
        // Check self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }
    
    private void gameOver() {
        running = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Start the game on the first move
        if (!firstMove) {
            firstMove = true;
            running = true;
            timer.start(); // Start the timer after the first move
        }
        
        if (running) {
            switch (key) {
                case KeyEvent.VK_UP:
                    if (direction != 2) direction = 0;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 3) direction = 1;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 0) direction = 2;
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 1) direction = 3;
                    break;
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {}
    
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }
}
