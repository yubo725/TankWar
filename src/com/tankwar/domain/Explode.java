package com.tankwar.domain;
import java.awt.Graphics;
import java.awt.Image;

import com.tankwar.ui.TankClient;
import com.tankwar.utils.FileUtils;


public class Explode {

	private int x;
	private int y;
	private TankClient tankClient;
	private boolean live;
	private int step = 0;
	private Image[] images = {
			FileUtils.getImage("images/0.gif"),
			FileUtils.getImage("images/1.gif"),
			FileUtils.getImage("images/2.gif"),
			FileUtils.getImage("images/3.gif"),
			FileUtils.getImage("images/4.gif"),
			FileUtils.getImage("images/5.gif"),
			FileUtils.getImage("images/6.gif"),
			FileUtils.getImage("images/7.gif"),
			FileUtils.getImage("images/8.gif"),
			FileUtils.getImage("images/9.gif"),
			FileUtils.getImage("images/10.gif"),
	};
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		live = true;
		this.tankClient = tc;
	}
	
	public void drawMe(Graphics g) {
		if(!live) {
			this.tankClient.removeExplode(this);
			return ;
		}
		if(step >= images.length) {
			step = 0;
			live = false;
			return ;
		}
		g.drawImage(images[step], x, y, null);
		step++;
	}
	
}
