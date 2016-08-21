package com.tankwar.domain;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;


public class Blood {
	
	private int x;
	private int y;
	public static final int WIDTH = 30;
	public static final int DURATION = 200;
	public static final int COLOR_STAY_STEP = 3;
	public static final int RELIVE_DURATION = 200;
	private int colorStep = 0;
	private int step = DURATION;
	private int posIndex;
	private int lastPosIndex;
	private int reliveStep = RELIVE_DURATION;
	private int currentColorIndex = 0;
	private boolean live;
	private static Random random = new Random();
	private int[][] positions = {
			{50, 100}, {100, 350}, {150, 150}, {200, 500}, 
			{300, 300}, {400, 500}, {500, 180}, {550, 80}, {700, 300}
	};
	private Image bloodImage = Toolkit.getDefaultToolkit().createImage("./images/blood.png");
	private Color[] colors;
	
	public Blood() {
		this.live = false;//初始化时不让显示
		posIndex = random.nextInt(positions.length);
		lastPosIndex = posIndex;
		colors = new Color[]{new Color(255, 0, 0, 255), 
				new Color(255, 0, 0, 200), new Color(255, 0, 0, 150), 
				new Color(255, 0, 0, 100)};
	}
	
	public void drawMe(Graphics g) {
		if(!live) {
			reliveStep--;
			if(reliveStep <= 0) {
				live = true;
				changeBloodPos();
				reliveStep = RELIVE_DURATION;
			}
			return ;
		}
		if(step == 0) {
			step = DURATION;
			changeBloodPos();
		}
		g.setColor(colors[currentColorIndex]);
		x = positions[posIndex][0];
		y = positions[posIndex][1];
		g.fillRect(x, y, WIDTH, WIDTH);
//		g.drawImage(bloodImage, x, y, null);
		step--;
		changeColor();
	}
	
	/**
	 * 切换血块位置
	 */
	private void changeBloodPos(){
		lastPosIndex = posIndex;
		while((posIndex = random.nextInt(positions.length)) == lastPosIndex) ;
	}
	
	private void changeColor(){
		colorStep++;
		if(colorStep >= COLOR_STAY_STEP) {
			colorStep = 0;
			currentColorIndex++;
			if(currentColorIndex >= colors.length) {
				currentColorIndex = 0;
			}
		}
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, WIDTH);
	}
	
}