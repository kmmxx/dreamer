package com.dreamer.tool.mediaplayer;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.util.Log;

public class MusicPlayerManager {
	private Context mContext;
	private MediaPlayer mMediaPlayer;
	private SoundPool mSoundPool;
	private int mCurrentStreamVolume;
	private AudioManager mAudioManager;
	private HashMap<String, Integer> mSoundPoolRawIDMap;
	private int mMaxStreamVolume;
	private int maxStreams = 4;
	private String TAG = "MusicPlayerManager";
	private float volume = 1.0f;
	private int priority = 0;
	private int loop = 0;
	private float rate = 1.0f;
	private boolean bgMusicLoop = true;
	private MusicPlayerManagerCallback mMusicPlayerManagerCallback;
	private static MusicPlayerManager mMusicPlayerManager;

	private MusicPlayerManager() {

	}

	public static MusicPlayerManager getInstance() {
		if (mMusicPlayerManager == null) {
			mMusicPlayerManager = new MusicPlayerManager();
		}
		return mMusicPlayerManager;
	}

	public void prepare(Context ctx) {
		mContext = ctx;
	}

	// 锟斤拷锟斤拷锟斤拷锟斤拷
	public void initSoundPollMusic(int maxStreams) {
		mSoundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
		// 锟斤拷锟斤拷锟斤拷锟斤拷璞革拷锟斤拷璞革拷锟斤拷锟�
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		mCurrentStreamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mMaxStreamVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = (float) mCurrentStreamVolume / (float) mMaxStreamVolume;
	}

	public HashMap<String, Integer> getSoundPoolHashMap() {
		if (mSoundPoolRawIDMap == null)
			mSoundPoolRawIDMap = new HashMap<String, Integer>();
		return mSoundPoolRawIDMap;
	}

	public AudioManager getAudioManager() {
		return mAudioManager;
	}

	public void playSoundPoolMusic(String idstr, int loop) {
		Integer id = getSoundPoolHashMap().get(idstr);
		Log.d(TAG, "soundPoolMusic ID:" + id);
		if (id == null)
			return;
		mSoundPool.play(id, volume, volume, priority, loop, rate);
	}

	public void playSoundPoolMusic(String idstr) {
		Integer id = getSoundPoolHashMap().get(idstr);
		Log.d(TAG, "soundPoolMusic ID:" + id);
		if (id == null)
			return;
		mSoundPool.play(id, volume, volume, priority, loop, rate);
	}

	public void stopSoundPoolMusic(String idstr) {
		Integer id = getSoundPoolHashMap().get(idstr);
		Log.d(TAG, "soundPoolMusic ID:" + id);
		if (id == null)
			return;
		mSoundPool.stop(id);
	}

	// 锟酵凤拷soundPool锟斤拷源
	public void releaseSoundPoolMusic() {
		try {
			if (mSoundPool != null) {
				Log.d(TAG, "releaseSoundPoolMusic start");
//				mSoundPool.release(); //canot release ,may be not response when switch too many times
				mSoundPool = null;
				Log.d(TAG, "releaseSoundPoolMusic success");
			} else {
				Log.d(TAG, "releaseSoundPoolMusic fail");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d(TAG, "releaseSoundPoolMusic exception");
		}

	}

	public void resumeSoundPoolMusic(String idstr) {
		Integer id = getSoundPoolHashMap().get(idstr);
		Log.d(TAG, "soundPoolMusic ID:" + id);
		if (id == null)
			return;
		mSoundPool.resume(id);
	}

	public void pauseSoundPoolMusic(String idstr) {
		Integer id = getSoundPoolHashMap().get(idstr);
		Log.d(TAG, "soundPoolMusic ID:" + id);
		if (id == null)
			return;
		mSoundPool.pause(id);
	}

	// 锟斤拷始锟斤拷锟斤拷频锟斤拷�?
	public void loadAllSoundPoolMusic(String[] audioString, int[] musics) {
		// TODO Auto-generated method stub
		for (int i = 0; i < musics.length; i++) {
			getSoundPoolHashMap().put(audioString[i],
					mSoundPool.load(mContext, musics[i], 1));
			// waitting for loading
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void addSoundPoolMusic(String audioString, int raw) {
		getSoundPoolHashMap().put(audioString,
				mSoundPool.load(mContext, raw, 1));
		// waitting for loading
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void playBgMusic(int id) {
		try {
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer = null;
			}
			mMediaPlayer = MediaPlayer.create(mContext, id);
			mMediaPlayer.setLooping(bgMusicLoop);
			mMediaPlayer.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playMediaPlayerResource(String path) {
		try {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
				mMediaPlayer = null;
			}
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startMediaPlayer() {
		mMediaPlayer.start();
	}

	public MediaPlayer getMediaPlayer() {
		return mMediaPlayer;
	}

	// 停止锟斤拷锟斤拷锟斤拷锟斤拷
	public void stopBgMusic() {
		try {
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				// mMediaPlayer.release();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public int getMaxStreams() {
		return maxStreams;
	}

	public int getCurrentStreamVolume() {
		return mCurrentStreamVolume;
	}

	public int getMaxStreamVolume() {
		return mMaxStreamVolume;
	}

	public void raiseStreamVolume() {
		mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, 0);
	}

	public void lowerStreamVolume() {
		mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, 0);
	}

	// //锟斤拷锟斤拷模式
	// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	// //锟斤拷锟斤拷模式
	// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	// //锟斤拷模�?
	// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

	public void setRingerMode(int audioMode) {
		mAudioManager.setRingerMode(audioMode);
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public boolean isBgMusicLoop() {
		return bgMusicLoop;
	}

	public void releaseAll() {
		releaseSoundPoolMusic();
		stopBgMusic();
	}

	public void setBgMusicLoop(boolean bgMusicLoop) {
		this.bgMusicLoop = bgMusicLoop;
	}

	public void setMusciPlayerManagerCallback(
			MusicPlayerManagerCallback mMusicPlayerManagerCallback) {
		this.mMusicPlayerManagerCallback = mMusicPlayerManagerCallback;
	}

	public interface MusicPlayerManagerCallback {
		// 锟斤拷锟斤拷锟斤拷锟揭拷锟斤拷锟斤拷锟�?
		public abstract void onMediaPlayerCompletion();

		// 锟斤拷锟脚达拷锟斤拷要锟斤拷锟斤拷锟斤拷
		public abstract void onMediaErrorCompletion();
	}

}
