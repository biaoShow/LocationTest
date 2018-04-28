package com.example.biao.locationtest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //当前可用的位置控制器
    Location location;
    List<String> list;
    TextView textView;
    LocationListener locationListener = new LocationListener() {

        @Override
        //状态改变的回调方法
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        }

        @Override
        //提供者（网络或GPS）被打开的回调方法
        public void onProviderEnabled(String arg0) {
        }

        @Override
        //提供者（网络或GPS）被关闭的回调方法
        public void onProviderDisabled(String arg0) {
        }

        @Override
        //经纬度改变的回调方法，基本都是使用这个回调方法
        public void onLocationChanged(Location arg0) {
            // 更新当前经纬度
            textView.append("经度：" + arg0.getLongitude() + "，纬度：" + arg0.getLatitude() + "\n");
        }
    };
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider; //是否为网络位置控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        //获取定位服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        if (locationManager != null) {
            list = locationManager.getProviders(true);
        }
        //检查是否打开了GPS或网络定位
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
            textView.append("GPS位置控制器" + "\n");
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
            textView.append("网络位置控制器" + "\n");
        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
        }

    }

    //获得我所在的位置的经纬度
    public void getLocation(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            //如果用户并没有同意该权限
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {//低版本手机，直接获取位置信息

        }
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string = ",经度：" + location.getLongitude() + " 纬度：" + location.getLatitude();
            textView.append(string + "\n");
        }

    }

    //位置的监听
    public void bandLocationListener(View view) {


        if (Build.VERSION.SDK_INT >= 23) {

            //如果用户并没有同意该权限
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            } else {
                //有权限直接获取地理位置
            }

        } else {//低版本手机，直接获取位置信息

        }
        //绑定定位事件，监听位置是否改变
        //第一个参数为控制器类型第二个参数为监听位置变化的时间间隔（单位：毫秒）
        //第三个参数为位置变化的间隔（单位：米）第四个参数为位置监听器
        locationManager.requestLocationUpdates(provider, 2000, 2, locationListener);
    }

    //关闭时解除监听器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}

