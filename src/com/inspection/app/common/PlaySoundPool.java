package com.inspection.app.common;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 提示声音播放类
 */
public class PlaySoundPool {

	private Context context;

	public PlaySoundPool(Context context) {
		this.context = context;
		initSounds();
	}

	// 音效的音量
	int streamVolume;

	// 定义SoundPool 对象
	private SoundPool soundPool;

	// 定义HASH表
	private HashMap<Integer, Integer> soundPoolMap;

	public void initSounds() {
		// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
		soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);

		// 初始化HASH表
		soundPoolMap = new HashMap<Integer, Integer>();

		// 获得声音设备和设备音量
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	public void loadSfx(int raw, int ID) {
		// 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
		soundPoolMap.put(ID, soundPool.load(context, raw, ID));
	}

	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
	}
}
