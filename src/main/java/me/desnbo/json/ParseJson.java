package me.desnbo.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ParseJson {
	JsonParser parser = new JsonParser();// 创建json解析器

	public static void main(String[] args) {
		ParseJson pj = new ParseJson();
		// pj.parseObj();
		// pj.parseArr();
		// pj.parsePlaylist1();
		pj.parsePlaylist();
	}

	public void write2file(String file) {
		FileWriter fw = null;
		try {
			fw = new FileWriter("E:\\playlist.json", false);// 写出到磁盘,并指定是否追加模式
			fw.write(file);// 这里向文件写到E盘下的 playlist.json 中
			fw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void parsePlaylist() {

		try {
			JsonObject playlist = (JsonObject) parser.parse(new FileReader(
					"src/main/resources/playlist.json"));// 创建JsonObject对象

			JsonObject result = playlist.get("result").getAsJsonObject();
			JsonArray tracks = result.get("tracks").getAsJsonArray(); // 获取歌曲数组
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (JsonElement temp : tracks) {
				JsonObject track = (JsonObject) temp;// 遍历数组获取单独歌曲
				sb.append("{\n  \"title\": \"");
				String songName = track.get("name").getAsString();// 歌曲名
				sb.append(songName);

				JsonArray artists = track.get("artists").getAsJsonArray(); // 歌手们
				sb.append("\",\n  \"artist\": \"");
				String artist = "";
				for (int j = 0; j < artists.size(); j++) {
					artist += artists.get(j).getAsJsonObject().get("name").getAsString() + "/";
				}
				artist = artist.substring(0, artist.length() - 1);
				sb.append(artist);

				// String album = track.get("album").getAsJsonObject().get("name").getAsString();
				// 专辑
				// sb.append("\",\n  \"album\": \"");
				// sb.append(album);

				String picUrl = track.get("album").getAsJsonObject().get("picUrl").getAsString();// 专辑封面
				sb.append("\",\n  \"cover\": " + "\"");
				sb.append(picUrl);

				String mp3Url = track.get("mp3Url").getAsString(); // 歌曲资源地址
				sb.append("?param=106x106\",\n  \"mp3\": \"" + mp3Url + "\",\n");
				sb.append("},");

			}
			sb.append("]");
			write2file(sb.toString());
			System.out.println(sb.toString());

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void parsePlaylist1() {

		try {
			JsonObject playlist = (JsonObject) parser.parse(new FileReader(
					"src/main/resources/playlist.json"));// 创建JsonObject对象

			JsonObject result = playlist.get("result").getAsJsonObject();
			JsonArray tracks = result.get("tracks").getAsJsonArray(); // 获取歌曲数组

			for (int i = 0; i < tracks.size(); i++) {
				JsonObject track = tracks.get(i).getAsJsonObject(); // 遍历数组获取单独歌曲
				System.out.println("{");
				String songName = track.get("name").getAsString();// 歌曲名
				System.out.println("\"title\": " + "\"" + songName + "\",");

				JsonArray artists = track.get("artists").getAsJsonArray(); // 歌手们
				String artist = "";
				for (int j = 0; j < artists.size(); j++) {
					artist += artists.get(j).getAsJsonObject().get("name").getAsString() + "/";
				}
				artist = artist.substring(0, artist.length() - 1);
				System.out.println("\"artist\": " + "\"" + artist + "\",");

				// String album = track.get("album").getAsJsonObject().get("name").getAsString();
				// 专辑
				// System.out.println("\"album\": " + "\"" + album + "\",");

				String picUrl = track.get("album").getAsJsonObject().get("picUrl").getAsString();// 专辑封面
				System.out.println("\"cover\": " + "\"" + picUrl + "?param=106x106\",");

				String mp3Url = track.get("mp3Url").getAsString(); // 歌曲资源地址
				System.out.println("\"mp3\": " + "\"" + mp3Url + "\",");

				System.out.println("},");
			}

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void parseObj() {

		try {
			JsonObject json = (JsonObject) parser.parse(new FileReader(
					"src/main/resources/weather.json"));// 创建JsonObject对象
			System.out.println("resultcode: " + json.get("resultcode").getAsInt());// 将json数据转为为int型的数据
			System.out.println("reason: " + json.get("reason").getAsString());// 将json数据转为为String型的数据

			JsonObject result = json.get("result").getAsJsonObject();
			JsonObject today = result.get("today").getAsJsonObject();
			System.out.println("temperature: " + today.get("temperature").getAsString());
			System.out.println("weather: " + today.get("weather").getAsString());

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void parseArr() {

		try {
			JsonObject object = (JsonObject) parser.parse(new FileReader(
					"src/main/resources/test1.json"));// 创建JsonObject对象
			System.out.println("category: " + object.get("category").getAsString());// 将json数据转为为String型的数据
			System.out.println("pop: " + object.get("pop").getAsBoolean());// 将json数据转为为boolean型的数据

			JsonArray array = object.get("language").getAsJsonArray();// 得到json数组
			for (int i = 0; i < array.size(); i++) {
				System.out.println("---------");
				JsonObject subObject = array.get(i).getAsJsonObject();
				System.out.println("id: " + subObject.get("id").getAsInt());
				System.out.println("ide: " + subObject.get("ide").getAsString());
				System.out.println("name: " + subObject.get("name").getAsString());
			}

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
