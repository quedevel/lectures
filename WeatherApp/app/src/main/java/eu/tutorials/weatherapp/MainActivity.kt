package eu.tutorials.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import eu.tutorials.weatherapp.models.WeatherResponse
import eu.tutorials.weatherapp.network.WeatherService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

  // A fused location client variable which is further used to get the user's current location
  private lateinit var mFusedLocationClient: FusedLocationProviderClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    if (!isLocationEnabled()) {
      Toast.makeText(
        this,
        "Your location provider is turned off. Please turn it on.",
        Toast.LENGTH_SHORT
      ).show()

      // This will redirect you to settings from where you need to turn on the location provider.
      val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
      startActivity(intent)
    } else {
      Dexter.withContext(this)
        .withPermissions(
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION
        )
        .withListener(object : MultiplePermissionsListener {
          override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
            if (report!!.areAllPermissionsGranted()) {
              requestLocationData()
            }

            if (report.isAnyPermissionPermanentlyDenied) {
              Toast.makeText(
                this@MainActivity,
                "You have denied location permission. Please allow it is mandatory.",
                Toast.LENGTH_SHORT
              ).show()
            }
          }

          override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>?,
            token: PermissionToken?
          ) {
            showRationalDialogForPermissions()
          }
        }).onSameThread()
        .check()
      // END
    }
  }

  /**
   * A function which is used to verify that the location or GPS is enabled or not.
   */
  private fun isLocationEnabled(): Boolean {
    // This provides access to the system location services.
    val locationManager: LocationManager =
      getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
      LocationManager.NETWORK_PROVIDER
    )
  }

  private fun getLocationWeatherDetails(latitude: Double, longitude: Double){
    if (Constants.isNetworkAvailable(this)){
      val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

      val service: WeatherService = retrofit.create(WeatherService::class.java)

      val listCall: Call<WeatherResponse> = service.getWeather(latitude, longitude, Constants.METRIC_UNIT, Constants.APP_ID)

      listCall.enqueue(object: Callback<WeatherResponse>{
        override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
          if (response.isSuccessful){
            val weather: WeatherResponse? = response.body()
            Log.i("Response result", "$weather")
          } else {
            when(response.code()){
              400 -> Log.e("Error 400", "Bad Connection")
              404 -> Log.e("Error 404", "Not Found")
              else -> Log.e("Error", "Generic error")
            }
          }
        }

        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
          Log.e("Error!!", t.message.toString())
        }

      })

    } else {
      Toast.makeText(this, "No connected", Toast.LENGTH_SHORT).show()
    }
  }

  /**
   * A function used to show the alert dialog when the permissions are denied and need to allow it from settings app info.
   */
  private fun showRationalDialogForPermissions() {
    AlertDialog.Builder(this)
      .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
      .setPositiveButton(
        "GO TO SETTINGS"
      ) { _, _ ->
        try {
          val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
          val uri = Uri.fromParts("package", packageName, null)
          intent.data = uri
          startActivity(intent)
        } catch (e: ActivityNotFoundException) {
          e.printStackTrace()
        }
      }
      .setNegativeButton("Cancel") { dialog, _ ->
        dialog.dismiss()
      }.show()
  }

  /**
   * A function to request the current location. Using the fused location provider client.
   */
  @SuppressLint("MissingPermission")
  private fun requestLocationData() {

    val mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    mFusedLocationClient.requestLocationUpdates(
      mLocationRequest, mLocationCallback,
      Looper.myLooper()
    )
  }

  /**
   * A location callback object of fused location provider client where we will get the current location details.
   */
  private val mLocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      val mLastLocation: Location = locationResult.lastLocation
      val latitude = mLastLocation.latitude
      Log.i("Current Latitude", "$latitude")

      val longitude = mLastLocation.longitude
      Log.i("Current Longitude", "$longitude")

      getLocationWeatherDetails(latitude, longitude)
    }
  }

}