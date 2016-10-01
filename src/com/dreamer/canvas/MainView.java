/**
 * 
 */
package com.dreamer.canvas;

import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.ipanel.game.flagsecret.BasicView;
import com.ipanel.game.flagsecret.FlagSecret;
import com.ipanel.game.flagsecret.util.DefaultTimer;
import com.ipanel.game.flagsecret.util.Desplitor;
import com.ipanel.game.flagsecret.util.KeyCode;
import com.ipanel.game.flagsecret.util.MusicPlayer;

/**
 * @author wx
 * 
 */
public class MainView extends BasicView {

	// 游戏中的若干状态
	public static final int STATE_LOAD = 0x00;

	public static final int STATE_MAIN = 0x01;

	public static final int STATE_BTN = 0x03;

	public static final int STATE_ABOUTSTR = 0x02;

	/**
	 * 定义Scale动画
	 */
	private Animation mAnimationScale;

	boolean isAnimation = false;

	int state;

	Desplitor des;

	TimerTask moveTask, aniTask, btnTask, timeTask;

	int loadFrame, anFrame;

	private float loadStatus = 0.0f;

	int btnIndex;

	int movelX, moverX;

	int anAlpha, aboutAlpha;

	int fStartX, fEndX, btnX, btnY;

	int btnRect;
	
	MusicPlayer musicPlayer;

	public MainView(Context context,MusicPlayer musicPlayer) {
		super(context);
		this.musicPlayer = musicPlayer;
		musicPlayer.loadAllMusic();
	}

	public void setAnimation(boolean isAnimation) {
		this.isAnimation = isAnimation;
	}

	/**
	 * view动画
	 */
	public void animation() {
		mAnimationScale = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mAnimationScale.setDuration(500);
		this.startAnimation(mAnimationScale);
	}

	public void alphaAnimation() {
		if (!isAnimation)
			return;
		mAnimationScale = new AlphaAnimation(0.0f, 1.0f);
		mAnimationScale.setDuration(500);
		this.startAnimation(mAnimationScale);
		isAnimation = false;
		System.gc();
	}

	protected void initView(Context context) {
		des = Desplitor.getInstance();
		des.setContext(context);
		des.initLoadImg();
		setBackgroundImg(des.getLoad_bg());

		loadFrame = 0;
		anFrame = 0;
		anAlpha = 250;

		btnIndex = 1;
		btnX = 355;
		btnY = 420;
//		fStartX = fEndX = btnX - 2 + btnRect;

		aboutAlpha = 0;

		state = STATE_LOAD;

		start();
	}

	private void init() {
		setBackgroundImg(des.getMain_bg0());

		movelX = 640 - des.getMain_move_l().getWidth();
		moverX = 640;

		btnRect = des.getMain_btn()[0].getWidth() + 110;
		
		fStartX = fEndX = btnX - 2 + btnRect;
	}

	protected void onDraw(Canvas c) {
		drawBackground(c);
		switch (state) {
		case STATE_LOAD:
			drawLoading(c);
			break;
		case STATE_MAIN:
			drawMenuBtn(c);
			drawMoveDoor(c);
			drawDoor(c);
			drawAni(c);
			break;
		case STATE_ABOUTSTR:
			drawMenuBtn(c);
			drawDoor(c);
			drawAbout(c);
			break;
		}
	}

	private void drawLoading(Canvas c) {

		int x = (1280 - des.getLoad_animation()[loadFrame].getWidth()) >> 1;
		int y = (720 - des.getLoad_animation()[loadFrame].getHeight()) >> 1;

		c.drawBitmap(des.getLoad_animation()[loadFrame], x - 11, y - 40, paint);

		if (loadStatus == 1.0f) {
			state = STATE_MAIN;
			postInvalidate();
		}
	}

	private void drawDoor(Canvas c) {
		c.drawBitmap(des.getMain_door_l(), 0, 0, paint);
		c.drawBitmap(des.getMain_door_r(), 1280 - des.getMain_door_r()
				.getWidth(), 0, paint);
	}

	private void drawMoveDoor(Canvas c) {
		c.drawBitmap(des.getMain_move_l(), movelX, 0, paint);
		c.drawBitmap(des.getMain_move_r(), moverX, 0, paint);
	}

	private void drawAni(Canvas c) {
		int x = (1280 - des.getMain_an_bg().getWidth()) >> 1;
		int y = (720 - des.getMain_an_bg().getHeight()) >> 1;
		paint.setAlpha(anAlpha);
		c.drawBitmap(des.getMain_an_bg(), x, y, paint);
		c.drawBitmap(des.getMain_an()[anFrame], x, y, paint);
		paint.setAlpha(250);
	}

	private void drawMenuBtn(Canvas c) {
		for (int i = 0; i < 3; i++) {
			if (btnIndex == i)
				c.drawBitmap(des.getMain_btn_f()[i], btnX + btnRect * i, btnY,
						paint);
			else
				c.drawBitmap(des.getMain_btn()[i], btnX + btnRect * i, btnY,
						paint);
		}
		c.drawBitmap(des.getMain_focus(), fStartX, btnY, paint);
	}

	private void drawAbout(Canvas c) {
		paint.setAlpha(aboutAlpha);
		c.drawBitmap(des.getMain_about(), (1280 - des.getMain_about()
				.getWidth()) >> 1,
				(720 - des.getMain_about().getHeight()) >> 1, paint);
		paint.setAlpha(250);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (state == STATE_MAIN && keyCode == KeyCode.KEY_BACK) {
			musicPlayer.playMusic(3);
			FlagSecret.midlet.exit();
		} else if (state == STATE_ABOUTSTR && keyCode == KeyCode.KEY_BACK) {
			musicPlayer.playMusic(3);
			startMinuteMonitorHelpOut(STATE_ABOUTSTR, STATE_MAIN);
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean consumed = true;
		switch (state) {
		case STATE_MAIN:
			switch (keyCode) {
			case KeyCode.KEY_LEFT:
				btnIndex--;
				if (btnIndex < 0) {
					btnIndex = 0;
				} else {
					musicPlayer.playMusic(2);
					fStartX = btnX - 2 + (btnIndex + 1) * btnRect;
					fEndX = btnX - 2 + btnIndex * btnRect;
					startMinuteMonitor();
				}
				break;
			case KeyCode.KEY_RIGHT:
				btnIndex++;
				if (btnIndex > 2) {
					btnIndex = 2;
				} else {
					musicPlayer.playMusic(2);
					fStartX = btnX - 2 + (btnIndex - 1) * btnRect;
					fEndX = btnX - 2 + btnIndex * btnRect;
					startMinuteMonitor();
				}
				break;
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				musicPlayer.playMusic(3);
				select(btnIndex);
				break;
			}
			break;
		case STATE_ABOUTSTR:
			if (keyCode == KeyEvent.KEYCODE_ENTER
					|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER
					) {
				musicPlayer.playMusic(3);
				startMinuteMonitorHelpOut(STATE_ABOUTSTR, STATE_MAIN);
			}
			break;
		}
		return consumed;
	}

	private void select(int index) {
		switch (index) {
		case 1:
			state = STATE_MAIN;
			animation();
			FlagSecret.midlet.showDisplayable(FlagSecret.ACTIVE_MENU);
			break;
		case 0:
			aboutAlpha = 0;
			startMinuteMonitorHelpIn(STATE_ABOUTSTR);
			break;
		case 2:
			FlagSecret.midlet.exit();
			break;
		}
	}

	public void start() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					loadStatus += 0.1f;
					loadFrame++;
					postInvalidate();
					des.initMainImg();
					loadStatus += 0.25f;
					loadFrame++;
					postInvalidate();
					Thread.sleep(150);
					des.initMenuImg();
					loadStatus += 0.25f;
					loadFrame++;
					postInvalidate();
					des.initGameImg();
					loadStatus += 0.2f;
					loadFrame++;
					postInvalidate();
					loadStatus += 0.1f;
					loadFrame = 0;
					postInvalidate();
					loadStatus += 0.1f;
					loadFrame++;
					init();
					postInvalidate();
					Thread.sleep(150);
					setBackgroundImg(des.getMain_bg());
					musicPlayer.playMusic(1);
					startDoorMove();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		DefaultTimer.timer.schedule(task, 0);
	}

	/**
	 * 墙体移动
	 */
	private void startDoorMove() {
		if (moveTask != null) {
			moveTask.cancel();
			moveTask = null;
		}
		moveTask = new TimerTask() {
			public void run() {
				movelX -= 2;
				moverX += 2;
				anFrame++;
				if (anFrame > 4)
					anFrame = 0;
				postInvalidate();
				if (movelX < -500) {
					movelX = -500;
					moveTask.cancel();
					moveTask = null;
					startAni();
				}
			}
		};
		DefaultTimer.timer.schedule(moveTask, 5, 5);
	}

	/**
	 * 中间动画
	 */
	private void startAni() {
		if (aniTask != null) {
			aniTask.cancel();
			aniTask = null;
		}
		aniTask = new TimerTask() {
			public void run() {
				anAlpha -= 25;
				postInvalidate();
				if (anAlpha < 0) {
					anAlpha = 0;
					aniTask.cancel();
					aniTask = null;
					postInvalidate();
				}
			}
		};
		DefaultTimer.timer.schedule(aniTask, 50, 50);
	}

	/**
	 * 按键移动
	 */
	private void startMinuteMonitor() {

		if (btnTask != null)
			btnTask.cancel();

		btnTask = new TimerTask() {
			public void run() {
				int movewidth = Math.abs(fEndX - fStartX);
				movewidth >>= 1;
				if (Math.abs(fEndX) - 1 == Math.abs(fStartX)
						|| Math.abs(fEndX) + 1 == Math.abs(fStartX)
						|| fEndX == fStartX) {
					postInvalidate();
					btnTask.cancel();
					return;
				} else {
					if (fEndX > fStartX) {
						fStartX += movewidth;
					} else if (fEndX < fStartX) {
						fStartX -= movewidth;
					}
					postInvalidate();
				}
			}
		};
		DefaultTimer.timer.schedule(btnTask, 0, 50);
	}

	/**
	 * 渐显效果
	 */
	private void startMinuteMonitorHelpIn(final int astate) {
		if (timeTask != null) {
			timeTask.cancel();
			timeTask = null;
		}
		timeTask = new TimerTask() {
			public void run() {
				state = astate;
				postInvalidate();
				aboutAlpha += 25;
				if (aboutAlpha > 250) {
					timeTask.cancel();
					timeTask = null;
					aboutAlpha = 250;
					postInvalidate();
				}
			}
		};
		DefaultTimer.timer.schedule(timeTask, 50, 50);
	}

	/**
	 * 渐隐效果
	 */
	private void startMinuteMonitorHelpOut(final int astate, final int nextState) {
		if (timeTask != null) {
			timeTask.cancel();
			timeTask = null;
		}
		timeTask = new TimerTask() {
			public void run() {
				state = astate;
				postInvalidate();
				aboutAlpha -= 25;
				if (aboutAlpha < 0) {
					timeTask.cancel();
					timeTask = null;
					aboutAlpha = 0;
					state = nextState;
					postInvalidate();
				}
			}
		};
		DefaultTimer.timer.schedule(timeTask, 50, 50);
	}
}
