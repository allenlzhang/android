package com.carlt.yema.ui.activity.carstate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.road.Crossroad;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.carlt.yema.R;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.base.LoadingActivity2;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.map.PositionEntity;
import com.carlt.yema.utils.map.RouteTask;
import com.carlt.yema.utils.map.SensorEventHelper;
import com.carlt.yema.utils.map.WalkRouteOverlay;
import com.google.gson.JsonParser;

import java.util.List;

/**
 * 定位寻车
 */
public class FindCarActivity extends LoadingActivity2 implements LocationSource, AMapLocationListener, View.OnClickListener {

    private View mViewInput;// 输入框
    private TextView mTxtPos;// 爱车位置
    private ImageView mImgCha;// 叉号按钮
    private ImageView mImgPLoc;// che的位置
    private TextView mTxtPLoc;
    private TextView txtRight;//title 右侧按钮

    private MapView mMapView;
    private AMap mMap;
    private AMapLocationClient mLocationClient;
    private AMapLocation mFirstLoc;
    private LatLng mFirstCarLoc;
    private RouteTask mRouteTask;
    private Dialog mPDialog;
    private PolylineOptions mlineOption;
    private WalkPath mPath;
    private WalkRouteResult mResult;
    private String locName;// 终点位置
    private String fromLocName;// 从另一页跳转
    private Marker mLocMarker;
    private Circle mLocCircle;
    private boolean isMyLocenable = false;
    private boolean isNeedRefresh=false;//是否需要刷新
    private AMapLocation mCurrentLoc;
    private SensorEventHelper mSensorHelper;
    private final static int ZOOM = 20;// 缩放级别
    private Text txtStart;
    private Text txtEnd;
    private Marker endMarker;
    private Polyline mRouteLine;
    private Marker startMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);
        initTitle("车辆定位");
        init(savedInstanceState);
        loadingDataUI();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        } else {
            mSensorHelper = new SensorEventHelper(this);
            if (mSensorHelper != null) {
                mSensorHelper.registerSensorListener();

                if (mSensorHelper.getCurrentMarker() == null && mLocMarker != null) {
                    mSensorHelper.setCurrentMarker(mLocMarker);
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private void initData() {
        CPControl.GetCarExtInfo(mCallback);
//        loadDataSuccess(null);
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
       // "position":"120.132851,30.281979",
//		 data = "108.95224799262152,34.19749321831597";
//      String value = "{'position':'120.132851,30.281979'}";
        String value = (String) ((BaseResponseInfo) bInfo).getValue();
        JsonParser jsonParser = new JsonParser();
      String  data =  jsonParser.parse(value).getAsJsonObject().get("position").getAsString();
        if (data != null && !TextUtils.isEmpty(data.toString())) {
                String ss = data.toString();
                String[] ll = ss.split(",");
                if (ll.length > 1) {
                    LatLng latLng = new LatLng(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                    CoordinateConverter converter = new CoordinateConverter(this);
                    converter.from(CoordinateConverter.CoordType.GPS);
                    converter.coord(latLng);
                    mFirstCarLoc = converter.convert();
                    getAddress(mFirstCarLoc);
                } else {
                    UUToast.showUUToast(this, "暂未获取到车辆位置");
                }
                drawFirstLine();
            } else {
                UUToast.showUUToast(this, "暂未获取到车辆位置");
            }
    }

    public void getAddress(LatLng ll) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
                if (arg1 == 1000) {
                    if (arg0 != null && arg0.getRegeocodeAddress() != null && arg0.getRegeocodeAddress().getFormatAddress() != null) {
                        // locName =
                        // arg0.getRegeocodeAddress().getFormatAddress();
                        RegeocodeAddress raddress = arg0.getRegeocodeAddress();
                        String building = raddress.getBuilding();
                        String bname = raddress.getBusinessAreas().size() > 0 ? raddress.getBusinessAreas().get(0).getName() : "";
                        String adCode = raddress.getAdCode();
                        String towncode = raddress.getTowncode();
                        String township = raddress.getTownship();
                        List<AoiItem> aos = raddress.getAois();
                        List<Crossroad> cRoads = raddress.getCrossroads();
                        List<PoiItem> pois = raddress.getPois();
                        List<RegeocodeRoad> roads = raddress.getRoads();
                        locName = township;
                        if (pois.size() > 0) {
                            locName += pois.get(0).getTitle();
                        }
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
            }

        });
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(ll.latitude, ll.longitude), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
        UUToast.showUUToast(this, "暂未获取到车辆位置");
    }

    private void init(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.findcar_mapView);
        mViewInput = findViewById(R.id.findcar_lay_input);
        mTxtPos = (TextView) findViewById(R.id.findcar_txt_carpos);
        mImgCha = (ImageView) findViewById(R.id.findcar_img_cha);
        mImgPLoc = (ImageView) findViewById(R.id.findCar_img_persion);
        mTxtPLoc = (TextView) findViewById(R.id.findCar_img_persion_txt);
        txtRight = (TextView) findViewById(R.id.head_back_text2);
        backTV2.setVisibility(View.GONE);
        mImgPLoc.setVisibility(View.GONE);
        mTxtPLoc.setVisibility(View.GONE);
        mViewInput.setVisibility(View.GONE);
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setText("修改终点");
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
        mMap.setLocationSource(this);
        mMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        setMylocEnable(false);

        mTxtPos.setOnClickListener(this);
        mImgCha.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        mRouteTask = RouteTask.getInstance(getApplicationContext());

        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

        // mRegeocodeTask = new RegeocodeTask(getApplicationContext());
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setInterval(2000);
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findcar_txt_carpos:
                // 跳转至智能搜索页面
                Intent mIntent = new Intent(FindCarActivity.this, SearchAddrActivity.class);
                if (mFirstLoc == null) {
                    UUToast.showUUToast(this, "未获取到当前位置!");
                    return;
                }
                fromLocName = null;
                mIntent.putExtra("latlng", mFirstLoc.getLatitude() + "," + mFirstLoc.getLongitude());
                mIntent.putExtra("cityCode", mFirstLoc.getCityCode());
                startActivityForResult(mIntent, 0);
                break;

            case R.id.findcar_img_cha:
                // 隐藏输入框
                clearMarkers();
                reSet();
                break;
            case R.id.head_back_text2:
                if (mViewInput.getVisibility() == View.VISIBLE) {
                    mViewInput.setVisibility(View.GONE);
                    mImgPLoc.setVisibility(View.GONE);
                    mTxtPLoc.setVisibility(View.GONE);
                    initTitle("终点变更");
                    txtRight.setText("修改终点");
                    // 点击确定开始导航画线
                    BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_car);
                    int w = mMapView.getWidth() / 2 + bd.getWidth() / 2;
                    int h = mMapView.getHeight() / 2 + bd.getHeight();
                    Point p = new Point(w, h);
                    LatLng ll = mMap.getProjection().fromScreenLocation(p);

                    if (mFirstLoc == null) {
                        UUToast.showUUToast(this, "定位失败");
                        return;
                    }

                    PositionEntity ps = new PositionEntity();
                    ps.longitude = mFirstLoc.getLongitude();
                    ps.latitue = mFirstLoc.getLatitude();

                    PositionEntity pe = new PositionEntity();
                    pe.longitude = ll.longitude;
                    pe.latitue = ll.latitude;
                    clearMarkers();
                    mCurrentLoc=null;
                    drawLine(ps, pe);
                    locName = "";
                    getAddress(ll);
                    mFirstCarLoc = ll;
                } else {
                    if (mFirstCarLoc == null) {
                        UUToast.showUUToast(this, "暂未获取到车辆位置");
                        return;
                    }

                    if (mFirstLoc == null) {
                        UUToast.showUUToast(this, "未获取到当前位置!");
                        return;
                    }

                    setMylocEnable(false);
                    mViewInput.setVisibility(View.VISIBLE);
                    txtRight.setText("确定");
                    clearMarkers();

                    if (mFirstLoc != null) {
                        LatLng ll = null;
                        if (mlineOption == null || mlineOption.getPoints().size() < 1) {
                            ll = new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude());
                        } else {
                            ll = mlineOption.getPoints().get(0);
                        }

                        MarkerOptions startOpt = new MarkerOptions();
                        BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_user);
                        startOpt.icon(descriptorStart);
                        startOpt.anchor(0.5f, 1.1f);
                        startOpt.position(ll);
                        startMarker = mMap.addMarker(startOpt);

                        TextOptions toptStart = new TextOptions();
                        toptStart.position(ll);
                        toptStart.fontSize((int) (YemaApplication.ScaledDensity * 14));
                        toptStart.fontColor(Color.parseColor("#FFFFFF"));
                        toptStart.text("您的位置");
                        toptStart.backgroundColor(Color.parseColor("#404040"));
                        mMap.addText(toptStart);
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mFirstCarLoc, 18);
                        mMap.animateCamera(update);
                    }
                    mImgPLoc.setVisibility(View.VISIBLE);
                    mTxtPLoc.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(locName)) {
                        mTxtPos.setText(Html.fromHtml(locName + "  <font color='#666666'>(点击修改)</font>"));
                    } else {
                        mTxtPos.setText(Html.fromHtml("暂无车辆位置信息" + "  <font color='#666666'>(点击修改)</font>"));
                        getAddress(mFirstCarLoc);
                    }
                }
                break;
        }
    }

    private void reSet() {
        mViewInput.setVisibility(View.GONE);
        txtRight.setText("修改终点");
        setMylocEnable(true);
        mImgPLoc.setVisibility(View.GONE);
        mTxtPLoc.setVisibility(View.GONE);
        if (mPath != null) {
            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, mMap, mPath, mResult.getStartPos(), mResult.getTargetPos());
            walkRouteOverlay.removeFromMap();
            walkRouteOverlay.zoomToSpan();

            mlineOption = new PolylineOptions();
            for (WalkStep walkStep : mPath.getSteps()) {
                for (LatLonPoint ll : walkStep.getPolyline()) {
                    LatLng l2 = new LatLng(ll.getLatitude(), ll.getLongitude());
                    mlineOption.add(l2);
                }
            }

            mlineOption.color(Color.parseColor("#3D93FD"));
            mlineOption.width(13);
            mRouteLine = mMap.addPolyline(mlineOption);

            TextOptions toptEnd = new TextOptions();
            toptEnd.position(mlineOption.getPoints().get(mlineOption.getPoints().size() - 1));
            toptEnd.fontSize((int) (YemaApplication.ScaledDensity * 14));
            toptEnd.fontColor(Color.parseColor("#FFFFFF"));
            toptEnd.text("爱车位置");
            toptEnd.backgroundColor(Color.parseColor("#404040"));
            txtEnd = mMap.addText(toptEnd);

            MarkerOptions endOpt = new MarkerOptions();
            BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_car);
            endOpt.icon(descriptorEnd);
            endOpt.anchor(0.5f, 1.1f);
            endOpt.position(mlineOption.getPoints().get(mlineOption.getPoints().size() - 1));
            endMarker = mMap.addMarker(endOpt);
        } else {
            if (mFirstCarLoc != null) {
                TextOptions toptEnd = new TextOptions();
                toptEnd.position(mFirstCarLoc);
                toptEnd.fontSize((int) (YemaApplication.ScaledDensity * 14));
                toptEnd.fontColor(Color.parseColor("#FFFFFF"));
                toptEnd.text("爱车位置");
                toptEnd.backgroundColor(Color.parseColor("#404040"));
                txtEnd = mMap.addText(toptEnd);

                MarkerOptions endOpt = new MarkerOptions();
                BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_car);
                endOpt.icon(descriptorEnd);
                endOpt.anchor(0.5f, 1.1f);
                endOpt.position(mFirstCarLoc);
                endMarker = mMap.addMarker(endOpt);


                MarkerOptions startOpt = new MarkerOptions();
                BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_user);
                startOpt.icon(descriptorStart);
                startOpt.anchor(0.5f, 1.1f);
                startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                startMarker = mMap.addMarker(startOpt);

                TextOptions toptStart = new TextOptions();
                toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                toptStart.fontSize((int) (YemaApplication.ScaledDensity * 14));
                toptStart.fontColor(Color.parseColor("#FFFFFF"));
                toptStart.text("您的位置");
                toptStart.backgroundColor(Color.parseColor("#404040"));
                txtStart = mMap.addText(toptStart);



                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mFirstCarLoc, 18);
                mMap.animateCamera(update);
            } else if (mFirstLoc != null) {
                MarkerOptions startOpt = new MarkerOptions();
                BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_user);
                startOpt.icon(descriptorStart);
                startOpt.anchor(0.5f, 1.1f);
                startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                startMarker = mMap.addMarker(startOpt);

                TextOptions toptStart = new TextOptions();
                toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                toptStart.fontSize((int) (YemaApplication.ScaledDensity * 14));
                toptStart.fontColor(Color.parseColor("#FFFFFF"));
                toptStart.text("您的位置");
                toptStart.backgroundColor(Color.parseColor("#404040"));
                txtStart = mMap.addText(toptStart);

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()), 18);
                mMap.animateCamera(update);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                String latlng = data.getStringExtra("latlng");
                if (!TextUtils.isEmpty(latlng)) {
                    fromLocName = data.getStringExtra("name");
                    mTxtPos.setText(Html.fromHtml(fromLocName + "  <font color='#666666'>(点击修改)</font>"));
                    PositionEntity pe = new PositionEntity();
                    pe.longitude = Double.parseDouble(latlng.split(",")[1]);
                    pe.latitue = Double.parseDouble(latlng.split(",")[0]);
                    Log.e("info", "pe.longitude=="+pe.longitude);
                    Log.e("info", "pe.latitue=="+pe.latitue);
                    mFirstCarLoc = new LatLng(pe.latitue, pe.longitude);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mFirstCarLoc, ZOOM);
                    //mMap.animateCamera(update);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            mFirstCarLoc, ZOOM));
                    clearMarkers();
                    MarkerOptions startOpt = new MarkerOptions();
                    BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_user);
                    startOpt.icon(descriptorStart);
                    startOpt.anchor(0.5f, 1.1f);
                    startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                    startMarker = mMap.addMarker(startOpt);

                    TextOptions toptStart = new TextOptions();
                    toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                    toptStart.fontSize((int) (YemaApplication.ScaledDensity * 14));
                    toptStart.fontColor(Color.parseColor("#FFFFFF"));
                    toptStart.text("您的位置");
                    toptStart.backgroundColor(Color.parseColor("#404040"));
                    txtStart = mMap.addText(toptStart);
                }
            }
        }
    }

    public void clearMarkers() {
        mMap.clear();
    }
    @Override
    public void onLocationChanged(AMapLocation arg0) {
        if (arg0 != null && arg0.getErrorCode() == 0) {

            if (mFirstLoc == null) {
                mFirstLoc = arg0;
                // 开始导航画线
                drawFirstLine();
            }

            mFirstLoc = arg0;

            if (isMyLocenable) {
                if (mCurrentLoc != null) {
                    if (mCurrentLoc.getLatitude() == arg0.getLatitude()
                            && mCurrentLoc.getLongitude() == arg0
                            .getLongitude()) {
                        isNeedRefresh = false;
                    } else {
                        isNeedRefresh = true;
                    }
                } else {
                    isNeedRefresh = true;
                }
                if(isNeedRefresh){
                    // 显示更新 定位蓝点
                    if (mLocMarker != null) {
                        mLocMarker.remove();
                        mLocMarker = null;
                    }

                    if (mLocCircle != null) {
                        mLocCircle.remove();
                        mLocCircle = null;
                    }

                    addCircle(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()), mFirstLoc.getAccuracy());
                    addMarker(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                    mSensorHelper.setCurrentMarker(mLocMarker);// 定位图标旋转
                }
                mCurrentLoc = arg0;
            }

        } else {
            String errText = "定位失败," + arg0.getErrorCode() + ": " + arg0.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mLocCircle = mMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_loc);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = mMap.addMarker(options);
    }


    public void drawFirstLine() {
        if (mFirstCarLoc != null) {
            if (mFirstLoc != null) {
                PositionEntity ps = new PositionEntity();
                ps.longitude = mFirstLoc.getLongitude();
                ps.latitue = mFirstLoc.getLatitude();
                PositionEntity pe = new PositionEntity();
                pe.longitude = mFirstCarLoc.longitude;
                pe.latitue = mFirstCarLoc.latitude;
                clearMarkers();
                drawLine(ps, pe);
            } else {
                UUToast.showUUToast(this, "正在定位您的位置");
                TextOptions toptEnd = new TextOptions();
                toptEnd.position(mFirstCarLoc);
                toptEnd.fontSize((int) (YemaApplication.ScaledDensity * 14));
                toptEnd.fontColor(Color.parseColor("#FFFFFF"));
                toptEnd.text("爱车位置");
                toptEnd.backgroundColor(Color.parseColor("#404040"));
                txtEnd = mMap.addText(toptEnd);

                MarkerOptions endOpt = new MarkerOptions();
                BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_car);
                endOpt.icon(descriptorEnd);
                endOpt.anchor(0.5f, 1.1f);
                endOpt.position(mFirstCarLoc);
                endMarker = mMap.addMarker(endOpt);
            }
        }
    }
    void showProgressDialog() {
        dissmissDialog();
        mPDialog = PopBoxCreat.createDialogWithProgress(this, "正在处理请稍等...");
        mPDialog.show();
    }

    void dissmissDialog() {
        if (mPDialog != null && mPDialog.isShowing()) {
            mPDialog.dismiss();
        }
    }


    public void drawLine(final PositionEntity ps, final PositionEntity pe) {
        showProgressDialog();
        setMylocEnable(true);
        mRouteTask.setStartPoint(ps);
        mRouteTask.setEndPoint(pe);
        mRouteTask.removeAllResultListener();
        mRouteTask.addRouteResultListener(new RouteTask.OnRouteResult() {

            @Override
            public void onRoutePath(WalkPath path, WalkRouteResult result) {
                dissmissDialog();
                LatLng lls = null;
                LatLng lle = null;
                if (path == null || result == null) {
                    UUToast.showUUToast(FindCarActivity.this, "路径规划失败，请检查您的网络和定位设置；（当您的爱车离您过远时，无法规划您的步行路径）");
                    lls = new LatLng(ps.latitue, ps.longitude);
                    lle = new LatLng(pe.latitue, pe.longitude);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lle, 18);
                    mMap.animateCamera(update);
                } else {
                    mPath = path;
                    mResult = result;

                    List<WalkStep> steps = path.getSteps();
                    mlineOption = new PolylineOptions();
                    for (WalkStep walkStep : steps) {
                        for (LatLonPoint ll : walkStep.getPolyline()) {
                            LatLng l2 = new LatLng(ll.getLatitude(), ll.getLongitude());
                            mlineOption.add(l2);
                        }
                    }

                    mlineOption.color(Color.parseColor("#3D93FD"));
                    mlineOption.width(13);
                    mRouteLine = mMap.addPolyline(mlineOption);
                    lls = mlineOption.getPoints().get(0);
                    lle = mlineOption.getPoints().get(mlineOption.getPoints().size() - 1);

                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(FindCarActivity.this, mMap, path, result.getStartPos(), result.getTargetPos());
                    walkRouteOverlay.zoomToSpan();
                    walkRouteOverlay.removeFromMap();
                }

                MarkerOptions startOpt = new MarkerOptions();
                BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_user);
                startOpt.icon(descriptorStart);
                startOpt.anchor(0.5f, 1.1f);
                startOpt.position(lls);
                startMarker = mMap.addMarker(startOpt);

                TextOptions toptStart = new TextOptions();
                toptStart.position(lls);
                toptStart.fontSize((int) (YemaApplication.ScaledDensity * 14));
                toptStart.fontColor(Color.parseColor("#FFFFFF"));
                toptStart.text("您的位置");
                toptStart.backgroundColor(Color.parseColor("#404040"));
                txtStart = mMap.addText(toptStart);



                TextOptions toptEnd = new TextOptions();
                toptEnd.position(lle);
                toptEnd.fontSize((int) (YemaApplication.ScaledDensity * 14));
                toptEnd.fontColor(Color.parseColor("#FFFFFF"));
                toptEnd.text("爱车位置");
                toptEnd.backgroundColor(Color.parseColor("#404040"));
                txtEnd = mMap.addText(toptEnd);

                MarkerOptions endOpt = new MarkerOptions();
                BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loaction_car);
                endOpt.icon(descriptorEnd);
                endOpt.anchor(0.5f, 1.1f);
                endOpt.position(lle);
                endMarker = mMap.addMarker(endOpt);

            }
        });
        mRouteTask.search();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    public void setMylocEnable(boolean enable) {
        this.isMyLocenable = enable;
        if (!enable) {
            if (mLocMarker != null) {
                mLocMarker.remove();
            }

            if (mLocCircle != null) {
                mLocCircle.remove();
            }

        }
    }
}
