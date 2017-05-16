package com.example.zte.day10_zte_baidumap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;

import overlayutil.PoiOverlay;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "111";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private BaiduMap baiduMap;
    private PoiSearch poiSearch;
    private EditText mCityEdit;
    private AutoCompleteTextView mAutoEdit;
    private SuggestionSearch suggestionSearch;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> searchResult;
    private LocationClient mLocationClient;
    private double latitude;
    private double longitude;
//    private int searchType = 1;
//    LatLng center = new LatLng(39.92235, 116.380338);
//    LatLng southwest = new LatLng( 39.92235, 116.380338 );
//    LatLng northeast = new LatLng( 39.947246, 116.414977);
//    LatLngBounds searchbound = new LatLngBounds.Builder().include(southwest).include(northeast).build();

    /**权限申请的结果回调借口
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意权限.
                    Log.e(TAG, "onRequestPermissionsResult: "+"用户同意了权限申请" );
                    location();
                } else {
                    //当用户拒绝提供定位权限时
                    Log.e(TAG, "onRequestPermissionsResult: "+"用户拒绝了权限" );
                }
                return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 23){
            //检查定位权限是否开启
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onCreate: "+"发现定位权限没有" );
                //没开启，久执行下面的方法申请权限，权限申请结果在回调借口中。
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }else{
                Log.e(TAG, "onCreate: "+"定位权限已经拥有，可以开始定位" );
                //如果权限已经拥有，久直接定位
                location();
            }
        }
        mCityEdit = (EditText) findViewById(R.id.main_city);
        mAutoEdit = (AutoCompleteTextView) findViewById(R.id.main_searchkey);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line);
        mAutoEdit.setAdapter(arrayAdapter);
        mAutoEdit.setThreshold(1);

        suggestionSearch = SuggestionSearch.newInstance();
        //设置搜索监听，获取搜索的结果
        suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult result) {
                if(result == null || result.getAllSuggestions() == null){
                    return;
                }
                searchResult = new ArrayList<>();
                for (SuggestionResult.SuggestionInfo info : result.getAllSuggestions()) {
                    if(info != null){
                        searchResult.add(info.key);
                    }
                }
                arrayAdapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,searchResult);
                mAutoEdit.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        });
        //mMapView = (MapView) findViewById(R.id.main_map);
        //获取百度地图
        baiduMap = ((SupportMapFragment) (getSupportFragmentManager()
                .findFragmentById(R.id.main_map))).getBaiduMap();
        //选择地图类型
        //baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置实时交通图
//        baiduMap.setTrafficEnabled(true);
       // baiduMap.setBaiduHeatMapEnabled(true);
//        ArrayList<BitmapDescriptor> list = new ArrayList<>();
//        list.add(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_delete));
//        list.add(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_delete));
//        list.add(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_btn_speak_now));
//        LatLng latLng = new LatLng(39.963175,116.400244);
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(android.R.drawable.ic_delete);
//        //设置一组覆盖物，
//        OverlayOptions overlay = new MarkerOptions().position(latLng).icons(list).zIndex(0).period(10).draggable(true);
//        baiduMap.addOverlay(overlay);
//        //设置成仅显示路段信息。
//        baiduMap.showMapPoi(false);
//        LatLng ll1 = new LatLng(39.93923, 116.357428);
//        LatLng ll2 = new LatLng(39.91923, 116.327428);
//        LatLng ll3 = new LatLng(39.89923, 116.347428);
//        LatLng ll4 = new LatLng(39.89923, 116.367428);
//        LatLng ll5 = new LatLng(39.91923, 116.387428);
//        List<LatLng> list = new ArrayList<>();
//        list.add(ll1);
//        list.add(ll2);
//        list.add(ll3);
//        list.add(ll4);
//        list.add(ll5);
//        OverlayOptions overlayOptions = new PolygonOptions()
//                .points(list)
//                .stroke(new Stroke(5,0xAA00FF00))
//                .fillColor(0xAAFFFF00);
//        baiduMap.addOverlay(overlayOptions);
        //设置文字改变监听，实现实时搜索
        mAutoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() <= 0){
                    return;
                }
                //根据关键字搜索
                suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .keyword(s.toString()).city(mCityEdit.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    baiduMap.clear();
                    //创建PoiOverlay
                    PoiOverlay overlay = new MyPoiOverlay(baiduMap);
                    //设置overlay可以处理标注点击事件
                    baiduMap.setOnMarkerClickListener(overlay);
                    //设置PoiOverlay数据
                    overlay.setData(result);
                    //添加PoiOverlay到地图中
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    return;
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    private void location() {
        mLocationClient = new LocationClient(this);
        initLocation();
        //开始定位
        mLocationClient.start();//声明LocationClient类
        //设置定位监听
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                //Receive Location
                latitude = location.getLatitude();

                longitude = location.getLongitude();
                Log.e(TAG, "onReceiveLocation: "+latitude+"____"+longitude );
                String city = location.getAddress().city;
                String s = city.substring(0, city.length()-1);
                String street = location.getAddress().street;

                if (!location.equals(null)) {
                    mLocationClient.stop();

                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });//注册监听函数
    }

    /**测试发现百度地图搜索并且获取覆盖物，必须要设置成点击事件。
     * @param view
     */
    public void search(View view) {
        //startActivity(new Intent(MainActivity.this,PoiSearchDemo.class));

    }

    public void searchButtonProcess(View view) {
        poiSearch.searchInCity(new PoiCitySearchOption()
                .city(mCityEdit.getText().toString())
                .keyword(mAutoEdit.getText().toString())
                .pageNum(1));
    }

    public void searchNearbyProcess(View view) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(mAutoEdit.getText()
                .toString()).sortType(PoiSortType.distance_from_near_to_far).location(new LatLng(latitude,longitude))
                .radius(2000).pageNum(0);
        poiSearch.searchNearby(nearbySearchOption);
    }

    private class MyPoiOverlay extends PoiOverlay{

        /**
         * 构造函数
         *
         * @param baiduMap 该 PoiOverlay 引用的 BaiduMap 对象
         */
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            poiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

//初始化位置

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


}
