package com.dreamer.tool.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtil {
	// ��������Ҫ��������һ��json�ı�
	// {
	// "phone" : ["12345678", "87654321"], // ����
	// "name" : "yuanzhifei89", // �ַ���
	// "age" : 100, // ��ֵ
	// "address" : { "country" : "china", "province" : "jiangsu" }, // ����
	// "married" : false // ����ֵ
	// }

	private static final String JSON = "{" + "   \"phone\" : [\"12345678\", \"87654321\"],"
			+ "   \"name\" : \"yuanzhifei89\"," + "   \"age\" : 100,"
			+ "   \"address\" : { \"country\" : \"china\", \"province\" : \"jiangsu\" }," + "   \"married\" : false,"
			+ "}";

	public JsonUtil() {

	}

	/**
	 * ������ת��ΪJSON��ʽ�����ݡ�
	 * 
	 * @param stoneList
	 *            ����Դ
	 * @return JSON��ʽ������
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
	 * ��JSONת��Ϊ���鲢���ء�
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
	// "[{id : '4191',songName : '�������',total : '5181',singer : '��̨Ů����'},"
	// + "{id : '14825',songName : '�ɲ�����',total : '5181',singer : '�ֶ����'},"
	// + "{id : '11650',songName : '΢Ц��Լ��',total : '5181',singer : '������̳Ů����'},"
	// + "{id : '14836',songName : 'ռȱ',total : '5181',singer : '��̨�и���'},"
	// +
	// "{id : '14778',songName : '��ÿ������ͬһ����Ը',total : '5181',singer : '��̨Ů����'},"
	// + "{id : '14994',songName : '���۾�',total : '5181',singer : '�ϳ�'},"
	// + "{id : '11094',songName : '��Ҫ���������',total : '5181',singer : '��½Ů����'},"
	// + "{id : '2041',songName : '�Ҹ�������',total : '5181',singer : '��̨Ů����'},"
	// + "{id : '14721',songName : '���������',total : '5181',singer : '��̨�и���'},"
	// + "{id : '14965',songName : '����ô����',total : '5181',singer : '�ϳ�'}]";
	// try {
	// // �����������{}���Ǵ���һ������
	// // JSONObject songsJson = new JSONObject(json);
	// // ��һ����phone��ֵ�����飬������Ҫ�����������
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
	// // ��Ϊnull��ʹ��json��֧�ֵ����ָ�ʽ(NaN, infinities)
	// ex.printStackTrace();
	// }
	// return list;
	// }

	public void parseByJsonTokener(String str) {
		try {
			JSONTokener jsonParser = new JSONTokener(JSON);
			// ��ʱ��δ��ȡ�κ�json�ı���ֱ�Ӷ�ȡ����һ��JSONObject����
			// �����ʱ�Ķ�ȡλ����"name" : �ˣ���ônextValue����"yuanzhifei89"��String��
			JSONObject person = (JSONObject) jsonParser.nextValue();
			// �������ľ���JSON����Ĳ�����
			person.getJSONArray("phone");
			person.getString("name");
			person.getInt("age");
			person.getJSONObject("address");
			person.getBoolean("married");
		} catch (JSONException ex) {
			// �쳣�������
		}
	}

}
