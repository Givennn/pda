package com.panda.pda.app

import android.app.Application
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/16
 */
class PdaApplication: Application() {

    companion object {
        lateinit var instance: PdaApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initTimber()
        initStetho()
    }

    private fun initStetho() {
//        if (BuildConfig.DEBUG) {
//            Stetho.initializeWithDefaults(this)
//        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}