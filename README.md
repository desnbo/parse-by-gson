获取并解析网易云歌曲资源
-----

### 获取歌曲资源数据json格式  

网易云可以从下面的接口获取歌曲资源,返回json.其中id为 _歌单 id_

    http://music.163.com/api/playlist/detail?id=507507153

为了便于理清结构层次,对获取的json数据进行瘦身,去掉不必要的部分,瘦身后的json文件结构如下:

```json
{
    "result": {
        "creator": {},
        "tracks": [{
            "name": "Locked Away",
            "id": 406072193,
            "artists": [{
                "name": "OutaMatic",
                "id": 1181741,
            }, {
                "name": "Madilyn Paige",
                "id": 935127,
            }],
            "album": {
                "name": "Locked Away (Maddie Wilson & Madilyn Paige) [OutaMatic Remix]",
                "id": 34526759,
                "picUrl": "http://p1.music.126.net/smViXHcka8aOsEkMYUwQDQ==/1416170978664558.jpg",
                "artists": [{
                    "name": "OutaMatic",
                    "id": 1181741,
                }]
            },
            "mp3Url": "http://m2.music.126.net/K4GzT1x6ApvjKKJlDruw2w==/3424978723336211.mp3"
        }, { 
            "name": "Bird of Sorrow",
            "artists": [{
                "name": "Glen Hansard",
            }],
            "album": {
                "name": "Rhythm And Repose",
                "picUrl": "http://p1.music.126.net/JNduXy0KR5qmYtlPrG6khw==/6674035581155710.jpg",
                "artists": [{
                    "name": "Glen Hansard",
                }]
            },
            "mp3Url": "http://m2.music.126.net/AwUKydvP3xmelCb6-EcO3w==/2026399930008807.mp3"
        }, ],
    },
    "code": 200
}

```
![](http://i2.muimg.com/567571/d95f87800afccc48.png "playlist.json")

### 利用 Gson.jar 对 json 数据进行处理  

* 在项目中添加Gson.jar或在`pom.xml` 中添加如下依赖:  

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.0</version>
</dependency>
```

* java解析json获取歌曲相关信息代码如下:

```java
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
        pj.parsePlaylist();
    }

    public void write2file(String file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("E:\\playlist.json", false);// 是否追加
            fw.write(file);// 这里向文件写入
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
                sb.append("\",\n  \"mp3\": \"" + mp3Url + "\",\n");
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
}
```

* 输出效果如下:  

```json
[{
  "title": "Locked Away",
  "artist": "OutaMatic/Maddie Wilson/Madilyn Paige",
  "cover": "http://p1.music.126.net/smViXHcka8aOsEkMYUwQDQ==/1416170978664558.jpg",
  "mp3": "http://m2.music.126.net/K4GzT1x6ApvjKKJlDruw2w==/3424978723336211.mp3",
},{
  "title": "Bird of Sorrow",
  "artist": "Glen Hansard",
  "cover": "http://p1.music.126.net/JNduXy0KR5qmYtlPrG6khw==/6674035581155710.jpg",
  "mp3": "http://m2.music.126.net/AwUKydvP3xmelCb6-EcO3w==/2026399930008807.mp3",
},{
  "title": "Fall Into You",
  "artist": "Night Terrors of 1927",
  "cover": "http://p1.music.126.net/YCtAnLMtw63wutAOpi4jug==/2549767464848248.jpg",
  "mp3": "http://m2.music.126.net/7jGcA6WYaTvL6Q9eGm_1MA==/5749346301763121.mp3",
},{
  "title": "夜机",
  "artist": "陈慧娴",
  "cover": "http://p1.music.126.net/gJ1Wnmdxcfg_dw-uNfzTDg==/54975581401072.jpg",
  "mp3": "http://m2.music.126.net/hV0i8WNmfm3NpKfeuDSgJw==/1010451185933747.mp3",
},]

```
