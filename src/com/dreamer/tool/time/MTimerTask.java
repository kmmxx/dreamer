package com.dreamer.tool.time;

import java.util.TimerTask;

public class MTimerTask {

	onRun or;
	TimerTask task;

	public void setTimerTask(TimerTask t) {
		this.task = t;
	}

	public void cancel() {
		// TODO Auto-generated method stub
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	public TimerTask getTimerTask() {
		return task;
	}

	public void run() {
		if (or != null) {
			or.onRun();
		}
	}

	public MTimerTask setOnRun(onRun or) {
		this.or = or;
		return this;
	}

	public interface onRun {
		public void onRun();
	}
}
