package com.wiesen.provider.net.wmts;


import com.wiesen.provider.net.HttpClientOptions;

public class WMTSUtils {
    /**
     * 获取WMTS网络描述信息
     * @param wmtsInfo WMTSInfo对象
     * @return
     */
    public static boolean connectToMapServerGetInfo(WMTSInfo wmtsInfo){
        HttpClientOptions options = HttpClientOptions.getInstance();
        String url_f = options.getServiceUrl() + "/WMTS?SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetCapabilities";
        byte[] response=options.doGet(url_f);
        if (response != null){
            ParseWMTS.parseWMTSSting(new String(response),wmtsInfo);
            return true;
        }
        return false;
    }

    /**
     * 构造瓦片获取url
     * @param url 基础url
     * @param info WMTSInfo对象
     * @param zoom 层级
     * @param x 行
     * @param y 列
     * @return
     */
    public static String getTileUrl(final String url, final WMTSInfo info , final int zoom, final int x, final int y){
            String url_f = url + "/WMTS?request=GetTile&" +
                        "Version="+info.ServiceVersion+"&Service=WMTS&Layer="
                        +info.Layer+"&Style="+info.Style+"&Format="+info.Format;
            if (info.TileMatrixSet.size()>0){
                url_f += "&TileMatrixSet="+info.TileMatrixSet.get(0);
             }
             url_f +="&TileMatrix="+zoom +"&TileRow="+y+"&TileCol="+x;
        return url_f;
    }
}
