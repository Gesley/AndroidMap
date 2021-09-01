package com.gesley.androidmap

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gesley.androidmap.databinding.ActivityMapsBinding
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by lazy {
        ViewModelProvider(this).get(MapsViewModel::class.java)
    }

    private val fragment: MyMapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.map) as MyMapFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        fragment.getMapAsync {
            //initUi()
            viewModel.connectGoogleApiClient()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.disconnectGoogleApiClient()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ERROR_PLAY_SERVICES && resultCode == Activity.RESULT_OK) {
            viewModel.connectGoogleApiClient()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == REQUEST_PERMISSIONS && permissions.isNotEmpty()) {
            if (permissions.firstOrNull() == Manifest.permission.ACCESS_FINE_LOCATION && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                loadLastLocation()
            } else {
                showError("Você deve aceitar as permissões para executar a aplicação")
                finish()
            }
        }
    }


    private fun handleLocationError(error: MapsViewModel.LocationError?) {
        if (error != null) {
            when (error) {
                is MapsViewModel.LocationError.ErrorLocationUnavailable -> showError("Não foi possivel obter a localização atual")
            }
        }
    }

    private fun loadLastLocation() {
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS
            )
            return
        }
        viewModel.requestLocation()
    }

    private fun hasPermission(): Boolean {
        val granted = PackageManager.PERMISSION_GRANTED
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == granted
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showPlayServicesErrorMessage(errorCode: Int) {
        GoogleApiAvailability.getInstance()
            .getErrorDialog(this, errorCode, REQUEST_ERROR_PLAY_SERVICES).show()
    }

    companion object {
        private const val REQUEST_ERROR_PLAY_SERVICES = 1
        private const val REQUEST_PERMISSIONS = 2
    }
}