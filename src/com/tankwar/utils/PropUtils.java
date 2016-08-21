package com.tankwar.utils;

import java.util.Properties;

public class PropUtils {
	
	private Properties properties;
	public static final int DEF_TANK_INIT_COUNT = 8;
	public static final int DEF_TANK_RECREATE_COUNT = 3;
	private static PropUtils instance;
	
	private PropUtils(){
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("init.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PropUtils getInstance() {
		if(instance == null)
			instance = new PropUtils();
		return instance;
	}
	
	public int getTankInitCount() {
		if(properties != null) {
			String str = properties.getProperty("initTankCount");
			if(str != null && !"".equals(str.trim())){
				int n;
				try {
					n = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					n = DEF_TANK_INIT_COUNT;
				}
				return n; 
			}
		}
		return DEF_TANK_INIT_COUNT;
	}
	
	public int getRecreateTankCount() {
		if(properties != null) {
			String str = properties.getProperty("recreateTankCount");
			if(str != null && !"".equals(str.trim())) {
				int n;
				try {
					n = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					n = DEF_TANK_INIT_COUNT;
				}
				return n; 
			}
		}
		return DEF_TANK_INIT_COUNT;
	}
	
}
