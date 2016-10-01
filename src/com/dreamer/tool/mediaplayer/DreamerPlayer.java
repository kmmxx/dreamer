/**
 * DreamerPlayer.java
 * TODO
 */
package com.dreamer.tool.mediaplayer;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.ViewGroup;

/**
 * @author dreamer 2014-3-26
 * 
 */
public class DreamerPlayer {

	public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 0xc8;
	public static final int MEDIA_ERROR_SERVER_DIED = 0x64;
	public static final int MEDIA_ERROR_UNKNOWN = 0x01;
	public static final int MEDIA_INFO_BAD_INTERLEAVING = 0x320;
	public static final int MEDIA_INFO_METADATA_UPDATE = 0x322;
	public static final int MEDIA_INFO_NOT_SEEKABLE = 0x321;
	public static final int MEDIA_INFO_UNKNOWN = 0x01;
	public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 0x2bc;
	public static final int MEDIA_INFO_BUFFERING_END = 0x2be;
	public static final int MEDIA_INFO_BUFFERING_START = 0x2bd;

	public static final int MUSIC_SINGLE = 0;
	public static final int MUSIC_LIST = 1;
	public static final int MUSIC_LOOP = 2;
	public static final int MUSIC_RANDOM = 3;

	public static final int MESSAGE_MUSIC_SINGLE = 0x101;
	public static final int MESSAGE_MUSIC_LIST = 0x102;
	public static final int MESSAGE_MUSIC_RANDOM = 0x103;
	public static final int MESSAGE_MUSIC_LOOP = 0x104;
	public static final int MESSAGE_MUSIC_LAST = 0x105;
	public static final int MESSAGE_MUSIC_FIRST = 0x106;
	public static final int MESSAGE_MUSIC_START = 0x107;
	public static final int MESSAGE_MUSIC_COMPLETE = 0x108;

	private static final int CMD_STOP_FASTPLAY = 199;

	private boolean isFastOrBack = false; // 当前是否正在快进快退
	private boolean isPrepare = false; // 播放器是否已经准备好
	private boolean isSubtitleVisible;// 当前是否已经打开字幕
	private List<String> playUrlList; // 播放列表
	private int playMode = -1;
	/** 播放窗口相关数据 */
	SurfaceView mSurfaceView;

	MediaPlayer mMediaPlayer;
	OnBufferingUpdateListener mOnBufferingUpdateListener;
	OnErrorListener mOnErrorListener;
	OnDreamerPlayerListener mOnDreamerPlayerListener;
	OnInfoListener mOnInfoListener;
	OnSeekCompleteListener mOnSeekCompleteListener;
	OnVideoSizeChangedListener mOnVideoSizeChangedListener;

	private Activity activity;
	private int videoWidth = 1280;
	private int videoHeight = 720;
	protected int index = 0;
	private int playSpeed = 0;
	private Timer mTimer;
	private long timePeroid = 1000;
	private long timeDelay = 500;
	protected boolean isSurfaceCreated;

	public DreamerPlayer(Activity activity) {
		this.activity = activity;
		mMediaPlayer = new MediaPlayer();
	}

	DreamerPlayer(MediaPlayer mediaPlayer) {
		mMediaPlayer = mediaPlayer;
	}

	public void setVideoSize(int width, int height) {
		this.videoWidth = width;
		this.videoHeight = height;
	}

	// 初始化视频播放窗口
	public void initDreamerPlayer(ViewGroup layout) {
		playUrlList = new ArrayList<String>();
		mSurfaceView = new SurfaceView(activity);
		layout.addView(mSurfaceView);
		videoWidth = layout.getWidth();
		videoHeight = layout.getHeight();
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener
							.handleMessage(MESSAGE_MUSIC_COMPLETE);
				}
				playNext();
			}
		});
		mSurfaceView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// if (playUrlList != null && index < playUrlList.size()) {
				// playUrl(playUrlList.get(index));
				// }
				isSurfaceCreated = true;
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// videoWidth = width;
				// videoHeight = height;
				Log.d(TAG, "videoWidth:" + videoWidth + "videoHeight:"
						+ videoHeight);
			}
		});
	}

	public boolean isSurfaceCreated() {
		return isSurfaceCreated;
	}

	public void setSurfaceCreated(boolean isSurfaceCreated) {
		this.isSurfaceCreated = isSurfaceCreated;
	}

	public boolean isPrepare() {
		return isPrepare;
	}

	public void setPrepare(boolean isPrepare) {
		this.isPrepare = isPrepare;
	}

	// 播放视频
	public boolean playUrl(String url) {
		try {
			Log.i(TAG, "*****************enter playVideo path:" + url);
			if (url == null || "".equals(url) || !isSurfaceCreated) {
				return false;
			}
			isPrepare = false;
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(url);
			/* 设置通过surfaceview来显示画面 */
			mMediaPlayer.setDisplay(mSurfaceView.getHolder());
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			/* 准备播放 */
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					isPause = false;
					isPrepare = true;
					if (mOnDreamerPlayerListener != null) {
						mOnDreamerPlayerListener
								.handleMessage(MESSAGE_MUSIC_START);
					}
					mSurfaceView.getHolder().setFixedSize(videoWidth,
							videoHeight);
					mMediaPlayer.start();
					isFastOrBack = false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "play error,exception:" + e);
			return false;
		}
		return true;
	}

	public static DreamerPlayer create(Context context, int resid) {
		return new DreamerPlayer(MediaPlayer.create(context, resid));
	}

	public static DreamerPlayer create(Context context, Uri uri) {
		return new DreamerPlayer(MediaPlayer.create(context, uri));
	}

	public static DreamerPlayer create(Context context, Uri uri,
			SurfaceHolder holder) {
		return new DreamerPlayer(MediaPlayer.create(context, uri, holder));
	}

	public List<String> getPlayUrlList() {
		return playUrlList;
	}

	public void setPlayUrlList(List<String> playUrlList) {
		this.playUrlList = playUrlList;
	}

	public boolean isSubtitleVisible() {
		return isSubtitleVisible;
	}

	public void setSubtitleVisible(boolean isSubtitleVisible) {
		this.isSubtitleVisible = isSubtitleVisible;
	}

	public void setAudioStreamType(int type) {
		mMediaPlayer.setAudioStreamType(type);
	}

	public void setDataSource(FileDescriptor fd) throws IOException {
		mMediaPlayer.setDataSource(fd);
	}

	public void setDataSource(String arg0) throws IOException {
		mMediaPlayer.setDataSource(arg0);
	}

	public void setDataSource(Context context, Uri uri) throws IOException {
		mMediaPlayer.setDataSource(context, uri);
	}

	public void setDataSource(FileDescriptor arg0, long arg1, long arg2)
			throws IOException {
		mMediaPlayer.setDataSource(arg0, arg1, arg2);
	}

	public void setDisplay(SurfaceHolder sh) {

		mMediaPlayer.setDisplay(sh);
	}

	public void setLooping(boolean arg0) {
		mMediaPlayer.setLooping(arg0);
	}

	public void setScreenOnWhilePlaying(boolean screenOn) {
		mMediaPlayer.setScreenOnWhilePlaying(screenOn);
	}

	public void setVolume(float arg0, float arg1) {
		mMediaPlayer.setVolume(arg0, arg1);
	}

	public void setWakeMode(Context context, int mode) {
		mMediaPlayer.setWakeMode(context, mode);
	}

	public int getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}

	public int getDuration() {
		return mMediaPlayer.getDuration();
	}

	public int getVideoHeight() {
		return mMediaPlayer.getVideoHeight();

	}

	public int getVideoWidth() {
		return mMediaPlayer.getVideoWidth();
	}

	public boolean isLooping() {
		return mMediaPlayer.isLooping();
	}

	public boolean isPalying() {
		return mMediaPlayer.isPlaying();
	}

	public String getPlayingUrl(int i) {
		if (playUrlList == null || i < 0 || i > playUrlList.size() - 1) {
			return null;
		}
		return playUrlList.get(i);
	}

	public boolean isFastOrBack() {
		return isFastOrBack;
	}

	public void setFastOrBack(boolean isFastOrBack) {
		this.isFastOrBack = isFastOrBack;
	}

	public MediaPlayer getmMediaPlayer() {
		return mMediaPlayer;
	}

	public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
		this.mMediaPlayer = mMediaPlayer;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	/**
	 * 播控接口
	 * *********************************************************************
	 * **************************************
	 **/
	public void pause() {
		mMediaPlayer.pause();
		isPause = true;
		isFastOrBack = false;
	}

	public void prepare() throws IOException {
		mMediaPlayer.prepare();
		isPause = false;
		isFastOrBack = false;
	}

	public void prepareAsync() {
		mMediaPlayer.prepareAsync();
		isFastOrBack = false;
		isPause = false;
	}

	public void release() {
		mMediaPlayer.release();
		mMediaPlayer = null;
		isPause = false;
	}

	public void seekTo(int arg0) {
		mMediaPlayer.seekTo(arg0);
		isPause = false;
	}

	/**
	 * 以指定速度seek到一个点位
	 * 
	 * @param playSpeed
	 */
	public void seek(int playSpeed) {
		try {
			isFastOrBack = false;
			setPlaySpeed(playSpeed);
			int current = mMediaPlayer.getCurrentPosition();
			int total = mMediaPlayer.getDuration();
			int speedTime = playSpeed * 1000;
			Log.i(TAG, "***current:" + current + " total:" + total
					+ " speedTime:" + speedTime + "playSpeed:" + playSpeed);
			if (current + speedTime > total) {
				mMediaPlayer.seekTo(total);
				setPlaySpeed(0);
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener
							.handleMessage(MESSAGE_MUSIC_COMPLETE);
				}
			} else if (current + speedTime < 0) {
				mMediaPlayer.seekTo(0);
				setPlaySpeed(0);
			} else {
				mMediaPlayer.seekTo(current + speedTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以当前速度seek到一个点位
	 * 
	 * @param playSpeed
	 */
	public void seek() {
		try {
			setPlaySpeed(playSpeed);
			int current = mMediaPlayer.getCurrentPosition();
			int total = mMediaPlayer.getDuration();
			int speedTime = playSpeed * 1000;
			Log.i(TAG, "***current:" + current + " total:" + total
					+ " speedTime:" + speedTime + "playSpeed:" + playSpeed);
			if (current + speedTime > total) {
				mMediaPlayer.seekTo(total);
				setPlaySpeed(0);
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener
							.handleMessage(MESSAGE_MUSIC_COMPLETE);
				}
			} else if (current + speedTime < 0) {
				mMediaPlayer.seekTo(0);
				setPlaySpeed(0);
			} else {
				mMediaPlayer.seekTo(current + speedTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置播放倍数
	 * 
	 * @param speed
	 *            当前参数合法值为：-32, -16, -8, -4, -2, 0, 2, 4, 8, 16, 32
	 *            其中，负值表示以相应倍速快退(FR)，0表示正常播放，正值以相应倍速表示快进(FF)
	 * 
	 * @return ture表示设置成功，false表示设置失败
	 */

	public int getPlaySpeed() {
		return playSpeed;
	}

	public void setPlaySpeed(int playSpeed) {
		this.playSpeed = playSpeed;
		if (playSpeed == 0) {
			isFastOrBack = false;
		}
	}

	/**
	 * @param i
	 *            >0 快进 <0 快退 ==0
	 */
	public void changePlaySpeed(int i) {
		if (i < 0) {
			if (playSpeed == 0 || playSpeed == 2) {
				setPlaySpeed(-2);
			} else if (playSpeed < 0 && playSpeed > -32) {
				setPlaySpeed(playMode * 2);
			} else if (playSpeed > 2 && playSpeed <= 32) {
				setPlaySpeed(playSpeed / 2);
			}
			isFastOrBack = true;
		} else if (i > 0) {
			if (playSpeed == 0 || playSpeed == -2) {
				setPlaySpeed(2);
			} else if (playSpeed < -2 && playSpeed >= -32) {
				setPlaySpeed(playMode / 2);
			} else if (playSpeed > 0 && playSpeed < 32) {
				setPlaySpeed(playSpeed * 2);
			}
			isFastOrBack = true;
		}
	}

	public void start() {
		isPause = false;
		isFastOrBack = false;
		mMediaPlayer.start();
	}

	public void stop() {
		mMediaPlayer.stop();
		isPause = false;
		isFastOrBack = false;
	}

	/**
	 * *************************************************************************
	 * ******************************************
	 **/

	/**
	 * 依赖厂商实现
	 * *******************************************************************
	 * ****************************************
	 **/
	/**
	 * 返回当前播放文件的PTS值
	 * 
	 * @return PTS值 > 0
	 */
	public long getCurrentPts() {
		return 1;
	}

	/**
	 * 返回当前播放文件的音轨总数
	 * 
	 * @return 音轨总数 > 0
	 */
	public int getAudioTrackNum() {
		return 1;
	}

	/**
	 * 设置当前播放文件的音轨
	 * 
	 * @param track
	 *            音频轨道序号track，其中track的范置为：0~(getAudioTrackNum()-1)
	 * 
	 * @return true表示设置成功，false表示设置失败
	 */
	public boolean setAudioTrack(int track) {
		return false;
	}

	public void resume() {
		isPause = false;
		isFastOrBack = false;
		mMediaPlayer.start();
	}

	public void setPlayMode(int speed) {
		this.playMode = speed;
	}

	public int getPlayMode() {
		return playMode;
	}

	public void removeVideo(String url) {
		playUrlList.remove(url);
	}

	public void addVideo(String url) {
		playUrlList.add(url);
	}

	// 手动点击按键播放下一视频
	public void playNext() {
		if (playUrlList != null && playUrlList.size() > 0) {
			Log.d(TAG, "playNextVideo:" + getPlayMode());
			changeIndex(1, getPlayMode());
		}
	}

	// 手动点击按键播放上一视频
	public void playPrev() {
		if (playUrlList != null && playUrlList.size() > 0) {
			Log.d(TAG, "playPrevVideo:" + getPlayMode());
			changeIndex(-1, getPlayMode());
		}
	}

	public String getTotalTime() {
		return SecondeToHour(mMediaPlayer.getDuration() / 1000);
	}

	public String getCurrentTime() {
		// TODO Auto-generated method stub
		return SecondeToHour(mMediaPlayer.getCurrentPosition() / 1000);
	}

	public int getProgress() {
		return (int) (100 * (float) mMediaPlayer.getCurrentPosition() / mMediaPlayer
				.getDuration());
	}

	/**
	 * 获得时间字符串
	 * 
	 * @param time
	 * @return
	 */
	public String SecondeToHour(int time) {
		// 分别得到时分秒
		int h = time / 3600;
		int m = time / 60 % 60;
		int s = time % 60;
		return String.format("%02d:%02d:%02d", h, m, s);
	}

	public void changeIndex(int i, int mode) {
		boolean hasPlay = true;
		synchronized (this) {
			index += i;
			switch (mode) {
			case MUSIC_SINGLE:
				index = 0;
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener
							.handleMessage(MESSAGE_MUSIC_SINGLE);
				}
				hasPlay = false;
				break;
			case MUSIC_LIST:
				if (index < 0) {
					index = 0;
					hasPlay = false;
					if (mOnDreamerPlayerListener != null) {
						mOnDreamerPlayerListener
								.handleMessage(MESSAGE_MUSIC_FIRST);
					}
				}
				if (index > playUrlList.size() - 1) {
					index = playUrlList.size() - 1;
					hasPlay = false;
					if (mOnDreamerPlayerListener != null) {
						mOnDreamerPlayerListener
								.handleMessage(MESSAGE_MUSIC_LAST);
					}
				}
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener.handleMessage(MESSAGE_MUSIC_LIST);
				}
				break;
			case MUSIC_RANDOM:
				index = getRandomNextInt(index, playUrlList.size());
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener
							.handleMessage(MESSAGE_MUSIC_RANDOM);
				}
				break;
			case MUSIC_LOOP:
				if (index < 0) {
					index = playUrlList.size() - 1;
				}
				if (index > playUrlList.size() - 1) {
					index = 0;
				}
				if (mOnDreamerPlayerListener != null) {
					mOnDreamerPlayerListener.handleMessage(MESSAGE_MUSIC_LOOP);
				}
				hasPlay = true;
				break;
			default:
				break;
			}
		}
		Log.d(TAG, "playVideo:" + index);
		if (hasPlay) {
			String path = playUrlList.get(index);
			playUrl(path);
		}

	}

	/** 从指定整数list的得到一个随机数且得到的该数不能与指定数字相等 */
	public int getRandomNextInt(int item, int max) {
		int next = -1;
		do {
			next = new Random().nextInt(max);
		} while (next == item);
		return next;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int i) {
		synchronized (this) {
			this.index = i;
		}
	}

	/**
	 * *************************************************************************
	 * ***********************************************
	 **/

	/**
	 * 消息监听器设置
	 * ******************************************************************
	 * *****************************************
	 **/

	public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
		mOnBufferingUpdateListener = listener;
		mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
	}

	public interface OnStartPlayListener {
		public void onStartPlayListener(String playUrl);
	}

	public void setOnErrorListener(OnErrorListener listener) {
		mOnErrorListener = listener;
		mMediaPlayer.setOnErrorListener(mOnErrorListenerImpl);
	}

	public interface OnErrorListener {

		public boolean onError(DreamerPlayer arg0, int arg1, int arg2);
	}

	MediaPlayer.OnErrorListener mOnErrorListenerImpl = new MediaPlayer.OnErrorListener() {

		public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			if (mOnErrorListener == null || arg0 != mMediaPlayer)
				return false;
			return mOnErrorListener.onError(DreamerPlayer.this, arg1, arg2);
		}
	};

	public void setOnInfoListener(OnInfoListener listener) {
		mOnInfoListener = listener;
		mMediaPlayer.setOnInfoListener(mOnInfoListenerImpl);
	}

	public interface OnInfoListener {

		public boolean onInfo(DreamerPlayer arg0, int arg1, int arg2);
	}

	MediaPlayer.OnInfoListener mOnInfoListenerImpl = new MediaPlayer.OnInfoListener() {

		public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			if (mOnInfoListener == null || arg0 != mMediaPlayer)
				return false;
			return mOnInfoListener.onInfo(DreamerPlayer.this, arg1, arg2);
		}
	};
	protected String TAG = "DreamerPlayer";

	public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
		mOnSeekCompleteListener = listener;
		mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListenerImpl);
	}

	public interface OnSeekCompleteListener {

		public void onSeekComplete(DreamerPlayer arg0);
	}

	MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListenerImpl = new MediaPlayer.OnSeekCompleteListener() {

		public void onSeekComplete(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			if (mOnSeekCompleteListener == null || arg0 != mMediaPlayer)
				return;
			mOnSeekCompleteListener.onSeekComplete(DreamerPlayer.this);
		}
	};

	public void setOnVideoSizeChangedListener(
			OnVideoSizeChangedListener listener) {
		mOnVideoSizeChangedListener = listener;
		mMediaPlayer
				.setOnVideoSizeChangedListener(mOnVideoSizeChangedListenerImpl);
	}

	public interface OnVideoSizeChangedListener {

		public void onVideoSizeChanged(DreamerPlayer arg0, int arg1, int arg2);
	}

	MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListenerImpl = new MediaPlayer.OnVideoSizeChangedListener() {

		public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			if (mOnVideoSizeChangedListener == null || arg0 != mMediaPlayer)
				return;
			mOnVideoSizeChangedListener.onVideoSizeChanged(DreamerPlayer.this,
					arg1, arg2);
		}
	};
	/**
	 * *************************************************************************
	 * ************************************************
	 **/
	MyOnFastBackwordCompleteListener mOnFastBackwordCompleteListener;
	private boolean isPause = false;
	private TimerTask mTimerTask;

	public void setOnFastBackwordCompleteListener(
			MyOnFastBackwordCompleteListener listener) {
		mOnFastBackwordCompleteListener = listener;
	}

	public interface MyOnFastBackwordCompleteListener {

		public void OnFastBackwordCompleteListener(DreamerPlayer arg0);
	}

	public boolean isPause() {
		// TODO Auto-generated method stub
		return isPause;
	}

	public void setOnDreamerPlayerListener(
			OnDreamerPlayerListener mOnDreamerPlayerListener) {
		this.mOnDreamerPlayerListener = mOnDreamerPlayerListener;
	}

	public interface OnDreamerPlayerListener {
		public void handleMessage(int msg);
	}

	public String getCurrentPlayingUrl() {
		// TODO Auto-generated method stub
		if (playUrlList != null && index < playUrlList.size()) {
			return playUrlList.get(index);
		}
		return null;
	}

	/**
	 * 以每次增加2倍速快退
	 */
	public void backward() {
		// TODO Auto-generated method stub
		changePlaySpeed(-1);
		startFastBackTimer();
	}

	public void startFastBackTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isFastOrBack) {
					seek();
				} else {
					mTimerTask.cancel();
					mTimerTask = null;
				}
			}
		};
		getTimer().schedule(mTimerTask, timeDelay, timePeroid);
	}

	public long getTimePeroid() {
		return timePeroid;
	}

	public void setTimePeroid(long timePeroid) {
		this.timePeroid = timePeroid;
	}

	public long getTimeDelay() {
		return timeDelay;
	}

	public void setTimeDelay(long timeDelay) {
		this.timeDelay = timeDelay;
	}

	/**
	 * 以每次增加2倍速快进
	 */
	public void forward() {
		// TODO Auto-generated method stub
		changePlaySpeed(1);
		startFastBackTimer();
	}

	public Timer getTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		return mTimer;
	}

	public void cancelTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

}