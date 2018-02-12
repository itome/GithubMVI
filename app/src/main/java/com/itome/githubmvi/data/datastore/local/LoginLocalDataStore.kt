package com.itome.githubmvi.data.datastore.local

import com.itome.githubmvi.data.model.LoginData
import com.itome.githubmvi.data.model.User
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.where

class LoginLocalDataStore {

    fun readAccessToken(): Single<String> {
        val realm = Realm.getDefaultInstance()
        val accessToken = realm.where<LoginData>().findFirst()?.accessToken ?: ""
        realm.close()
        return Single.just(accessToken)
    }

    fun saveAccessToken(accessToken: String): Completable {
        val realm = Realm.getDefaultInstance()
        val loginData = LoginData(accessToken = accessToken, loginUser = null)
        realm.executeTransaction { it.copyToRealm(loginData) }
        realm.close()
        return Completable.complete()
    }

    fun getLoginUser(): Single<User> {
        val realm = Realm.getDefaultInstance()
        val user = realm.where<LoginData>().findFirst()?.loginUser
        realm.close()
        return Single.just(user)
    }

    fun saveLoginUser(user: User): Completable {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val loginData = it.where<LoginData>().findFirst()
            loginData?.loginUser = user
        }
        realm.close()
        return Completable.complete()
    }
}