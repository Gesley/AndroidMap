package com.gesley.androidmap

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gesley.androidmap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btIntentMap.setOnClickListener {

            val latitudeLongitude = "-23.5565804,-46.662113"
            val zoom = 15
            val geo = "geo:$latitudeLongitude?z=$zoom"

            val geoUri = Uri.parse( geo )
            val intent = Intent( Intent.ACTION_VIEW, geoUri )

            intent.setPackage( "com.google.android.apps.maps" )
            startActivity( intent )

        }

        binding.btInternMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.btWaze.setOnClickListener {
            openWaze(1.11111,2.222222)
        }
    }

    fun openWaze(latitude: Double, longitude: Double) {
        packageManager?.let {
            val url = "waze://?ll=$latitude,$longitude&navigate=yes"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.resolveActivity(it)?.let {
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Waze nao instalado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}