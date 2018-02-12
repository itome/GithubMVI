package com.itome.githubmvi

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration



class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded() // todo remove for production
                .build()
        Realm.setDefaultConfiguration(config)
    }
}