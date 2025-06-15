package princemishra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int BOX_SIZE = 25;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int ALL_DOTS = (WIDTH * HEIGHT) / (BOX_SIZE * BOX_SIZE);
    private final int DELAY = 100;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;
    private int foodX;
    private int foodY;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        random = new Random();
        startGame();
    }

    public void startGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 100 - i * BOX_SIZE;
            y[i] = 100;
        }
        locateFood();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, BOX_SIZE, BOX_SIZE);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(x[i], y[i], BOX_SIZE, BOX_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + (dots - 3), 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void locateFood() {
        foodX = random.nextInt(WIDTH / BOX_SIZE) * BOX_SIZE;
        foodY = random.nextInt(HEIGHT / BOX_SIZE) * BOX_SIZE;
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) x[0] -= BOX_SIZE;
        if (right) x[0] += BOX_SIZE;
        if (up) y[0] -= BOX_SIZE;
        if (down) y[0] += BOX_SIZE;
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            dots++;
            locateFood();
        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over";
        String score = "Your Score: " + (dots - 3);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(msg, WIDTH / 2 - 120, HEIGHT / 2 - 20);
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        g.drawString(score, WIDTH / 2 - 90, HEIGHT / 2 + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && !right) {
            left = true;
            up = down = false;
        }
        if (key == KeyEvent.VK_RIGHT && !left) {
            right = true;
            up = down = false;
        }
        if (key == KeyEvent.VK_UP && !down) {
            up = true;
            left = right = false;
        }
        if (key == KeyEvent.VK_DOWN && !up) {
            down = true;
            left = right = false;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game by Prince");
        SnakeGame gamePanel = new SnakeGame();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
