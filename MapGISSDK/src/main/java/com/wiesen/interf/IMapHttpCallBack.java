package com.wiesen.interf;

public interface IMapHttpCallBack {
    /**
     * 数据请求成功，并返回地图瓦片数据
     * @param response 瓦片数据
     */
    void httpConnectSucceed(byte[] response);

    /**
     * 数据请求失败，返回失败原因
     * @param strErr 失败信息描述
     */
    void httpConnectFailed(StringBuffer strErr);
}
