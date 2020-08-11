package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy 
{
	private  double x,y;
	private int widht; 
	private int height;
	private  final double VELOCITY = 1.5;
	private double dx, dy;
	private int frames = 0; 
	private double damage = 1; 
	private int maxFrames = 5; 
	private double reductionVelocity = 0.34;
	private int curAnimaton; 
	private int maxAnimation = 3; 
	private double distanceX , distanceY;
	private Spritesheet sheet; 
	private BufferedImage[] enemy; 
	private double range;
	private double distanceFromPlayer;
	
	public Enemy(int x, int y,int widht,int height)
	{
		this.x = x;
		this.y = y;
		this.widht = widht;
		this.height = height;
		range = 100;
		sheet = new Spritesheet("/spritesheet_enemy.png");
		enemy = new BufferedImage[maxAnimation];
		enemy[0] = sheet.getSprite(0, 0, 16, 16);
		enemy[1] = sheet.getSprite(16, 0, 16, 16);
		enemy[2] = sheet.getSprite(32, 0, 16, 16);
		int angle = new Random().nextInt(359);
    	dx = Math.cos(Math.toRadians(angle));
    	dy = Math.sin(Math.toRadians(angle));
	}
	public void tick() 
	{
		distanceFromPlayer = Math.hypot((Player.x-8 - x+8),(Player.y-8 - y+8));
		if(distanceFromPlayer > range) 
		{
			walkRandom();
		}
		if(distanceFromPlayer <= range) 
		{
			followPlayer();
		}
		animation();
	}
	public void walkRandom() 
	{
		if(x < Game.WIDHT - widht && x > 0) 
		{
			x += dx*reductionVelocity + distanceX;
		}
		if(x >= Game.WIDHT - widht || x <= 0) 
		{
			dx *= -1;
			x += dx*reductionVelocity + distanceX;
		}
		if( y < Game.HEIGHT - height && y> 0) 
		{
			y += dy*reductionVelocity + distanceY;
		}
		if(y >= Game.HEIGHT - height || y <= 0) 
		{
			dy *= -1;
			y += dy*reductionVelocity + distanceY;
		}
	}
	public void followPlayer() 
	{
			range = 150;
			if(x < Player.x) 
			{
				x += VELOCITY;
			}
			if(x > Player.x) 
			{
				x -= VELOCITY;
			}
			if(y < Player.y) 
			{
				y += VELOCITY;
			}
			if(y > Player.y) 
			{
				y -= VELOCITY;
			}
			if(distanceFromPlayer < 12) 
			{
				Player.hp -= damage;
			}
	}
	public void animation()
	{
		frames++;
		if(frames > maxFrames) 
		{
			frames = 0;
			curAnimaton++;
			if(curAnimaton >= maxAnimation) 
			{
					curAnimaton = 0;
			}
		}
	}
	public void render(Graphics2D g2) 
	{
		if(x > Player.x && distanceFromPlayer <= range)
		{
			g2.drawImage(enemy[curAnimaton],(int)x+widht,(int)y,-widht,height,null);
		}
		else 
		{
			g2.drawImage(enemy[curAnimaton],(int)x,(int)y,widht,height,null);
		}
	}
	public void restart() 
	{
		int angle = new Random().nextInt(359);
		x = 20;
		y = 20;
    	dx = Math.cos(Math.toRadians(angle));
    	dy = Math.sin(Math.toRadians(angle));
	}
}
