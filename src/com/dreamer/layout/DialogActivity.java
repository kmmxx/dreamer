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
		new AlertDialog.Builder(this).setTitle("�Զ��岼��").setView(layout)
				.setPositiveButton("ȷ��", null).setNegativeButton("ȡ��", null)
				.show();
	}

	private void initAlertDialog() {
		// TODO Auto-generated method stub
		// ����ʽ�Ի���� Demo���ȵ��� Builder()���� Create()�� ��Ҫ��ʾ�Ի�����Ǻ��ٵ��� show()
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("�����Ի���")
				.create();
		dialog.show();

		// �����Ǹ��ֶԻ���� Demo
		MyButtonClickListener listener = new MyButtonClickListener();
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setText("�򵥵ĶԻ���� Demo");
		btn1.setOnClickListener(listener);

		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setText("�����������õĶԻ���(�������� xml)");
		btn2.setOnClickListener(listener);

		Button btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setText("�����ĶԻ����������һ�� View");
		btn3.setOnClickListener(listener);

		Button btn4 = (Button) this.findViewById(R.id.btn4);
		btn4.setText("����ѡ��Ի���");
		btn4.setOnClickListener(listener);

		Button btn5 = (Button) this.findViewById(R.id.btn5);
		btn5.setText("ʱ��ѡ��Ի���");
		btn5.setOnClickListener(listener);

		Button btn6 = (Button) this.findViewById(R.id.btn6);
		btn6.setText("�������Ի���");
		btn6.setOnClickListener(listener);
	}

	class MyButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			// ����ĶԻ����ʵ�ֿ���ͨ����д onCreateDialog ���

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
			// һ���򵥵ĵ����Ի���
			return new AlertDialog.Builder(this).setTitle("����һ���򵥵ĵ����Ի���� Demo")
					.create();

		case 1:
			// һ����Ը��ӵĵ����Ի���
			return new AlertDialog.Builder(this)
					.setTitle("����")
					// ���ñ���
					.setIcon(R.drawable.test)
					// ���ñ���ͼƬ

					// .setCustomTitle(findViewById(R.id.tab_channel_search))
					// ��һ�� View ��Ϊ����
					// .setMessage("��Ϣ") // ��Ҫ��ʾ�ĵ�������
					.setPositiveButton("ȷ��", new OnClickListener() { // ���õ����ȷ�ϰ�ť����ʾ���ı����Լ�������ť�����Ӧ��Ϊ
								@Override
								public void onClick(DialogInterface a0, int a1) {
									TextView txtMsg = (TextView) DialogActivity.this
											.findViewById(R.id.txtMsg);
									txtMsg.append("�����˶Ի����ϵġ�ȷ�ϡ���ť\n");
								}
							})
					.setItems(R.array.ary,
							new DialogInterface.OnClickListener() { // ��������ʾ����������һ�����顣�����е����ݻ�һ��һ�е���������
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					// �������÷�������
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
							txtMsg.append("�����˶Ի����ϵġ�Neutral����ť\n");
						}
					}).setNegativeButton("ȡ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TextView txtMsg = (TextView) DialogActivity.this
									.findViewById(R.id.txtMsg);
							txtMsg.append("�����˶Ի����ϵġ�ȡ������ť\n");
						}
					}).create();

		case 2:
			// �����Ի���Ϊָ���� View �� Demo
			return new AlertDialog.Builder(this)
					.setTitle("�˶Ի����������һ�� View")
					.setView(
							SystemTool.inflateView(DialogActivity.this,
									R.layout.view)).create();

		case 3:
			// ��������ѡ��Ի���
			Calendar c = Calendar.getInstance();
			return new DatePickerDialog(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							TextView txtMsg = (TextView) DialogActivity.this
									.findViewById(R.id.txtMsg);
							txtMsg.append("�����õ�����Ϊ��" + String.valueOf(year)
									+ "-" + String.valueOf(monthOfYear) + "-"
									+ String.valueOf(dayOfMonth) + "\n");
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DATE));

		case 4:
			// ����ʱ��ѡ��Ի���
			Calendar c2 = Calendar.getInstance();
			return new TimePickerDialog(this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					TextView txtMsg = (TextView) DialogActivity.this
							.findViewById(R.id.txtMsg);
					txtMsg.append("�����õ�ʱ��Ϊ��" + String.valueOf(hourOfDay) + ":"
							+ String.valueOf(minute) + "\n");
				}

			}, c2.get(Calendar.HOUR), c2.get(Calendar.MINUTE), true);

		case 5:
			// �����������Ի���
			ProgressDialog progress = new ProgressDialog(this);
			progress.setView(SystemTool.inflateView(DialogActivity.this,
					R.layout.fragment_search));
			progress.setMessage("������������..");
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
