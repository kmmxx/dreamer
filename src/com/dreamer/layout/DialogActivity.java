package com.dreamer.layout;

import java.util.Calendar;

import com.dreamer.R;
import com.dreamer.tool.message.NoticeManager;
import com.dreamer.tool.system.SystemTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DialogActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private String URL = "http://www.baidu.com/";
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dialog);

		NoticeManager.getInstance().prepare(DialogActivity.this);

		initAlertDialog();

//		initLayoutViewDialog();
		AnalogClock clock = (AnalogClock) findViewById(R.id.analogClock);
	}

	private void initLayoutViewDialog() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog,
				(ViewGroup) findViewById(R.id.alert_text));
		new AlertDialog.Builder(this).setTitle("自定义布局").setView(layout)
				.setPositiveButton("确定", null).setNegativeButton("取消", null)
				.show();
	}

	private void initAlertDialog() {
		// TODO Auto-generated method stub
		// 弹出式对话框的 Demo。先调用 Builder()，在 Create()， 需要显示对话框的是后再调用 show()
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("弹出对话框")
				.create();
		dialog.show();

		// 以下是各种对话框的 Demo
		MyButtonClickListener listener = new MyButtonClickListener();
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setText("简单的对话框的 Demo");
		btn1.setOnClickListener(listener);

		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setText("包括常用设置的对话框(数据来自 xml)");
		btn2.setOnClickListener(listener);

		Button btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setText("弹出的对话框的内容是一个 View");
		btn3.setOnClickListener(listener);

		Button btn4 = (Button) this.findViewById(R.id.btn4);
		btn4.setText("日期选择对话框");
		btn4.setOnClickListener(listener);

		Button btn5 = (Button) this.findViewById(R.id.btn5);
		btn5.setText("时间选择对话框");
		btn5.setOnClickListener(listener);

		Button btn6 = (Button) this.findViewById(R.id.btn6);
		btn6.setText("进度条对话框");
		btn6.setOnClickListener(listener);
	}

	class MyButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			// 具体的对话框的实现可以通过重写 onCreateDialog 完成

			switch (v.getId()) {
			case R.id.btn1:
				// DialogActivity.this.showDialog(0);
				// ToastManager.getInstance().showLongText("long");
				// ToastManager.getInstance().showShortText("Short");
				// ToastManager.getInstance().showLongView(
				// ViewTool.inflateView(DialogActivity.this,
				// R.layout.viewpager_item1));
				// ImageView img = new ImageView(DialogActivity.this);
				// img.setBackgroundResource(R.drawable.test);
				// ToastManager.getInstance().showShortView(img);
				Intent intent = new Intent();
				intent.setClassName("com.dreamer",
						"com.dreamer.layout.ViewpagerActivity");
				NoticeManager
						.getInstance()
						.notice(intent,
								0,
								"DreamerTool   test ToastManager.getInstance().notify(intent,",
								R.drawable.test, "kemm",
								"test ToastManager.getInstance().notify(intent,");
				break;
			case R.id.btn2:
				NoticeManager.getInstance().cancel(0);
				DialogActivity.this.showDialog(1);
				break;
			case R.id.btn3:
				DialogActivity.this.showDialog(2);
				break;
			case R.id.btn4:
				DialogActivity.this.showDialog(3);
				break;
			case R.id.btn5:
				DialogActivity.this.showDialog(4);
				break;
			case R.id.btn6:
				DialogActivity.this.showDialog(5);
				break;
			}
		}
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {

		case 0:
			// 一个简单的弹出对话框
			return new AlertDialog.Builder(this).setTitle("这是一个简单的弹出对话框的 Demo")
					.create();

		case 1:
			// 一个相对复杂的弹出对话框
			return new AlertDialog.Builder(this)
					.setTitle("标题")
					// 设置标题
					.setIcon(R.drawable.test)
					// 设置标题图片

					// .setCustomTitle(findViewById(R.id.tab_channel_search))
					// 以一个 View 作为标题
					// .setMessage("信息") // 需要显示的弹出内容
					.setPositiveButton("确定", new OnClickListener() { // 设置弹框的确认按钮所显示的文本，以及单击按钮后的响应行为
								@Override
								public void onClick(DialogInterface a0, int a1) {
									TextView txtMsg = (TextView) DialogActivity.this
											.findViewById(R.id.txtMsg);
									txtMsg.append("单击了对话框上的“确认”按钮\n");
								}
							})
					.setItems(R.array.ary,
							new DialogInterface.OnClickListener() { // 弹框所显示的内容来自一个数组。数组中的数据会一行一行地依次排列
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					// 其他常用方法如下
					.setMultiChoiceItems(new String[] { "item1", "item2" },
							new boolean[] { true, false },
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1, boolean arg2) {
									// TODO Auto-generated method stub

								}
							})
					// .setSingleChoiceItems(arg0, arg1, arg2)
					.setNeutralButton("Neutral", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TextView txtMsg = (TextView) DialogActivity.this
									.findViewById(R.id.txtMsg);
							txtMsg.append("单击了对话框上的“Neutral”按钮\n");
						}
					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TextView txtMsg = (TextView) DialogActivity.this
									.findViewById(R.id.txtMsg);
							txtMsg.append("单击了对话框上的“取消”按钮\n");
						}
					}).create();

		case 2:
			// 弹出对话框为指定的 View 的 Demo
			return new AlertDialog.Builder(this)
					.setTitle("此对话框的内容是一个 View")
					.setView(
							SystemTool.inflateView(DialogActivity.this,
									R.layout.view)).create();

		case 3:
			// 弹出日期选择对话框
			Calendar c = Calendar.getInstance();
			return new DatePickerDialog(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							TextView txtMsg = (TextView) DialogActivity.this
									.findViewById(R.id.txtMsg);
							txtMsg.append("新设置的日期为：" + String.valueOf(year)
									+ "-" + String.valueOf(monthOfYear) + "-"
									+ String.valueOf(dayOfMonth) + "\n");
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DATE));

		case 4:
			// 弹出时间选择对话框
			Calendar c2 = Calendar.getInstance();
			return new TimePickerDialog(this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					TextView txtMsg = (TextView) DialogActivity.this
							.findViewById(R.id.txtMsg);
					txtMsg.append("新设置的时间为：" + String.valueOf(hourOfDay) + ":"
							+ String.valueOf(minute) + "\n");
				}

			}, c2.get(Calendar.HOUR), c2.get(Calendar.MINUTE), true);

		case 5:
			// 弹出进度条对话框
			ProgressDialog progress = new ProgressDialog(this);
			progress.setView(SystemTool.inflateView(DialogActivity.this,
					R.layout.fragment_search));
			progress.setMessage("正在玩命加载..");
			return progress;

		default:
			return null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(DialogActivity.this,
						"goto URL:" + URL + "content:" + content,
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
	};

	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

}
