package com.spreadwin.vehiclestate.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.spreadwin.vehiclestate.R;
import com.spreadwin.vehiclestate.config.AppConfig;
import com.spreadwin.vehiclestate.utils.AMapUtil;
import com.spreadwin.vehiclestate.utils.DrivingRouteOverLay;
import com.spreadwin.vehiclestate.utils.LoationModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParkChargingFragment  extends Fragment implements RadioGroup.OnCheckedChangeListener , AMap.OnMyLocationChangeListener,View.OnClickListener{
    private  final  String TAG="main";

    //private LatLng latlng = new LatLng(39.761, 116.434);
    private MapView mapView;
    private AMap aMap;
    Circle circle;
    public  static  ParkChargingFragment parkChargingFragment;
    private RouteSearch routeSearch;
    private TextView AddressText;
    private  TextView DistanceText;
    private ArrayList<LoationModel>  addressList=new ArrayList<LoationModel>();
   private  LatLng currentLatLng=null;
   private ImageView parkButton;
   private  ImageView ChargeButton;
    private LatLonPoint mStartPoint ;
    private LatLonPoint mEndPoint=null;
   private  Marker ChoosenPoint;
   private  Marker LastPoint;
   private  DriveRouteResult  mDriveRouteResult;
   private Boolean isPark=true;
   private FrameLayout naviFrameLayout;
   private Context mContext=getContext();
    public static ParkChargingFragment getInstance(){
        if (parkChargingFragment==null){
            parkChargingFragment=new ParkChargingFragment ();
        }
        return parkChargingFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View  ParkChargingView=inflater.inflate(R.layout.fragment_parkcharging,container,false);
        return  ParkChargingView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView= (MapView) view.findViewById(R.id.map);
        AddressText= (TextView) view.findViewById(R.id.location_text);
        DistanceText= (TextView) view.findViewById(R.id.distance_text);
        parkButton= (ImageView) view.findViewById(R.id.park_button);
        ChargeButton= (ImageView) view.findViewById(R.id.chanrge_button);
        naviFrameLayout= (FrameLayout) view.findViewById(R.id.frame_navi);

        mapView.onCreate(savedInstanceState);
        if (aMap==null){
            aMap=mapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        }
        init();

    }
    private void  init(){
        addCurrentMark();
        addData();
        aMap.setMyLocationEnabled(true);//
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnMarkerClickListener(markerClickListener);
        naviFrameLayout.setOnClickListener(this);

        drawParkMark();

        routeSearch=new RouteSearch(getContext());
        routeSearch.setRouteSearchListener(routeSearchListener);
         parkButton.setOnClickListener(this);
         ChargeButton.setOnClickListener(this);

    }
    private void  addCurrentMark(){
        MyLocationStyle myLocationStyle= new MyLocationStyle();

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //设置定位图标
        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.ic_car_map_0);
        myLocationStyle.myLocationIcon(bitmapDescriptor);
       aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        Log.d(TAG, "addCurrentMark: ");
    }
    private  void  drawCircle(LatLng latLng){

        CircleOptions circleOptions=new CircleOptions()
                .center(latLng)
                .radius(3000)
                .strokeColor(Color.parseColor("#31f5de"))
                .fillColor(Color.parseColor("#4b97d692"));

        circle=aMap.addCircle( circleOptions);
        Log.d(TAG, "drawCircle: 画了一次");


    }
    //画停车场
    private  void  drawParkMark(){
        for (int i=0;i<13;i++){
            drawMark(aMap,addressList.get(i).getLatLng(),addressList.get(i).getAddress());
        }

    }
    private  void removeMark(){
       // Log.d(TAG, "removeMark: "+aMap.getMapScreenMarkers().size());

        List<Marker> list=aMap.getMapScreenMarkers();
            for ( int i= 0;i<list.size();i++){
                Log.d(TAG, "removeMark: "+aMap.getMapScreenMarkers().size());
               if (list.get(i).getPeriod()==5){
                   list.get(i).remove();
               }

            }


    }
   //画电桩
    private  void drawChargeMark(){
         for (int i=13;i<56;i++){
             drawMark(aMap,addressList.get(i).getLatLng(),addressList.get(i).getAddress());
         }

    }


//画mark的方法
    private  void  drawMark(AMap aMap ,LatLng myll,String title){
       if (AMapUtils.calculateLineDistance(currentLatLng,myll)<=3000){
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_location_2)))
                .position(myll)
                .visible(true)
                .period(5)
                .title(title);

          aMap.addMarker(markerOptions);

      }
    }

//mark点击事件
     private  AMap.OnMarkerClickListener markerClickListener=new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //把“上个点”还原
            if (ChoosenPoint!=null){
                LastPoint =ChoosenPoint;
            LastPoint.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_location_2)));
        }
          //换当前选中点的图标
            marker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_location_0)));
             AddressText.setText(marker.getTitle());

         //获取距离并转换为kM显示
            float f= (AMapUtils.calculateLineDistance(currentLatLng,marker.getPosition()))/1000;
            BigDecimal bg = new BigDecimal(f);
            double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            DistanceText.setText("距离："+String.valueOf(f1)+"KM");

            //将点击的点刷新为当前选中点
            ChoosenPoint=null;
            ChoosenPoint=marker;
            return false;
        }
    };
    private RouteSearch.OnRouteSearchListener routeSearchListener=new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

        }
        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {


            if (i == AMapException.CODE_AMAP_SUCCESS) {
                if (driveRouteResult!= null && driveRouteResult.getPaths() != null) {
                    if (driveRouteResult.getPaths().size() > 0) {
                        mDriveRouteResult = driveRouteResult;
                        final DrivePath drivePath = mDriveRouteResult.getPaths()
                                .get(0);
                        DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                                mContext, aMap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                        drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.addToMap();
                        drivingRouteOverlay.zoomToSpan();

//                        int dis = (int) drivePath.getDistance();
//                        int dur = (int) drivePath.getDuration();
                        //String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";

                        //int taxiCost = (int) mDriveRouteResult.getTaxiCost();

                    } else if (driveRouteResult!= null && driveRouteResult.getPaths() == null) {
                        Toast.makeText(mContext,"没有结果",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext,"没有结果",Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "onDriveRouteSearched: 错误码"+i);
            }


        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        }
        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        }
    };
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {


    }

    @Override
    public void onMyLocationChange(Location location) {
        currentLatLng=null;
        if (circle!=null){
            circle.remove();
        }
        currentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        drawCircle(new LatLng(location.getLatitude(),location.getLongitude()));
        Log.d(TAG, "定位一次");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chanrge_button:
                if (isPark){
                    parkButton.setImageResource(R.drawable.ic_park_1);
                    ChargeButton.setImageResource(R.drawable.ic_charge_1);
                }
                isPark=false;
               removeMark();
               drawChargeMark();
                break;
            case  R.id.park_button:
                if (!isPark){
                    parkButton.setImageResource(R.drawable.ic_park_2);
                    ChargeButton.setImageResource(R.drawable.ic_charge_2);
                }
                isPark=true;
                removeMark();
                drawParkMark();
                break;
            case  R.id.frame_navi:
                Log.d(TAG, "onClick: "+"点到导航按钮");
                mStartPoint=null;
                mEndPoint=null;

                mStartPoint=new LatLonPoint(currentLatLng.latitude,currentLatLng.longitude);
                mEndPoint=new LatLonPoint(ChoosenPoint.getPosition().latitude,ChoosenPoint.getPosition().longitude);

                Log.d(TAG, "经度"+ChoosenPoint.getPosition().latitude+"纬度"+ChoosenPoint.getPosition().longitude);

                final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint,mEndPoint);
                RouteSearch.DriveRouteQuery driveRouteQuery =new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault,null,null,"");
                routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
                break;
        }
    }
    private void addData(){
        addressList.add(new LoationModel(AppConfig.latlng0,AppConfig.string0));
        addressList.add(new LoationModel(AppConfig.latlng1,AppConfig.string1));
        addressList.add(new LoationModel(AppConfig.latlng2,AppConfig.string2));
        addressList.add(new LoationModel(AppConfig.latlng3,AppConfig.string3));
        addressList.add(new LoationModel(AppConfig.latlng4,AppConfig.string4));
        addressList.add(new LoationModel(AppConfig.latlng5,AppConfig.string5));
        addressList.add(new LoationModel(AppConfig.latlng6,AppConfig.string6));
        addressList.add(new LoationModel(AppConfig.latlng7,AppConfig.string7));
        addressList.add(new LoationModel(AppConfig.latlng8,AppConfig.string8));
        addressList.add(new LoationModel(AppConfig.latlng9,AppConfig.string9));
        addressList.add(new LoationModel(AppConfig.latlng10,AppConfig.string10));
        addressList.add(new LoationModel(AppConfig.latlng11,AppConfig.string11));
        addressList.add(new LoationModel(AppConfig.latlng12,AppConfig.string12));
        addressList.add(new LoationModel(AppConfig.latlng13,AppConfig.string13));
        addressList.add(new LoationModel(AppConfig.latlng14,AppConfig.string14));
        addressList.add(new LoationModel(AppConfig.latlng15,AppConfig.string15));
        addressList.add(new LoationModel(AppConfig.latlng16,AppConfig.string16));
        addressList.add(new LoationModel(AppConfig.latlng17,AppConfig.string17));
        addressList.add(new LoationModel(AppConfig.latlng18,AppConfig.string18));
        addressList.add(new LoationModel(AppConfig.latlng19,AppConfig.string19));
        addressList.add(new LoationModel(AppConfig.latlng20,AppConfig.string20));
        addressList.add(new LoationModel(AppConfig.latlng21,AppConfig.string21));
        addressList.add(new LoationModel(AppConfig.latlng22,AppConfig.string22));
        addressList.add(new LoationModel(AppConfig.latlng23,AppConfig.string23));
        addressList.add(new LoationModel(AppConfig.latlng24,AppConfig.string24));
        addressList.add(new LoationModel(AppConfig.latlng25,AppConfig.string25));
        addressList.add(new LoationModel(AppConfig.latlng26,AppConfig.string26));
        addressList.add(new LoationModel(AppConfig.latlng27,AppConfig.string27));
        addressList.add(new LoationModel(AppConfig.latlng28,AppConfig.string28));
        addressList.add(new LoationModel(AppConfig.latlng29,AppConfig.string29));
        addressList.add(new LoationModel(AppConfig.latlng30,AppConfig.string30));
        addressList.add(new LoationModel(AppConfig.latlng31,AppConfig.string31));
        addressList.add(new LoationModel(AppConfig.latlng32,AppConfig.string32));
        addressList.add(new LoationModel(AppConfig.latlng33,AppConfig.string33));
        addressList.add(new LoationModel(AppConfig.latlng34,AppConfig.string34));
        addressList.add(new LoationModel(AppConfig.latlng35,AppConfig.string35));
        addressList.add(new LoationModel(AppConfig.latlng36,AppConfig.string36));
        addressList.add(new LoationModel(AppConfig.latlng37,AppConfig.string37));
        addressList.add(new LoationModel(AppConfig.latlng38,AppConfig.string38));
        addressList.add(new LoationModel(AppConfig.latlng39,AppConfig.string39));
        addressList.add(new LoationModel(AppConfig.latlng40,AppConfig.string40));
        addressList.add(new LoationModel(AppConfig.latlng41,AppConfig.string41));
        addressList.add(new LoationModel(AppConfig.latlng42,AppConfig.string42));
        addressList.add(new LoationModel(AppConfig.latlng43,AppConfig.string43));
        addressList.add(new LoationModel(AppConfig.latlng44,AppConfig.string44));
        addressList.add(new LoationModel(AppConfig.latlng45,AppConfig.string45));
        addressList.add(new LoationModel(AppConfig.latlng46,AppConfig.string46));
        addressList.add(new LoationModel(AppConfig.latlng47,AppConfig.string47));
        addressList.add(new LoationModel(AppConfig.latlng48,AppConfig.string48));
        addressList.add(new LoationModel(AppConfig.latlng49,AppConfig.string49));
        addressList.add(new LoationModel(AppConfig.latlng50,AppConfig.string50));
        addressList.add(new LoationModel(AppConfig.latlng51,AppConfig.string51));
        addressList.add(new LoationModel(AppConfig.latlng52,AppConfig.string52));
        addressList.add(new LoationModel(AppConfig.latlng53,AppConfig.string53));
        addressList.add(new LoationModel(AppConfig.latlng54,AppConfig.string54));
        addressList.add(new LoationModel(AppConfig.latlng55,AppConfig.string55));










    }
}
