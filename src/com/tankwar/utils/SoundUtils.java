package com.tankwar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 * 控制播放声音的工具类
 * @author yubo
 *
 */
public class SoundUtils {
	
	private static SoundUtils instance;
	private static AudioStream boomAudioStream;
	private static URL boomUrl;
	private static AudioStream fireAudioStream;
	private static URL fireUrl;
	private static AudioStream eatBloodAudioStream;
	private static URL eatBloodUrl;
	
	private SoundUtils() {
		boomUrl = getClass().getClassLoader().getResource("sounds/boom.wav");
		fireUrl = getClass().getClassLoader().getResource("sounds/fire.wav");
		eatBloodUrl = getClass().getClassLoader().getResource("sounds/eatBlood.wav");
	}
	
	public static SoundUtils getInstance() {
		if(instance == null) 
			instance = new SoundUtils();
		return instance;
	}
	
	public void playBoomSound() {
		try {
			boomAudioStream = new AudioStream(new FileInputStream(new File(boomUrl.getFile())));
			AudioPlayer.player.start(boomAudioStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playFireSound() {
		try {
			fireAudioStream = new AudioStream(new FileInputStream(new File(fireUrl.getFile())));
			AudioPlayer.player.start(fireAudioStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playEatBloodSound() {
		try {
			eatBloodAudioStream = new AudioStream(new FileInputStream(new File(eatBloodUrl.getFile())));
			AudioPlayer.player.start(eatBloodAudioStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
