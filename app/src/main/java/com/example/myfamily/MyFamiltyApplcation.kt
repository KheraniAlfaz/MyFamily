package com.example.myfamily

import android.app.Application

class MyFamiltyApplcation : Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }

}