package com.tankwar.domain;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tankwar.ui.TankClient;
import com.tankwar.utils.FileUtils;
import com.tankwar.utils.SoundUtils;


public class Missile {
	
	public static final int WIDTH = 10;
	
	private int posX;
	private int posY;
	private int speedX = 7;
	private int speedY = 7;
	private Direction dir;
	private boolean live = true;
	private boolean good;
	private TankClient tankClient;
	
	private static Map<Direction, Image> imageMap;
	
	public Missile(int x, int y, Direction dir) {
		this.posX = x;
		this.posY = y;
		this.dir = dir;
		imageMap = new HashMap<Direction, Image>();
		imageMap.put(Direction.LEFT, FileUtils.getImage("images/missileL.gif"));
		imageMap.put(Direction.RIGHT, FileUtils.getImage("images/missileR.gif"));
		imageMap.put(Direction.DOWN, FileUtils.getImage("images/missileD.gif"));
		imageMap.put(Direction.UP, FileUtils.getImage("images/missileU.gif"));
		imageMap.put(Direction.DL, FileUtils.getImage("images/missileLD.gif"));
		imageMap.put(Direction.LU, FileUtils.getImage("images/missileLU.gif"));
		imageMap.put(Direction.UR, FileUtils.getImage("images/missileRU.gif"));
		imageMap.put(Direction.RD, FileUtils.getImage("images/missileRD.gif"));
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tankClient = tc;
		this.good = good;
	}
	
	public void drawMe(Graphics g) {
		if(!live) return ;
		if(good) {
			g.setColor(Color.RED);
		}else{
			g.setColor(Color.BLUE);
		}
//		g.fillOval(posX, posY, WIDTH, WIDTH);
		g.drawImage(imageMap.get(dir), posX, posY, null);
		move();
	}
	
	public void move(){
		switch(dir) {
		case LEFT:
			posX -= speedX;
			break;
		case RIGHT:
			posX += speedX;
			break;
		case DOWN:
			posY += speedY;
			break;
		case UP:
			posY -= speedY;
			break;
		case LU:
			posX -= speedX;
			posY -= speedY;
			break;
		case UR:
			posX += speedX;
			posY -= speedY;
			break;
		case RD:
			posX += speedX;
			posY += speedY;
			break;
		case DL:
			posX -= speedX;
			posY += speedY;
			break;
		case STOP:
			break;
		}
		if(posX < 0 || posY < 0 || posX > TankClient.FRAME_WIDTH || posY > TankClient.FRAME_HEIGHT) {
			live = false;
			this.tankClient.removeMissile(this);
		}
	}
	
	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(posX, posY, WIDTH, WIDTH);
	}
	
	public boolean hitTank(Tank tank) {
		if(live && tank.isLive() && good != tank.isGood() && this.getRect().intersects(tank.getRect())) {
			try {
				this.live = false;
				Explode e = new Explode(posX, posY, tankClient);
				this.tankClient.addExplode(e);
				this.tankClient.removeMissile(this);
//				this.tankClient.removeTank(tank);
				tank.beingHit();
				SoundUtils.getInstance().playBoomSound();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;    
	}
	
	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public void hitTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			hitTank(tanks.get(i));
		}
	}
	
	public boolean hitWall(Wall wall) {
		if(live && this.getRect().intersects(wall.getRect())) {
			this.live = false;
			this.tankClient.removeMissile(this);
			return true;
		}
		return false;
	}
	
	public void hitWalls(List<Wall> walls) {
		for(int i = 0; i < walls.size(); i++) {
			hitWall(walls.get(i));
		}
	}
	
}
