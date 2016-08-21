package com.tankwar.domain;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tankwar.ui.TankClient;
import com.tankwar.utils.FileUtils;
import com.tankwar.utils.SoundUtils;


public class Tank {
	
	public static final int WIDTH = 50;
	
	private int posX;
	private int posY;
	private int lastX = 0;
	private int lastY = 0;
	private int speedX = 5;
	private int speedY = 5;
	private boolean bL = false, bR = false, bU = false, bD = false;
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.LU;
	private boolean good;
	private boolean live;
	private int life = 100;
	private int step = random.nextInt(10) + 10;
	
	private static Random random = new Random();
	private TankClient tankClient;
	private static Map<Direction, Image> imageMap;
	
	public Tank(int posX, int posY, boolean good) {
		this.posX = posX;
		this.posY = posY;
		this.good = good;
		this.live = true;
		imageMap = new HashMap<Direction, Image>();
		imageMap.put(Direction.LEFT, FileUtils.getImage("images/tankL.gif"));
		imageMap.put(Direction.RIGHT, FileUtils.getImage("images/tankR.gif"));
		imageMap.put(Direction.UP, FileUtils.getImage("images/tankU.gif"));
		imageMap.put(Direction.DOWN, FileUtils.getImage("images/tankD.gif"));
		imageMap.put(Direction.DL, FileUtils.getImage("images/tankLD.gif"));
		imageMap.put(Direction.LU, FileUtils.getImage("images/tankLU.gif"));
		imageMap.put(Direction.RD, FileUtils.getImage("images/tankRD.gif"));
		imageMap.put(Direction.UR, FileUtils.getImage("images/tankRU.gif"));
		imageMap.put(Direction.STOP, FileUtils.getImage("images/tankD.gif"));
	}
	
	public Tank(int x, int y, boolean good, TankClient tc) {
		this(x, y, good);
		this.tankClient = tc;
	}
	
	public void drawMe(Graphics g) {
		if(!live) return ;
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
//		g.fillOval(posX, posY, WIDTH, WIDTH);
		g.drawImage(imageMap.get(ptDir), posX, posY, null);
//		drawPt(g);
		drawLife(g);
		if(!good)
			move();
	}
	
	private void drawLife(Graphics g){
		g.setColor(Color.BLACK);
		g.drawRect(posX - 1, posY - 11, WIDTH + 1, 6);
		if(life >= 30)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.RED);
		g.fillRect(posX, posY - 10, (int) (WIDTH * life * 1.0 / 100), 5);
	}
	
	/**
	 * 按键按下
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			bD = true;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_F2:
			//我的坦克死掉后按F2复活
			recoveryMyTank();
			break;
		}
		setDirection();
		move();
	}
	
	private void recoveryMyTank() {
		if(good && !live) {
			this.life = 100;
			this.live = true;
			this.posX = 100;
			this.posY = 100;
			this.tankClient.addTank(this);
			SoundUtils.getInstance().playEatBloodSound();
		}
	}
	
	/**
	 * 按键释放
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_SPACE://开火
			fire();
			break;
		case KeyEvent.VK_ENTER:
			superFire();
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			bD = false;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			bU = false;
			break;
		}
		setDirection();
		move();
	}

	/**
	 * 根据按键确定方向
	 */
	private void setDirection() {
		if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
		else if(bL && !bU && !bR && !bD) ptDir = dir = Direction.LEFT;
		else if(!bL && !bU && bR && !bD) ptDir = dir = Direction.RIGHT;
		else if(!bL && bU && !bR && !bD) ptDir = dir = Direction.UP;
		else if(!bL && !bU && !bR && bD) ptDir = dir = Direction.DOWN;
		else if(bL && bU && !bR && !bD) ptDir = dir = Direction.LU;
		else if(!bL && bU && bR && !bD) ptDir = dir = Direction.UR;
		else if(!bL && !bU && bR && bD) ptDir = dir = Direction.RD;
		else if(bL && !bU && !bR && bD) ptDir = dir = Direction.DL;
	}
	
	public void move() {
		lastX = posX;
		lastY = posY;
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
		if(posX <= 0) 
			posX = 0;
		else if(posX >= TankClient.FRAME_WIDTH - WIDTH) 
			posX = TankClient.FRAME_WIDTH - WIDTH;
		if(posY <= 30) 
			posY = 30;
		else if(posY >= TankClient.FRAME_HEIGHT - WIDTH) 
			posY = TankClient.FRAME_HEIGHT - WIDTH;
		if(!good) {
			if(step == 0) {
				step = random.nextInt(10) + 10;
				changeDirRandom();
			}
			step--;
			if(step % 16 == 0)
				fire();
		}
	}
	
	/**
	 * 随机改变方向
	 */
	private void changeDirRandom(){
		int n = random.nextInt(8);
		this.dir = this.ptDir = Direction.values()[n];
	}
	
	/**
	 * 画炮筒
	 */
	private void drawPt(Graphics g){
		g.setColor(Color.BLACK);
		switch(ptDir) {
		case LEFT:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX, posY + WIDTH / 2);
			break;
		case RIGHT:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX + WIDTH, posY + WIDTH / 2);
			break;
		case DOWN:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX + WIDTH / 2, posY + WIDTH);
			break;
		case UP:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX + WIDTH / 2, posY);
			break;
		case LU:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX, posY);
			break;
		case UR:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX + WIDTH, posY);
			break;
		case RD:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX + WIDTH, posY + WIDTH);
			break;
		case DL:
			g.drawLine(posX + WIDTH / 2, posY + WIDTH / 2, posX, posY + WIDTH);
			break;
		case STOP:
			break;
		}
	}
	
	private Missile fire() {
		if(!live) {
			return null;
		}
		int x = posX + WIDTH / 2 - Missile.WIDTH / 2;
		int y = posY + WIDTH / 2 - Missile.WIDTH / 2;
		Missile m = new Missile(x, y, good, ptDir, tankClient);
		this.tankClient.addMissle(m);
		if(good) {
			SoundUtils.getInstance().playFireSound();
		}
		return m;
	}
	
	private void superFire() {
		if(!live) {
			return;
		}
		Direction[] dirs = Direction.values();
		int x = posX + WIDTH / 2 - Missile.WIDTH / 2;
		int y = posY + WIDTH / 2 - Missile.WIDTH / 2;
		for(int i = 0; i < dirs.length - 1; i++) {
			Missile m = new Missile(x, y, good, dirs[i], tankClient);
			this.tankClient.addMissle(m);
		}
		if(good) {
			SoundUtils.getInstance().playFireSound();
		}
	}
	
	public void beingHit(){
		life -= 20;
		if(life <= 0) {
			this.live = false;
			this.tankClient.removeTank(this);
		}
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public Rectangle getRect(){
		return new Rectangle(posX, posY, WIDTH, WIDTH);
	}
	
	/**
	 * 即将碰撞时回到上一步
	 */
	public void toPreStep(){
		posX = lastX;
		posY = lastY;
	}
	
	private boolean collideWithWall(Wall wall) {
		if(live && getRect().intersects(wall.getRect())) {
			//碰到墙了，回到上一步
			toPreStep();
			return true;
		}
		return false;
	}
	
	public void collideWithWalls(List<Wall> walls) {
		for(int i = 0; i < walls.size(); i++) {
			collideWithWall(walls.get(i));
		}
	}
	
	private boolean collideWithTank(Tank tank) {
		if(this != tank && live && tank.isLive() && getRect().intersects(tank.getRect())) {
			this.toPreStep();
			tank.toPreStep();
			return true;
		}
		return false;
	}
	
	public void collideWithTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			collideWithTank(tanks.get(i));
		}
	}
	
	public boolean eatBlood(Blood blood) {
		if(live && good && blood.isLive() && getRect().intersects(blood.getRect())) {
			blood.setLive(false);
			this.life = 100;
			SoundUtils.getInstance().playEatBloodSound();
			return true;
		}
		return false;
	}
	
}
