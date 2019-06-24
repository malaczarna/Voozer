package pl.voozer.ui.main

import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import pl.voozer.R
import pl.voozer.ui.base.BaseActivity
import android.Manifest.permission
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.navigation.NavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.content_main.*
import pl.voozer.service.model.Destination
import pl.voozer.service.model.Position
import pl.voozer.service.model.Profile
import pl.voozer.service.model.User
import pl.voozer.service.network.Connection
import pl.voozer.ui.adapter.DriversAdapter
import pl.voozer.utils.SharedPreferencesHelper
import java.util.*

class MainActivity : BaseActivity<MainController, MainView>(), MainView, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location
    private lateinit var destination: Destination
    private var requestingLocationUpdates: Boolean = false
    private lateinit var user: User
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private var fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.setOnMarkerClickListener(this)
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                applicationContext,
                permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

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

    override fun updateDrivers(drivers: List<User>) {
        for (driver in drivers) {
            placeDriverMarkerOnMap(LatLng(driver.lat, driver.lng))
        }
        rvDriversList.layoutManager = LinearLayoutManager(applicationContext)
        rvDriversList.adapter = DriversAdapter(drivers = drivers, context = applicationContext, listener = object: DriversAdapter.OnItemClickListener {
            override fun onDriverClick(driver: User) {
                splDrivers.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(driver.lat, driver.lng), 12f))
            }
        })
    }

    override fun updateUser(user: User) {
        this.user = user
        tvUsername.text = "${getString(R.string.search_bar_greeting)} ${user.name}!"
        when (user.profile) {
            Profile.PASSENGER -> {
                navView.getHeaderView(0).findViewById<LinearLayout>(R.id.llHeaderMain).setBackgroundColor(
                    ContextCompat.getColor(applicationContext, R.color.colorPrimary
                    ))
                fabMyPosition.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                toolbar.findViewById<ImageView>(R.id.ivProfile)
                    .setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_walking))
                toolbar.findViewById<TextView>(R.id.tvProfile).text = getString(R.string.menu_passenger)
                btnAcceptDestination.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            }
            Profile.DRIVER -> {
                navView.getHeaderView(0).findViewById<LinearLayout>(R.id.llHeaderMain).setBackgroundColor(
                    ContextCompat.getColor(applicationContext, R.color.colorPrimaryDriver
                    ))
                fabMyPosition.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDriver))
                toolbar.findViewById<ImageView>(R.id.ivProfile)
                    .setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_steering_wheel))
                toolbar.findViewById<TextView>(R.id.tvProfile).text = getString(R.string.menu_driver)
                btnAcceptDestination.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDriver))
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data is Intent) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        destination = Destination(name = place.name!!, lat = place.latLng!!.latitude, lng = place.latLng!!.longitude)
                        place.latLng?.let { placeDestinationMarkerOnMap(it) }
                        tvSearch.text = place.address
                        when (user.profile) {
                            Profile.PASSENGER -> {
                                controller.loadDrivers()
                                splDrivers.anchorPoint = 0.7f
                                splDrivers.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                                ObjectAnimator.ofFloat(llFabButtons, "translationY", -splDrivers.panelHeight.toFloat()).apply {
                                    duration = 300
                                    start()
                                }
                                Handler().postDelayed(
                                    {
                                        splDrivers.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                                    },300L
                                )
                            }
                            Profile.DRIVER -> {
                                btnAcceptDestination.isEnabled = true
                                ObjectAnimator.ofFloat(llAcceptDestination, "translationY", llHeaderBar.height.toFloat()).apply {
                                    duration = 1000
                                    start()
                                }
                            }
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("Places-API", status.statusMessage)
                    }
                    RESULT_CANCELED -> {
                        // The user canceled the operation.
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if(requestingLocationUpdates) {
            stopLocationUpdates()
        }
    }

    override fun onBackPressed() {
        when {
            splDrivers.panelState == SlidingUpPanelLayout.PanelState.EXPANDED -> {
                splDrivers.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            }
            splDrivers.panelState == SlidingUpPanelLayout.PanelState.ANCHORED -> {
                splDrivers.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
            splDrivers.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                removeMarkers()
                tvSearch.text = getString(R.string.search_bar_title)
                splDrivers.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
                ObjectAnimator.ofFloat(llFabButtons, "translationY", 0f).apply {
                    duration = 1000
                    start()
                }
                llSearchBar.isEnabled = true
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (SharedPreferencesHelper.isMainTheme(applicationContext)) {
            true -> {
                setTheme(R.style.AppTheme)
            }
            false -> {
                setTheme(R.style.AppThemeDriver)
            }

        }
        super.onCreate(savedInstanceState)
        initLayout()
        getLocationPermission()
        Log.d("height", llHeaderBar.height.toString())
        fabMyPosition.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
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
            MaterialDialog(this).show {
                customView(R.layout.popup_change_profile, scrollable = true)
                cancelOnTouchOutside(true)
                noAutoDismiss()
                negativeButton(R.string.cancel) {
                    dismiss()
                }
                positiveButton(R.string.confirm) {
                    dismiss()
                    controller.changeProfile()
                    when (SharedPreferencesHelper.isMainTheme(applicationContext)) {
                        true -> {
                            SharedPreferencesHelper.setMainTheme(applicationContext, false)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }

                        false -> {
                            SharedPreferencesHelper.setMainTheme(applicationContext, true)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
        llSearchBar.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setCountry("PL")
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    controller.sendPosition(Position(location.latitude, location.longitude))
                }
            }
        }
        btnAcceptDestination.setOnClickListener {
            requestingLocationUpdates = true
            startLocationUpdates()
            controller.setDestination(destination = destination)
            ObjectAnimator.ofFloat(llAcceptDestination, "translationY", -llAcceptDestination.height.toFloat()).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(llHeaderBar, "translationY", -llHeaderBar.height.toFloat()).apply {
                duration = 1000
                start()
            }
            fabCancelRide.animate().alpha(1f).setDuration(1000).withStartAction {
                fabCancelRide.show()
            }
            btnAcceptDestination.isEnabled = false
        }
        fabCancelRide.setOnClickListener {
            MaterialDialog(this).show {
                title(text = getString(R.string.popup_cancel_ride_title))
                cancelOnTouchOutside(true)
                noAutoDismiss()
                negativeButton(R.string.cancel) {
                    dismiss()
                }
                positiveButton(R.string.confirm) {
                    dismiss()
                    controller.finishDestination()
                    requestingLocationUpdates = false
                    stopLocationUpdates()
                    this@MainActivity.tvSearch.text = getString(R.string.search_bar_title)
                    removeMarkers()
                    ObjectAnimator.ofFloat(this@MainActivity.llAcceptDestination, "translationY", 0f).apply {
                        duration = 1000
                        start()
                    }
                    ObjectAnimator.ofFloat(this@MainActivity.llHeaderBar, "translationY", 0f).apply {
                        duration = 1000
                        start()
                    }
                    this@MainActivity.fabCancelRide.animate().alpha(0f).setDuration(1000).withEndAction {
                        this@MainActivity.fabCancelRide.hide()
                    }
                }
            }
        }
    }

    private fun initLayout() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        controller.loadUser()
    }

    private fun initLocation(){
        val apiKey = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString(
            "com.google.android.geo.API_KEY"
        )
        Places.initialize(applicationContext, apiKey)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 10
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                applicationContext,
                permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun placeDestinationMarkerOnMap(location: LatLng) {
        googleMap.clear()
        val markerOptions = MarkerOptions().position(location)
        googleMap.addMarker(markerOptions)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    private fun placeDriverMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_side))
        googleMap.addMarker(markerOptions)
    }

    private fun removeMarkers() {
        googleMap.clear()
    }

    @SuppressWarnings("CheckResult")
    private fun getLocationPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions
            .request(permission.ACCESS_FINE_LOCATION)
            .subscribe{granted ->
                if (granted){
                    initLocation()
                }
            }
    }
}
