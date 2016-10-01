package com.dreamer.canvas;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FlagSecret extends Activity {

	public static FlagSecret midlet;

	public static final String ACTIVE_MAIN = "active_main";

	public static final String ACTIVE_MENU = "active_menu";

	public static final String ACTIVE_GAME = "active_game";

	public static final String ACTIVE_OVER = "active_over";

	public MainView mainDisplayable;

	public View currentView;

	public Intent intent;

	public FlagSecret() {
		midlet = this;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置为全屏模式
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showDisplayable(FlagSecret.ACTIVE_MAIN);
	}

	public synchronized void showDisplayable(String cmd) {
		if (cmd == null || "".equals(cmd))
			return;
		if (cmd == FlagSecret.ACTIVE_MAIN) {
			if (mainDisplayable == null) {
				mainDisplayable = new MainView(this);
			}
			musicPlayer.setBackgroundMusic(R.raw.main_bg);
			musicPlayer.playBackgroundMusic();
			showDisplayable(mainDisplayable);
			mainDisplayable.alphaAnimation();
		} else if (cmd == FlagSecret.ACTIVE_MENU) {
			if (menuDisplayable == null) {
				menuDisplayable = new MenuView(this,musicPlayer);
			}
			musicPlayer.setBackgroundMusic(R.raw.menu_bg);
			musicPlayer.playBackgroundMusic();
			menuDisplayable.init();
			showDisplayable(menuDisplayable);
			menuDisplayable.alphaAnimation();
		} else if (cmd == FlagSecret.ACTIVE_GAME) {
			if (gameDisplayable == null) {
				gameDisplayable = new GameView(this,musicPlayer);
			}
			musicPlayer.setBackgroundMusic(R.raw.game_bg);
			musicPlayer.playBackgroundMusic();
			gameDisplayable.setMapInfo();
			showDisplayable(gameDisplayable);
			gameDisplayable.alphaAnimation();
		}
	}

	/**
	 * 切换view
	 * 
	 * @param view
	 */
	void showDisplayable(View view) {
		if (view == null)
			return;
		setContentView(view);
		currentView = view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return currentView.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyCode.KEY_ESC) {
			exit();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_MUTE||keyCode == KeyEvent.KEYCODE_VOLUME_DOWN||keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			return false;
		}
		return currentView.onKeyUp(keyCode, event);
	}

	public void exit() {
		musicPlayer.stopBackgroundMusic();
		finish();
		System.exit(0);
	}

}