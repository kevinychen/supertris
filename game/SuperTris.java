/**
 * @(#)SuperTris.java
 *
 * SuperTris application
 *
 * @Francesco
 * @version 1.00 2008/7/6
 */

package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SuperTris
{
    static final int GRID_WIDTH = 12;
    static final int GRID_HEIGHT = 24;
    static final int START_SPEED = 400;
    static final int DOWN_SCORE = 1;
    static final int LINE_SCORE = 500;
    static final int SPEED_CHANGE_TIME = 120;
    static final double SPEED_CHANGE_CONSTANT = .95;

    private JFrame frame;
    private JFrame infoScreen;
    private final int gridWidth, gridHeight;
    private final Point[][] blocks;
    private final Grid grid;
    private Block currentBlock;
    private Point[] nextBlock;
    private BufferedImage nextBlockImage;
    private Timer timer;
    private int score;
    private int lines;
    private int timeCounter;

    public SuperTris(int gridWidth, int gridHeight, int startSpeed,
            Point[][] blocks)
    {
        this.frame = new JFrame();
        this.infoScreen = new JFrame();
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.blocks = blocks;
        this.grid = new Grid(gridWidth, gridHeight);

        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                timeCounter++;
                if (timeCounter == SPEED_CHANGE_TIME)
                {
                    timer.setDelay((int) (timer.getDelay() * SPEED_CHANGE_CONSTANT));
                    timeCounter = 0;
                }

                Point[] down = currentBlock.down();

                grid.erase(currentBlock.getCurrent());
                if (grid.valid(down))
                    currentBlock.move(down);
                else
                {
                    if (currentBlock.getBase().row <= 0)
                        endGame();
                    else
                    {
                        grid.place(currentBlock.getCurrent());
                        int numLines = grid.destroyLines();
                        lines += numLines;
                        score += LINE_SCORE * numLines;
                        currentBlock = new Block(new Point(0, GRID_WIDTH / 2),
                                nextBlock);
                        nextBlock = randomBlock();
                        loadImage(nextBlock);
                    }
                }
                grid.place(currentBlock.getCurrent());

                frame.repaint();
                infoScreen.repaint();
            }
        };

        this.timer = new Timer(startSpeed, listener);
    }

    private Point[] randomBlock()
    {
        return blocks[(int) (Math.random() * blocks.length)];
    }

    private void loadImage(Point... points)
    {
        nextBlockImage = new BufferedImage(100, 100,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = nextBlockImage.createGraphics();

        for (Point p : points)
            drawTile(g, 40 + 20 * p.col, 40 + 20 * p.row, 20, 20);
    }

    private void loadFrame()
    {
        frame.dispose();
        infoScreen.dispose();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("SUPER TRIS! \t by Kevin Y. Chen");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width / 3, size.height - 100);
        frame.setLocation(size.width / 3, 0);
        frame.setResizable(false);
    }

    public void menu()
    {
        loadFrame();

        JButton button = new JButton("CLICK TO START");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                start();
            }
        });

        frame.add(button);
        frame.setVisible(true);
    }

    public void start()
    {
        loadFrame();

        grid.clear();
        score = lines = timeCounter = 0;

        infoScreen = new JFrame();
        infoScreen.setTitle("Stats");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        infoScreen.setSize(size.width / 4, size.height / 4);
        infoScreen.setLocation(size.width * 3 / 4, 0);
        infoScreen.setResizable(false);

        currentBlock = new Block(new Point(0, GRID_WIDTH / 2), randomBlock());
        nextBlock = randomBlock();

        JPanel panel = new JPanel()
        {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                for (int i = 0; i < gridHeight; i++)
                    for (int j = 0; j < gridWidth; j++)
                        if (grid.get(new Point(i, j)))
                        {
                            drawTile(g, getSize().width * j / GRID_WIDTH,
                                    getSize().height * i / GRID_HEIGHT,
                                    getSize().width / GRID_WIDTH,
                                    getSize().height / GRID_HEIGHT);
                        }
            }

            public boolean isFocusable()
            {
                return true;
            }
        };

        panel.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                {
                    Point[] turn = currentBlock.turn();
                    grid.erase(currentBlock.getCurrent());
                    if (grid.valid(turn))
                        currentBlock.move(turn);
                    grid.place(currentBlock.getCurrent());
                }
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    Point[] left = currentBlock.left();
                    grid.erase(currentBlock.getCurrent());
                    if (grid.valid(left))
                        currentBlock.move(left);
                    grid.place(currentBlock.getCurrent());
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    Point[] right = currentBlock.right();
                    grid.erase(currentBlock.getCurrent());
                    if (grid.valid(right))
                        currentBlock.move(right);
                    grid.place(currentBlock.getCurrent());
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    Point[] down = currentBlock.down();
                    grid.erase(currentBlock.getCurrent());
                    if (grid.valid(down))
                    {
                        currentBlock.move(down);
                        score += DOWN_SCORE;
                    }
                    grid.place(currentBlock.getCurrent());
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    endGame();
                }

                frame.repaint();
                infoScreen.repaint();
            }
        });

        JPanel panel2 = new JPanel()
        {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Times New Roman", 0, 24));
                g.drawString("SCORE: " + score, 10, 30);
                g.drawString("LINES: " + lines, 10, 60);
                g.drawImage(nextBlockImage, 0, 70, null);
            }
        };

        infoScreen.add(panel2);
        infoScreen.setVisible(true);
        frame.add(panel);
        frame.setVisible(true);

        loadImage(nextBlock);
        timer.start();
    }

    public void endGame()
    {
        timer.stop();

        try
        {
            Scoreboard scoreboard = Scoreboard.getScoreboard();
            if (scoreboard.makesList(score))
            {
                String name = JOptionPane
                        .showInputDialog("YOUR SCORE WAS "
                                + score
                                + "\n\nYOU MADE THE HIGH SCORE LIST!\nEnter your name:");
                if (name != null && !name.trim().equals(""))
                    scoreboard.add(name, score);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "YOUR SCORE WAS " + score);
            }

            scoreboard.write();
        }
        catch (Exception e)
        {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "YOUR SCORE WAS "
                                    + score
                                    + "\n\nSORRY, THERE IS A PROBLEM WITH THE HIGH SCORE FILE.");
        }

        menu();
    }

    private void drawTile(Graphics g, int x, int y, int width, int height)
    {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    public static void main(String... kevy)
    {
        SuperTris s = new SuperTris(GRID_WIDTH, GRID_HEIGHT, START_SPEED,
                Block.PENTAMINO_BLOCK_SET);

        s.menu();
    }
}
