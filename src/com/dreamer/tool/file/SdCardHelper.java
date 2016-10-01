package com.dreamer.tool.file;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Video.VideoColumns;

public class SdCardHelper {
	public static final int ALL_FILES = 0;
	public static final int ALL_VIDEOS = 1;
	public static final int ALL_MUSIC = 2;
	public static final int ALL_PICTURE = 3;

	int currentAndroidVersion;
	List<String> pathes = null;

	ArrayList<FileNode> list = null;
	int nodeId = 0;

	public SdCardHelper() {
		list = new ArrayList<FileNode>();
		pathes = new ArrayList<String>();
		pathes = PropertiesToolkit.getReadPathes("/assets/mount.properties");
	}

	// 得到所有文件夹和所有类型的文件
	public ArrayList<FileNode> getAllFiles(int find_mode) {
		for (int m = 0; m < pathes.size(); m++) {
			File root = new File(pathes.get(m));
			File[] fff = root.listFiles();
			if (fff != null && fff.length > 0) {
				System.out.println(pathes.get(m));
				FileNode rootNode = new FileNode(nodeId++, root.getName(),
						false, -1, root);
				list.add(rootNode);
				switch (find_mode) {
				case ALL_FILES:
					newNodes(rootNode);
					break;
				case ALL_MUSIC:
					newAudioNodes(rootNode);
					break;
				case ALL_VIDEOS:
					newVideoNodes(rootNode);
					break;
				case ALL_PICTURE:
					newPictureNodes(rootNode);
					break;
				}
			}
		}
		return list;
	}

	// 得到所有文件
	private void newNodes(FileNode parent) {
		File[] tempFiles = parent.getFile().listFiles();
		if (tempFiles != null && tempFiles.length > 0) {
			for (int i = 0; i < tempFiles.length; i++) {
				File f = tempFiles[i];
				FileNode child = new FileNode(nodeId++, f.getName(), false,
						parent.getId(), f);
				list.add(child);
				newNodes(child);
			}
		} else {
			return;
		}
	}

	// 得到所有mp4(视频)文件
	private void newVideoNodes(FileNode parent) {
		File[] tempFiles = parent.getFile().listFiles();
		if (tempFiles != null && tempFiles.length > 0) {
			for (int i = 0; i < tempFiles.length; i++) {
				File f = tempFiles[i];
				if (f.isFile()) {
					if (isVideoFilter(f.getName())) {
						FileNode child = new FileNode(nodeId++, f.getName(),
								false, parent.getId(), f);
						list.add(child);
					}
				} else {
					FileNode child = new FileNode(nodeId++, f.getName(), false,
							parent.getId(), f);
					list.add(child);
					newVideoNodes(child);
				}
			}
		} else {
			return;
		}
	}

	// 得到所有mp3(音频)文件
	private void newAudioNodes(FileNode parent) {
		File[] tempFiles = parent.getFile().listFiles();
		if (tempFiles != null && tempFiles.length > 0) {
			for (int i = 0; i < tempFiles.length; i++) {
				File f = tempFiles[i];
				if (f.isFile()) {
					if (isAudioFilter(f.getName())) {
						FileNode child = new FileNode(nodeId++, f.getName(),
								false, parent.getId(), f);
						list.add(child);
					}
				} else {
					FileNode child = new FileNode(nodeId++, f.getName(), false,
							parent.getId(), f);
					list.add(child);
					newAudioNodes(child);
				}
			}
		} else {
			return;
		}
	}

	// 得到所有jpg(图片)文件
	private void newPictureNodes(FileNode parent) {
		File[] tempFiles = parent.getFile().listFiles();
		if (tempFiles != null && tempFiles.length > 0) {
			// Log.i("yangcy", tempFiles.length+"");
			for (int i = 0; i < tempFiles.length; i++) {
				File f = tempFiles[i];
				if (f.isFile()) {
					if (isPictureFilter(f.getName())) {
						FileNode child = new FileNode(nodeId++, f.getName(),
								false, parent.getId(), f);
						list.add(child);
					}
				} else {
					FileNode child = new FileNode(nodeId++, f.getName(), false,
							parent.getId(), f);
					list.add(child);
					newPictureNodes(child);
				}
			}
		} else {
			return;
		}
	}

	// 将所有以父节点ID为key的空hashmap赋值
	public HashMap<Integer, List<FileNode>> sortChildren(
			ArrayList<FileNode> allChild) {
		HashMap<Integer, List<FileNode>> parent = getNullParentList(getAllPid(allChild));
		for (int i = 0; i < allChild.size(); i++) {
			parent.get(allChild.get(i).getParentId()).add(allChild.get(i));
			// Log.i("yangcy"," parentid ==> "+allChild.get(i).getParentId());
		}
		return parent;
	}

	// 生成所有以父节点ID为key的空hashmap
	private HashMap<Integer, List<FileNode>> getNullParentList(
			List<Integer> pIds) {
		HashMap<Integer, List<FileNode>> pIdHashMap = new HashMap<Integer, List<FileNode>>();
		for (int i = 0; i < pIds.size(); i++) {
			List<FileNode> tempList = new ArrayList<FileNode>();
			pIdHashMap.put(pIds.get(i), tempList);
			// Log.i("yangcy"," parentid = "+pIds.get(i));
		}
		return pIdHashMap;
	}

	// 得到所有父节点的id
	public List<Integer> getAllPid(ArrayList<FileNode> allChild) {
		List<Integer> pIds = new ArrayList<Integer>();
		for (int i = 0; i < allChild.size(); i++) {
			if (!isExists(pIds, allChild.get(i).getParentId())) {
				pIds.add(allChild.get(i).getParentId());
				// Log.i("yangcy"," parentid = "+allChild.get(i).getParentId());
			}
		}
		return pIds;
	}

	// 判断list当中是否存在指定value(int)，返回true则存在，false则不存在
	private boolean isExists(List<Integer> childs, int value) {
		boolean exist = false;
		for (int i = 0; i < childs.size(); i++) {
			if (value == childs.get(i)) {
				exist = true;
				return exist;
			}
		}
		return exist;
	}

	// 通过指定id，从指定list中取出FileNode
	public static FileNode getFileNode(int nodeId, List<FileNode> list) {
		FileNode fn = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == nodeId) {
				fn = list.get(i);
				return fn;
			}
		}
		return fn;
	}

	/**
	 * 将long类型Byte转换成保留两位小数的M
	 * 
	 * @return String
	 **/
	public static String ByteToMb(long b) {
		String str = null;
		float s = (float) b / 1024 / 1024;
		if (s >= 1) {
			// str = (int)s+"M";
			str = new DecimalFormat("#.00").format(s) + "M";
		} else {
			str = (int) (s * 1024) + "KB";
		}
		return str;
	}

	/**
	 * 将long类型日期转换成标准日期格式yy-mm-dd-h:min:sec
	 * 
	 * @return String
	 **/
	public static String LongDataToStandered(long data) {
		String dataStr = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss")
				.format(data);
		return dataStr;
	}

	/**
	 * 获取音频文件的持续时间
	 **/
	public static String getAudioDuration(Context context, String srcPath) {
		String strDURATION = null;
		String[] projection = {
		// AudioColumns.DATA, //得到路径
		// AudioColumns.TITLE, //歌名
		// AudioColumns.ALBUM, //专辑
		// AudioColumns.ARTIST, //艺术家
		// AudioColumns.SIZE }; //文件大小
		AudioColumns.DURATION }; // 持续时长
		String selection = MediaStore.Audio.Media.DATA + "=?";
		String[] selectionArgs = { srcPath };
		Cursor mAudioCursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, // 字段　
				selection, // 查询条件
				selectionArgs, // 条件的对应?的参数
				AudioColumns.TITLE);// 排序方式
		System.out.println("The videos number is : " + mAudioCursor.getCount());
		for (int i = 0; i < mAudioCursor.getCount(); i++) {
			mAudioCursor.moveToNext();
			int indexDURATION = mAudioCursor
					.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION); // 持续时长
			strDURATION = mAudioCursor.getString(indexDURATION);
		}
		return strDURATION;
	}

	/**
	 * 获取视频文件的持续时间
	 * */
	// public static String getVideoDuration(Context context, String srcPath) {
	// String strDURATION = null;
	// String[] projection = {
	// VideoColumns.DURATION }; // 持续时长
	// String selection = MediaStore.Video.Media.DATA + "=?";
	// String[] selectionArgs = { srcPath };
	// Cursor mVideoCursor = context.getContentResolver().query(
	// MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, //
	// 字段　如果为null　就是查询所有信息　相当于SQL语句中的　“
	// // *
	// // ”
	// selection, // 查询条件
	// selectionArgs, // 条件的对应?的参数
	// VideoColumns.TITLE);// 排序方式
	//
	// for (int i = 0; i < mVideoCursor.getCount(); i++) {
	// mVideoCursor.moveToNext();
	// int indexDURATION = mVideoCursor
	// .getColumnIndex(MediaStore.Video.VideoColumns.DURATION); // 持续时长
	// strDURATION = mVideoCursor.getString(indexDURATION);
	// }
	// System.out.println("The strDURATION is : "+strDURATION);
	// return strDURATION;
	// }
	/**
	 * 获得时间字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String SecondeToHour(int time) {
		// 分别得到时分秒
		int h = time / 3600;
		int m = time / 60 % 60;
		int s = time % 60;
		return String.format("%02d:%02d:%02d", h, m, s);
	}

	/**
	 * 将字符串毫秒转换为时分秒显示
	 **/
	// public static String SecondeToHour1(String second) {
	// String str = null;
	// double s;
	// s = Double.parseDouble(second) / 1000;
	// int h = (int) (s / 3600);
	// int min = (int) ((s - h * 3600) / 60);
	// int sec = (int) (s % 60);
	// if (h > 0) {
	// str = h + ":" + min + ":" + sec;
	// } else {
	// if (min < 10 && sec < 10) {
	// str = "0" + min + ":" + "0" + sec;
	// } else if (min < 10 && sec > 10) {
	// str = "0" + min + ":" + sec;
	// } else if (min > 10 && sec < 10) {
	// str = min + ":" + "0" + sec;
	// } else {
	// str = min + ":" + sec;
	// }
	// }
	// return str;
	// }
	/** 从指定整数list的得到一个随机数且得到的该数不能与指定数字相等 */
	public static int getRandomNextInt(int item, int max) {
		int next = -1;
		do {
			next = new Random().nextInt(max);
		} while (next == item);
		return next;
	}

	/** 将fileNode的list按文件名排序 */
	public static List<FileNode> SortFileByName(List<FileNode> src) {
		Collections.sort(src, new Comparator<FileNode>() {
			public int compare(FileNode f1, FileNode f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		return src;
	}

	/** 将fileNode的list按时间排序 */
	public static List<FileNode> SortFileByTime(List<FileNode> src) {
		Collections.sort(src, new Comparator<FileNode>() {
			public int compare(FileNode f1, FileNode f2) {
				return (f1.getFile().lastModified() - f2.getFile()
						.lastModified()) > 0 ? 1 : -1;
			}
		});
		return src;
	}

	// 音乐文件过滤器
	public static boolean isAudioFilter(String filename) {
		boolean result = false;
		if (filename.endsWith(".mp3") || filename.endsWith(".aac")
				|| filename.endsWith(".AAC") || filename.endsWith(".AMR")
				|| filename.endsWith(".wma") || filename.endsWith(".wav")) {
			result = true;
		}
		return result;
	}

	// 视频文件过滤器
	public static boolean isVideoFilter(String filename) {
		boolean result = false;
		if (filename.endsWith(".mp4") || filename.endsWith(".AC3")
				|| filename.endsWith(".AVI") || filename.endsWith(".avi")
				|| filename.endsWith(".ts") || filename.endsWith(".mpg")
				|| filename.endsWith(".mkv") || filename.endsWith(".Divx")
				|| filename.endsWith(".MKV") || filename.endsWith(".rmvb")
				|| filename.endsWith(".rm") || filename.endsWith(".wmv")
				|| filename.endsWith(".mov") || filename.endsWith(".MOV")
				|| filename.endsWith(".MPG") || filename.endsWith(".ac3")
				|| filename.endsWith(".MP4") || filename.endsWith(".RMVB")
				|| filename.endsWith(".TS") || filename.endsWith(".WMV")
				|| filename.endsWith(".flv") || filename.endsWith(".f4v")
				|| filename.endsWith(".F4V")) {
			result = true;
		}
		return result;
	}

	// 图片文件过滤器
	public static boolean isPictureFilter(String filename) {
		boolean result = false;
		if (filename.endsWith(".jpg") || filename.endsWith(".JPEG")
				|| filename.endsWith(".PNG") || filename.endsWith(".png")
				|| filename.endsWith(".bmp") || filename.endsWith(".jpeg")
				|| filename.endsWith(".gif") || filename.endsWith(".GIF")) {
			result = true;
		}
		return result;
	}

}
