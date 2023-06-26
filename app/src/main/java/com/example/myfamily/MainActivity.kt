package com.example.myfamily

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.CAMERA
    )
    val permissionCode = 78

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askForPermissions()

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottomBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.nav_guard -> {
                    inflateFragment(GuardFragment.newInstance())
                }
                R.id.nav_home -> {
                    inflateFragment(HomeFragment.newInstance())
                }
                R.id.nav_dashboard -> {
                    inflateFragment(MapsFragment())
                }
                R.id.nav_profile -> {
                    inflateFragment(ProfileFragment.newInstance())
                }
            }


            true
        }
        bottomBar.selectedItemId = R.id.nav_home
    }

    private fun askForPermissions() {
        ActivityCompat.requestPermissions(this,permissions,permissionCode)
    }


    private fun inflateFragment(newInstance: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == permissionCode){

            if(allPermissionsGranted()){
                openCamera()

            }else{

            }

        }

    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionsGranted(): Boolean {

        for(item in permissions){
            if(ContextCompat.checkSelfPermission(this,item) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true

    }
}