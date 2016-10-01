package com.dreamer.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Properties;

import javax.microedition.khronos.opengles.GL10;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dreamer.R;
import com.dreamer.opengles.GlesAnimation;
import com.dreamer.opengles.GlesAnimation.Animation;
import com.dreamer.opengles.GlesImage;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesRectangle;
import com.dreamer.opengles.GlesText;
import com.dreamer.opengles.GlesToolkit;
import com.dreamer.opengles.GlesView;
import com.dreamer.opengles.expend.GlesObjectManager;
import com.dreamer.tool.bitmap.BitmapToolkit;
import com.dreamer.tool.cache.ExternalOverFlowUtils;
import com.dreamer.tool.database.DatabaseInfo;
import com.dreamer.tool.database.DatabaseManager;
import com.dreamer.tool.database.DatabaseManager.DatabaseType;
import com.dreamer.tool.database.LoveChannelData;
import com.dreamer.tool.file.PropertiesToolkit;
import com.dreamer.tool.http.AsyncHttpClient;
import com.dreamer.tool.http.FileHttpResponseHandler;
import com.dreamer.tool.log.Mlog;
import com.dreamer.tool.message.MessageUtil;
import com.dreamer.tool.network.IpUtil;
import com.dreamer.tool.network.SIMCardInfoUtil;
import com.dreamer.tool.resource.ResourceManager;
import com.dreamer.tool.system.ActivityTool;
import com.dreamer.tool.system.ReflectTool;
import com.dreamer.tool.util.CheckIDFormat;
import com.dreamer.tool.util.UtilTools;
import com.dreamer.tool.xmldom.DomXmlParser;
import com.dreamer.tool.xmlsax.Xml;
import com.dreamer.tool.xmlsax.XmlNode;
import com.dreamer.tool.xmlsax.XmlParse;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Environment;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class LoadingView extends GlesView {

	Context context;
	private String TAG = LoadingView.class.getSimpleName();
	final static String DOWNLOAD_DIR = "download1.mp3";
	private float x;
	private float y;
	private float previousX;
	private float previousY;
	private GlesImage bg1;
	private GlesImage bg2;
	private GlesText tip;
	private boolean isConnectStatus = true;

	AsyncHttpClient asyncHttpClient = null;
	FileHttpResponseHandler fileHttpResponseHandler = null;
	private Xml xml;
	private String str;
	private XmlNode rootNode;
	private float tranZ;
	private float rotateX;

	public LoadingView(Context ctx) {
		Mlog.d(TAG, "init LoadingView");
		context = ctx;
		bg1 = new GlesImage(context, 400, 300);
		bg1.setBackground(context, R.drawable.ic_launcher);
		GlesAnimation mGlesAnimation = new GlesAnimation();
		mGlesAnimation.setAnimationType(Animation.ANIMATION_TYPE_ACCELERATE);
		bg1.setGlesAnimation(mGlesAnimation);
		bg2 = new GlesImage(context, 400, 200);
		bg2.setBackground(BitmapToolkit.toGrayscale(BitmapFactory
				.decodeResource(context.getResources(),
						UtilTools.getResoureId(R.drawable.class, "loading"))));
		tip = new GlesText(context, 2000, 100);

		// initReflect();
		// initResource();

		tip.setTextColor(Color.RED);
		tip.setTextAlign(Align.LEFT);

		initSyncHttp();
		// initSaxXmlParse();
		// initDomXmlParse();
		// initPropertity();
		initDatabase();
		tip.setText(str);
	}

	private void initDatabase() {
		DatabaseManager.getInstance().prepare(context);
		LoveChannelData mLoveChannelData = new LoveChannelData();
		mLoveChannelData.setFrequency(259000000);
		mLoveChannelData.setLove_status(0);
		mLoveChannelData.setProgram_number(100);
		DatabaseManager.getInstance().insertLoveChannelData(mLoveChannelData);
		str = String.valueOf(DatabaseManager.getInstance()
				.queryLoveChannelsData().get(0).getFrequency());
		// DatabaseManager.getInstance()
		// .initDatabaseHelper(DatabaseType.WRITEABLE);
		// // DatabaseManager.getInstance().execSQL(
		// // DatabaseInfo.CREATE_LOVECHANNELS_TABLE);
		// DatabaseManager.getInstance().beginTransaction();
		// for(int i = 0;i<100;i++){
		// DatabaseManager.getInstance().execSQL(
		// "INSERT INTO lovechannels (frequency, program_number,love_status)"
		// + "VALUES (25600000, 5000,0)");
		// }
		// DatabaseManager.getInstance().endTransaction();
		// Cursor cursor = DatabaseManager.getInstance().rawQuery(
		// "SELECT * FROM lovechannels");
		// str = String.valueOf(cursor.getColumnName(0) + "," +
		// cursor.getCount());
		DatabaseManager.getInstance().close();
	}

	private void initDomXmlParse() {
		// TODO Auto-generated method stub
		DomXmlParser parser = new DomXmlParser();
		try {
			parser.parseXml(context.getAssets().open("areaConfig.xml"));
			String str = null;
			Element root = parser.getDocumentElement();
			NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				NodeList citys = node.getChildNodes();
				for (int j = 0; j < citys.getLength(); j++) {
					Node city = citys.item(j);
					str = "node:" + node.getNodeName() + ",city:"
							+ citys.getLength() + "," + city.getNodeType();
				}
			}
			tip.setText(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initPropertity() {
		// TODO Auto-generated method stub
		// PropertiesToolkit.addProperties(context, "/sdcard/config1.dat",
		// "kemm",
		// cid.GetArea("360430198703101114"));
		PropertiesToolkit.addProperties(
				"/sdcard/config1.dat",
				"kemm",
				ReflectTool.getFieldValue("com.dreamer.tool.log.Mlog",
						"special", context).toString());
		str = PropertiesToolkit.getConfig("/sdcard/config1.dat", "kemm");
	}

	private void initReflect() {
		// TODO Auto-generated method stub
		Class<?> clazz = ReflectTool.getClass("com.dreamer.tool.log.Mlog",
				context);
		// try {
		// Method mdthod = clazz.getMethod("getSeprateor");
		// str = (String) mdthod.invoke(clazz.newInstance());
		// str = ReflectTool.parseClass("com.dreamer.tool.log.Mlog", context);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
	}

	private void initResource() {
		// TODO Auto-generated method stub
		ResourceManager.getInstance().prepare(context);
		try {
			String.valueOf(ResourceManager.getInstance().readResID("drawable",
					"bg"));
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void initSaxXmlParse() {
		// TODO Auto-generated method stub
		XmlParse xmlparse = XmlParse.builder();
		try {
			xml = xmlparse.parse(context.getAssets().open("areaCode.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rootNode = xml.getRootNode();
		XmlNode[] nodes = rootNode.getChildNodes("city");
		tip.setText(rootNode.getName() + "  nodes->" + nodes[0].getName());
	}

	private void initSyncHttp() {
		// TODO Auto-generated method stub
		asyncHttpClient = new AsyncHttpClient();
		fileHttpResponseHandler = new FileHttpResponseHandler(
				ExternalOverFlowUtils.getDiskCacheDir(context, DOWNLOAD_DIR)
						.getAbsolutePath()) {

			@Override
			public void onProgress(long totalSize, long currentSize, long speed) {
				// TODO Auto-generated method stub
				super.onProgress(totalSize, currentSize, speed);
				long downloadPercent = currentSize * 100 / totalSize;
				tip.setText("progress:" + downloadPercent + "%--speed:" + speed
						+ "kbps");
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				super.onFailure(error);
				tip.setText("下载失败！");
			}

			@Override
			public void onSuccess(byte[] binaryData) {
				// TODO Auto-generated method stub
				super.onSuccess(binaryData);
				tip.setText("下载成功！");
			}
		};
	}

	private void startCom() {
		// TODO Auto-generated method stub
		fileHttpResponseHandler.setInterrupt(false);
		asyncHttpClient
				.download(
						"http://zhangmenshiting.baidu.com/data2/music/109017367/87603393205200128.mp3?xcode=f8edb687b9e6aed851d70e0ab3e50aa405a4fdf55a71cce7",
						fileHttpResponseHandler);
		tip.setText("start download...");
	}

	private void stopCom() {
		// TODO Auto-generated method stub
		fileHttpResponseHandler.setInterrupt(true);
		tip.setText("end download...");
	}

	protected boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}

	protected boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			((MainActivity) context).finish();
			return true;
		case KeyEvent.KEYCODE_0:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.tool.fragment.MFragmentActivity");
			((MainActivity) context).finish();
			return true;
		case KeyEvent.KEYCODE_1:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.layout.ViewpagerActivity");
			((MainActivity) context).finish();
			return true;
		case KeyEvent.KEYCODE_2:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.layout.MenuActivity");
			((MainActivity) context).finish();
			return true;
		case KeyEvent.KEYCODE_3:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.main.HttpActivity");
			((MainActivity) context).finish();
			return true;
		case KeyEvent.KEYCODE_4:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.layout.WidgetActivity");
			((MainActivity) context).finish();
			return true;
		case KeyEvent.KEYCODE_5:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.layout.AnimationActivity");
			return true;
		case KeyEvent.KEYCODE_6:
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.layout.GalleryActivity");
			return true;
		case KeyEvent.KEYCODE_PROG_RED:
			return true;
		case KeyEvent.KEYCODE_PROG_GREEN:
			return true;
		case KeyEvent.KEYCODE_PROG_BLUE:
			return true;
		case KeyEvent.KEYCODE_PROG_YELLOW:
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			((MainActivity) context).inputRequestFocus();
			return true;
		case KeyEvent.KEYCODE_PAGE_UP:
			return true;
		case KeyEvent.KEYCODE_PAGE_DOWN:
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			// startCom();
			// bg1.getGlesAnimation().setMaxProcess(100);
			// bg1.getGlesAnimation().setAnimationStep(0.01f);
			// bg1.getGlesAnimation().setAnimationAlpha(0.5f, 2f);
			// bg1.getGlesAnimation().setAnimationScale(1.1f, 1.1f, 1f);
			// bg1.getGlesAnimation().setAnimationScale(1f,1.1f,0f, 1.1f,1f,
			// 1f);
			// bg1.getGlesAnimation().startAnimation();
			// tranZ -=0.1f;
			rotateX -= 5f;
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			// stopCom();
			// this.getGlesAnimation().setAnimationTranslate(0.2f, 0f, 0f);
			// this.getGlesAnimation().setAnimationTranslate(-0.2f, 0.2f, 0f,
			// 0f,
			// 0f, 0f);
			// this.getGlesAnimation().setAnimationRotate(0, 0, 5);
			// // this.getGlesAnimation().setAnimationRotate(0,0,0 ,0,0,5);
			// this.getGlesAnimation().startAnimation();
			// bg1.getGlesAnimation().setAnimationScale(0.1f, 0.1f, 0.1f);
			// bg1.getGlesAnimation().startAnimation();
			// tranZ += 0.1f;
			rotateX += 5f;
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			// bg1.getGlesAnimation().setAnimationTranslate(-0.1f,
			// GlesToolkit.translateY(100), 0);
			bg1.getGlesAnimation().setAnimationRotate(-5, 0, 0);
			bg1.startGlesAnimation();
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			bg1.getGlesAnimation().setAnimationRotate(5, 0, 0);
			// bg1.getGlesAnimation().setAnimationTranslate(0.1f,
			// GlesToolkit.translateY(100), 0);
			bg1.getGlesAnimation().startAnimation();
			return true;
		case KeyEvent.KEYCODE_MENU:
			// MessageUtil.sendMail(context, "kmmxxx", "test....");
			// MessageUtil.sendSms(context, "kmmxxx");
			// ActivityTool.startActivity(context, "com.dreamer",
			// "com.dreamer.layout.DialogActivity");
			// ((MainActivity) context).finish();
			ActivityTool.startActivity(context, "com.dreamer",
					"com.dreamer.layout.AnimationActivity");
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Mlog.d(TAG, "event:" + event.getAction());
		x = GlesToolkit.translateX(event.getX());
		y = GlesToolkit.translateY(event.getY());
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - previousX;
			float dy = y - previousY;
			if (dx > 0) {
			} else {
			}
			if (dy > 0) {
			} else {
			}
			break;
		case MotionEvent.ACTION_UP:
			// motionFocus.setDirXY(previousX, previousY);
			bg1.getGlesAnimation().setAnimationTranslate(-0.1f,
					GlesToolkit.translateY(100), 0);
			bg1.getGlesAnimation().startAnimation();
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_DOWN:
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	public boolean onDraw() {
		// TODO Auto-generated method stub
		super.onDraw();
		GlesMatrix.pushMatrix();
		GlesMatrix.translate(GlesToolkit.translateX(500),
				GlesToolkit.translateY(360), tranZ);
		GlesMatrix.translate(0, 0, 0.2f);
		GlesMatrix.rotate(rotateX, 1f, 0, 0);
		bg1.onDraw();
		GlesMatrix.rotate(-rotateX, 1f, 0, 0);
		GlesMatrix.translate(0, 0, -0.2f);
		GlesMatrix.popMatrix();
		GlesMatrix.pushMatrix();
		GlesMatrix.setAlpha(2f);
		bg2.onDraw();
		GlesMatrix.setAlpha(1f);
		GlesMatrix.popMatrix();

		if (isConnectStatus) {
			GlesMatrix.pushMatrix();
			GlesMatrix.translate(GlesToolkit.translateX(40),
					GlesToolkit.translateY(20), 0);
			tip.onDraw();
			GlesMatrix.popMatrix();
		}

		GlesMatrix.pushMatrix();
		GlesMatrix.translate(GlesToolkit.translateX(40),
				GlesToolkit.translateY(20), 100);
		GlesObjectManager.getInstance().getGlesLoadedObject().onDraw();
		GlesMatrix.popMatrix();
		return true;
	}

	public boolean isConnectStatus() {
		return isConnectStatus;
	}

	public void setConnectStatus(boolean s) {
		this.isConnectStatus = s;
	}

}
