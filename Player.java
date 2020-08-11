package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player
{
    public static boolean down;
    public static boolean up;
    public static boolean left;
    public static boolean right;
    public static boolean alive;
    public static double hp;
    public static int widht;
    public static int height;
    private double damageVisual;
    private boolean flipPlayer;
    public boolean isRunning;
    public static int x;
    public static int y;
    private final double VELOCITY = 2.0;
    public boolean vertical;
    public boolean horizontal;
    private double run;
    private int frames;
    private int maxFrames;
    private int curAnimaton;
    private int maxAnimation;
    private Spritesheet sheet;
    private BufferedImage[] player;
    
    public Player(final int x, final int y, final int widht, final int height) 
    {
        frames = 0;
        maxFrames = 5;
        maxAnimation = 3;
        Player.x = x;
        Player.y = y;
        alive = true;
        hp = 100.0;
        Player.widht = widht;
        Player.height = height;
        sheet = new Spritesheet("/spritesheet_player.png");
        (player = new BufferedImage[maxAnimation])[0] = sheet.getSprite(0, 0, 16, 16);
        player[1] = sheet.getSprite(16, 0, 16, 16);
        player[2] = sheet.getSprite(32, 0, 16, 16);
    }
    
    public void tick() {
        damageVisual = (100.0 - hp) * 0.4;
        move();
        animation();
        if (hp <= 0.0) {
            alive = false;
        }
    }
    
    public void move() 
    {
        if (isRunning) 
        {
            run = 1.0;
        }
        else if (!isRunning) 
        {
            run = 0.0;
        }
        if (up && y > 0) 
        {
            y -= VELOCITY + run;
        }
        if (down && y < 320 - height) 
        {
            y += VELOCITY + run;
        }
        if (right && x < 480 - widht) 
        {
            x += VELOCITY + run;
            flipPlayer = false;
        }
        if (left && x > 0) 
        {
            x -= VELOCITY + run;
            flipPlayer = true;
        }
    }
    
    public void animation() 
    {
        if (up || down || left || right) 
        {
            ++frames;
            if (frames > maxFrames) 
            {
                frames = 0;
                ++curAnimaton;
                if (curAnimaton >= maxAnimation) 
                {
                    curAnimaton = 0;
                }
            }
        }
        else 
        {
            curAnimaton = 0;
        }
    }
    
    public void render(final Graphics2D g2) 
    {
        if (flipPlayer) 
        {
            g2.drawImage(player[curAnimaton], x + widht, y, -widht, height, null);
        }
        else 
        {
            g2.drawImage(player[curAnimaton], x, y, widht, height, null);
        }
    }
    
    public void renderHp(final Graphics2D g2) 
    {
    	g2.setColor(Color.black);
        g2.fillRect(x - 4, y - 7, 40, 5);
        g2.setColor(Color.red);
        g2.fillRect(x - 4, y - 7, (int)(40.0 - damageVisual), 5);
    }
    
    public void restart() 
    {
        x = 240;
        y = 160;
        alive = true;
        hp = 100.0;
    }
}
