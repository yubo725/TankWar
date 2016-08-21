package com.tankwar.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankwar.domain.Blood;
import com.tankwar.domain.Explode;
import com.tankwar.domain.Missile;
import com.tankwar.domain.Tank;
import com.tankwar.domain.Wall;
import com.tankwar.net.TankClientUtils;
import com.tankwar.net.TankServer;
import com.tankwar.utils.PropUtils;

public class TankClient extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
	
	private Tank myTank = new Tank(100, 100, true, this);
	
	private List<Missile> missiles = new ArrayList<Missile>();
	private List<Explode> explodes = new ArrayList<Explode>();
	private List<Tank> tanks = new ArrayList<Tank>();
	private List<Wall> walls = new ArrayList<Wall>();
	private int killCount = 0;
	
	private Blood blood = new Blood();
	private TankClientUtils tankClientUtils;
	private Image offScreenImage;
	
	public TankClient() {
		tankClientUtils = new TankClientUtils();
	}
	
	private void addTanks(){
		tanks.add(myTank);
		for(int i = 0; i < PropUtils.getInstance().getTankInitCount(); i++) {
			Tank t = new Tank(400, 60 + 60 * i, false, this);
			tanks.add(t);
		}
	}
	
	public void launchFrame() {
		addTanks();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = dimension.width;
		int screenHeight = dimension.height;
		int x = (screenWidth - FRAME_WIDTH) / 2;
		int y = (screenHeight - FRAME_HEIGHT) / 2;
		this.setBounds(x, y, FRAME_WIDTH, FRAME_HEIGHT);
		this.setTitle("Tank War");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addGround();
		this.setResizable(false);
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		new RepaintThread().start();
//		tankClientUtils.connectServer("localhost", TankServer.TCP_PORT);
	}
	
	private void addGround(){
		URL url = TankClient.class.getClassLoader().getResource("images/ground_bg.png");
		JLabel backgroundLabel = new JLabel(new ImageIcon(url));
		backgroundLabel.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		JPanel jpanel = (JPanel) this.getContentPane();
		jpanel.setOpaque(false);
		this.getLayeredPane().setLayout(null);
		this.getLayeredPane().add(backgroundLabel, Integer.MIN_VALUE);
	}
	
	@Override
	public void paint(Graphics g) {
		if(offScreenImage == null)
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		Graphics og = offScreenImage.getGraphics();
		og.setColor(og.getColor());
		og.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		super.paint(og);
		drawLogs(og);
		drawTanks(og);
		drawMissiles(og);
		drawExplodes(og);
		drawWalls(og);
		blood.drawMe(og);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	/**
	 * 敌方坦克全部消灭后，再创建5辆新坦克
	 */
	public void recreateEnermyTanks(){
		for(int i = 0; i < 5; i++) {
			Tank t = new Tank(400, 60 + 80 * i, false, this);
			tanks.add(t);
		}
	}
	
	private void drawLogs(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("missiles count: " + missiles.size(), 10, 50);
		g.drawString("explodes count: " + explodes.size(), 10, 70);
		g.drawString("tanks count: " + tanks.size(), 10, 90);
		g.drawString("kill tank count: " + killCount, 10, 110);
	}
	
	private void drawTanks(Graphics g) {
		myTank.drawMe(g);
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collideWithWalls(walls);
			t.collideWithTanks(tanks);
			t.eatBlood(blood);
			t.drawMe(g);
		}
		if(tanks.size() == 1 && tanks.get(0).isGood()) {
			//如果只有我一辆坦克活着，就再增加一些敌军坦克
			recreateEnermyTanks();
		}
	}
	
	private void drawMissiles(Graphics g) {
		for(int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitWalls(walls);
			m.drawMe(g);
		}
	}
	
	private void drawExplodes(Graphics g) {
		for(int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.drawMe(g);
		}
	}
	
	private void drawWalls(Graphics g) {
		Wall wall1 = new Wall(200, 100, 1, 15);
		wall1.drawMe(g);
		Wall wall2 = new Wall(600, 200, 1, 15);
		wall2.drawMe(g);
		walls.add(wall1);
		walls.add(wall2);
	}
	
	private class RepaintThread extends Thread {
		@Override
		public void run() {
			super.run();
			while(true) {
				repaint();
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			myTank.keyPressed(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			myTank.keyReleased(e);
		}
	}
	
	public void addMissle(Missile m) {
		if(m != null)
			missiles.add(m);
	}
	
	public void addExplode(Explode e) {
		if(e != null)
			explodes.add(e);
	}
	
	public void addTank(Tank tank) {
		if(tank != null)
			tanks.add(tank);
	}
	
	public void removeMissile(Missile m) {
		missiles.remove(m);
	}
	
	public void removeExplode(Explode e) {
		explodes.remove(e);
	}
	
	public void removeTank(Tank t) {
		tanks.remove(t);
		if(t != myTank) 
			killCount++;
	}
	
	static enum Dir{LEFT, RIGHT};

	public static void main(String[] args) {
		new TankClient().launchFrame();
	}
	
}
