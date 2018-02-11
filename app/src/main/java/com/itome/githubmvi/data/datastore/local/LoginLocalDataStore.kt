package com.itome.githubmvi.data.datastore.local

import com.itome.githubmvi.data.model.LoginData
import io.reactivex.Completable
import io.reactivex.Flowable
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
        return Completable.create {
            val realm = Realm.getDefaultInstance()
            val loginData = LoginData(accessToken = accessToken, loginUser = null)
            realm.executeTransaction { realm.copyToRealm(loginData) }
            it.onComplete()
        }
    }
}