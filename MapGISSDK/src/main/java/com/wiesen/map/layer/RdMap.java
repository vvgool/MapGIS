package com.wiesen.map.layer;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.wiesen.interf.IMapControl;
import com.wiesen.interf.IMapTileCallBack;
import com.wiesen.map.MapLoading;
import com.wiesen.map.MapGestureListener;
import com.wiesen.map.gl.GL20DrawBase;
import com.wiesen.map.gl.GL20MapShader;
import com.wiesen.map.gl.GL20ScreenPosition;
import com.wiesen.map.gl.GL20ShaderUtils;
import com.wiesen.map.gl.GLBufferUtils;
import com.wiesen.map.gl.MatrixState;
import com.wiesen.provider.cache.BitMapCache;
import com.wiesen.provider.local.OfflineTilePro;
import com.wiesen.utils.MapConstants;
import com.wiesen.utils.geo.GeoPoint;
import com.wiesen.utils.geo.GeoUtils;
import com.wiesen.utils.geo.MapOptions;
import com.wiesen.utils.geo.MapStatus;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wiesen on 16-8-3.
 */
public class RdMap extends GL20DrawBase implements GLSurfaceView.Renderer,IMapControl, MapOptions.MapStatusChanged, IMapTileCallBack {
    private static final int TileSize = MapConstants.TILE_SIZE;
    private final  Context mContext;
    private int mAttribPosition;
    private int mMVPMatrixHandle;
    private int mAttribTexCoord;
    private float[] mTextureCoods={ 0,0, 0,1, 1,0, 1,1};

    private FloatBuffer mVerticeBuf;
    private FloatBuffer mTextureCoodBuf;
    private int mTextureID;

    private LayerDrawable mLayerDrawable;

    private float mMoveX = 0;
    private float mMoveY = 0;
    private float mScale = 1;
    private float mStartDistance;
    private Point mCenterPoint;

    private GL20ScreenPosition mScreenPosition;
    private RequestRenderFresh mRequestRenderFresh;
    private MapOptions mMapOptions;
    private MapStatus mMapStatus;
    private Rect mLimitRect;
    private MapLoading mMapLoading;
    private BitMapCache mBitmapCache;
    private boolean mIsFreshMap = false;
    private Thread mMapLoadThread;

    public RdMap(Context context) {
        mContext = context;
        mMapOptions = MapOptions.getInstance();
        mMapOptions.setOnMapStatusChangedListener(this);
        mMapStatus = mMapOptions.getMapStatus();
        mLimitRect = new Rect();
        mBitmapCache = new BitMapCache(context);
        mMapLoading = new MapLoading(this,mBitmapCache);
        freshMapCenterPoint();
        OfflineTilePro.getInstance().setOnDecodeDeuMapIndexListener(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLayerDrawable = new LayerDrawable();
        mMapLoadThread= new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (mIsFreshMap){
                        mMapLoading.loadCurrentMap(mCenterPoint,mMapOptions.getDeuMapEnabled());
                        mIsFreshMap = false;
                    }
                }
            }
        });
        mMapLoadThread.start();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        onCreate();
        initTexture();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        mScreenPosition = GL20ScreenPosition.getInstance(width,height);
        mLayerDrawable.onLayerSizeChanged(width,height);
        GLES20.glViewport(0,0,width,height);
        float ratio = (float)width/(float)height;
        MatrixState.setProjectOrthoM(-ratio,ratio,-1,1,1,10);
        loadMap();
        initBuf(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        doDraw();
    }

    @Override
    protected void onCreateProgram() {
        try {
            String vertexStr = GL20MapShader.getVertexShaderString();
            String fragmentStr = GL20MapShader.getFragmentShaderString();
            mGL20Factory.createShader(new GL20ShaderUtils(vertexStr,fragmentStr))
                    .createProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onGetLocation() {
        mAttribPosition = mGL20Factory.getAttribLocation("aPosition");
        //获取程序中总变换矩阵引用id
        mMVPMatrixHandle = mGL20Factory.getUniformLocation("uMVPMatrix");
        mAttribTexCoord = mGL20Factory.getAttribLocation("aTexCoor");
    }

    public void freshTexture(){
        if (mRequestRenderFresh != null){
            mRequestRenderFresh.onFresh(mFreshRunnable);
        }
    }

    private Runnable mFreshRunnable = new Runnable() {
        @Override
        public void run() {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,mLayerDrawable.getBitmap(),0);
        }
    };

    private void initTexture(){
        int textureIds[] = new int[1];
        // 启用纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textureIds, 0);
        // 将绑定纹理(texuture[0]表示指针指向纹理数据的初始位置)
        mTextureID = textureIds[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_NEAREST);
    }


    /**
     * init texture inflater vertex
     * @param width screen width
     * @param height screen height
     */
    private void initBuf(float width,float height){
        float layerWidth = mLayerDrawable.getWidth();
        float layerHeight = mLayerDrawable.getHeight();
        float startX = -(mLayerDrawable.getWidth()-width)/2;
        float startY =-(mLayerDrawable.getHeight()-height)/2;
        float[] point0_0 = mScreenPosition.changePX2GL(startX,startY);
        float[] point0_1 = mScreenPosition.changePX2GL(startX,startY+layerHeight);
        float[] point1_1 = mScreenPosition.changePX2GL(startX+layerWidth,startY + layerHeight);
        float[] point1_0 = mScreenPosition.changePX2GL(startX+layerWidth,startY);
        float[] vertex={
                point0_0[0],point0_0[1],0,
                point0_1[0],point0_1[1],0,
                point1_0[0],point1_0[1],0,
                point1_1[0],point1_1[1],0
        };
        mVerticeBuf = GLBufferUtils.buildFloatBuffer(vertex);
        mTextureCoodBuf = GLBufferUtils.buildFloatBuffer(mTextureCoods);
    }

    @Override
    protected void doDraw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT|GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(163,156,158,255);
        mGL20Factory.useProgram();
        MatrixState.setInitStack();
        MatrixState.translate(mScreenPosition.getMoveX(mMoveX),mScreenPosition.getMoveY(mMoveY),0);
        MatrixState.scale(mScale,mScale,0);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrixMM(), 0);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glVertexAttribPointer(mAttribPosition, 3, GLES20.GL_FLOAT, false, 0, mVerticeBuf);
        GLES20.glVertexAttribPointer(mAttribTexCoord, 2, GLES20.GL_FLOAT, false, 0, mTextureCoodBuf);

        GLES20.glEnableVertexAttribArray(mAttribPosition);
        GLES20.glEnableVertexAttribArray(mAttribTexCoord);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(mAttribPosition);
        GLES20.glDisableVertexAttribArray(mAttribTexCoord);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLayerDrawable.onDestroy();
        mBitmapCache.clearCache();
        mMapLoadThread.interrupt();
    }

    @Override
    public void zoomIn() {
        int level = mMapStatus.getCurrentZoom();
        if (level < MapConstants.MAX_ZOOM){
            mMapStatus.setZoomLevel(++level);
            freshMapCenterPoint();
            loadMap();
        }
    }

    @Override
    public void zoomOut() {
        int level = mMapStatus.getCurrentZoom();
        if (level > MapConstants.MIN_ZOOM){
            mMapStatus.setZoomLevel(--level);
            freshMapCenterPoint();
            loadMap();
        }
    }

    @Override
    public int getZoomLevel() {
        return mMapStatus.getCurrentZoom();
    }
    /**
     * finger touch the screen
     * @param event touch event
     */
    public void onTouchEvent(MotionEvent event){
        mTouchEvent.onTouchEvent(event);
        freshTexture();
    }
    @Override
    public void setZoomLevel(int zoomLevel) {
        if (zoomLevel>MapConstants.MIN_ZOOM&&zoomLevel<MapConstants.MAX_ZOOM) {
            mMapStatus.setZoomLevel(zoomLevel);
            freshMapCenterPoint();
            loadMap();
        }
    }

    @Override
    public GeoPoint getMapCenter() {
        return mMapStatus.getGeoPoint();
    }

    @Override
    public void setMapCenter(GeoPoint geoPoint) {
        mMapStatus.setGeoPoint(geoPoint);
        freshMapCenterPoint();
        loadMap();
    }

    @Override
    public MapOptions getMapOptions() {
        return mMapOptions;
    }

    /**
     * 更新画布的偏移量
     *
     * @param moveX X方向移动的距离
     * @param moveY y方向移动的距离
     */
    public void freshOffsetPix(float moveX, float moveY) {
        this.mMoveX = moveX;
        this.mMoveY = moveY;
    }
    public void loadMap(){
        mIsFreshMap = true;
    }
    /**
     *
     * 根据地图中心经纬度更新中心坐标
     */
    private void freshMapCenterPoint() {
        mCenterPoint = GeoUtils.LatLongToPixelXY(mMapStatus.getGeoPoint().getLatitude()
                , mMapStatus.getGeoPoint().getLongitude(), mMapStatus.getCurrentZoom(), mCenterPoint);

        Point startP=GeoUtils.LatLongToPixelXY(MapConstants.MaxLatitude
                , MapConstants.MinLongitude, mMapStatus.getCurrentZoom(), null);
        Point endP=GeoUtils.LatLongToPixelXY(MapConstants.MinLatitude
                , MapConstants.MaxLongitude, mMapStatus.getCurrentZoom(), null);
        mLimitRect.set(startP.x,startP.y,endP.x,endP.y);
    }


    /**
     * 更新地图中心点经纬度
     */
    private void freshMapGeoLatLon() {
        mMapStatus.setGeoPoint(GeoUtils.PixelXYToLatLong(mCenterPoint.x, mCenterPoint.y, mMapStatus.getCurrentZoom(), null));
    }

    /**
     * 计算两点间的距离
     * @param event 事件响应的对象
     * @return float
     */
    private float getSpaceByTwoPoint(MotionEvent event) throws Exception{
        float disX = event.getX(0) - event.getX(1);
        float disY = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(disX * disX + disY * disY);
    }

    public LayerDrawable getLayerDrawable(){
        return mLayerDrawable;
    }

    public MapDrawable getMapDrawable(){
        return mLayerDrawable.getMapDrawable();
    }

    public void setOnRequestRenderFresh(RequestRenderFresh requestRenderFresh){
        mRequestRenderFresh = requestRenderFresh;
    }

    @Override
    public void onMapStatusChanged() {
        loadMap();
    }
    private MapGestureListener mTouchEvent=new  MapGestureListener() {

        @Override
        public void onTouchClick(MotionEvent event) {

        }

        @Override
        public void onTouchMove(MotionEvent event) {
            mapTranslateGesture(event);
        }

        @Override
        public void onTouchTwoPointDown(MotionEvent event) {
            try {
                mStartDistance = getSpaceByTwoPoint(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTouchTwoPointMove(MotionEvent event) {
            mapZoomScaleGesture(event);
        }

        @Override
        public void onTouchMorePointUp(MotionEvent event) {
            if (mScale > 1.5) {
                zoomIn();

            } else if (mScale < 0.5) {
                zoomOut();
            }
            mScale = 1;
        }

        /**
         * 手指缩放手势事件
         * @param event 事件响应的对象
         */
        private void mapZoomScaleGesture(MotionEvent event) {
            if (mMapOptions.getZoomGesturesEnabled()) {
                try {
                    mScale *= getSpaceByTwoPoint(event) / mStartDistance;
                    mStartDistance = getSpaceByTwoPoint(event);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        /**
         * 手指移动事件
         * @param event 事件响应的对象
         */
        private void mapTranslateGesture(MotionEvent event) {
            if (mMapOptions.getScrollGesturesEnabled()) {
                float moveX = event.getX() - mDownXPoint;
                float moveY = event.getY() - mDownYPoint;
                mCenterPoint.x -= moveX;
                mCenterPoint.y -= moveY;
                if (mCenterPoint.x > mLimitRect.right||
                        mCenterPoint.x<mLimitRect.left) {
                    mCenterPoint.x = mCenterPoint.x < mLimitRect.left? mLimitRect.left:mLimitRect.right;
                }else {
                    mMoveX += moveX;
                }

                if (mCenterPoint.y>mLimitRect.bottom||
                        mCenterPoint.y<mLimitRect.top){
                    mCenterPoint.y = mCenterPoint.y < mLimitRect.top? mLimitRect.top:mLimitRect.bottom;
                }else {
                    mMoveY += moveY;
                }
                freshMapGeoLatLon();
            }
            mDownXPoint = event.getX();
            mDownYPoint = event.getY();

            if (Math.abs(mMoveX) > TileSize || Math.abs(mMoveY) > TileSize) {
                loadMap();
            }
        }
    };

    @Override
    public void TilePrePareOk() {
        loadMap();
    }

    @Override
    public void TilePrePareError() {

    }

    public interface RequestRenderFresh{
        void onFresh(Runnable runnable);
    }
}
