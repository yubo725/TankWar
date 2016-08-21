package com.tankwar.domain;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.tankwar.utils.FileUtils;


public class Wall {
	
	private int posX;	
	private int posY;
	private int widthCellsCount;//水平方向的格子个数
	private int heightCellsCount;//垂直方向的格子个数
	private Rectangle rect;
	private Image image;
	
	public Wall(int x, int y, int widthCellsCount, int heightCellsCount) {
		this.posX = x;
		this.posY = y;
		this.widthCellsCount = widthCellsCount;
		this.heightCellsCount = heightCellsCount;
//		this.image = FileUtils.getImage("images/wall_cell.gif");
		this.image = FileUtils.getImage("images/wall_cell2.png");
	}
	
	public void drawMe(Graphics g) {
		g.setColor(Color.BLACK);
		for(int j = 0; j < widthCellsCount; j++) {
			for(int i = 0; i < heightCellsCount; i++) {
				g.drawImage(image, posX, posY + i * 20, null);
			}
		}
		//g.fillRect(posX, posY, width, height);
	}
	
	public Rectangle getRect(){
		if(rect == null) 
			rect = new Rectangle(posX, posY, widthCellsCount * 20, heightCellsCount * 20);
		return rect;
	}

}
