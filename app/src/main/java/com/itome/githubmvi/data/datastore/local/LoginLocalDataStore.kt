package com.itome.githubmvi.data.datastore.local

import com.itome.githubmvi.data.model.LoginData
import io.reactivex.Completable
import io.realm.Realm
import io.realm.kotlin.where

class LoginLocalDataStore {

    fun getAccessToken(): String {
        val realm = Realm.getDefaultInstance()
        val accessToken = realm.where<LoginData>().findFirst()?.accessToken ?: ""
        realm.close()
        return accessToken
    }

    fun saveAccessToken(accessToken: String): Completable {
        val realm = Realm.getDefaultInstance()
        val loginData = LoginData(accessToken = accessToken, loginUser = null)
        realm.executeTransaction { realm.copyToRealm(loginData) }
        realm.close()
        return Completable.complete()
    }
}