package com.tankwar.utils;

import java.awt.Image;
import java.awt.Toolkit;

import com.tankwar.domain.Tank;

public class FileUtils {
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public static Image getImage(String path) {
		return toolkit.getImage(Tank.class.getClassLoader().getResource(path));
	}
	
}
