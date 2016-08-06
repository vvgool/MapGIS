package com.wiesen.provider.net.wmts;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ParseWMTS {

    /**
     * 解析wmts网络xml
     * @param str 数据
     * @param wmtsInfo 解析为封装的WMTSInfo对象
     * @return
     */
    public static boolean parseWMTSSting(String str , WMTSInfo wmtsInfo){

        str=str.substring(str.indexOf("<ows:ServiceIdentification>"),str.indexOf("</Layer>")+"</Layer>".length());
        wmtsInfo.TileMatrixSet = new ArrayList<String>();
        try {
            boolean isLayer = false;
            boolean isStyle = false;
            InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
            XmlPullParserFactory xpp = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xpp.newPullParser();
            xmlPullParser.setInput(is,"UTF-8");

            int eventType = xmlPullParser.getEventType();

            while (eventType!= XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String tagName = xmlPullParser.getName();
                        if (tagName .equals("ows:ServiceTypeVersion")){
                            wmtsInfo.ServiceVersion = xmlPullParser.nextText();
                        }
                        if (tagName.equals("Layer")) {
                            isLayer = true;
                        }
                        if (xmlPullParser.getName().equals("ows:Title")&&isLayer) {
                            wmtsInfo.Layer = xmlPullParser.nextText();
                            isLayer =false;
                        }
                        if (tagName.equals("Style")){
                            isStyle = true;
                        }
                        if (xmlPullParser.getName().equals("ows:Identifier")&&isStyle) {
                            wmtsInfo.Style = xmlPullParser.nextText();
                            isStyle = false;
                        }
                        if (tagName.equals("Format")){
                            wmtsInfo.Format = xmlPullParser.nextText();
                        }
                        if (tagName.equals("TileMatrixSet")){
                            wmtsInfo.TileMatrixSet.add(xmlPullParser.nextText());
                        }
                        break;

                }
                eventType = xmlPullParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
