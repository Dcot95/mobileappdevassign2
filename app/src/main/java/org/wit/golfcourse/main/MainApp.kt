package org.wit.golfcourse.main

import android.app.Application
import org.wit.golfcourse.models.GolfcourseJSONStore
import org.wit.golfcourse.models.GolfcourseMemStore
import org.wit.golfcourse.models.GolfcourseModel
import org.wit.golfcourse.models.GolfcourseStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var golfcourses: GolfcourseStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        golfcourses = GolfcourseJSONStore(applicationContext)
        i("Golfcourse started")

    }
}