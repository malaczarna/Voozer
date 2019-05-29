package pl.voozer.ui.main

import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import pl.voozer.R
import pl.voozer.ui.base.BaseActivity
import android.Manifest.permission
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import pl.voozer.service.model.Position
import pl.voozer.service.model.Profile
import pl.voozer.service.model.User
import pl.voozer.service.network.Connection

class MainActivity : BaseActivity<MainController, MainView>(), MainView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var user: User
    private lateinit var toolbar: Toolbar

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.setOnMarkerClickListener(this)
        if (ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean = false

    override fun updatePosition(position: Position) {
        Log.d("latLng", "${position.lat}, ${position.lng}")
    }

    override fun updateUser(user: User) {
        this.user = user
        if (user.profile == Profile.PASSENGER) {
            toolbar.findViewById<ImageView>(R.id.ivProfile).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_walking))
            toolbar.findViewById<TextView>(R.id.tvProfile).text = getString(R.string.menu_passenger)
        } else {
            toolbar.findViewById<ImageView>(R.id.ivProfile).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_steering_wheel))
            toolbar.findViewById<TextView>(R.id.tvProfile).text = getString(R.string.menu_driver)
        }
    }

    override fun reloadUser() {
        controller.loadUser()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initController() {
        controller = MainController()
        controller.view = this
        controller.api = Connection.Builder().provideOkHttpClient(applicationContext).provideRetrofit().createApi()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_rides -> {

            }
            R.id.nav_preferences -> {

            }
            R.id.nav_help -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_regulations -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        getLocationPermission()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        controller.loadUser()

        fabMyPosition.setOnClickListener {
            if (ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                    if (location != null) {
                        lastLocation = location
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    }
                }
            }
        }

        toolbar.findViewById<ImageView>(R.id.ivProfile).setOnClickListener {
            controller.changeProfile()
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        googleMap.addMarker(markerOptions)
    }

    @SuppressWarnings("CheckResult")
    private fun getLocationPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions
            .request(permission.ACCESS_FINE_LOCATION)
            .subscribe {}
    }
}
