package com.wiesen.provider.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.wiesen.interf.IOnlineMapTileRequestCallBack;
import com.wiesen.location.RowTile;
import com.wiesen.provider.net.wmts.WMTSInfo;
import com.wiesen.provider.net.wmts.WMTSUtils;
import com.wiesen.utils.MapConstants;

import java.io.File;
import java.io.FileOutputStream;


public class ApiClient {
    private static String TAG="TileClient";
    /**
     * 联网加载瓦片
     * @param tile
     * @param type
     * @param requestCallBack
     */
    public static boolean sendToServer(final RowTile tile, final int type, WMTSInfo info, final IOnlineMapTileRequestCallBack requestCallBack){
                HttpClientOptions options = HttpClientOptions.getInstance();
                String url= WMTSUtils.getTileUrl(options.getServiceUrl(),info,tile.zoom,tile.x,tile.y);
                byte[] response=options.doGet(url/*, new IDeuMapHttpCallBack() {
                    @Override
                    public void httpConnectSucceed(byte[] response) {
                        writeByteToCaChe(response,type,tile);
                        Bitmap  bitmap = BitmapFactory.decodeByteArray(response, 0, response.length);
                        Log.i(TAG,response.length+"" + "string:"+new String(response).toString());
                        requestCallBack.getTileBitmapSucceed(tile,bitmap);
                    }

                    @Override
                    public void httpConnectFailed(StringBuffer strErr) {
                        requestCallBack.getTileBitmapFailed(tile);
                    }
                }*/);
            if (response!= null){
                writeByteToCaChe(response,type,tile);
                Bitmap bitmap = BitmapFactory.decodeByteArray(response, 0, response.length);
                Log.i(TAG,response.length+"" + "string:"+new String(response).toString());
                requestCallBack.getTileBitmapSucceed(tile,bitmap);
                return true;
            }else {
                requestCallBack.getTileBitmapFailed(tile);
            }
        return false;
    }

    /**
     * 联网获取的瓦片缓存到手机内存中
     * @param img
     * @param type
     * @param tile
     */
    private static void writeByteToCaChe(byte[] img, int type, RowTile tile) {
        try {
            String path = MapConstants.onlinePath + File.separator +type+ File.separator +tile.zoom + File.separator + tile.x;
            File dirFile = new File(path);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            FileOutputStream fops = new FileOutputStream(path+ File.separator + tile.y + ".deu");
            fops.write(img);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
