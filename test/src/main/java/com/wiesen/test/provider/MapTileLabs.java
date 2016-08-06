package com.wiesen.test.provider;


import org.json.JSONException;
import org.json.JSONObject;

public class MapTileLabs {
    public String version = "1.0";
    public String desc = "";//地区
    public int tile_num;//瓦片数量
    public String tile_format = "JPG";//瓦片格式
    public double center_x;//坐标中心点x
    public double center_y;//坐标中心点y
    //地图包络矩形
    public double min_x;
    public double min_y;
    public double max_x;
    public double max_y;
    public int index_start;//索引段起始位置
    public int index_len;//索引段的长度
    public int data_start;//数据段起始位置
    public int data_len;//数据段的长度

    /**
     * 将信息封装成json
     * @return 封装后的字符串
     */
    public String encodeJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VER", version);
            jsonObject.put("DESC", desc);
            jsonObject.put("TILE_NUM", tile_num);
            jsonObject.put("TILE_FORMAT", tile_format);
            jsonObject.put("CENTER_X", center_x);
            jsonObject.put("CENTER_Y", center_y);
            jsonObject.put("MIN_X", min_x);
            jsonObject.put("MIN_Y", min_y);
            jsonObject.put("MAX_X", max_x);
            jsonObject.put("MAX_Y", max_y);
            jsonObject.put("INDEX_START", index_start);
            jsonObject.put("INDEX_LEN", index_len);
            jsonObject.put("DATA_START", data_start);
            jsonObject.put("DATA_LEN", data_len);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 将string解析为json
     */
    public void decodeJson(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            version = jsonObject.getString("VER");
            desc = jsonObject.getString("DESC");
            tile_num = jsonObject.getInt("TILE_NUM");
            tile_format = jsonObject.getString("TILE_FORMAT");
            center_x = jsonObject.getDouble("CENTER_X");
            center_y = jsonObject.getDouble("CENTER_Y");
            min_x = jsonObject.getDouble("MIN_X");
            min_y = jsonObject.getDouble("MIN_Y");
            max_x = jsonObject.getDouble("MAX_X");
            max_y = jsonObject.getDouble("MAX_Y");
            index_start = jsonObject.getInt("INDEX_START");
            index_len = jsonObject.getInt("INDEX_LEN");
            data_start = jsonObject.getInt("DATA_START");
            data_len = jsonObject.getInt("DATA_LEN");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
