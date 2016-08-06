package com.wiesen.provider.net;


import com.wiesen.interf.IMapHttpCallBack;
import com.wiesen.utils.MapConstants;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClientOptions {
    private String mUrl = MapConstants.ROOT_URL;

    /**
     * 单例模式获取联网类的对象
     * @return HttpClientOptions [联网操作类的对象]
     */
    public static HttpClientOptions getInstance(){

        return InstanceHolder.mHttpClientOptions;
    }

    private static class InstanceHolder{
        private static final HttpClientOptions mHttpClientOptions = new HttpClientOptions();
    }
    /**
     * 设置地图服务器地址
     * @param url [瓦片服务器的地址]
     */
    public void setServiceUrl(String url){
        mUrl = url;
    }

    /**
     * 获取瓦片地图服务器地址
     * @return Url [瓦片地图服务器地址]
     */
    public String getServiceUrl(){
        return this.mUrl;
    }

    /**
     * 联网GET请求
     * @param url [瓦片地图服务器地址]
     * @param callBack [联网操作的监听接口]
     */
    public void doGet(String url, IMapHttpCallBack callBack){
        connectToServer(url,null,true,callBack);
    }

    public byte[] doGet(String url){
        return connectToServer(url,null,true,null);
    }

    /**
     * 联网POST请求
     * @param url [瓦片地图服务器地址]
     * @param request [post请求的参数]
     * @param callBack [联网操作的监听接口]
     */
    public void doPost(String url, byte[] request, IMapHttpCallBack callBack){
        connectToServer(url,request,false,callBack);
    }

    public byte[] doPost(String url, byte[] request){
        return connectToServer(url,request,false,null);
    }

    /**
     * 联网操作
     * @param url [瓦片地图服务器地址]
     * @param request [post请求的参数]
     * @param isGet [判断是get还是post请求]
     * @param callBack [联网操作的监听接口]
     */
    private byte[] connectToServer(String url, byte[] request, boolean isGet, IMapHttpCallBack callBack){
        StringBuffer strErr = new StringBuffer();
        try
        {
            URL objUrl = new URL(url);

            // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

            //设置连接与读取超时时间，单位是毫秒
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(2000);

            //设置可输入输出状态
            if (isGet) {
                // 设置请求方式，默认为GET
                connection.setRequestMethod("GET");
            }else {
                // 设置请求方式，默认为POST
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
            }
            connection.setDoInput(true);


            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);

            // 配置连接的Content-type，配置为application/x- www-form-urlencoded的意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
            connection.setRequestProperty(" Content-Type ", " application/x-www-form-urlencoded; charset=utf-8 ");
            if (request != null) {
                // 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
                // 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                // DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
                out.write(request);
                out.flush();
                out.close(); // flush and close
            }

            InputStream inputStream = connection.getInputStream();

            byte[] bytes = readInputStream(inputStream);

            inputStream.close();
            if (callBack != null) {
                callBack.httpConnectSucceed(bytes);
            }
            return bytes;

        }
        catch(Exception e)
        {
            if (e.getMessage() != null)
            {
                if (e.getMessage().indexOf("failed to connect to") >= 0)
                {
                    strErr.append("网络链接异常！");
                }
                else
                {
                    strErr.append(e.getMessage());
                }
            }
            else
            {
                strErr.append("连接超时");
            }
            if (callBack != null) {
                callBack.httpConnectFailed(strErr);
            }
        }
        return null;
    }

    /**
     * 从数据流中获得数据
     * @param inputStream [联网获取的输入流]
     * @return bos.toByteArray() [返回的yte数组]
     * @throws IOException [抛出的io异常信息]
     */
    public  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }

}
