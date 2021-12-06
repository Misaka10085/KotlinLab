package com.misakanetwork.kotlinlab.jetpack.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack
 * class name：LocationObserver
 * desc：Jetpack实现Service-LifecycleObserver
 */
class LocationServiceObserver(private val context: Context) : LifecycleObserver {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: ServiceLocationListener

    /**
     * onCreate()时创建LocationManager并开始定位
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun startGetLocation() {
        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager // 获取系统定位服务
        locationListener = ServiceLocationListener() // 创建自定义定位监听
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val settingsIntent = Intent()
            settingsIntent.flags = FLAG_ACTIVITY_NEW_TASK
            settingsIntent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            context.startActivity(settingsIntent)
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000, 0.01f,
            locationListener
        ) // 设置为GPS定位，每三秒定位一次
        Log.e("startGetLocation", "started!")
    }

    /**
     * onDestroy()时销毁LocationManager
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun stopGetLocation() {
        locationManager.removeUpdates(locationListener)
        Log.e("stopGetLocation", "stopped!")
    }

    internal class ServiceLocationListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            Log.e("ServiceLocationListener", "LocationChanged $location")
        }
    }
}