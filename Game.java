package game;
//https://www.mediafire.com/file/h3qvtt4lja0njvy/Zombie.rar/file
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
//import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable,KeyListener 
{
	public static JFrame frame;
	public final static int WIDHT = 480; 
	public final static int HEIGHT = 320;
	private final int SCALE = 2;
	private Thread thread;
	private boolean isRunning;
	private BufferedImage image;
	public static boolean start; 
	static public Player player;
	static public List<Enemy> enemies;
	static public Enemy enemy;
	private Image background;
	private double timeStart;
	private double time;
	private String timeStr;
	public int k;
	public int timeToRespawn = 10*1000;
	public int timeToRespawnDelay = 30;
	private boolean spawnEnemy;
	private int x,y;
	
	public Game()
	{
		player = new Player(240,160,32,32);
		enemies = new ArrayList<Enemy>();
		spawnEnemy = true;
		timeStart = System.currentTimeMillis();
		setPreferredSize(new Dimension(WIDHT*SCALE,HEIGHT*SCALE));
		image = new BufferedImage(WIDHT,HEIGHT,BufferedImage.TYPE_INT_RGB);
		ImageIcon reference = new ImageIcon("res\\background.png");
		background = reference.getImage();
		initFrame();
	}
	
	public void initFrame() 
	{
		frame = new JFrame("Game 1#");
		frame.addKeyListener(this);
		frame.add(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	public synchronized void start() 
	{
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	public synchronized void stop() 
	{
		isRunning = false;
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
	
		}
	}
	public static void main(String[] args) 
	{
		Game game = new Game();
		game.start();
	}
	public void tick() 
	{
		player.tick();
		if(time >= timeToRespawn+k && time <= timeToRespawn+timeToRespawnDelay+k) 
		{
			spawnEnemy = true;
			k += timeToRespawn;
		}
		else if(time > timeToRespawn+timeToRespawnDelay+k) 
		{
			k += timeToRespawn;
		}
		for(int i = 0; i < enemies.size(); i++) 
		{
			Enemy enemy = enemies.get(i);
			enemy.tick();
		}
		if(spawnEnemy) 
		{
			spawnEnemy = false;
			Random num = new Random();
			int r = num.nextInt(4);
			if(r == 0) 
			{
				x = 5;
				y = 200;
				enemies.add(new Enemy(x,y,32,32));
			}
			if(r == 1) 
			{
				x = 440;
				y = 200;
				enemies.add(new Enemy(x,y,32,32));
			}
			if(r == 2) 
			{
				x = 220;
				y = 5;
				enemies.add(new Enemy(x,y,32,32));
			}
			else
			{
				x = 220;
				y = 280;
				enemies.add(new Enemy(x,y,32,32));;
			}
		}
		timer();
	}

	public void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) 
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		//Background
		g2.drawImage(background,0,0,WIDHT,HEIGHT,null);
		//
		if(Player.alive == false) 
		{
			g2.setColor(new Color(0,0,0,160));
			g2.fillRect(0, 0,WIDHT*SCALE,HEIGHT*SCALE);
			g2.setFont(new Font("Arial",Font.BOLD,40));
			g2.setColor(new Color(190,0,0));
			g2.drawString("You died.",164,160);
			g2.setFont(new Font("Arial",Font.BOLD,20));
			g2.setColor(Color.white);
			g2.drawString("Score:"+timeStr,210,180);
		}
		if(Player.alive) 
		{
		player.render(g2);
		for(int i = 0; i < enemies.size(); i++) 
		{
			Enemy enemy = enemies.get(i);
			enemy.render(g2);
		}
		player.renderHp(g2);
		g2.setFont(new Font("Arial",Font.BOLD,15));
		g2.setColor(Color.white);
		g2.drawString("Score:"+timeStr,0,12);
		}
		//
		g.dispose();
		g2.dispose();
		
		g = bs.getDrawGraphics();
		
		g.drawImage(image,0,0,WIDHT*SCALE,HEIGHT*SCALE,null);
		bs.show();
	}
	public void run() 
	{
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60;
		final double ns = 1000000000/ amountOfTicks;
		double delta = 0;
		while(isRunning) 
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) 
			{
				render();
				tick();
				delta--;
			}
		}
		stop();
	}
	public void restart() 
	{
		enemies.clear();
		timeStart = System.currentTimeMillis();
		player.restart();
		spawnEnemy = true;
		for(int i = 0; i < enemies.size(); i++) 
		{
			Enemy enemy = enemies.get(i);
			enemy.restart();
		}
	}
	public void timer() 
	{
		if(Player.alive) 
		{
			time = System.currentTimeMillis() - timeStart;
			timeStr = String.valueOf(time);
			if(timeStr.length() >= 6) 
			{
				timeStr = timeStr.substring (0, timeStr.length() - 5);
			}
			else  if(timeStr.length() < 6) 
			{
				timeStr = "1";
			} 
		}
	}
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode()  == KeyEvent.VK_RIGHT) 
		{
			Player.right = true;
		}
		if(e.getKeyCode()  == KeyEvent.VK_A || e.getKeyCode()  == KeyEvent.VK_LEFT) 
		{
			Player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode()  == KeyEvent.VK_UP) 
		{
			Player.up = true;
		}
		if(e.getKeyCode()  == KeyEvent.VK_S || e.getKeyCode()  == KeyEvent.VK_DOWN) 
		{
			Player.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) 
		{
			player.isRunning = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode()  == KeyEvent.VK_RIGHT) 
		{
			Player.right = false;
		}
		if(e.getKeyCode()  == KeyEvent.VK_A || e.getKeyCode()  == KeyEvent.VK_LEFT) 
		{
			Player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode()  == KeyEvent.VK_UP) 
		{
			Player.up = false;
		}
		if(e.getKeyCode()  == KeyEvent.VK_S || e.getKeyCode()  == KeyEvent.VK_DOWN) 
		{
			Player.down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			player.isRunning = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE && Player.alive == false) 
		{
			restart();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) 
	{

	}
}
