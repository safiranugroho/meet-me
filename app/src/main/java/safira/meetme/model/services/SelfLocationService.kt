package safira.meetme.model.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log


/**
 * Created by safiranugroho on 18/09/2017.
 *
 * A LocationListener that listens for any change in location and returns said location.
 */

class SelfLocationService(private val context: Context, private val activity: Activity) : LocationListener {

    private val LOCATION_REFRESH_TIME = 1000
    private val LOCATION_REFRESH_DISTANCE = 5


    private var location: Location? = null

    override fun onLocationChanged(location: Location) {
        this.location = location
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    fun getLocation(): Location? {
        try {

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            Log.i(LOG_TAG, "Is GPS enabled? $isGPSEnabled")

            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            Log.i(LOG_TAG, "Is Network enabled? $isNetworkEnabled")

            if (isNetworkEnabled) {
                location = null

                if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this.activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 800)

                }

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME.toLong(), LOCATION_REFRESH_DISTANCE.toFloat(), this)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            } else if (isGPSEnabled) {
                location = null

                if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this.activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 800)
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(), LOCATION_REFRESH_DISTANCE.toFloat(), this)
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location == null) {
                    Log.d(LOG_TAG, "Location is null!")
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    companion object {

        private val LOG_TAG = SelfLocationService::class.java.name
    }

}
