package com.dreamer.tool.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtil {
	// 假设现在要创建这样一个json文本
	// {
	// "phone" : ["12345678", "87654321"], // 数组
	// "name" : "yuanzhifei89", // 字符串
	// "age" : 100, // 数值
	// "address" : { "country" : "china", "province" : "jiangsu" }, // 对象
	// "married" : false // 布尔值
	// }

	private static final String JSON = "{" + "   \"phone\" : [\"12345678\", \"87654321\"],"
			+ "   \"name\" : \"yuanzhifei89\"," + "   \"age\" : 100,"
			+ "   \"address\" : { \"country\" : \"china\", \"province\" : \"jiangsu\" }," + "   \"married\" : false,"
			+ "}";

	public JsonUtil() {

	}

	/**
	 * 将数组转换为JSON格式的数据。
	 * 
	 * @param stoneList
	 *            数据源
	 * @return JSON格式的数据
	 */
	public static String changeArrayDateToJson(ArrayList<Stone> stoneList) {
		try {
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			int length = stoneList.size();
			for (int i = 0; i < length; i++) {
				Stone stone = stoneList.get(i);
				String name = stone.getName();
				String size = stone.getSize();
				JSONObject stoneObject = new JSONObject();
				stoneObject.put("name", name);
				stoneObject.put("size", size);
				array.put(stoneObject);
			}
			object.put("stones", array);
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON转化为数组并返回。
	 * 
	 * @param Json
	 * @return ArrayList<Stone>
	 */
	public static ArrayList<Stone> changeJsonToArray(String Json) {
		ArrayList<Stone> gameList = new ArrayList<Stone>();
		try {
			JSONObject jsonObject = new JSONObject(Json);
			if (!jsonObject.isNull("stones")) {
				String aString = jsonObject.getString("stones");
				JSONArray aJsonArray = new JSONArray(aString);
				int length = aJsonArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject stoneJson = aJsonArray.getJSONObject(i);
					String name = stoneJson.getString("name");
					String size = stoneJson.getString("size");
					Stone stone = new Stone();
					stone.setName(name);
					stone.setSize(size);
					gameList.add(stone);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameList;
	}

	// public List<SongInfo> parse(String json) {
	// List<SongInfo> list = new ArrayList<SongInfo>();
	// json =
	// "[{id : '4191',songName : '梦里相逢',total : '5181',singer : '港台女歌手'},"
	// + "{id : '14825',songName : '可不可以',total : '5181',singer : '乐队组合'},"
	// + "{id : '11650',songName : '微笑的约定',total : '5181',singer : '其他乐坛女歌手'},"
	// + "{id : '14836',songName : '占缺',total : '5181',singer : '港台男歌手'},"
	// +
	// "{id : '14778',songName : '我每天许下同一个心愿',total : '5181',singer : '港台女歌手'},"
	// + "{id : '14994',songName : '蓝眼睛',total : '5181',singer : '合唱'},"
	// + "{id : '11094',songName : '爱要有你才完美',total : '5181',singer : '大陆女歌手'},"
	// + "{id : '2041',songName : '幸福的泡泡',total : '5181',singer : '港台女歌手'},"
	// + "{id : '14721',songName : '轻轻呼唤你',total : '5181',singer : '港台男歌手'},"
	// + "{id : '14965',songName : '你那么爱她',total : '5181',singer : '合唱'}]";
	// try {
	// // 首先最外层是{}，是创建一个对象
	// // JSONObject songsJson = new JSONObject(json);
	// // 第一个键phone的值是数组，所以需要创建数组对象
	// JSONArray songs = new JSONArray(json);
	// for (int i = 0; i < songs.length(); i++) {
	// JSONObject song = songs.getJSONObject(i);
	// SongInfo si = new SongInfo();
	// si.setId(song.getString("id"));
	// si.setSongName(song.getString("songName"));
	// si.setTotal(song.getInt("total"));
	// si.setSinger(song.getString("singer"));
	// list.add(si);
	// }
	// } catch (JSONException ex) {
	// // 键为null或使用json不支持的数字格式(NaN, infinities)
	// ex.printStackTrace();
	// }
	// return list;
	// }

	public void parseByJsonTokener(String str) {
		try {
			JSONTokener jsonParser = new JSONTokener(JSON);
			// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
			// 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
			JSONObject person = (JSONObject) jsonParser.nextValue();
			// 接下来的就是JSON对象的操作了
			person.getJSONArray("phone");
			person.getString("name");
			person.getInt("age");
			person.getJSONObject("address");
			person.getBoolean("married");
		} catch (JSONException ex) {
			// 异常处理代码
		}
	}

}
